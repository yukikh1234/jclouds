
package org.jclouds.s3.blobstore.internal;

import static com.google.common.base.Throwables.propagate;

import jakarta.inject.Inject;

import org.jclouds.rest.ResourceNotFoundException;
import org.jclouds.s3.S3Client;
import org.jclouds.s3.domain.AccessControlList;

import com.google.common.annotations.Beta;
import com.google.common.cache.CacheLoader;

@Beta
public class BackoffOnNotFoundWhenGetBucketACL extends CacheLoader<String, AccessControlList> {
   private final S3Client client;
   private static final int maxTries = 5;

   @Inject
   BackoffOnNotFoundWhenGetBucketACL(S3Client client) {
      this.client = client;
   }

   @Override
   public AccessControlList load(String bucketName) {
      return attemptToGetBucketACL(bucketName);
   }

   private AccessControlList attemptToGetBucketACL(String bucketName) {
      ResourceNotFoundException lastException = null;
      for (int currentTries = 0; currentTries < maxTries; currentTries++) {
         try {
            return client.getBucketACL(bucketName);
         } catch (ResourceNotFoundException e) {
            performBackoff(currentTries);
            lastException = e;
         }
      }
      throw lastException;
   }

   private void performBackoff(int currentTries) {
      long period = 100L;
      long maxPeriod = 200L;
      int pow = 2;
      imposeBackoffExponentialDelay(period, maxPeriod, pow, currentTries);
   }

   private static void imposeBackoffExponentialDelay(long period, long maxPeriod, int pow, int failureCount) {
      long delayMs = (long) (period * Math.pow(failureCount, pow));
      delayMs = Math.min(delayMs, maxPeriod);
      try {
         Thread.sleep(delayMs);
      } catch (InterruptedException e) {
         throw propagate(e);
      }
   }

   @Override
   public String toString() {
      return "getBucketAcl()";
   }
}
