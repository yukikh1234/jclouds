

package org.jclouds.docker.domain;

import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Info extends Info {

  private final int containers;
  private final boolean debug;
  private final String driver;
  private final List<List<String>> driverStatus;
  private final String executionDriver;
  private final boolean iPv4Forwarding;
  private final int images;
  private final String indexServerAddress;
  private final String initPath;
  private final String initSha1;
  private final String kernelVersion;
  private final boolean memoryLimit;
  private final int nEventsListener;
  private final int nFd;
  private final int nGoroutines;
  private final String operatingSystem;
  private final boolean swapLimit;
  private final String dockerRootDir;
  private final List<String> labels;
  private final long memTotal;
  private final int ncpu;
  private final String id;
  private final String name;

  AutoValue_Info(
      int containers,
      boolean debug,
      String driver,
      List<List<String>> driverStatus,
      String executionDriver,
      boolean iPv4Forwarding,
      int images,
      String indexServerAddress,
      @Nullable String initPath,
      @Nullable String initSha1,
      String kernelVersion,
      boolean memoryLimit,
      int nEventsListener,
      int nFd,
      int nGoroutines,
      String operatingSystem,
      boolean swapLimit,
      String dockerRootDir,
      @Nullable List<String> labels,
      long memTotal,
      int ncpu,
      String id,
      String name) {
    this.containers = containers;
    this.debug = debug;
    if (driver == null) {
      throw new NullPointerException("Null driver");
    }
    this.driver = driver;
    if (driverStatus == null) {
      throw new NullPointerException("Null driverStatus");
    }
    this.driverStatus = driverStatus;
    if (executionDriver == null) {
      throw new NullPointerException("Null executionDriver");
    }
    this.executionDriver = executionDriver;
    this.iPv4Forwarding = iPv4Forwarding;
    this.images = images;
    if (indexServerAddress == null) {
      throw new NullPointerException("Null indexServerAddress");
    }
    this.indexServerAddress = indexServerAddress;
    this.initPath = initPath;
    this.initSha1 = initSha1;
    if (kernelVersion == null) {
      throw new NullPointerException("Null kernelVersion");
    }
    this.kernelVersion = kernelVersion;
    this.memoryLimit = memoryLimit;
    this.nEventsListener = nEventsListener;
    this.nFd = nFd;
    this.nGoroutines = nGoroutines;
    if (operatingSystem == null) {
      throw new NullPointerException("Null operatingSystem");
    }
    this.operatingSystem = operatingSystem;
    this.swapLimit = swapLimit;
    if (dockerRootDir == null) {
      throw new NullPointerException("Null dockerRootDir");
    }
    this.dockerRootDir = dockerRootDir;
    this.labels = labels;
    this.memTotal = memTotal;
    this.ncpu = ncpu;
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    if (name == null) {
      throw new NullPointerException("Null name");
    }
    this.name = name;
  }

  @Override
  public int containers() {
    return containers;
  }

  @Override
  public boolean debug() {
    return debug;
  }

  @Override
  public String driver() {
    return driver;
  }

  @Override
  public List<List<String>> driverStatus() {
    return driverStatus;
  }

  @Override
  public String executionDriver() {
    return executionDriver;
  }

  @Override
  public boolean iPv4Forwarding() {
    return iPv4Forwarding;
  }

  @Override
  public int images() {
    return images;
  }

  @Override
  public String indexServerAddress() {
    return indexServerAddress;
  }

  @Nullable
  @Override
  public String initPath() {
    return initPath;
  }

  @Nullable
  @Override
  public String initSha1() {
    return initSha1;
  }

  @Override
  public String kernelVersion() {
    return kernelVersion;
  }

  @Override
  public boolean memoryLimit() {
    return memoryLimit;
  }

  @Override
  public int nEventsListener() {
    return nEventsListener;
  }

  @Override
  public int nFd() {
    return nFd;
  }

  @Override
  public int nGoroutines() {
    return nGoroutines;
  }

  @Override
  public String operatingSystem() {
    return operatingSystem;
  }

  @Override
  public boolean swapLimit() {
    return swapLimit;
  }

  @Override
  public String dockerRootDir() {
    return dockerRootDir;
  }

  @Nullable
  @Override
  public List<String> labels() {
    return labels;
  }

  @Override
  public long memTotal() {
    return memTotal;
  }

  @Override
  public int ncpu() {
    return ncpu;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String toString() {
    return "Info{"
         + "containers=" + containers + ", "
         + "debug=" + debug + ", "
         + "driver=" + driver + ", "
         + "driverStatus=" + driverStatus + ", "
         + "executionDriver=" + executionDriver + ", "
         + "iPv4Forwarding=" + iPv4Forwarding + ", "
         + "images=" + images + ", "
         + "indexServerAddress=" + indexServerAddress + ", "
         + "initPath=" + initPath + ", "
         + "initSha1=" + initSha1 + ", "
         + "kernelVersion=" + kernelVersion + ", "
         + "memoryLimit=" + memoryLimit + ", "
         + "nEventsListener=" + nEventsListener + ", "
         + "nFd=" + nFd + ", "
         + "nGoroutines=" + nGoroutines + ", "
         + "operatingSystem=" + operatingSystem + ", "
         + "swapLimit=" + swapLimit + ", "
         + "dockerRootDir=" + dockerRootDir + ", "
         + "labels=" + labels + ", "
         + "memTotal=" + memTotal + ", "
         + "ncpu=" + ncpu + ", "
         + "id=" + id + ", "
         + "name=" + name
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Info) {
      Info that = (Info) o;
      return (this.containers == that.containers())
           && (this.debug == that.debug())
           && (this.driver.equals(that.driver()))
           && (this.driverStatus.equals(that.driverStatus()))
           && (this.executionDriver.equals(that.executionDriver()))
           && (this.iPv4Forwarding == that.iPv4Forwarding())
           && (this.images == that.images())
           && (this.indexServerAddress.equals(that.indexServerAddress()))
           && ((this.initPath == null) ? (that.initPath() == null) : this.initPath.equals(that.initPath()))
           && ((this.initSha1 == null) ? (that.initSha1() == null) : this.initSha1.equals(that.initSha1()))
           && (this.kernelVersion.equals(that.kernelVersion()))
           && (this.memoryLimit == that.memoryLimit())
           && (this.nEventsListener == that.nEventsListener())
           && (this.nFd == that.nFd())
           && (this.nGoroutines == that.nGoroutines())
           && (this.operatingSystem.equals(that.operatingSystem()))
           && (this.swapLimit == that.swapLimit())
           && (this.dockerRootDir.equals(that.dockerRootDir()))
           && ((this.labels == null) ? (that.labels() == null) : this.labels.equals(that.labels()))
           && (this.memTotal == that.memTotal())
           && (this.ncpu == that.ncpu())
           && (this.id.equals(that.id()))
           && (this.name.equals(that.name()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= containers;
    h$ *= 1000003;
    h$ ^= debug ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= driver.hashCode();
    h$ *= 1000003;
    h$ ^= driverStatus.hashCode();
    h$ *= 1000003;
    h$ ^= executionDriver.hashCode();
    h$ *= 1000003;
    h$ ^= iPv4Forwarding ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= images;
    h$ *= 1000003;
    h$ ^= indexServerAddress.hashCode();
    h$ *= 1000003;
    h$ ^= (initPath == null) ? 0 : initPath.hashCode();
    h$ *= 1000003;
    h$ ^= (initSha1 == null) ? 0 : initSha1.hashCode();
    h$ *= 1000003;
    h$ ^= kernelVersion.hashCode();
    h$ *= 1000003;
    h$ ^= memoryLimit ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= nEventsListener;
    h$ *= 1000003;
    h$ ^= nFd;
    h$ *= 1000003;
    h$ ^= nGoroutines;
    h$ *= 1000003;
    h$ ^= operatingSystem.hashCode();
    h$ *= 1000003;
    h$ ^= swapLimit ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= dockerRootDir.hashCode();
    h$ *= 1000003;
    h$ ^= (labels == null) ? 0 : labels.hashCode();
    h$ *= 1000003;
    h$ ^= (int) ((memTotal >>> 32) ^ memTotal);
    h$ *= 1000003;
    h$ ^= ncpu;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= name.hashCode();
    return h$;
  }

}
