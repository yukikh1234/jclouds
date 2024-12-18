

package org.jclouds.openstack.keystone.auth.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_TokenCredentials extends TokenCredentials {

  private final String id;

  private AutoValue_TokenCredentials(
      String id) {
    this.id = id;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String toString() {
    return "TokenCredentials{"
         + "id=" + id
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof TokenCredentials) {
      TokenCredentials that = (TokenCredentials) o;
      return (this.id.equals(that.id()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    return h$;
  }

  static final class Builder extends TokenCredentials.Builder {
    private String id;
    Builder() {
    }
    @Override
    public TokenCredentials.Builder id(String id) {
      if (id == null) {
        throw new NullPointerException("Null id");
      }
      this.id = id;
      return this;
    }
    @Override
    public TokenCredentials build() {
      String missing = "";
      if (this.id == null) {
        missing += " id";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_TokenCredentials(
          this.id);
    }
  }

}
