

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Resource extends Resource {

  private final String resource;

  AutoValue_Resource(
      String resource) {
    if (resource == null) {
      throw new NullPointerException("Null resource");
    }
    this.resource = resource;
  }

  @Override
  public String resource() {
    return resource;
  }

  @Override
  public String toString() {
    return "Resource{"
         + "resource=" + resource
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Resource) {
      Resource that = (Resource) o;
      return (this.resource.equals(that.resource()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= resource.hashCode();
    return h$;
  }

}
