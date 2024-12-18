

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Region extends Region {

  private final String id;
  private final String description;
  private final Link link;
  private final String parentRegionId;

  private AutoValue_Region(
      String id,
      String description,
      @Nullable Link link,
      @Nullable String parentRegionId) {
    this.id = id;
    this.description = description;
    this.link = link;
    this.parentRegionId = parentRegionId;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Nullable
  @Override
  public Link link() {
    return link;
  }

  @Nullable
  @Override
  public String parentRegionId() {
    return parentRegionId;
  }

  @Override
  public String toString() {
    return "Region{"
         + "id=" + id + ", "
         + "description=" + description + ", "
         + "link=" + link + ", "
         + "parentRegionId=" + parentRegionId
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Region) {
      Region that = (Region) o;
      return (this.id.equals(that.id()))
           && (this.description.equals(that.description()))
           && ((this.link == null) ? (that.link() == null) : this.link.equals(that.link()))
           && ((this.parentRegionId == null) ? (that.parentRegionId() == null) : this.parentRegionId.equals(that.parentRegionId()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= description.hashCode();
    h$ *= 1000003;
    h$ ^= (link == null) ? 0 : link.hashCode();
    h$ *= 1000003;
    h$ ^= (parentRegionId == null) ? 0 : parentRegionId.hashCode();
    return h$;
  }

  @Override
  public Region.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends Region.Builder {
    private String id;
    private String description;
    private Link link;
    private String parentRegionId;
    Builder() {
    }
    private Builder(Region source) {
      this.id = source.id();
      this.description = source.description();
      this.link = source.link();
      this.parentRegionId = source.parentRegionId();
    }
    @Override
    public Region.Builder id(String id) {
      if (id == null) {
        throw new NullPointerException("Null id");
      }
      this.id = id;
      return this;
    }
    @Override
    public Region.Builder description(String description) {
      if (description == null) {
        throw new NullPointerException("Null description");
      }
      this.description = description;
      return this;
    }
    @Override
    public Region.Builder link(@Nullable Link link) {
      this.link = link;
      return this;
    }
    @Override
    public Region.Builder parentRegionId(@Nullable String parentRegionId) {
      this.parentRegionId = parentRegionId;
      return this;
    }
    @Override
    public Region build() {
      String missing = "";
      if (this.id == null) {
        missing += " id";
      }
      if (this.description == null) {
        missing += " description";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Region(
          this.id,
          this.description,
          this.link,
          this.parentRegionId);
    }
  }

}
