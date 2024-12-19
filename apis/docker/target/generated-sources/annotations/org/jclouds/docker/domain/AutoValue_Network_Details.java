

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Network_Details extends Network.Details {

  private final String endpoint;
  private final String macAddress;
  private final String ipv4address;
  private final String ipv6address;

  private AutoValue_Network_Details(
      String endpoint,
      String macAddress,
      String ipv4address,
      String ipv6address) {
    this.endpoint = endpoint;
    this.macAddress = macAddress;
    this.ipv4address = ipv4address;
    this.ipv6address = ipv6address;
  }

  @Override
  public String endpoint() {
    return endpoint;
  }

  @Override
  public String macAddress() {
    return macAddress;
  }

  @Override
  public String ipv4address() {
    return ipv4address;
  }

  @Override
  public String ipv6address() {
    return ipv6address;
  }

  @Override
  public String toString() {
    return "Details{"
         + "endpoint=" + endpoint + ", "
         + "macAddress=" + macAddress + ", "
         + "ipv4address=" + ipv4address + ", "
         + "ipv6address=" + ipv6address
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Network.Details) {
      Network.Details that = (Network.Details) o;
      return (this.endpoint.equals(that.endpoint()))
           && (this.macAddress.equals(that.macAddress()))
           && (this.ipv4address.equals(that.ipv4address()))
           && (this.ipv6address.equals(that.ipv6address()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= endpoint.hashCode();
    h$ *= 1000003;
    h$ ^= macAddress.hashCode();
    h$ *= 1000003;
    h$ ^= ipv4address.hashCode();
    h$ *= 1000003;
    h$ ^= ipv6address.hashCode();
    return h$;
  }

  static final class Builder extends Network.Details.Builder {
    private String endpoint;
    private String macAddress;
    private String ipv4address;
    private String ipv6address;
    Builder() {
    }
    @Override
    public Network.Details.Builder endpoint(String endpoint) {
      if (endpoint == null) {
        throw new NullPointerException("Null endpoint");
      }
      this.endpoint = endpoint;
      return this;
    }
    @Override
    public Network.Details.Builder macAddress(String macAddress) {
      if (macAddress == null) {
        throw new NullPointerException("Null macAddress");
      }
      this.macAddress = macAddress;
      return this;
    }
    @Override
    public Network.Details.Builder ipv4address(String ipv4address) {
      if (ipv4address == null) {
        throw new NullPointerException("Null ipv4address");
      }
      this.ipv4address = ipv4address;
      return this;
    }
    @Override
    public Network.Details.Builder ipv6address(String ipv6address) {
      if (ipv6address == null) {
        throw new NullPointerException("Null ipv6address");
      }
      this.ipv6address = ipv6address;
      return this;
    }
    @Override
    Network.Details build() {
      String missing = "";
      if (this.endpoint == null) {
        missing += " endpoint";
      }
      if (this.macAddress == null) {
        missing += " macAddress";
      }
      if (this.ipv4address == null) {
        missing += " ipv4address";
      }
      if (this.ipv6address == null) {
        missing += " ipv6address";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Network_Details(
          this.endpoint,
          this.macAddress,
          this.ipv4address,
          this.ipv6address);
    }
  }

}
