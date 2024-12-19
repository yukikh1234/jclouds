

package org.jclouds.docker.domain;

import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_NetworkSettings extends NetworkSettings {

  private final String bridge;
  private final String sandboxId;
  private final boolean hairpinMode;
  private final String linkLocalIPv6Address;
  private final int linkLocalIPv6PrefixLen;
  private final Map<String, List<Map<String, String>>> ports;
  private final String sandboxKey;
  private final List<String> secondaryIPAddresses;
  private final List<String> secondaryIPv6Addresses;
  private final String endpointId;
  private final String gateway;
  private final String globalIPv6Address;
  private final int globalIPv6PrefixLen;
  private final String ipAddress;
  private final int ipPrefixLen;
  private final String ipv6Gateway;
  private final String macAddress;
  private final Map<String, NetworkSettings.Details> networks;
  private final String portMapping;

  AutoValue_NetworkSettings(
      String bridge,
      @Nullable String sandboxId,
      boolean hairpinMode,
      @Nullable String linkLocalIPv6Address,
      int linkLocalIPv6PrefixLen,
      @Nullable Map<String, List<Map<String, String>>> ports,
      @Nullable String sandboxKey,
      List<String> secondaryIPAddresses,
      List<String> secondaryIPv6Addresses,
      @Nullable String endpointId,
      String gateway,
      @Nullable String globalIPv6Address,
      int globalIPv6PrefixLen,
      String ipAddress,
      int ipPrefixLen,
      @Nullable String ipv6Gateway,
      @Nullable String macAddress,
      Map<String, NetworkSettings.Details> networks,
      @Nullable String portMapping) {
    if (bridge == null) {
      throw new NullPointerException("Null bridge");
    }
    this.bridge = bridge;
    this.sandboxId = sandboxId;
    this.hairpinMode = hairpinMode;
    this.linkLocalIPv6Address = linkLocalIPv6Address;
    this.linkLocalIPv6PrefixLen = linkLocalIPv6PrefixLen;
    this.ports = ports;
    this.sandboxKey = sandboxKey;
    if (secondaryIPAddresses == null) {
      throw new NullPointerException("Null secondaryIPAddresses");
    }
    this.secondaryIPAddresses = secondaryIPAddresses;
    if (secondaryIPv6Addresses == null) {
      throw new NullPointerException("Null secondaryIPv6Addresses");
    }
    this.secondaryIPv6Addresses = secondaryIPv6Addresses;
    this.endpointId = endpointId;
    if (gateway == null) {
      throw new NullPointerException("Null gateway");
    }
    this.gateway = gateway;
    this.globalIPv6Address = globalIPv6Address;
    this.globalIPv6PrefixLen = globalIPv6PrefixLen;
    if (ipAddress == null) {
      throw new NullPointerException("Null ipAddress");
    }
    this.ipAddress = ipAddress;
    this.ipPrefixLen = ipPrefixLen;
    this.ipv6Gateway = ipv6Gateway;
    this.macAddress = macAddress;
    if (networks == null) {
      throw new NullPointerException("Null networks");
    }
    this.networks = networks;
    this.portMapping = portMapping;
  }

  @Override
  public String bridge() {
    return bridge;
  }

  @Nullable
  @Override
  public String sandboxId() {
    return sandboxId;
  }

  @Override
  public boolean hairpinMode() {
    return hairpinMode;
  }

  @Nullable
  @Override
  public String linkLocalIPv6Address() {
    return linkLocalIPv6Address;
  }

  @Override
  public int linkLocalIPv6PrefixLen() {
    return linkLocalIPv6PrefixLen;
  }

  @Nullable
  @Override
  public Map<String, List<Map<String, String>>> ports() {
    return ports;
  }

  @Nullable
  @Override
  public String sandboxKey() {
    return sandboxKey;
  }

  @Override
  public List<String> secondaryIPAddresses() {
    return secondaryIPAddresses;
  }

  @Override
  public List<String> secondaryIPv6Addresses() {
    return secondaryIPv6Addresses;
  }

  @Nullable
  @Override
  public String endpointId() {
    return endpointId;
  }

  @Override
  public String gateway() {
    return gateway;
  }

  @Nullable
  @Override
  public String globalIPv6Address() {
    return globalIPv6Address;
  }

  @Override
  public int globalIPv6PrefixLen() {
    return globalIPv6PrefixLen;
  }

  @Override
  public String ipAddress() {
    return ipAddress;
  }

  @Override
  public int ipPrefixLen() {
    return ipPrefixLen;
  }

  @Nullable
  @Override
  public String ipv6Gateway() {
    return ipv6Gateway;
  }

  @Nullable
  @Override
  public String macAddress() {
    return macAddress;
  }

  @Override
  public Map<String, NetworkSettings.Details> networks() {
    return networks;
  }

  @Nullable
  @Override
  public String portMapping() {
    return portMapping;
  }

  @Override
  public String toString() {
    return "NetworkSettings{"
         + "bridge=" + bridge + ", "
         + "sandboxId=" + sandboxId + ", "
         + "hairpinMode=" + hairpinMode + ", "
         + "linkLocalIPv6Address=" + linkLocalIPv6Address + ", "
         + "linkLocalIPv6PrefixLen=" + linkLocalIPv6PrefixLen + ", "
         + "ports=" + ports + ", "
         + "sandboxKey=" + sandboxKey + ", "
         + "secondaryIPAddresses=" + secondaryIPAddresses + ", "
         + "secondaryIPv6Addresses=" + secondaryIPv6Addresses + ", "
         + "endpointId=" + endpointId + ", "
         + "gateway=" + gateway + ", "
         + "globalIPv6Address=" + globalIPv6Address + ", "
         + "globalIPv6PrefixLen=" + globalIPv6PrefixLen + ", "
         + "ipAddress=" + ipAddress + ", "
         + "ipPrefixLen=" + ipPrefixLen + ", "
         + "ipv6Gateway=" + ipv6Gateway + ", "
         + "macAddress=" + macAddress + ", "
         + "networks=" + networks + ", "
         + "portMapping=" + portMapping
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof NetworkSettings) {
      NetworkSettings that = (NetworkSettings) o;
      return (this.bridge.equals(that.bridge()))
           && ((this.sandboxId == null) ? (that.sandboxId() == null) : this.sandboxId.equals(that.sandboxId()))
           && (this.hairpinMode == that.hairpinMode())
           && ((this.linkLocalIPv6Address == null) ? (that.linkLocalIPv6Address() == null) : this.linkLocalIPv6Address.equals(that.linkLocalIPv6Address()))
           && (this.linkLocalIPv6PrefixLen == that.linkLocalIPv6PrefixLen())
           && ((this.ports == null) ? (that.ports() == null) : this.ports.equals(that.ports()))
           && ((this.sandboxKey == null) ? (that.sandboxKey() == null) : this.sandboxKey.equals(that.sandboxKey()))
           && (this.secondaryIPAddresses.equals(that.secondaryIPAddresses()))
           && (this.secondaryIPv6Addresses.equals(that.secondaryIPv6Addresses()))
           && ((this.endpointId == null) ? (that.endpointId() == null) : this.endpointId.equals(that.endpointId()))
           && (this.gateway.equals(that.gateway()))
           && ((this.globalIPv6Address == null) ? (that.globalIPv6Address() == null) : this.globalIPv6Address.equals(that.globalIPv6Address()))
           && (this.globalIPv6PrefixLen == that.globalIPv6PrefixLen())
           && (this.ipAddress.equals(that.ipAddress()))
           && (this.ipPrefixLen == that.ipPrefixLen())
           && ((this.ipv6Gateway == null) ? (that.ipv6Gateway() == null) : this.ipv6Gateway.equals(that.ipv6Gateway()))
           && ((this.macAddress == null) ? (that.macAddress() == null) : this.macAddress.equals(that.macAddress()))
           && (this.networks.equals(that.networks()))
           && ((this.portMapping == null) ? (that.portMapping() == null) : this.portMapping.equals(that.portMapping()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= bridge.hashCode();
    h$ *= 1000003;
    h$ ^= (sandboxId == null) ? 0 : sandboxId.hashCode();
    h$ *= 1000003;
    h$ ^= hairpinMode ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= (linkLocalIPv6Address == null) ? 0 : linkLocalIPv6Address.hashCode();
    h$ *= 1000003;
    h$ ^= linkLocalIPv6PrefixLen;
    h$ *= 1000003;
    h$ ^= (ports == null) ? 0 : ports.hashCode();
    h$ *= 1000003;
    h$ ^= (sandboxKey == null) ? 0 : sandboxKey.hashCode();
    h$ *= 1000003;
    h$ ^= secondaryIPAddresses.hashCode();
    h$ *= 1000003;
    h$ ^= secondaryIPv6Addresses.hashCode();
    h$ *= 1000003;
    h$ ^= (endpointId == null) ? 0 : endpointId.hashCode();
    h$ *= 1000003;
    h$ ^= gateway.hashCode();
    h$ *= 1000003;
    h$ ^= (globalIPv6Address == null) ? 0 : globalIPv6Address.hashCode();
    h$ *= 1000003;
    h$ ^= globalIPv6PrefixLen;
    h$ *= 1000003;
    h$ ^= ipAddress.hashCode();
    h$ *= 1000003;
    h$ ^= ipPrefixLen;
    h$ *= 1000003;
    h$ ^= (ipv6Gateway == null) ? 0 : ipv6Gateway.hashCode();
    h$ *= 1000003;
    h$ ^= (macAddress == null) ? 0 : macAddress.hashCode();
    h$ *= 1000003;
    h$ ^= networks.hashCode();
    h$ *= 1000003;
    h$ ^= (portMapping == null) ? 0 : portMapping.hashCode();
    return h$;
  }

}
