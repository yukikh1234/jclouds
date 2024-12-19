

package org.jclouds.docker.domain;

import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ExposedPorts extends ExposedPorts {

  private final String portAndProtocol;
  private final List<String> hostPorts;

  AutoValue_ExposedPorts(
      String portAndProtocol,
      List<String> hostPorts) {
    if (portAndProtocol == null) {
      throw new NullPointerException("Null portAndProtocol");
    }
    this.portAndProtocol = portAndProtocol;
    if (hostPorts == null) {
      throw new NullPointerException("Null hostPorts");
    }
    this.hostPorts = hostPorts;
  }

  @Override
  public String portAndProtocol() {
    return portAndProtocol;
  }

  @Override
  public List<String> hostPorts() {
    return hostPorts;
  }

  @Override
  public String toString() {
    return "ExposedPorts{"
         + "portAndProtocol=" + portAndProtocol + ", "
         + "hostPorts=" + hostPorts
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ExposedPorts) {
      ExposedPorts that = (ExposedPorts) o;
      return (this.portAndProtocol.equals(that.portAndProtocol()))
           && (this.hostPorts.equals(that.hostPorts()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= portAndProtocol.hashCode();
    h$ *= 1000003;
    h$ ^= hostPorts.hashCode();
    return h$;
  }

}
