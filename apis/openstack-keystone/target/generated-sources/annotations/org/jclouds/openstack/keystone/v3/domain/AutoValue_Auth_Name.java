

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_Name extends Auth.Name {

  private final String name;

  AutoValue_Auth_Name(
      @Nullable String name) {
    this.name = name;
  }

  @Nullable
  @Override
  public String name() {
    return name;
  }

  @Override
  public String toString() {
    return "Name{"
         + "name=" + name
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.Name) {
      Auth.Name that = (Auth.Name) o;
      return ((this.name == null) ? (that.name() == null) : this.name.equals(that.name()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (name == null) ? 0 : name.hashCode();
    return h$;
  }

}
