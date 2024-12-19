

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_State extends State {

  private final int pid;
  private final boolean running;
  private final int exitCode;
  private final String startedAt;
  private final String finishedAt;
  private final boolean paused;
  private final boolean restarting;
  private final String status;
  private final boolean oomKilled;
  private final boolean dead;
  private final String error;

  AutoValue_State(
      int pid,
      boolean running,
      int exitCode,
      String startedAt,
      String finishedAt,
      boolean paused,
      boolean restarting,
      @Nullable String status,
      boolean oomKilled,
      boolean dead,
      @Nullable String error) {
    this.pid = pid;
    this.running = running;
    this.exitCode = exitCode;
    if (startedAt == null) {
      throw new NullPointerException("Null startedAt");
    }
    this.startedAt = startedAt;
    if (finishedAt == null) {
      throw new NullPointerException("Null finishedAt");
    }
    this.finishedAt = finishedAt;
    this.paused = paused;
    this.restarting = restarting;
    this.status = status;
    this.oomKilled = oomKilled;
    this.dead = dead;
    this.error = error;
  }

  @Override
  public int pid() {
    return pid;
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
  public String startedAt() {
    return startedAt;
  }

  @Override
  public String finishedAt() {
    return finishedAt;
  }

  @Override
  public boolean paused() {
    return paused;
  }

  @Override
  public boolean restarting() {
    return restarting;
  }

  @Nullable
  @Override
  public String status() {
    return status;
  }

  @Override
  public boolean oomKilled() {
    return oomKilled;
  }

  @Override
  public boolean dead() {
    return dead;
  }

  @Nullable
  @Override
  public String error() {
    return error;
  }

  @Override
  public String toString() {
    return "State{"
         + "pid=" + pid + ", "
         + "running=" + running + ", "
         + "exitCode=" + exitCode + ", "
         + "startedAt=" + startedAt + ", "
         + "finishedAt=" + finishedAt + ", "
         + "paused=" + paused + ", "
         + "restarting=" + restarting + ", "
         + "status=" + status + ", "
         + "oomKilled=" + oomKilled + ", "
         + "dead=" + dead + ", "
         + "error=" + error
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof State) {
      State that = (State) o;
      return (this.pid == that.pid())
           && (this.running == that.running())
           && (this.exitCode == that.exitCode())
           && (this.startedAt.equals(that.startedAt()))
           && (this.finishedAt.equals(that.finishedAt()))
           && (this.paused == that.paused())
           && (this.restarting == that.restarting())
           && ((this.status == null) ? (that.status() == null) : this.status.equals(that.status()))
           && (this.oomKilled == that.oomKilled())
           && (this.dead == that.dead())
           && ((this.error == null) ? (that.error() == null) : this.error.equals(that.error()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= pid;
    h$ *= 1000003;
    h$ ^= running ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= exitCode;
    h$ *= 1000003;
    h$ ^= startedAt.hashCode();
    h$ *= 1000003;
    h$ ^= finishedAt.hashCode();
    h$ *= 1000003;
    h$ ^= paused ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= restarting ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= (status == null) ? 0 : status.hashCode();
    h$ *= 1000003;
    h$ ^= oomKilled ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= dead ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= (error == null) ? 0 : error.hashCode();
    return h$;
  }

}
