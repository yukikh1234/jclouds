

package org.jclouds.docker.domain;

import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_HostConfig extends HostConfig {

  private final String containerIDFile;
  private final List<String> binds;
  private final List<Map<String, String>> lxcConf;
  private final boolean privileged;
  private final List<String> dns;
  private final List<String> dnsSearch;
  private final Map<String, List<Map<String, String>>> portBindings;
  private final List<String> links;
  private final List<String> extraHosts;
  private final boolean publishAllPorts;
  private final List<String> volumesFrom;
  private final String networkMode;
  private final List<String> securityOpt;
  private final List<String> capAdd;
  private final List<String> capDrop;
  private final Map<String, String> restartPolicy;

  AutoValue_HostConfig(
      @Nullable String containerIDFile,
      @Nullable List<String> binds,
      List<Map<String, String>> lxcConf,
      boolean privileged,
      @Nullable List<String> dns,
      @Nullable List<String> dnsSearch,
      Map<String, List<Map<String, String>>> portBindings,
      @Nullable List<String> links,
      @Nullable List<String> extraHosts,
      boolean publishAllPorts,
      @Nullable List<String> volumesFrom,
      @Nullable String networkMode,
      @Nullable List<String> securityOpt,
      @Nullable List<String> capAdd,
      @Nullable List<String> capDrop,
      Map<String, String> restartPolicy) {
    this.containerIDFile = containerIDFile;
    this.binds = binds;
    if (lxcConf == null) {
      throw new NullPointerException("Null lxcConf");
    }
    this.lxcConf = lxcConf;
    this.privileged = privileged;
    this.dns = dns;
    this.dnsSearch = dnsSearch;
    if (portBindings == null) {
      throw new NullPointerException("Null portBindings");
    }
    this.portBindings = portBindings;
    this.links = links;
    this.extraHosts = extraHosts;
    this.publishAllPorts = publishAllPorts;
    this.volumesFrom = volumesFrom;
    this.networkMode = networkMode;
    this.securityOpt = securityOpt;
    this.capAdd = capAdd;
    this.capDrop = capDrop;
    if (restartPolicy == null) {
      throw new NullPointerException("Null restartPolicy");
    }
    this.restartPolicy = restartPolicy;
  }

  @Nullable
  @Override
  public String containerIDFile() {
    return containerIDFile;
  }

  @Nullable
  @Override
  public List<String> binds() {
    return binds;
  }

  @Override
  public List<Map<String, String>> lxcConf() {
    return lxcConf;
  }

  @Override
  public boolean privileged() {
    return privileged;
  }

  @Nullable
  @Override
  public List<String> dns() {
    return dns;
  }

  @Nullable
  @Override
  public List<String> dnsSearch() {
    return dnsSearch;
  }

  @Override
  public Map<String, List<Map<String, String>>> portBindings() {
    return portBindings;
  }

  @Nullable
  @Override
  public List<String> links() {
    return links;
  }

  @Nullable
  @Override
  public List<String> extraHosts() {
    return extraHosts;
  }

  @Override
  public boolean publishAllPorts() {
    return publishAllPorts;
  }

  @Nullable
  @Override
  public List<String> volumesFrom() {
    return volumesFrom;
  }

  @Nullable
  @Override
  public String networkMode() {
    return networkMode;
  }

  @Nullable
  @Override
  public List<String> securityOpt() {
    return securityOpt;
  }

  @Nullable
  @Override
  public List<String> capAdd() {
    return capAdd;
  }

  @Nullable
  @Override
  public List<String> capDrop() {
    return capDrop;
  }

  @Override
  public Map<String, String> restartPolicy() {
    return restartPolicy;
  }

  @Override
  public String toString() {
    return "HostConfig{"
         + "containerIDFile=" + containerIDFile + ", "
         + "binds=" + binds + ", "
         + "lxcConf=" + lxcConf + ", "
         + "privileged=" + privileged + ", "
         + "dns=" + dns + ", "
         + "dnsSearch=" + dnsSearch + ", "
         + "portBindings=" + portBindings + ", "
         + "links=" + links + ", "
         + "extraHosts=" + extraHosts + ", "
         + "publishAllPorts=" + publishAllPorts + ", "
         + "volumesFrom=" + volumesFrom + ", "
         + "networkMode=" + networkMode + ", "
         + "securityOpt=" + securityOpt + ", "
         + "capAdd=" + capAdd + ", "
         + "capDrop=" + capDrop + ", "
         + "restartPolicy=" + restartPolicy
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof HostConfig) {
      HostConfig that = (HostConfig) o;
      return ((this.containerIDFile == null) ? (that.containerIDFile() == null) : this.containerIDFile.equals(that.containerIDFile()))
           && ((this.binds == null) ? (that.binds() == null) : this.binds.equals(that.binds()))
           && (this.lxcConf.equals(that.lxcConf()))
           && (this.privileged == that.privileged())
           && ((this.dns == null) ? (that.dns() == null) : this.dns.equals(that.dns()))
           && ((this.dnsSearch == null) ? (that.dnsSearch() == null) : this.dnsSearch.equals(that.dnsSearch()))
           && (this.portBindings.equals(that.portBindings()))
           && ((this.links == null) ? (that.links() == null) : this.links.equals(that.links()))
           && ((this.extraHosts == null) ? (that.extraHosts() == null) : this.extraHosts.equals(that.extraHosts()))
           && (this.publishAllPorts == that.publishAllPorts())
           && ((this.volumesFrom == null) ? (that.volumesFrom() == null) : this.volumesFrom.equals(that.volumesFrom()))
           && ((this.networkMode == null) ? (that.networkMode() == null) : this.networkMode.equals(that.networkMode()))
           && ((this.securityOpt == null) ? (that.securityOpt() == null) : this.securityOpt.equals(that.securityOpt()))
           && ((this.capAdd == null) ? (that.capAdd() == null) : this.capAdd.equals(that.capAdd()))
           && ((this.capDrop == null) ? (that.capDrop() == null) : this.capDrop.equals(that.capDrop()))
           && (this.restartPolicy.equals(that.restartPolicy()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (containerIDFile == null) ? 0 : containerIDFile.hashCode();
    h$ *= 1000003;
    h$ ^= (binds == null) ? 0 : binds.hashCode();
    h$ *= 1000003;
    h$ ^= lxcConf.hashCode();
    h$ *= 1000003;
    h$ ^= privileged ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= (dns == null) ? 0 : dns.hashCode();
    h$ *= 1000003;
    h$ ^= (dnsSearch == null) ? 0 : dnsSearch.hashCode();
    h$ *= 1000003;
    h$ ^= portBindings.hashCode();
    h$ *= 1000003;
    h$ ^= (links == null) ? 0 : links.hashCode();
    h$ *= 1000003;
    h$ ^= (extraHosts == null) ? 0 : extraHosts.hashCode();
    h$ *= 1000003;
    h$ ^= publishAllPorts ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= (volumesFrom == null) ? 0 : volumesFrom.hashCode();
    h$ *= 1000003;
    h$ ^= (networkMode == null) ? 0 : networkMode.hashCode();
    h$ *= 1000003;
    h$ ^= (securityOpt == null) ? 0 : securityOpt.hashCode();
    h$ *= 1000003;
    h$ ^= (capAdd == null) ? 0 : capAdd.hashCode();
    h$ *= 1000003;
    h$ ^= (capDrop == null) ? 0 : capDrop.hashCode();
    h$ *= 1000003;
    h$ ^= restartPolicy.hashCode();
    return h$;
  }

}
