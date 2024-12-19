

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ExecInspect extends ExecInspect {

  private final String id;
  private final boolean running;
  private final int exitCode;

  AutoValue_ExecInspect(
      String id,
      boolean running,
      int exitCode) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    this.running = running;
    this.exitCode = exitCode;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public boolean running() {
    return running;
  }

  @Override
  public int exitCode() {
    return exitCode;
  }

  @Override
  public String toString() {
    return "ExecInspect{"
         + "id=" + id + ", "
         + "running=" + running + ", "
         + "exitCode=" + exitCode
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ExecInspect) {
      ExecInspect that = (ExecInspect) o;
      return (this.id.equals(that.id()))
           && (this.running == that.running())
           && (this.exitCode == that.exitCode());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= running ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= exitCode;
    return h$;
  }

}
