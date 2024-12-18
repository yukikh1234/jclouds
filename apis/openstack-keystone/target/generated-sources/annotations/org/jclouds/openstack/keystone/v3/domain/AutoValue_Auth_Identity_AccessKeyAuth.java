

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_Identity_AccessKeyAuth extends Auth.Identity.AccessKeyAuth {

  private final String id;
  private final String secret;

  AutoValue_Auth_Identity_AccessKeyAuth(
      String id,
      String secret) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    if (secret == null) {
      throw new NullPointerException("Null secret");
    }
    this.secret = secret;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String secret() {
    return secret;
  }

  @Override
  public String toString() {
    return "AccessKeyAuth{"
         + "id=" + id + ", "
         + "secret=" + secret
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.Identity.AccessKeyAuth) {
      Auth.Identity.AccessKeyAuth that = (Auth.Identity.AccessKeyAuth) o;
      return (this.id.equals(that.id()))
           && (this.secret.equals(that.secret()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= secret.hashCode();
    return h$;
  }

}
