
package org.jclouds.s3.handlers;

import static org.jclouds.http.HttpUtils.closeClientButKeepContentStream;
import static org.jclouds.http.Uris.uriBuilder;

import java.net.URI;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.aws.domain.AWSError;
import org.jclouds.aws.util.AWSUtils;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.handlers.BackoffLimitedRetryHandler;
import org.jclouds.http.handlers.RedirectionRetryHandler;

import com.google.common.net.HttpHeaders;

/**
 * Handles Retryable responses with error codes in the 3xx range
 */
@Singleton
public class S3RedirectionRetryHandler extends RedirectionRetryHandler {
   private final AWSUtils utils;

   @Inject
   public S3RedirectionRetryHandler(BackoffLimitedRetryHandler backoffHandler, AWSUtils utils) {
      super(backoffHandler);
      this.utils = utils;
   }

   @Override
   public boolean shouldRetryRequest(HttpCommand command, HttpResponse response) {
      if (isRedirectWithoutLocation(response)) {
         handleRedirect(command, response);
         return true;
      } else {
         return super.shouldRetryRequest(command, response);
      }
   }

   private boolean isRedirectWithoutLocation(HttpResponse response) {
      return response.getFirstHeaderOrNull(HttpHeaders.LOCATION) == null
            && (response.getStatusCode() == 301 || response.getStatusCode() == 307);
   }

   private void handleRedirect(HttpCommand command, HttpResponse response) {
      command.incrementRedirectCount();
      closeClientButKeepContentStream(response);
      AWSError error = utils.parseAWSErrorFromContent(command.getCurrentRequest(), response);
      String host = error.getDetails().get("Endpoint");
      if (host != null) {
         processHost(command, host, response);
      }
   }

   private void processHost(HttpCommand command, String host, HttpResponse response) {
      if (isSameHost(command, host)) {
         backoffHandler.shouldRetryRequest(command, response);
      } else {
         updateRequestEndpoint(command, host);
      }
   }

   private boolean isSameHost(HttpCommand command, String host) {
      return host.equals(command.getCurrentRequest().getEndpoint().getHost());
   }

   private void updateRequestEndpoint(HttpCommand command, String host) {
      URI newHost = uriBuilder(command.getCurrentRequest().getEndpoint()).host(host).build();
      command.setCurrentRequest(command.getCurrentRequest().toBuilder().endpoint(newHost).build());
   }
}
