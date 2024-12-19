

package org.jclouds.docker.domain;

import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Config extends Config {

  private final String hostname;
  private final String domainname;
  private final String user;
  private final int memory;
  private final int memorySwap;
  private final int cpuShares;
  private final boolean attachStdin;
  private final boolean attachStdout;
  private final boolean attachStderr;
  private final boolean tty;
  private final boolean openStdin;
  private final boolean stdinOnce;
  private final List<String> env;
  private final List<String> cmd;
  private final List<String> entrypoint;
  private final String image;
  private final Map<String, ?> volumes;
  private final String workingDir;
  private final boolean networkDisabled;
  private final Map<String, ?> exposedPorts;
  private final List<String> securityOpts;
  private final HostConfig hostConfig;

  AutoValue_Config(
      @Nullable String hostname,
      @Nullable String domainname,
      @Nullable String user,
      int memory,
      int memorySwap,
      int cpuShares,
      boolean attachStdin,
      boolean attachStdout,
      boolean attachStderr,
      boolean tty,
      boolean openStdin,
      boolean stdinOnce,
      @Nullable List<String> env,
      @Nullable List<String> cmd,
      @Nullable List<String> entrypoint,
      String image,
      @Nullable Map<String, ?> volumes,
      @Nullable String workingDir,
      boolean networkDisabled,
      Map<String, ?> exposedPorts,
      List<String> securityOpts,
      @Nullable HostConfig hostConfig) {
    this.hostname = hostname;
    this.domainname = domainname;
    this.user = user;
    this.memory = memory;
    this.memorySwap = memorySwap;
    this.cpuShares = cpuShares;
    this.attachStdin = attachStdin;
    this.attachStdout = attachStdout;
    this.attachStderr = attachStderr;
    this.tty = tty;
    this.openStdin = openStdin;
    this.stdinOnce = stdinOnce;
    this.env = env;
    this.cmd = cmd;
    this.entrypoint = entrypoint;
    if (image == null) {
      throw new NullPointerException("Null image");
    }
    this.image = image;
    this.volumes = volumes;
    this.workingDir = workingDir;
    this.networkDisabled = networkDisabled;
    if (exposedPorts == null) {
      throw new NullPointerException("Null exposedPorts");
    }
    this.exposedPorts = exposedPorts;
    if (securityOpts == null) {
      throw new NullPointerException("Null securityOpts");
    }
    this.securityOpts = securityOpts;
    this.hostConfig = hostConfig;
  }

  @Nullable
  @Override
  public String hostname() {
    return hostname;
  }

  @Nullable
  @Override
  public String domainname() {
    return domainname;
  }

  @Nullable
  @Override
  public String user() {
    return user;
  }

  @Override
  public int memory() {
    return memory;
  }

  @Override
  public int memorySwap() {
    return memorySwap;
  }

  @Override
  public int cpuShares() {
    return cpuShares;
  }

  @Override
  public boolean attachStdin() {
    return attachStdin;
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
  public boolean tty() {
    return tty;
  }

  @Override
  public boolean openStdin() {
    return openStdin;
  }

  @Override
  public boolean stdinOnce() {
    return stdinOnce;
  }

  @Nullable
  @Override
  public List<String> env() {
    return env;
  }

  @Nullable
  @Override
  public List<String> cmd() {
    return cmd;
  }

  @Nullable
  @Override
  public List<String> entrypoint() {
    return entrypoint;
  }

  @Override
  public String image() {
    return image;
  }

  @Nullable
  @Override
  public Map<String, ?> volumes() {
    return volumes;
  }

  @Nullable
  @Override
  public String workingDir() {
    return workingDir;
  }

  @Override
  public boolean networkDisabled() {
    return networkDisabled;
  }

  @Override
  public Map<String, ?> exposedPorts() {
    return exposedPorts;
  }

  @Override
  public List<String> securityOpts() {
    return securityOpts;
  }

  @Nullable
  @Override
  public HostConfig hostConfig() {
    return hostConfig;
  }

  @Override
  public String toString() {
    return "Config{"
         + "hostname=" + hostname + ", "
         + "domainname=" + domainname + ", "
         + "user=" + user + ", "
         + "memory=" + memory + ", "
         + "memorySwap=" + memorySwap + ", "
         + "cpuShares=" + cpuShares + ", "
         + "attachStdin=" + attachStdin + ", "
         + "attachStdout=" + attachStdout + ", "
         + "attachStderr=" + attachStderr + ", "
         + "tty=" + tty + ", "
         + "openStdin=" + openStdin + ", "
         + "stdinOnce=" + stdinOnce + ", "
         + "env=" + env + ", "
         + "cmd=" + cmd + ", "
         + "entrypoint=" + entrypoint + ", "
         + "image=" + image + ", "
         + "volumes=" + volumes + ", "
         + "workingDir=" + workingDir + ", "
         + "networkDisabled=" + networkDisabled + ", "
         + "exposedPorts=" + exposedPorts + ", "
         + "securityOpts=" + securityOpts + ", "
         + "hostConfig=" + hostConfig
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Config) {
      Config that = (Config) o;
      return ((this.hostname == null) ? (that.hostname() == null) : this.hostname.equals(that.hostname()))
           && ((this.domainname == null) ? (that.domainname() == null) : this.domainname.equals(that.domainname()))
           && ((this.user == null) ? (that.user() == null) : this.user.equals(that.user()))
           && (this.memory == that.memory())
           && (this.memorySwap == that.memorySwap())
           && (this.cpuShares == that.cpuShares())
           && (this.attachStdin == that.attachStdin())
           && (this.attachStdout == that.attachStdout())
           && (this.attachStderr == that.attachStderr())
           && (this.tty == that.tty())
           && (this.openStdin == that.openStdin())
           && (this.stdinOnce == that.stdinOnce())
           && ((this.env == null) ? (that.env() == null) : this.env.equals(that.env()))
           && ((this.cmd == null) ? (that.cmd() == null) : this.cmd.equals(that.cmd()))
           && ((this.entrypoint == null) ? (that.entrypoint() == null) : this.entrypoint.equals(that.entrypoint()))
           && (this.image.equals(that.image()))
           && ((this.volumes == null) ? (that.volumes() == null) : this.volumes.equals(that.volumes()))
           && ((this.workingDir == null) ? (that.workingDir() == null) : this.workingDir.equals(that.workingDir()))
           && (this.networkDisabled == that.networkDisabled())
           && (this.exposedPorts.equals(that.exposedPorts()))
           && (this.securityOpts.equals(that.securityOpts()))
           && ((this.hostConfig == null) ? (that.hostConfig() == null) : this.hostConfig.equals(that.hostConfig()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (hostname == null) ? 0 : hostname.hashCode();
    h$ *= 1000003;
    h$ ^= (domainname == null) ? 0 : domainname.hashCode();
    h$ *= 1000003;
    h$ ^= (user == null) ? 0 : user.hashCode();
    h$ *= 1000003;
    h$ ^= memory;
    h$ *= 1000003;
    h$ ^= memorySwap;
    h$ *= 1000003;
    h$ ^= cpuShares;
    h$ *= 1000003;
    h$ ^= attachStdin ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= attachStdout ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= attachStderr ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= tty ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= openStdin ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= stdinOnce ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= (env == null) ? 0 : env.hashCode();
    h$ *= 1000003;
    h$ ^= (cmd == null) ? 0 : cmd.hashCode();
    h$ *= 1000003;
    h$ ^= (entrypoint == null) ? 0 : entrypoint.hashCode();
    h$ *= 1000003;
    h$ ^= image.hashCode();
    h$ *= 1000003;
    h$ ^= (volumes == null) ? 0 : volumes.hashCode();
    h$ *= 1000003;
    h$ ^= (workingDir == null) ? 0 : workingDir.hashCode();
    h$ *= 1000003;
    h$ ^= networkDisabled ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= exposedPorts.hashCode();
    h$ *= 1000003;
    h$ ^= securityOpts.hashCode();
    h$ *= 1000003;
    h$ ^= (hostConfig == null) ? 0 : hostConfig.hashCode();
    return h$;
  }

}
