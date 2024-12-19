

package org.jclouds.docker.domain;

import java.util.Map;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Network extends Network {

  private final String name;
  private final String id;
  private final String scope;
  private final String driver;
  private final Network.IPAM ipam;
  private final Map<String, Network.Details> containers;
  private final Map<String, String> options;

  private AutoValue_Network(
      @Nullable String name,
      @Nullable String id,
      @Nullable String scope,
      @Nullable String driver,
      @Nullable Network.IPAM ipam,
      @Nullable Map<String, Network.Details> containers,
      @Nullable Map<String, String> options) {
    this.name = name;
    this.id = id;
    this.scope = scope;
    this.driver = driver;
    this.ipam = ipam;
    this.containers = containers;
    this.options = options;
  }

  @Nullable
  @Override
  public String name() {
    return name;
  }

  @Nullable
  @Override
  public String id() {
    return id;
  }

  @Nullable
  @Override
  public String scope() {
    return scope;
  }

  @Nullable
  @Override
  public String driver() {
    return driver;
  }

  @Nullable
  @Override
  public Network.IPAM ipam() {
    return ipam;
  }

  @Nullable
  @Override
  public Map<String, Network.Details> containers() {
    return containers;
  }

  @Nullable
  @Override
  public Map<String, String> options() {
    return options;
  }

  @Override
  public String toString() {
    return "Network{"
         + "name=" + name + ", "
         + "id=" + id + ", "
         + "scope=" + scope + ", "
         + "driver=" + driver + ", "
         + "ipam=" + ipam + ", "
         + "containers=" + containers + ", "
         + "options=" + options
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Network) {
      Network that = (Network) o;
      return ((this.name == null) ? (that.name() == null) : this.name.equals(that.name()))
           && ((this.id == null) ? (that.id() == null) : this.id.equals(that.id()))
           && ((this.scope == null) ? (that.scope() == null) : this.scope.equals(that.scope()))
           && ((this.driver == null) ? (that.driver() == null) : this.driver.equals(that.driver()))
           && ((this.ipam == null) ? (that.ipam() == null) : this.ipam.equals(that.ipam()))
           && ((this.containers == null) ? (that.containers() == null) : this.containers.equals(that.containers()))
           && ((this.options == null) ? (that.options() == null) : this.options.equals(that.options()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (name == null) ? 0 : name.hashCode();
    h$ *= 1000003;
    h$ ^= (id == null) ? 0 : id.hashCode();
    h$ *= 1000003;
    h$ ^= (scope == null) ? 0 : scope.hashCode();
    h$ *= 1000003;
    h$ ^= (driver == null) ? 0 : driver.hashCode();
    h$ *= 1000003;
    h$ ^= (ipam == null) ? 0 : ipam.hashCode();
    h$ *= 1000003;
    h$ ^= (containers == null) ? 0 : containers.hashCode();
    h$ *= 1000003;
    h$ ^= (options == null) ? 0 : options.hashCode();
    return h$;
  }

  static final class Builder extends Network.Builder {
    private String name;
    private String id;
    private String scope;
    private String driver;
    private Network.IPAM ipam;
    private Map<String, Network.Details> containers;
    private Map<String, String> options;
    Builder() {
    }
    @Override
    public Network.Builder name(@Nullable String name) {
      this.name = name;
      return this;
    }
    @Override
    public Network.Builder id(@Nullable String id) {
      this.id = id;
      return this;
    }
    @Override
    public Network.Builder scope(@Nullable String scope) {
      this.scope = scope;
      return this;
    }
    @Override
    public Network.Builder driver(@Nullable String driver) {
      this.driver = driver;
      return this;
    }
    @Override
    public Network.Builder ipam(@Nullable Network.IPAM ipam) {
      this.ipam = ipam;
      return this;
    }
    @Override
    public Network.Builder containers(@Nullable Map<String, Network.Details> containers) {
      this.containers = containers;
      return this;
    }
    @Override
    @Nullable Map<String, Network.Details> containers() {
      return containers;
    }
    @Override
    public Network.Builder options(@Nullable Map<String, String> options) {
      this.options = options;
      return this;
    }
    @Override
    @Nullable Map<String, String> options() {
      return options;
    }
    @Override
    Network autoBuild() {
      return new AutoValue_Network(
          this.name,
          this.id,
          this.scope,
          this.driver,
          this.ipam,
          this.containers,
          this.options);
    }
  }

}
