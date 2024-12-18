

package org.jclouds.openstack.keystone.v3.domain;

import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Catalog extends Catalog {

  private final String id;
  private final String name;
  private final String type;
  private final List<Endpoint> endpoints;

  private AutoValue_Catalog(
      String id,
      @Nullable String name,
      String type,
      List<Endpoint> endpoints) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.endpoints = endpoints;
  }

  @Override
  public String id() {
    return id;
  }

  @Nullable
  @Override
  public String name() {
    return name;
  }

  @Override
  public String type() {
    return type;
  }

  @Override
  public List<Endpoint> endpoints() {
    return endpoints;
  }

  @Override
  public String toString() {
    return "Catalog{"
         + "id=" + id + ", "
         + "name=" + name + ", "
         + "type=" + type + ", "
         + "endpoints=" + endpoints
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Catalog) {
      Catalog that = (Catalog) o;
      return (this.id.equals(that.id()))
           && ((this.name == null) ? (that.name() == null) : this.name.equals(that.name()))
           && (this.type.equals(that.type()))
           && (this.endpoints.equals(that.endpoints()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= (name == null) ? 0 : name.hashCode();
    h$ *= 1000003;
    h$ ^= type.hashCode();
    h$ *= 1000003;
    h$ ^= endpoints.hashCode();
    return h$;
  }

  @Override
  public Catalog.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends Catalog.Builder {
    private String id;
    private String name;
    private String type;
    private List<Endpoint> endpoints;
    Builder() {
    }
    private Builder(Catalog source) {
      this.id = source.id();
      this.name = source.name();
      this.type = source.type();
      this.endpoints = source.endpoints();
    }
    @Override
    public Catalog.Builder id(String id) {
      if (id == null) {
        throw new NullPointerException("Null id");
      }
      this.id = id;
      return this;
    }
    @Override
    public Catalog.Builder name(@Nullable String name) {
      this.name = name;
      return this;
    }
    @Override
    public Catalog.Builder type(String type) {
      if (type == null) {
        throw new NullPointerException("Null type");
      }
      this.type = type;
      return this;
    }
    @Override
    public Catalog.Builder endpoints(List<Endpoint> endpoints) {
      if (endpoints == null) {
        throw new NullPointerException("Null endpoints");
      }
      this.endpoints = endpoints;
      return this;
    }
    @Override
    List<Endpoint> endpoints() {
      if (endpoints == null) {
        throw new IllegalStateException("Property \"endpoints\" has not been set");
      }
      return endpoints;
    }
    @Override
    Catalog autoBuild() {
      String missing = "";
      if (this.id == null) {
        missing += " id";
      }
      if (this.type == null) {
        missing += " type";
      }
      if (this.endpoints == null) {
        missing += " endpoints";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Catalog(
          this.id,
          this.name,
          this.type,
          this.endpoints);
    }
  }

}
