
package org.jclouds.s3.functions;

import java.util.concurrent.ExecutionException;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.logging.Logger;
import org.jclouds.s3.Bucket;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

@Singleton
public class GetRegionForBucket implements Function<String, Optional<String>> {
   @Resource
   protected Logger logger = Logger.NULL;

   protected final LoadingCache<String, Optional<String>> bucketToRegion;

   @Inject
   public GetRegionForBucket(@Bucket LoadingCache<String, Optional<String>> bucketToRegion) {
      this.bucketToRegion = bucketToRegion;
   }

   @Override
   public Optional<String> apply(String bucket) {
      try {
         return getRegionForBucket(bucket);
      } catch (ExecutionException e) {
         handleExecutionException(bucket, e);
      } catch (UncheckedExecutionException e) {
         handleUncheckedExecutionException(bucket, e);
      } catch (InvalidCacheLoadException e) {
         handleInvalidCacheLoadException(bucket, e);
      }
      return Optional.absent();
   }

   private Optional<String> getRegionForBucket(String bucket) throws ExecutionException {
      return bucketToRegion.get(bucket);
   }

   private void handleExecutionException(String bucket, ExecutionException e) {
      logger.debug("error looking up region for bucket %s: %s", bucket, e);
   }

   private void handleUncheckedExecutionException(String bucket, UncheckedExecutionException e) {
      logger.debug("error looking up region for bucket %s: %s", bucket, e);
   }

   private void handleInvalidCacheLoadException(String bucket, InvalidCacheLoadException e) {
      logger.trace("bucket %s not found: %s", bucket, e);
   }
}
