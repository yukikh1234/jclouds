

package org.jclouds.docker.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Version extends Version {

  private final String apiVersion;
  private final String arch;
  private final String gitCommit;
  private final String goVersion;
  private final String kernelVersion;
  private final String os;
  private final String version;

  AutoValue_Version(
      String apiVersion,
      String arch,
      String gitCommit,
      String goVersion,
      String kernelVersion,
      String os,
      String version) {
    if (apiVersion == null) {
      throw new NullPointerException("Null apiVersion");
    }
    this.apiVersion = apiVersion;
    if (arch == null) {
      throw new NullPointerException("Null arch");
    }
    this.arch = arch;
    if (gitCommit == null) {
      throw new NullPointerException("Null gitCommit");
    }
    this.gitCommit = gitCommit;
    if (goVersion == null) {
      throw new NullPointerException("Null goVersion");
    }
    this.goVersion = goVersion;
    if (kernelVersion == null) {
      throw new NullPointerException("Null kernelVersion");
    }
    this.kernelVersion = kernelVersion;
    if (os == null) {
      throw new NullPointerException("Null os");
    }
    this.os = os;
    if (version == null) {
      throw new NullPointerException("Null version");
    }
    this.version = version;
  }

  @Override
  public String apiVersion() {
    return apiVersion;
  }

  @Override
  public String arch() {
    return arch;
  }

  @Override
  public String gitCommit() {
    return gitCommit;
  }

  @Override
  public String goVersion() {
    return goVersion;
  }

  @Override
  public String kernelVersion() {
    return kernelVersion;
  }

  @Override
  public String os() {
    return os;
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String toString() {
    return "Version{"
         + "apiVersion=" + apiVersion + ", "
         + "arch=" + arch + ", "
         + "gitCommit=" + gitCommit + ", "
         + "goVersion=" + goVersion + ", "
         + "kernelVersion=" + kernelVersion + ", "
         + "os=" + os + ", "
         + "version=" + version
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Version) {
      Version that = (Version) o;
      return (this.apiVersion.equals(that.apiVersion()))
           && (this.arch.equals(that.arch()))
           && (this.gitCommit.equals(that.gitCommit()))
           && (this.goVersion.equals(that.goVersion()))
           && (this.kernelVersion.equals(that.kernelVersion()))
           && (this.os.equals(that.os()))
           && (this.version.equals(that.version()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= apiVersion.hashCode();
    h$ *= 1000003;
    h$ ^= arch.hashCode();
    h$ *= 1000003;
    h$ ^= gitCommit.hashCode();
    h$ *= 1000003;
    h$ ^= goVersion.hashCode();
    h$ *= 1000003;
    h$ ^= kernelVersion.hashCode();
    h$ *= 1000003;
    h$ ^= os.hashCode();
    h$ *= 1000003;
    h$ ^= version.hashCode();
    return h$;
  }

}
