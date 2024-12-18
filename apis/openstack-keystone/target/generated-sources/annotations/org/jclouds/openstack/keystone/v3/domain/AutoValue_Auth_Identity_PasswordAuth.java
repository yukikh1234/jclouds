

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_Identity_PasswordAuth extends Auth.Identity.PasswordAuth {

  private final Auth.Identity.PasswordAuth.UserAuth user;

  AutoValue_Auth_Identity_PasswordAuth(
      Auth.Identity.PasswordAuth.UserAuth user) {
    if (user == null) {
      throw new NullPointerException("Null user");
    }
    this.user = user;
  }

  @Override
  public Auth.Identity.PasswordAuth.UserAuth user() {
    return user;
  }

  @Override
  public String toString() {
    return "PasswordAuth{"
         + "user=" + user
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.Identity.PasswordAuth) {
      Auth.Identity.PasswordAuth that = (Auth.Identity.PasswordAuth) o;
      return (this.user.equals(that.user()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= user.hashCode();
    return h$;
  }

}
