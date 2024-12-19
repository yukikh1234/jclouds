

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Node extends Node {

  private final String ip;

  AutoValue_Node(
      @Nullable String ip) {
    this.ip = ip;
  }

  @Nullable
  @Override
  public String ip() {
    return ip;
  }

  @Override
  public String toString() {
    return "Node{"
         + "ip=" + ip
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Node) {
      Node that = (Node) o;
      return ((this.ip == null) ? (that.ip() == null) : this.ip.equals(that.ip()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (ip == null) ? 0 : ip.hashCode();
    return h$;
  }

}
