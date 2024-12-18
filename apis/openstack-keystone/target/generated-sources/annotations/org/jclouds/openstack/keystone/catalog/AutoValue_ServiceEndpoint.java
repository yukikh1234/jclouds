

package org.jclouds.openstack.keystone.catalog;

import java.net.URI;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ServiceEndpoint extends ServiceEndpoint {

  private final String id;
  private final String regionId;
  private final URI url;
  private final ServiceEndpoint.Interface iface;
  private final String type;
  private final String version;

  private AutoValue_ServiceEndpoint(
      @Nullable String id,
      @Nullable String regionId,
      URI url,
      ServiceEndpoint.Interface iface,
      String type,
      @Nullable String version) {
    this.id = id;
    this.regionId = regionId;
    this.url = url;
    this.iface = iface;
    this.type = type;
    this.version = version;
  }

  @Nullable
  @Override
  public String id() {
    return id;
  }

  @Nullable
  @Override
  public String regionId() {
    return regionId;
  }

  @Override
  public URI url() {
    return url;
  }

  @Override
  public ServiceEndpoint.Interface iface() {
    return iface;
  }

  @Override
  public String type() {
    return type;
  }

  @Nullable
  @Override
  public String version() {
    return version;
  }

  @Override
  public String toString() {
    return "ServiceEndpoint{"
         + "id=" + id + ", "
         + "regionId=" + regionId + ", "
         + "url=" + url + ", "
         + "iface=" + iface + ", "
         + "type=" + type + ", "
         + "version=" + version
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ServiceEndpoint) {
      ServiceEndpoint that = (ServiceEndpoint) o;
      return ((this.id == null) ? (that.id() == null) : this.id.equals(that.id()))
           && ((this.regionId == null) ? (that.regionId() == null) : this.regionId.equals(that.regionId()))
           && (this.url.equals(that.url()))
           && (this.iface.equals(that.iface()))
           && (this.type.equals(that.type()))
           && ((this.version == null) ? (that.version() == null) : this.version.equals(that.version()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (id == null) ? 0 : id.hashCode();
    h$ *= 1000003;
    h$ ^= (regionId == null) ? 0 : regionId.hashCode();
    h$ *= 1000003;
    h$ ^= url.hashCode();
    h$ *= 1000003;
    h$ ^= iface.hashCode();
    h$ *= 1000003;
    h$ ^= type.hashCode();
    h$ *= 1000003;
    h$ ^= (version == null) ? 0 : version.hashCode();
    return h$;
  }

  static final class Builder extends ServiceEndpoint.Builder {
    private String id;
    private String regionId;
    private URI url;
    private ServiceEndpoint.Interface iface;
    private String type;
    private String version;
    Builder() {
    }
    @Override
    public ServiceEndpoint.Builder id(@Nullable String id) {
      this.id = id;
      return this;
    }
    @Override
    public ServiceEndpoint.Builder regionId(@Nullable String regionId) {
      this.regionId = regionId;
      return this;
    }
    @Override
    public ServiceEndpoint.Builder url(URI url) {
      if (url == null) {
        throw new NullPointerException("Null url");
      }
      this.url = url;
      return this;
    }
    @Override
    public ServiceEndpoint.Builder iface(ServiceEndpoint.Interface iface) {
      if (iface == null) {
        throw new NullPointerException("Null iface");
      }
      this.iface = iface;
      return this;
    }
    @Override
    public ServiceEndpoint.Builder type(String type) {
      if (type == null) {
        throw new NullPointerException("Null type");
      }
      this.type = type;
      return this;
    }
    @Override
    public ServiceEndpoint.Builder version(@Nullable String version) {
      this.version = version;
      return this;
    }
    @Override
    public ServiceEndpoint build() {
      String missing = "";
      if (this.url == null) {
        missing += " url";
      }
      if (this.iface == null) {
        missing += " iface";
      }
      if (this.type == null) {
        missing += " type";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_ServiceEndpoint(
          this.id,
          this.regionId,
          this.url,
          this.iface,
          this.type,
          this.version);
    }
  }

}
