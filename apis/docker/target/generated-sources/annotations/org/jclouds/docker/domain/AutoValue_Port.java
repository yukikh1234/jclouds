

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Port extends Port {

  private final String ip;
  private final int privatePort;
  private final Integer publicPort;
  private final String type;

  AutoValue_Port(
      @Nullable String ip,
      int privatePort,
      @Nullable Integer publicPort,
      String type) {
    this.ip = ip;
    this.privatePort = privatePort;
    this.publicPort = publicPort;
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
  }

  @Nullable
  @Override
  public String ip() {
    return ip;
  }

  @Override
  public int privatePort() {
    return privatePort;
  }

  @Nullable
  @Override
  public Integer publicPort() {
    return publicPort;
  }

  @Override
  public String type() {
    return type;
  }

  @Override
  public String toString() {
    return "Port{"
         + "ip=" + ip + ", "
         + "privatePort=" + privatePort + ", "
         + "publicPort=" + publicPort + ", "
         + "type=" + type
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Port) {
      Port that = (Port) o;
      return ((this.ip == null) ? (that.ip() == null) : this.ip.equals(that.ip()))
           && (this.privatePort == that.privatePort())
           && ((this.publicPort == null) ? (that.publicPort() == null) : this.publicPort.equals(that.publicPort()))
           && (this.type.equals(that.type()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (ip == null) ? 0 : ip.hashCode();
    h$ *= 1000003;
    h$ ^= privatePort;
    h$ *= 1000003;
    h$ ^= (publicPort == null) ? 0 : publicPort.hashCode();
    h$ *= 1000003;
    h$ ^= type.hashCode();
    return h$;
  }

}
