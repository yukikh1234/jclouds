

package org.jclouds.openstack.keystone.auth.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_PasswordCredentials extends PasswordCredentials {

  private final String username;
  private final String password;

  private AutoValue_PasswordCredentials(
      String username,
      String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public String username() {
    return username;
  }

  @Override
  public String password() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PasswordCredentials) {
      PasswordCredentials that = (PasswordCredentials) o;
      return (this.username.equals(that.username()))
           && (this.password.equals(that.password()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= username.hashCode();
    h$ *= 1000003;
    h$ ^= password.hashCode();
    return h$;
  }

  static final class Builder extends PasswordCredentials.Builder {
    private String username;
    private String password;
    Builder() {
    }
    @Override
    public PasswordCredentials.Builder username(String username) {
      if (username == null) {
        throw new NullPointerException("Null username");
      }
      this.username = username;
      return this;
    }
    @Override
    public PasswordCredentials.Builder password(String password) {
      if (password == null) {
        throw new NullPointerException("Null password");
      }
      this.password = password;
      return this;
    }
    @Override
    public PasswordCredentials build() {
      String missing = "";
      if (this.username == null) {
        missing += " username";
      }
      if (this.password == null) {
        missing += " password";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_PasswordCredentials(
          this.username,
          this.password);
    }
  }

}
