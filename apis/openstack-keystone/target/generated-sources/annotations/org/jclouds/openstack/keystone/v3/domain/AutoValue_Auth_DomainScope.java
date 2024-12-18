

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_DomainScope extends Auth.DomainScope {

  private final Auth.Name domain;

  AutoValue_Auth_DomainScope(
      Auth.Name domain) {
    if (domain == null) {
      throw new NullPointerException("Null domain");
    }
    this.domain = domain;
  }

  @Override
  public Auth.Name domain() {
    return domain;
  }

  @Override
  public String toString() {
    return "DomainScope{"
         + "domain=" + domain
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.DomainScope) {
      Auth.DomainScope that = (Auth.DomainScope) o;
      return (this.domain.equals(that.domain()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= domain.hashCode();
    return h$;
  }

}