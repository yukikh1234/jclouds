

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Network_IPAM_Config extends Network.IPAM.Config {

  private final String subnet;
  private final String ipRange;
  private final String gateway;

  private AutoValue_Network_IPAM_Config(
      String subnet,
      @Nullable String ipRange,
      @Nullable String gateway) {
    this.subnet = subnet;
    this.ipRange = ipRange;
    this.gateway = gateway;
  }

  @Override
  public String subnet() {
    return subnet;
  }

  @Nullable
  @Override
  public String ipRange() {
    return ipRange;
  }

  @Nullable
  @Override
  public String gateway() {
    return gateway;
  }

  @Override
  public String toString() {
    return "Config{"
         + "subnet=" + subnet + ", "
         + "ipRange=" + ipRange + ", "
         + "gateway=" + gateway
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Network.IPAM.Config) {
      Network.IPAM.Config that = (Network.IPAM.Config) o;
      return (this.subnet.equals(that.subnet()))
           && ((this.ipRange == null) ? (that.ipRange() == null) : this.ipRange.equals(that.ipRange()))
           && ((this.gateway == null) ? (that.gateway() == null) : this.gateway.equals(that.gateway()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= subnet.hashCode();
    h$ *= 1000003;
    h$ ^= (ipRange == null) ? 0 : ipRange.hashCode();
    h$ *= 1000003;
    h$ ^= (gateway == null) ? 0 : gateway.hashCode();
    return h$;
  }

  static final class Builder extends Network.IPAM.Config.Builder {
    private String subnet;
    private String ipRange;
    private String gateway;
    Builder() {
    }
    @Override
    public Network.IPAM.Config.Builder subnet(String subnet) {
      if (subnet == null) {
        throw new NullPointerException("Null subnet");
      }
      this.subnet = subnet;
      return this;
    }
    @Override
    public Network.IPAM.Config.Builder ipRange(@Nullable String ipRange) {
      this.ipRange = ipRange;
      return this;
    }
    @Override
    public Network.IPAM.Config.Builder gateway(@Nullable String gateway) {
      this.gateway = gateway;
      return this;
    }
    @Override
    Network.IPAM.Config build() {
      String missing = "";
      if (this.subnet == null) {
        missing += " subnet";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Network_IPAM_Config(
          this.subnet,
          this.ipRange,
          this.gateway);
    }
  }

}
