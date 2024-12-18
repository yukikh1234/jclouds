
package org.jclouds.s3.blobstore.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.Blob.Factory;
import org.jclouds.s3.domain.S3Object;

import com.google.common.base.Function;

@Singleton
public class ObjectToBlob implements Function<S3Object, Blob> {
   private final Factory blobFactory;
   private final ObjectToBlobMetadata object2BlobMd;

   @Inject
   ObjectToBlob(Factory blobFactory, ObjectToBlobMetadata object2BlobMd) {
      this.blobFactory = checkNotNull(blobFactory, "blobFactory");
      this.object2BlobMd = checkNotNull(object2BlobMd, "object2BlobMd");
   }

   public Blob apply(S3Object from) {
      validateInput(from);
      return createBlob(from);
   }

   private void validateInput(S3Object from) {
      checkNotNull(from, "S3Object cannot be null");
      checkNotNull(from.getPayload(), "payload: " + from);
   }

   private Blob createBlob(S3Object from) {
      Blob blob = blobFactory.create(object2BlobMd.apply(from.getMetadata()));
      blob.setPayload(from.getPayload());
      blob.setAllHeaders(from.getAllHeaders());
      return blob;
   }
}
