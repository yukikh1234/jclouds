

package org.jclouds.openstack.keystone.v3.domain;

import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_Identity extends Auth.Identity {

  private final List<String> methods;
  private final Auth.Id token;
  private final Auth.Identity.PasswordAuth password;
  private final Auth.Identity.AccessKeyAuth secret;

  AutoValue_Auth_Identity(
      List<String> methods,
      @Nullable Auth.Id token,
      @Nullable Auth.Identity.PasswordAuth password,
      @Nullable Auth.Identity.AccessKeyAuth secret) {
    if (methods == null) {
      throw new NullPointerException("Null methods");
    }
    this.methods = methods;
    this.token = token;
    this.password = password;
    this.secret = secret;
  }

  @Override
  public List<String> methods() {
    return methods;
  }

  @Nullable
  @Override
  public Auth.Id token() {
    return token;
  }

  @Nullable
  @Override
  public Auth.Identity.PasswordAuth password() {
    return password;
  }

  @Nullable
  @Override
  public Auth.Identity.AccessKeyAuth secret() {
    return secret;
  }

  @Override
  public String toString() {
    return "Identity{"
         + "methods=" + methods + ", "
         + "token=" + token + ", "
         + "password=" + password + ", "
         + "secret=" + secret
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.Identity) {
      Auth.Identity that = (Auth.Identity) o;
      return (this.methods.equals(that.methods()))
           && ((this.token == null) ? (that.token() == null) : this.token.equals(that.token()))
           && ((this.password == null) ? (that.password() == null) : this.password.equals(that.password()))
           && ((this.secret == null) ? (that.secret() == null) : this.secret.equals(that.secret()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= methods.hashCode();
    h$ *= 1000003;
    h$ ^= (token == null) ? 0 : token.hashCode();
    h$ *= 1000003;
    h$ ^= (password == null) ? 0 : password.hashCode();
    h$ *= 1000003;
    h$ ^= (secret == null) ? 0 : secret.hashCode();
    return h$;
  }

}
