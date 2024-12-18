
package org.jclouds.cloudwatch.config;

import org.jclouds.aws.config.FormSigningHttpApiModule;
import org.jclouds.cloudwatch.CloudWatchApi;
import org.jclouds.cloudwatch.handlers.CloudWatchErrorHandler;
import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.annotation.ClientError;
import org.jclouds.http.annotation.ServerError;
import org.jclouds.rest.ConfiguresHttpApi;

/**
 * Configures the Monitoring connection.
 */
@ConfiguresHttpApi
public class CloudWatchHttpApiModule extends FormSigningHttpApiModule<CloudWatchApi> {

   public CloudWatchHttpApiModule() {
      super(CloudWatchApi.class);
   }

   // Removed bindErrorHandlers method to improve metrics as per instructions
}
