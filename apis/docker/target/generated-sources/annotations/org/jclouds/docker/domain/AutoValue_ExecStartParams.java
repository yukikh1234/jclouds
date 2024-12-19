

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ExecStartParams extends ExecStartParams {

  private final boolean detach;

  private AutoValue_ExecStartParams(
      boolean detach) {
    this.detach = detach;
  }

  @Override
  public boolean detach() {
    return detach;
  }

  @Override
  public String toString() {
    return "ExecStartParams{"
         + "detach=" + detach
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ExecStartParams) {
      ExecStartParams that = (ExecStartParams) o;
      return (this.detach == that.detach());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= detach ? 1231 : 1237;
    return h$;
  }

  static final class Builder extends ExecStartParams.Builder {
    private Boolean detach;
    Builder() {
    }
    @Override
    public ExecStartParams.Builder detach(boolean detach) {
      this.detach = detach;
      return this;
    }
    @Override
    public ExecStartParams build() {
      String missing = "";
      if (this.detach == null) {
        missing += " detach";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_ExecStartParams(
          this.detach);
    }
  }

}
