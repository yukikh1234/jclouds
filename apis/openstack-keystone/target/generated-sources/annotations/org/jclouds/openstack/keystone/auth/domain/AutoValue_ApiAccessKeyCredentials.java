

package org.jclouds.openstack.keystone.auth.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ApiAccessKeyCredentials extends ApiAccessKeyCredentials {

  private final String accessKey;
  private final String secretKey;

  private AutoValue_ApiAccessKeyCredentials(
      String accessKey,
      String secretKey) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
  }

  @Override
  public String accessKey() {
    return accessKey;
  }

  @Override
  public String secretKey() {
    return secretKey;
  }

  @Override
  public String toString() {
    return "ApiAccessKeyCredentials{"
         + "accessKey=" + accessKey + ", "
         + "secretKey=" + secretKey
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ApiAccessKeyCredentials) {
      ApiAccessKeyCredentials that = (ApiAccessKeyCredentials) o;
      return (this.accessKey.equals(that.accessKey()))
           && (this.secretKey.equals(that.secretKey()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= accessKey.hashCode();
    h$ *= 1000003;
    h$ ^= secretKey.hashCode();
    return h$;
  }

  static final class Builder extends ApiAccessKeyCredentials.Builder {
    private String accessKey;
    private String secretKey;
    Builder() {
    }
    @Override
    public ApiAccessKeyCredentials.Builder accessKey(String accessKey) {
      if (accessKey == null) {
        throw new NullPointerException("Null accessKey");
      }
      this.accessKey = accessKey;
      return this;
    }
    @Override
    public ApiAccessKeyCredentials.Builder secretKey(String secretKey) {
      if (secretKey == null) {
        throw new NullPointerException("Null secretKey");
      }
      this.secretKey = secretKey;
      return this;
    }
    @Override
    public ApiAccessKeyCredentials build() {
      String missing = "";
      if (this.accessKey == null) {
        missing += " accessKey";
      }
      if (this.secretKey == null) {
        missing += " secretKey";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_ApiAccessKeyCredentials(
          this.accessKey,
          this.secretKey);
    }
  }

}
