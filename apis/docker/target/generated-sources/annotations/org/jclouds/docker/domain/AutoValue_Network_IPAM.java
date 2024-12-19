

package org.jclouds.docker.domain;

import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Network_IPAM extends Network.IPAM {

  private final String driver;
  private final List<Network.IPAM.Config> config;

  private AutoValue_Network_IPAM(
      @Nullable String driver,
      List<Network.IPAM.Config> config) {
    this.driver = driver;
    this.config = config;
  }

  @Nullable
  @Override
  public String driver() {
    return driver;
  }

  @Override
  public List<Network.IPAM.Config> config() {
    return config;
  }

  @Override
  public String toString() {
    return "IPAM{"
         + "driver=" + driver + ", "
         + "config=" + config
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Network.IPAM) {
      Network.IPAM that = (Network.IPAM) o;
      return ((this.driver == null) ? (that.driver() == null) : this.driver.equals(that.driver()))
           && (this.config.equals(that.config()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (driver == null) ? 0 : driver.hashCode();
    h$ *= 1000003;
    h$ ^= config.hashCode();
    return h$;
  }

  static final class Builder extends Network.IPAM.Builder {
    private String driver;
    private List<Network.IPAM.Config> config;
    Builder() {
    }
    @Override
    public Network.IPAM.Builder driver(@Nullable String driver) {
      this.driver = driver;
      return this;
    }
    @Override
    public Network.IPAM.Builder config(List<Network.IPAM.Config> config) {
      if (config == null) {
        throw new NullPointerException("Null config");
      }
      this.config = config;
      return this;
    }
    @Override
    List<Network.IPAM.Config> config() {
      if (config == null) {
        throw new IllegalStateException("Property \"config\" has not been set");
      }
      return config;
    }
    @Override
    Network.IPAM autoBuild() {
      String missing = "";
      if (this.config == null) {
        missing += " config";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Network_IPAM(
          this.driver,
          this.config);
    }
  }

}
