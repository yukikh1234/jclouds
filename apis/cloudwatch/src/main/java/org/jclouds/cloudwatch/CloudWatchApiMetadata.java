
package org.jclouds.cloudwatch;

import static org.jclouds.aws.reference.AWSConstants.PROPERTY_AUTH_TAG;
import static org.jclouds.aws.reference.AWSConstants.PROPERTY_HEADER_TAG;

import java.net.URI;
import java.util.Properties;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.cloudwatch.config.CloudWatchHttpApiModule;
import org.jclouds.rest.internal.BaseHttpApiMetadata;

import com.google.auto.service.AutoService;

/**
 * Implementation of {@link ApiMetadata} for Amazon's CloudWatch api.
 */
@AutoService(ApiMetadata.class)
public class CloudWatchApiMetadata extends BaseHttpApiMetadata {

   private static final String IDENTITY_NAME = "Access Key ID";
   private static final String CREDENTIAL_NAME = "Secret Access Key";
   private static final String VERSION = "2010-08-01";
   private static final String DOCUMENTATION_URL = "http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/";
   private static final String DEFAULT_ENDPOINT = "https://monitoring.us-east-1.amazonaws.com";

   public CloudWatchApiMetadata() {
      this(new ConcreteBuilder());
   }

   protected CloudWatchApiMetadata(Builder<?> builder) {
      super(builder);
   }

   @Override
   public Builder<?> toBuilder() {
      return new ConcreteBuilder().fromApiMetadata(this);
   }

   public static Properties defaultProperties() {
      Properties properties = BaseHttpApiMetadata.defaultProperties();
      properties.setProperty(PROPERTY_AUTH_TAG, "AWS");
      properties.setProperty(PROPERTY_HEADER_TAG, "amz");
      return properties;
   }

   public abstract static class Builder<T extends Builder<T>> extends BaseHttpApiMetadata.Builder<CloudWatchApi, T> {

      protected Builder() {
         id("cloudwatch")
         .name("Amazon CloudWatch Api")
         .identityName(IDENTITY_NAME)
         .credentialName(CREDENTIAL_NAME)
         .version(VERSION)
         .documentation(URI.create(DOCUMENTATION_URL))
         .defaultEndpoint(DEFAULT_ENDPOINT)
         .defaultProperties(CloudWatchApiMetadata.defaultProperties())
         .defaultModule(CloudWatchHttpApiModule.class);
      }

      @Override
      public CloudWatchApiMetadata build() {
         return new CloudWatchApiMetadata(this);
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }
}
