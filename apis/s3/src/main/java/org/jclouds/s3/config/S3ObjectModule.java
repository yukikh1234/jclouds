
package org.jclouds.s3.config;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import org.jclouds.blobstore.config.BlobStoreObjectModule;
import org.jclouds.s3.domain.MutableObjectMetadata;
import org.jclouds.s3.domain.S3Object;
import org.jclouds.s3.domain.internal.S3ObjectImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Configures the domain object mappings needed for all S3 implementations
 */
public class S3ObjectModule extends AbstractModule {

   @Override
   protected void configure() {
      install(new BlobStoreObjectModule());
      bind(S3Object.Factory.class).to(S3ObjectFactory.class).asEagerSingleton();
   }

   private static class S3ObjectFactory implements S3Object.Factory {
      private final Provider<MutableObjectMetadata> metadataProvider;

      @Inject
      S3ObjectFactory(Provider<MutableObjectMetadata> metadataProvider) {
         this.metadataProvider = metadataProvider;
      }

      @Override
      public S3Object create(MutableObjectMetadata metadata) {
         MutableObjectMetadata effectiveMetadata = (metadata != null) ? metadata : metadataProvider.get();
         return new S3ObjectImpl(effectiveMetadata);
      }
   }

   @Provides
   final S3Object provideS3Object(S3Object.Factory factory) {
      return factory.create(null);
   }
}
