

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_NetworkSettings_Details extends NetworkSettings.Details {

  private final String endpoint;
  private final String gateway;
  private final String ipAddress;
  private final int ipPrefixLen;
  private final String ipv6Gateway;
  private final String globalIPv6Address;
  private final int globalIPv6PrefixLen;
  private final String macAddress;

  private AutoValue_NetworkSettings_Details(
      String endpoint,
      String gateway,
      String ipAddress,
      int ipPrefixLen,
      String ipv6Gateway,
      String globalIPv6Address,
      int globalIPv6PrefixLen,
      String macAddress) {
    this.endpoint = endpoint;
    this.gateway = gateway;
    this.ipAddress = ipAddress;
    this.ipPrefixLen = ipPrefixLen;
    this.ipv6Gateway = ipv6Gateway;
    this.globalIPv6Address = globalIPv6Address;
    this.globalIPv6PrefixLen = globalIPv6PrefixLen;
    this.macAddress = macAddress;
  }

  @Override
  public String endpoint() {
    return endpoint;
  }

  @Override
  public String gateway() {
    return gateway;
  }

  @Override
  public String ipAddress() {
    return ipAddress;
  }

  @Override
  public int ipPrefixLen() {
    return ipPrefixLen;
  }

  @Override
  public String ipv6Gateway() {
    return ipv6Gateway;
  }

  @Override
  public String globalIPv6Address() {
    return globalIPv6Address;
  }

  @Override
  public int globalIPv6PrefixLen() {
    return globalIPv6PrefixLen;
  }

  @Override
  public String macAddress() {
    return macAddress;
  }

  @Override
  public String toString() {
    return "Details{"
         + "endpoint=" + endpoint + ", "
         + "gateway=" + gateway + ", "
         + "ipAddress=" + ipAddress + ", "
         + "ipPrefixLen=" + ipPrefixLen + ", "
         + "ipv6Gateway=" + ipv6Gateway + ", "
         + "globalIPv6Address=" + globalIPv6Address + ", "
         + "globalIPv6PrefixLen=" + globalIPv6PrefixLen + ", "
         + "macAddress=" + macAddress
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof NetworkSettings.Details) {
      NetworkSettings.Details that = (NetworkSettings.Details) o;
      return (this.endpoint.equals(that.endpoint()))
           && (this.gateway.equals(that.gateway()))
           && (this.ipAddress.equals(that.ipAddress()))
           && (this.ipPrefixLen == that.ipPrefixLen())
           && (this.ipv6Gateway.equals(that.ipv6Gateway()))
           && (this.globalIPv6Address.equals(that.globalIPv6Address()))
           && (this.globalIPv6PrefixLen == that.globalIPv6PrefixLen())
           && (this.macAddress.equals(that.macAddress()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= endpoint.hashCode();
    h$ *= 1000003;
    h$ ^= gateway.hashCode();
    h$ *= 1000003;
    h$ ^= ipAddress.hashCode();
    h$ *= 1000003;
    h$ ^= ipPrefixLen;
    h$ *= 1000003;
    h$ ^= ipv6Gateway.hashCode();
    h$ *= 1000003;
    h$ ^= globalIPv6Address.hashCode();
    h$ *= 1000003;
    h$ ^= globalIPv6PrefixLen;
    h$ *= 1000003;
    h$ ^= macAddress.hashCode();
    return h$;
  }

  @Override
  public NetworkSettings.Details.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends NetworkSettings.Details.Builder {
    private String endpoint;
    private String gateway;
    private String ipAddress;
    private Integer ipPrefixLen;
    private String ipv6Gateway;
    private String globalIPv6Address;
    private Integer globalIPv6PrefixLen;
    private String macAddress;
    Builder() {
    }
    private Builder(NetworkSettings.Details source) {
      this.endpoint = source.endpoint();
      this.gateway = source.gateway();
      this.ipAddress = source.ipAddress();
      this.ipPrefixLen = source.ipPrefixLen();
      this.ipv6Gateway = source.ipv6Gateway();
      this.globalIPv6Address = source.globalIPv6Address();
      this.globalIPv6PrefixLen = source.globalIPv6PrefixLen();
      this.macAddress = source.macAddress();
    }
    @Override
    public NetworkSettings.Details.Builder endpoint(String endpoint) {
      if (endpoint == null) {
        throw new NullPointerException("Null endpoint");
      }
      this.endpoint = endpoint;
      return this;
    }
    @Override
    public NetworkSettings.Details.Builder gateway(String gateway) {
      if (gateway == null) {
        throw new NullPointerException("Null gateway");
      }
      this.gateway = gateway;
      return this;
    }
    @Override
    public NetworkSettings.Details.Builder ipAddress(String ipAddress) {
      if (ipAddress == null) {
        throw new NullPointerException("Null ipAddress");
      }
      this.ipAddress = ipAddress;
      return this;
    }
    @Override
    public NetworkSettings.Details.Builder ipPrefixLen(int ipPrefixLen) {
      this.ipPrefixLen = ipPrefixLen;
      return this;
    }
    @Override
    public NetworkSettings.Details.Builder ipv6Gateway(String ipv6Gateway) {
      if (ipv6Gateway == null) {
        throw new NullPointerException("Null ipv6Gateway");
      }
      this.ipv6Gateway = ipv6Gateway;
      return this;
    }
    @Override
    public NetworkSettings.Details.Builder globalIPv6Address(String globalIPv6Address) {
      if (globalIPv6Address == null) {
        throw new NullPointerException("Null globalIPv6Address");
      }
      this.globalIPv6Address = globalIPv6Address;
      return this;
    }
    @Override
    public NetworkSettings.Details.Builder globalIPv6PrefixLen(int globalIPv6PrefixLen) {
      this.globalIPv6PrefixLen = globalIPv6PrefixLen;
      return this;
    }
    @Override
    public NetworkSettings.Details.Builder macAddress(String macAddress) {
      if (macAddress == null) {
        throw new NullPointerException("Null macAddress");
      }
      this.macAddress = macAddress;
      return this;
    }
    @Override
    public NetworkSettings.Details build() {
      String missing = "";
      if (this.endpoint == null) {
        missing += " endpoint";
      }
      if (this.gateway == null) {
        missing += " gateway";
      }
      if (this.ipAddress == null) {
        missing += " ipAddress";
      }
      if (this.ipPrefixLen == null) {
        missing += " ipPrefixLen";
      }
      if (this.ipv6Gateway == null) {
        missing += " ipv6Gateway";
      }
      if (this.globalIPv6Address == null) {
        missing += " globalIPv6Address";
      }
      if (this.globalIPv6PrefixLen == null) {
        missing += " globalIPv6PrefixLen";
      }
      if (this.macAddress == null) {
        missing += " macAddress";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_NetworkSettings_Details(
          this.endpoint,
          this.gateway,
          this.ipAddress,
          this.ipPrefixLen,
          this.ipv6Gateway,
          this.globalIPv6Address,
          this.globalIPv6PrefixLen,
          this.macAddress);
    }
  }

}
