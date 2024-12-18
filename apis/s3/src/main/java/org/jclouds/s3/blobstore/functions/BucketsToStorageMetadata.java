
package org.jclouds.s3.blobstore.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
import java.util.concurrent.Callable;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.internal.PageSetImpl;
import org.jclouds.concurrent.FutureIterables;
import org.jclouds.logging.Logger;
import org.jclouds.s3.domain.BucketMetadata;

import com.google.common.base.Function;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

@Singleton
public class BucketsToStorageMetadata implements
         Function<Set<BucketMetadata>, PageSet<? extends StorageMetadata>> {

   @Resource
   protected Logger logger = Logger.NULL;
   
   private final ListeningExecutorService userExecutor;
   private final BucketToResourceMetadata bucket2ResourceMd;

   @Inject
   public BucketsToStorageMetadata(@Named(Constants.PROPERTY_USER_THREADS) ListeningExecutorService userExecutor, BucketToResourceMetadata bucket2ResourceMd) {
      this.userExecutor = checkNotNull(userExecutor, "userExecutor");
      this.bucket2ResourceMd = checkNotNull(bucket2ResourceMd, "bucket2ResourceMd");
   }

   @Override
   public PageSet<? extends StorageMetadata> apply(Set<BucketMetadata> input) {
      Iterable<? extends StorageMetadata> buckets = FutureIterables
               .<BucketMetadata, StorageMetadata> transformParallel(input,
                        new BucketMetadataToStorageMetadataFunction(), userExecutor, null, logger, "my buckets");
      return new PageSetImpl<StorageMetadata>(buckets, null);
   }

   private class BucketMetadataToStorageMetadataFunction 
         implements Function<BucketMetadata, ListenableFuture<? extends StorageMetadata>> {
      @Override
      public ListenableFuture<? extends StorageMetadata> apply(final BucketMetadata from) {
         return userExecutor.submit(new BucketMetadataCallable(from));
      }
   }

   private class BucketMetadataCallable implements Callable<StorageMetadata> {
      private final BucketMetadata from;

      BucketMetadataCallable(BucketMetadata from) {
         this.from = from;
      }

      @Override
      public StorageMetadata call() throws Exception {
         return bucket2ResourceMd.apply(from);
      }

      @Override
      public String toString() {
         return "bucket2ResourceMd.apply(" + from + ")";
      }
   }
}
