

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_StatusCode extends StatusCode {

  private final int statusCode;

  AutoValue_StatusCode(
      int statusCode) {
    this.statusCode = statusCode;
  }

  @Override
  public int statusCode() {
    return statusCode;
  }

  @Override
  public String toString() {
    return "StatusCode{"
         + "statusCode=" + statusCode
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof StatusCode) {
      StatusCode that = (StatusCode) o;
      return (this.statusCode == that.statusCode());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= statusCode;
    return h$;
  }

}
