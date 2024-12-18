

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_Identity_PasswordAuth_UserAuth extends Auth.Identity.PasswordAuth.UserAuth {

  private final String name;
  private final Auth.Identity.PasswordAuth.UserAuth.DomainAuth domain;
  private final String password;

  AutoValue_Auth_Identity_PasswordAuth_UserAuth(
      String name,
      Auth.Identity.PasswordAuth.UserAuth.DomainAuth domain,
      String password) {
    if (name == null) {
      throw new NullPointerException("Null name");
    }
    this.name = name;
    if (domain == null) {
      throw new NullPointerException("Null domain");
    }
    this.domain = domain;
    if (password == null) {
      throw new NullPointerException("Null password");
    }
    this.password = password;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Auth.Identity.PasswordAuth.UserAuth.DomainAuth domain() {
    return domain;
  }

  @Override
  public String password() {
    return password;
  }

  @Override
  public String toString() {
    return "UserAuth{"
         + "name=" + name + ", "
         + "domain=" + domain + ", "
         + "password=" + password
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.Identity.PasswordAuth.UserAuth) {
      Auth.Identity.PasswordAuth.UserAuth that = (Auth.Identity.PasswordAuth.UserAuth) o;
      return (this.name.equals(that.name()))
           && (this.domain.equals(that.domain()))
           && (this.password.equals(that.password()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= name.hashCode();
    h$ *= 1000003;
    h$ ^= domain.hashCode();
    h$ *= 1000003;
    h$ ^= password.hashCode();
    return h$;
  }

}
