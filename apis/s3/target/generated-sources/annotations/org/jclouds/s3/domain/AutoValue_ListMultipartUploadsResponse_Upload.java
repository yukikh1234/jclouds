

package org.jclouds.s3.domain;

import java.util.Date;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ListMultipartUploadsResponse_Upload extends ListMultipartUploadsResponse.Upload {

  private final String key;
  private final String uploadId;
  private final CanonicalUser initiator;
  private final CanonicalUser owner;
  private final ObjectMetadata.StorageClass storageClass;
  private final Date initiated;

  AutoValue_ListMultipartUploadsResponse_Upload(
      String key,
      String uploadId,
      CanonicalUser initiator,
      CanonicalUser owner,
      ObjectMetadata.StorageClass storageClass,
      Date initiated) {
    if (key == null) {
      throw new NullPointerException("Null key");
    }
    this.key = key;
    if (uploadId == null) {
      throw new NullPointerException("Null uploadId");
    }
    this.uploadId = uploadId;
    if (initiator == null) {
      throw new NullPointerException("Null initiator");
    }
    this.initiator = initiator;
    if (owner == null) {
      throw new NullPointerException("Null owner");
    }
    this.owner = owner;
    if (storageClass == null) {
      throw new NullPointerException("Null storageClass");
    }
    this.storageClass = storageClass;
    if (initiated == null) {
      throw new NullPointerException("Null initiated");
    }
    this.initiated = initiated;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public String uploadId() {
    return uploadId;
  }

  @Override
  public CanonicalUser initiator() {
    return initiator;
  }

  @Override
  public CanonicalUser owner() {
    return owner;
  }

  @Override
  public ObjectMetadata.StorageClass storageClass() {
    return storageClass;
  }

  @Override
  public Date initiated() {
    return initiated;
  }

  @Override
  public String toString() {
    return "Upload{"
         + "key=" + key + ", "
         + "uploadId=" + uploadId + ", "
         + "initiator=" + initiator + ", "
         + "owner=" + owner + ", "
         + "storageClass=" + storageClass + ", "
         + "initiated=" + initiated
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ListMultipartUploadsResponse.Upload) {
      ListMultipartUploadsResponse.Upload that = (ListMultipartUploadsResponse.Upload) o;
      return (this.key.equals(that.key()))
           && (this.uploadId.equals(that.uploadId()))
           && (this.initiator.equals(that.initiator()))
           && (this.owner.equals(that.owner()))
           && (this.storageClass.equals(that.storageClass()))
           && (this.initiated.equals(that.initiated()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= key.hashCode();
    h$ *= 1000003;
    h$ ^= uploadId.hashCode();
    h$ *= 1000003;
    h$ ^= initiator.hashCode();
    h$ *= 1000003;
    h$ ^= owner.hashCode();
    h$ *= 1000003;
    h$ ^= storageClass.hashCode();
    h$ *= 1000003;
    h$ ^= initiated.hashCode();
    return h$;
  }

}
