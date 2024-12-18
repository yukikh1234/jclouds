

package org.jclouds.s3.domain;

import java.util.Date;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ListMultipartUploadResponse extends ListMultipartUploadResponse {

  private final int partNumber;
  private final Date lastModified;
  private final String eTag;
  private final long size;

  AutoValue_ListMultipartUploadResponse(
      int partNumber,
      @Nullable Date lastModified,
      String eTag,
      long size) {
    this.partNumber = partNumber;
    this.lastModified = lastModified;
    if (eTag == null) {
      throw new NullPointerException("Null eTag");
    }
    this.eTag = eTag;
    this.size = size;
  }

  @Override
  public int partNumber() {
    return partNumber;
  }

  @Nullable
  @Override
  public Date lastModified() {
    return lastModified;
  }

  @Override
  public String eTag() {
    return eTag;
  }

  @Override
  public long size() {
    return size;
  }

  @Override
  public String toString() {
    return "ListMultipartUploadResponse{"
         + "partNumber=" + partNumber + ", "
         + "lastModified=" + lastModified + ", "
         + "eTag=" + eTag + ", "
         + "size=" + size
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ListMultipartUploadResponse) {
      ListMultipartUploadResponse that = (ListMultipartUploadResponse) o;
      return (this.partNumber == that.partNumber())
           && ((this.lastModified == null) ? (that.lastModified() == null) : this.lastModified.equals(that.lastModified()))
           && (this.eTag.equals(that.eTag()))
           && (this.size == that.size());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= partNumber;
    h$ *= 1000003;
    h$ ^= (lastModified == null) ? 0 : lastModified.hashCode();
    h$ *= 1000003;
    h$ ^= eTag.hashCode();
    h$ *= 1000003;
    h$ ^= (int) ((size >>> 32) ^ size);
    return h$;
  }

}
