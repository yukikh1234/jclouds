
package org.jclouds.s3.handlers;

import static com.google.common.collect.Lists.newArrayList;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_SERVICE_PATH;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;

import java.net.URI;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jclouds.aws.domain.AWSError;
import org.jclouds.aws.handlers.ParseAWSErrorFromXmlContent;
import org.jclouds.aws.util.AWSUtils;
import org.jclouds.blobstore.ContainerNotFoundException;
import org.jclouds.blobstore.KeyNotFoundException;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpResponse;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.rest.ResourceNotFoundException;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

@Singleton
public class ParseS3ErrorFromXmlContent extends ParseAWSErrorFromXmlContent {

   private final String servicePath;
   private final boolean isVhostStyle;
   private final ProviderMetadata providerMetadata;

   @Inject
   public ParseS3ErrorFromXmlContent(AWSUtils utils, @Named(PROPERTY_S3_VIRTUAL_HOST_BUCKETS) boolean isVhostStyle,
            @Named(PROPERTY_S3_SERVICE_PATH) String servicePath, ProviderMetadata providerMetadata) {
      super(utils);
      this.servicePath = servicePath;
      this.isVhostStyle = isVhostStyle;
      this.providerMetadata = providerMetadata;
   }

   protected Exception refineException(HttpCommand command, HttpResponse response, Exception exception, AWSError error,
            String message) {
      if (response.getStatusCode() == 404) {
         if (!command.getCurrentRequest().getMethod().equals("DELETE")) {
            exception = handleNotFound(command, exception, message);
         }
         return exception;
      }
      return super.refineException(command, response, exception, error, message);
   }

   private Exception handleNotFound(HttpCommand command, Exception exception, String message) {
      URI defaultS3Endpoint = URI.create(providerMetadata.getApiMetadata().getDefaultEndpoint().get());
      URI requestEndpoint = command.getCurrentRequest().getEndpoint();
      boolean wasPathBasedRequest = isPathBasedRequest(defaultS3Endpoint, requestEndpoint);

      if (isVhostStyle && !wasPathBasedRequest) {
         exception = handleVhostStyleNotFound(command, message);
      } else if (isPathBasedRequest(command)) {
         exception = handlePathBasedRequestNotFound(command, message);
      } else {
         exception = new ResourceNotFoundException(message, exception);
      }
      return exception;
   }

   private boolean isPathBasedRequest(URI defaultS3Endpoint, URI requestEndpoint) {
      return requestEndpoint.getHost().contains(defaultS3Endpoint.getHost()) &&
             requestEndpoint.getHost().equals(defaultS3Endpoint.getHost());
   }

   private boolean isPathBasedRequest(HttpCommand command) {
      return command.getCurrentRequest().getEndpoint().getPath()
             .indexOf(servicePath.equals("/") ? "/" : servicePath + "/") == 0;
   }

   private Exception handleVhostStyleNotFound(HttpCommand command, String message) {
      String container = command.getCurrentRequest().getEndpoint().getHost();
      String key = command.getCurrentRequest().getEndpoint().getPath();
      if (key == null || key.equals("/")) {
         return new ContainerNotFoundException(container, message);
      } else {
         return new KeyNotFoundException(container, key, message);
      }
   }

   private Exception handlePathBasedRequestNotFound(HttpCommand command, String message) {
      String path = command.getCurrentRequest().getEndpoint().getPath().substring(servicePath.length());
      List<String> parts = newArrayList(Splitter.on('/').omitEmptyStrings().split(path));

      if (parts.size() == 1) {
         return new ContainerNotFoundException(parts.get(0), message);
      } else if (parts.size() > 1) {
         return new KeyNotFoundException(parts.remove(0), Joiner.on('/').join(parts), message);
      }
      return new ResourceNotFoundException(message);
   }
}
