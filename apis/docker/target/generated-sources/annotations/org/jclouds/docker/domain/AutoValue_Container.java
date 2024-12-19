

package org.jclouds.docker.domain;

import com.google.common.base.Optional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Container extends Container {

  private final String id;
  private final Date created;
  private final String path;
  private final String name;
  private final List<String> args;
  private final Config config;
  private final State state;
  private final String image;
  private final NetworkSettings networkSettings;
  private final String sysInitPath;
  private final String resolvConfPath;
  private final Map<String, String> volumes;
  private final HostConfig hostConfig;
  private final String driver;
  private final String execDriver;
  private final Map<String, Boolean> volumesRW;
  private final String command;
  private final String status;
  private final List<Port> ports;
  private final String hostnamePath;
  private final String hostsPath;
  private final String mountLabel;
  private final String processLabel;
  private final Optional<Node> node;

  AutoValue_Container(
      String id,
      @Nullable Date created,
      @Nullable String path,
      @Nullable String name,
      List<String> args,
      @Nullable Config config,
      @Nullable State state,
      @Nullable String image,
      @Nullable NetworkSettings networkSettings,
      @Nullable String sysInitPath,
      @Nullable String resolvConfPath,
      Map<String, String> volumes,
      @Nullable HostConfig hostConfig,
      @Nullable String driver,
      @Nullable String execDriver,
      Map<String, Boolean> volumesRW,
      @Nullable String command,
      @Nullable String status,
      List<Port> ports,
      @Nullable String hostnamePath,
      @Nullable String hostsPath,
      @Nullable String mountLabel,
      @Nullable String processLabel,
      Optional<Node> node) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    this.created = created;
    this.path = path;
    this.name = name;
    if (args == null) {
      throw new NullPointerException("Null args");
    }
    this.args = args;
    this.config = config;
    this.state = state;
    this.image = image;
    this.networkSettings = networkSettings;
    this.sysInitPath = sysInitPath;
    this.resolvConfPath = resolvConfPath;
    if (volumes == null) {
      throw new NullPointerException("Null volumes");
    }
    this.volumes = volumes;
    this.hostConfig = hostConfig;
    this.driver = driver;
    this.execDriver = execDriver;
    if (volumesRW == null) {
      throw new NullPointerException("Null volumesRW");
    }
    this.volumesRW = volumesRW;
    this.command = command;
    this.status = status;
    if (ports == null) {
      throw new NullPointerException("Null ports");
    }
    this.ports = ports;
    this.hostnamePath = hostnamePath;
    this.hostsPath = hostsPath;
    this.mountLabel = mountLabel;
    this.processLabel = processLabel;
    if (node == null) {
      throw new NullPointerException("Null node");
    }
    this.node = node;
  }

  @Override
  public String id() {
    return id;
  }

  @Nullable
  @Override
  public Date created() {
    return created;
  }

  @Nullable
  @Override
  public String path() {
    return path;
  }

  @Nullable
  @Override
  public String name() {
    return name;
  }

  @Override
  public List<String> args() {
    return args;
  }

  @Nullable
  @Override
  public Config config() {
    return config;
  }

  @Nullable
  @Override
  public State state() {
    return state;
  }

  @Nullable
  @Override
  public String image() {
    return image;
  }

  @Nullable
  @Override
  public NetworkSettings networkSettings() {
    return networkSettings;
  }

  @Nullable
  @Override
  public String sysInitPath() {
    return sysInitPath;
  }

  @Nullable
  @Override
  public String resolvConfPath() {
    return resolvConfPath;
  }

  @Override
  public Map<String, String> volumes() {
    return volumes;
  }

  @Nullable
  @Override
  public HostConfig hostConfig() {
    return hostConfig;
  }

  @Nullable
  @Override
  public String driver() {
    return driver;
  }

  @Nullable
  @Override
  public String execDriver() {
    return execDriver;
  }

  @Override
  public Map<String, Boolean> volumesRW() {
    return volumesRW;
  }

  @Nullable
  @Override
  public String command() {
    return command;
  }

  @Nullable
  @Override
  public String status() {
    return status;
  }

  @Override
  public List<Port> ports() {
    return ports;
  }

  @Nullable
  @Override
  public String hostnamePath() {
    return hostnamePath;
  }

  @Nullable
  @Override
  public String hostsPath() {
    return hostsPath;
  }

  @Nullable
  @Override
  public String mountLabel() {
    return mountLabel;
  }

  @Nullable
  @Override
  public String processLabel() {
    return processLabel;
  }

  @Override
  public Optional<Node> node() {
    return node;
  }

  @Override
  public String toString() {
    return "Container{"
         + "id=" + id + ", "
         + "created=" + created + ", "
         + "path=" + path + ", "
         + "name=" + name + ", "
         + "args=" + args + ", "
         + "config=" + config + ", "
         + "state=" + state + ", "
         + "image=" + image + ", "
         + "networkSettings=" + networkSettings + ", "
         + "sysInitPath=" + sysInitPath + ", "
         + "resolvConfPath=" + resolvConfPath + ", "
         + "volumes=" + volumes + ", "
         + "hostConfig=" + hostConfig + ", "
         + "driver=" + driver + ", "
         + "execDriver=" + execDriver + ", "
         + "volumesRW=" + volumesRW + ", "
         + "command=" + command + ", "
         + "status=" + status + ", "
         + "ports=" + ports + ", "
         + "hostnamePath=" + hostnamePath + ", "
         + "hostsPath=" + hostsPath + ", "
         + "mountLabel=" + mountLabel + ", "
         + "processLabel=" + processLabel + ", "
         + "node=" + node
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Container) {
      Container that = (Container) o;
      return (this.id.equals(that.id()))
           && ((this.created == null) ? (that.created() == null) : this.created.equals(that.created()))
           && ((this.path == null) ? (that.path() == null) : this.path.equals(that.path()))
           && ((this.name == null) ? (that.name() == null) : this.name.equals(that.name()))
           && (this.args.equals(that.args()))
           && ((this.config == null) ? (that.config() == null) : this.config.equals(that.config()))
           && ((this.state == null) ? (that.state() == null) : this.state.equals(that.state()))
           && ((this.image == null) ? (that.image() == null) : this.image.equals(that.image()))
           && ((this.networkSettings == null) ? (that.networkSettings() == null) : this.networkSettings.equals(that.networkSettings()))
           && ((this.sysInitPath == null) ? (that.sysInitPath() == null) : this.sysInitPath.equals(that.sysInitPath()))
           && ((this.resolvConfPath == null) ? (that.resolvConfPath() == null) : this.resolvConfPath.equals(that.resolvConfPath()))
           && (this.volumes.equals(that.volumes()))
           && ((this.hostConfig == null) ? (that.hostConfig() == null) : this.hostConfig.equals(that.hostConfig()))
           && ((this.driver == null) ? (that.driver() == null) : this.driver.equals(that.driver()))
           && ((this.execDriver == null) ? (that.execDriver() == null) : this.execDriver.equals(that.execDriver()))
           && (this.volumesRW.equals(that.volumesRW()))
           && ((this.command == null) ? (that.command() == null) : this.command.equals(that.command()))
           && ((this.status == null) ? (that.status() == null) : this.status.equals(that.status()))
           && (this.ports.equals(that.ports()))
           && ((this.hostnamePath == null) ? (that.hostnamePath() == null) : this.hostnamePath.equals(that.hostnamePath()))
           && ((this.hostsPath == null) ? (that.hostsPath() == null) : this.hostsPath.equals(that.hostsPath()))
           && ((this.mountLabel == null) ? (that.mountLabel() == null) : this.mountLabel.equals(that.mountLabel()))
           && ((this.processLabel == null) ? (that.processLabel() == null) : this.processLabel.equals(that.processLabel()))
           && (this.node.equals(that.node()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= (created == null) ? 0 : created.hashCode();
    h$ *= 1000003;
    h$ ^= (path == null) ? 0 : path.hashCode();
    h$ *= 1000003;
    h$ ^= (name == null) ? 0 : name.hashCode();
    h$ *= 1000003;
    h$ ^= args.hashCode();
    h$ *= 1000003;
    h$ ^= (config == null) ? 0 : config.hashCode();
    h$ *= 1000003;
    h$ ^= (state == null) ? 0 : state.hashCode();
    h$ *= 1000003;
    h$ ^= (image == null) ? 0 : image.hashCode();
    h$ *= 1000003;
    h$ ^= (networkSettings == null) ? 0 : networkSettings.hashCode();
    h$ *= 1000003;
    h$ ^= (sysInitPath == null) ? 0 : sysInitPath.hashCode();
    h$ *= 1000003;
    h$ ^= (resolvConfPath == null) ? 0 : resolvConfPath.hashCode();
    h$ *= 1000003;
    h$ ^= volumes.hashCode();
    h$ *= 1000003;
    h$ ^= (hostConfig == null) ? 0 : hostConfig.hashCode();
    h$ *= 1000003;
    h$ ^= (driver == null) ? 0 : driver.hashCode();
    h$ *= 1000003;
    h$ ^= (execDriver == null) ? 0 : execDriver.hashCode();
    h$ *= 1000003;
    h$ ^= volumesRW.hashCode();
    h$ *= 1000003;
    h$ ^= (command == null) ? 0 : command.hashCode();
    h$ *= 1000003;
    h$ ^= (status == null) ? 0 : status.hashCode();
    h$ *= 1000003;
    h$ ^= ports.hashCode();
    h$ *= 1000003;
    h$ ^= (hostnamePath == null) ? 0 : hostnamePath.hashCode();
    h$ *= 1000003;
    h$ ^= (hostsPath == null) ? 0 : hostsPath.hashCode();
    h$ *= 1000003;
    h$ ^= (mountLabel == null) ? 0 : mountLabel.hashCode();
    h$ *= 1000003;
    h$ ^= (processLabel == null) ? 0 : processLabel.hashCode();
    h$ *= 1000003;
    h$ ^= node.hashCode();
    return h$;
  }

}
