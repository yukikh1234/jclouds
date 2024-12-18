
package org.jclouds.s3.blobstore.functions;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.blobstore.domain.MutableStorageMetadata;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.domain.internal.MutableStorageMetadataImpl;
import org.jclouds.domain.Location;
import org.jclouds.s3.domain.BucketMetadata;

import com.google.common.base.Function;

@Singleton
public class BucketToResourceMetadata implements Function<BucketMetadata, StorageMetadata> {
   private final Function<String, Location> locationOfBucket;

   @Inject
   public BucketToResourceMetadata(Function<String, Location> locationOfBucket) {
      this.locationOfBucket = locationOfBucket;
   }

   @Override
   public StorageMetadata apply(BucketMetadata from) {
      return createStorageMetadata(from);
   }

   private StorageMetadata createStorageMetadata(BucketMetadata from) {
      MutableStorageMetadata to = new MutableStorageMetadataImpl();
      to.setName(from.getName());
      to.setType(StorageType.CONTAINER);
      to.setLocation(locationOfBucket.apply(from.getName()));
      to.setCreationDate(from.getCreationDate());
      return to;
   }
}
