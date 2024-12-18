
package org.jclouds.s3.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.Beta;
import org.jclouds.javax.annotation.Nullable;

@AutoValue
@Beta
public abstract class ListMultipartUploadResponse {
   public abstract int partNumber();
   @Nullable public abstract Date lastModified();
   public abstract String eTag();
   public abstract long size();

   public static ListMultipartUploadResponse create(int partNumber, @Nullable Date lastModified, String eTag, long size) {
      validateArguments(partNumber, eTag, size);
      Date clonedLastModified = cloneDate(lastModified);
      return new AutoValue_ListMultipartUploadResponse(partNumber, clonedLastModified, eTag, size);
   }

   private static void validateArguments(int partNumber, String eTag, long size) {
      checkArgument(partNumber > 0, "partNumber must be greater than zero, was: %s", partNumber);
      checkNotNull(eTag, "eTag");
      checkArgument(size >= 0, "size must be positive, was: %s", size);
   }

   @Nullable
   private static Date cloneDate(@Nullable Date date) {
      return date != null ? (Date) date.clone() : null;
   }
}
