
package org.jclouds.s3.blobstore.internal;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.Context;
import org.jclouds.blobstore.BlobRequestSigner;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.attr.ConsistencyModel;
import org.jclouds.blobstore.internal.BlobStoreContextImpl;
import org.jclouds.location.Provider;
import org.jclouds.rest.Utils;
import org.jclouds.s3.blobstore.S3BlobStore;
import org.jclouds.s3.blobstore.S3BlobStoreContext;

import com.google.common.reflect.TypeToken;

@Singleton
public class S3BlobStoreContextImpl extends BlobStoreContextImpl implements S3BlobStoreContext {

   private final S3BlobStore s3BlobStore;

   @Inject
   public S3BlobStoreContextImpl(@Provider Context backend, 
                                 @Provider TypeToken<? extends Context> backendType,
                                 Utils utils, 
                                 ConsistencyModel consistencyModel, 
                                 BlobStore blobStore, 
                                 BlobRequestSigner blobRequestSigner) {
      super(backend, backendType, utils, consistencyModel, blobStore, blobRequestSigner);
      this.s3BlobStore = S3BlobStore.class.cast(blobStore);
   }

   @Override
   public S3BlobStore getBlobStore() {
      return s3BlobStore;
   }
}
