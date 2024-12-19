

package org.jclouds.docker.domain;

import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ExecCreateParams extends ExecCreateParams {

  private final boolean attachStdout;
  private final boolean attachStderr;
  private final List<String> cmd;

  private AutoValue_ExecCreateParams(
      boolean attachStdout,
      boolean attachStderr,
      List<String> cmd) {
    this.attachStdout = attachStdout;
    this.attachStderr = attachStderr;
    this.cmd = cmd;
  }

  @Override
  public boolean attachStdout() {
    return attachStdout;
  }

  @Override
  public boolean attachStderr() {
    return attachStderr;
  }

  @Override
  public List<String> cmd() {
    return cmd;
  }

  @Override
  public String toString() {
    return "ExecCreateParams{"
         + "attachStdout=" + attachStdout + ", "
         + "attachStderr=" + attachStderr + ", "
         + "cmd=" + cmd
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ExecCreateParams) {
      ExecCreateParams that = (ExecCreateParams) o;
      return (this.attachStdout == that.attachStdout())
           && (this.attachStderr == that.attachStderr())
           && (this.cmd.equals(that.cmd()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= attachStdout ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= attachStderr ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= cmd.hashCode();
    return h$;
  }

  static final class Builder extends ExecCreateParams.Builder {
    private Boolean attachStdout;
    private Boolean attachStderr;
    private List<String> cmd;
    Builder() {
    }
    @Override
    public ExecCreateParams.Builder attachStdout(boolean attachStdout) {
      this.attachStdout = attachStdout;
      return this;
    }
    @Override
    public ExecCreateParams.Builder attachStderr(boolean attachStderr) {
      this.attachStderr = attachStderr;
      return this;
    }
    @Override
    public ExecCreateParams.Builder cmd(List<String> cmd) {
      if (cmd == null) {
        throw new NullPointerException("Null cmd");
      }
      this.cmd = cmd;
      return this;
    }
    @Override
    List<String> cmd() {
      if (cmd == null) {
        throw new IllegalStateException("Property \"cmd\" has not been set");
      }
      return cmd;
    }
    @Override
    ExecCreateParams autoBuild() {
      String missing = "";
      if (this.attachStdout == null) {
        missing += " attachStdout";
      }
      if (this.attachStderr == null) {
        missing += " attachStderr";
      }
      if (this.cmd == null) {
        missing += " cmd";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_ExecCreateParams(
          this.attachStdout,
          this.attachStderr,
          this.cmd);
    }
  }

}
