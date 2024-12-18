

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_Id extends Auth.Id {

  private final String id;

  AutoValue_Auth_Id(
      String id) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String toString() {
    return "Id{"
         + "id=" + id
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.Id) {
      Auth.Id that = (Auth.Id) o;
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

}
