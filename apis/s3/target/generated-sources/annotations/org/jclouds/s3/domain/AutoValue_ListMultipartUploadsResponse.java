

package org.jclouds.s3.domain;

import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ListMultipartUploadsResponse extends ListMultipartUploadsResponse {

  private final String bucket;
  private final String keyMarker;
  private final String uploadIdMarker;
  private final String nextKeyMarker;
  private final String nextUploadIdMarker;
  private final int maxUploads;
  private final boolean isTruncated;
  private final List<ListMultipartUploadsResponse.Upload> uploads;

  AutoValue_ListMultipartUploadsResponse(
      String bucket,
      @Nullable String keyMarker,
      @Nullable String uploadIdMarker,
      @Nullable String nextKeyMarker,
      @Nullable String nextUploadIdMarker,
      int maxUploads,
      boolean isTruncated,
      List<ListMultipartUploadsResponse.Upload> uploads) {
    if (bucket == null) {
      throw new NullPointerException("Null bucket");
    }
    this.bucket = bucket;
    this.keyMarker = keyMarker;
    this.uploadIdMarker = uploadIdMarker;
    this.nextKeyMarker = nextKeyMarker;
    this.nextUploadIdMarker = nextUploadIdMarker;
    this.maxUploads = maxUploads;
    this.isTruncated = isTruncated;
    if (uploads == null) {
      throw new NullPointerException("Null uploads");
    }
    this.uploads = uploads;
  }

  @Override
  public String bucket() {
    return bucket;
  }

  @Nullable
  @Override
  public String keyMarker() {
    return keyMarker;
  }

  @Nullable
  @Override
  public String uploadIdMarker() {
    return uploadIdMarker;
  }

  @Nullable
  @Override
  public String nextKeyMarker() {
    return nextKeyMarker;
  }

  @Nullable
  @Override
  public String nextUploadIdMarker() {
    return nextUploadIdMarker;
  }

  @Override
  public int maxUploads() {
    return maxUploads;
  }

  @Override
  public boolean isTruncated() {
    return isTruncated;
  }

  @Override
  public List<ListMultipartUploadsResponse.Upload> uploads() {
    return uploads;
  }

  @Override
  public String toString() {
    return "ListMultipartUploadsResponse{"
         + "bucket=" + bucket + ", "
         + "keyMarker=" + keyMarker + ", "
         + "uploadIdMarker=" + uploadIdMarker + ", "
         + "nextKeyMarker=" + nextKeyMarker + ", "
         + "nextUploadIdMarker=" + nextUploadIdMarker + ", "
         + "maxUploads=" + maxUploads + ", "
         + "isTruncated=" + isTruncated + ", "
         + "uploads=" + uploads
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ListMultipartUploadsResponse) {
      ListMultipartUploadsResponse that = (ListMultipartUploadsResponse) o;
      return (this.bucket.equals(that.bucket()))
           && ((this.keyMarker == null) ? (that.keyMarker() == null) : this.keyMarker.equals(that.keyMarker()))
           && ((this.uploadIdMarker == null) ? (that.uploadIdMarker() == null) : this.uploadIdMarker.equals(that.uploadIdMarker()))
           && ((this.nextKeyMarker == null) ? (that.nextKeyMarker() == null) : this.nextKeyMarker.equals(that.nextKeyMarker()))
           && ((this.nextUploadIdMarker == null) ? (that.nextUploadIdMarker() == null) : this.nextUploadIdMarker.equals(that.nextUploadIdMarker()))
           && (this.maxUploads == that.maxUploads())
           && (this.isTruncated == that.isTruncated())
           && (this.uploads.equals(that.uploads()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= bucket.hashCode();
    h$ *= 1000003;
    h$ ^= (keyMarker == null) ? 0 : keyMarker.hashCode();
    h$ *= 1000003;
    h$ ^= (uploadIdMarker == null) ? 0 : uploadIdMarker.hashCode();
    h$ *= 1000003;
    h$ ^= (nextKeyMarker == null) ? 0 : nextKeyMarker.hashCode();
    h$ *= 1000003;
    h$ ^= (nextUploadIdMarker == null) ? 0 : nextUploadIdMarker.hashCode();
    h$ *= 1000003;
    h$ ^= maxUploads;
    h$ *= 1000003;
    h$ ^= isTruncated ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= uploads.hashCode();
    return h$;
  }

}
