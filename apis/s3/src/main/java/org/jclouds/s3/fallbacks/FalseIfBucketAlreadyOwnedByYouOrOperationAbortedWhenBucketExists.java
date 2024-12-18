
package org.jclouds.s3.fallbacks;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static org.jclouds.s3.util.S3Utils.getBucketName;
import static org.jclouds.util.Throwables2.getFirstThrowableOfType;

import jakarta.inject.Inject;

import org.jclouds.Fallback;
import org.jclouds.aws.AWSResponseException;
import org.jclouds.http.HttpRequest;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.rest.InvocationContext;
import org.jclouds.s3.S3Client;

public class FalseIfBucketAlreadyOwnedByYouOrOperationAbortedWhenBucketExists implements Fallback<Boolean>,
      InvocationContext<FalseIfBucketAlreadyOwnedByYouOrOperationAbortedWhenBucketExists> {

   private final S3Client client;
   private String bucket;

   @Inject
   FalseIfBucketAlreadyOwnedByYouOrOperationAbortedWhenBucketExists(S3Client client) {
      this.client = client;
   }

   @Override
   public Boolean createOrPropagate(Throwable t) throws Exception {
      AWSResponseException exception = getAWSErrorResponseException(t);
      if (exception != null) {
         String code = exception.getError().getCode();
         if (isBucketAlreadyOwnedByYou(code) || isOperationAbortedAndBucketExists(code))
            return false;
      }
      throw propagate(t);
   }

   private AWSResponseException getAWSErrorResponseException(Throwable t) {
      return getFirstThrowableOfType(checkNotNull(t, "throwable"), AWSResponseException.class);
   }

   private boolean isBucketAlreadyOwnedByYou(String code) {
      return "BucketAlreadyOwnedByYou".equals(code);
   }

   private boolean isOperationAbortedAndBucketExists(String code) throws Exception {
      return "OperationAborted".equals(code) && bucket != null && client.bucketExists(bucket);
   }

   @Override
   public FalseIfBucketAlreadyOwnedByYouOrOperationAbortedWhenBucketExists setContext(@Nullable HttpRequest request) {
      if (request != null) {
         this.bucket = getBucketName(request);
      }
      return this;
   }
}
