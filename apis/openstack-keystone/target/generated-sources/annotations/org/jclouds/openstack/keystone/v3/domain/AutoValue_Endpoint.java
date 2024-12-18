

package org.jclouds.openstack.keystone.v3.domain;

import java.net.URI;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Endpoint extends Endpoint {

  private final String id;
  private final String region;
  private final String regionId;
  private final String serviceId;
  private final URI url;
  private final Boolean enabled;
  private final String iface;

  private AutoValue_Endpoint(
      @Nullable String id,
      @Nullable String region,
      @Nullable String regionId,
      @Nullable String serviceId,
      URI url,
      @Nullable Boolean enabled,
      String iface) {
    this.id = id;
    this.region = region;
    this.regionId = regionId;
    this.serviceId = serviceId;
    this.url = url;
    this.enabled = enabled;
    this.iface = iface;
  }

  @Nullable
  @Override
  public String id() {
    return id;
  }

  @Nullable
  @Override
  public String region() {
    return region;
  }

  @Nullable
  @Override
  public String regionId() {
    return regionId;
  }

  @Nullable
  @Override
  public String serviceId() {
    return serviceId;
  }

  @Override
  public URI url() {
    return url;
  }

  @Nullable
  @Override
  public Boolean enabled() {
    return enabled;
  }

  @Override
  public String iface() {
    return iface;
  }

  @Override
  public String toString() {
    return "Endpoint{"
         + "id=" + id + ", "
         + "region=" + region + ", "
         + "regionId=" + regionId + ", "
         + "serviceId=" + serviceId + ", "
         + "url=" + url + ", "
         + "enabled=" + enabled + ", "
         + "iface=" + iface
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Endpoint) {
      Endpoint that = (Endpoint) o;
      return ((this.id == null) ? (that.id() == null) : this.id.equals(that.id()))
           && ((this.region == null) ? (that.region() == null) : this.region.equals(that.region()))
           && ((this.regionId == null) ? (that.regionId() == null) : this.regionId.equals(that.regionId()))
           && ((this.serviceId == null) ? (that.serviceId() == null) : this.serviceId.equals(that.serviceId()))
           && (this.url.equals(that.url()))
           && ((this.enabled == null) ? (that.enabled() == null) : this.enabled.equals(that.enabled()))
           && (this.iface.equals(that.iface()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (id == null) ? 0 : id.hashCode();
    h$ *= 1000003;
    h$ ^= (region == null) ? 0 : region.hashCode();
    h$ *= 1000003;
    h$ ^= (regionId == null) ? 0 : regionId.hashCode();
    h$ *= 1000003;
    h$ ^= (serviceId == null) ? 0 : serviceId.hashCode();
    h$ *= 1000003;
    h$ ^= url.hashCode();
    h$ *= 1000003;
    h$ ^= (enabled == null) ? 0 : enabled.hashCode();
    h$ *= 1000003;
    h$ ^= iface.hashCode();
    return h$;
  }

  @Override
  public Endpoint.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends Endpoint.Builder {
    private String id;
    private String region;
    private String regionId;
    private String serviceId;
    private URI url;
    private Boolean enabled;
    private String iface;
    Builder() {
    }
    private Builder(Endpoint source) {
      this.id = source.id();
      this.region = source.region();
      this.regionId = source.regionId();
      this.serviceId = source.serviceId();
      this.url = source.url();
      this.enabled = source.enabled();
      this.iface = source.iface();
    }
    @Override
    public Endpoint.Builder id(@Nullable String id) {
      this.id = id;
      return this;
    }
    @Override
    public Endpoint.Builder region(@Nullable String region) {
      this.region = region;
      return this;
    }
    @Override
    public Endpoint.Builder regionId(@Nullable String regionId) {
      this.regionId = regionId;
      return this;
    }
    @Override
    public Endpoint.Builder serviceId(@Nullable String serviceId) {
      this.serviceId = serviceId;
      return this;
    }
    @Override
    public Endpoint.Builder url(URI url) {
      if (url == null) {
        throw new NullPointerException("Null url");
      }
      this.url = url;
      return this;
    }
    @Override
    public Endpoint.Builder enabled(@Nullable Boolean enabled) {
      this.enabled = enabled;
      return this;
    }
    @Override
    public Endpoint.Builder iface(String iface) {
      if (iface == null) {
        throw new NullPointerException("Null iface");
      }
      this.iface = iface;
      return this;
    }
    @Override
    public Endpoint build() {
      String missing = "";
      if (this.url == null) {
        missing += " url";
      }
      if (this.iface == null) {
        missing += " iface";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Endpoint(
          this.id,
          this.region,
          this.regionId,
          this.serviceId,
          this.url,
          this.enabled,
          this.iface);
    }
  }

}
