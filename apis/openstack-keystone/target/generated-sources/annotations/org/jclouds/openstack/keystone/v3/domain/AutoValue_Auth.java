

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth extends Auth {

  private final Auth.Identity identity;
  private final Object scope;

  AutoValue_Auth(
      Auth.Identity identity,
      @Nullable Object scope) {
    if (identity == null) {
      throw new NullPointerException("Null identity");
    }
    this.identity = identity;
    this.scope = scope;
  }

  @Override
  public Auth.Identity identity() {
    return identity;
  }

  @Nullable
  @Override
  public Object scope() {
    return scope;
  }

  @Override
  public String toString() {
    return "Auth{"
         + "identity=" + identity + ", "
         + "scope=" + scope
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth) {
      Auth that = (Auth) o;
      return (this.identity.equals(that.identity()))
           && ((this.scope == null) ? (that.scope() == null) : this.scope.equals(that.scope()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= identity.hashCode();
    h$ *= 1000003;
    h$ ^= (scope == null) ? 0 : scope.hashCode();
    return h$;
  }

}
