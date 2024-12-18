

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Link extends Link {

  private final String self;

  AutoValue_Link(
      String self) {
    if (self == null) {
      throw new NullPointerException("Null self");
    }
    this.self = self;
  }

  @Override
  public String self() {
    return self;
  }

  @Override
  public String toString() {
    return "Link{"
         + "self=" + self
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Link) {
      Link that = (Link) o;
      return (this.self.equals(that.self()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= self.hashCode();
    return h$;
  }

}
