

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Group extends Group {

  private final String id;
  private final String name;
  private final String description;
  private final String domainId;
  private final Link link;

  private AutoValue_Group(
      String id,
      String name,
      @Nullable String description,
      @Nullable String domainId,
      @Nullable Link link) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.domainId = domainId;
    this.link = link;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String name() {
    return name;
  }

  @Nullable
  @Override
  public String description() {
    return description;
  }

  @Nullable
  @Override
  public String domainId() {
    return domainId;
  }

  @Nullable
  @Override
  public Link link() {
    return link;
  }

  @Override
  public String toString() {
    return "Group{"
         + "id=" + id + ", "
         + "name=" + name + ", "
         + "description=" + description + ", "
         + "domainId=" + domainId + ", "
         + "link=" + link
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Group) {
      Group that = (Group) o;
      return (this.id.equals(that.id()))
           && (this.name.equals(that.name()))
           && ((this.description == null) ? (that.description() == null) : this.description.equals(that.description()))
           && ((this.domainId == null) ? (that.domainId() == null) : this.domainId.equals(that.domainId()))
           && ((this.link == null) ? (that.link() == null) : this.link.equals(that.link()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= name.hashCode();
    h$ *= 1000003;
    h$ ^= (description == null) ? 0 : description.hashCode();
    h$ *= 1000003;
    h$ ^= (domainId == null) ? 0 : domainId.hashCode();
    h$ *= 1000003;
    h$ ^= (link == null) ? 0 : link.hashCode();
    return h$;
  }

  @Override
  public Group.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends Group.Builder {
    private String id;
    private String name;
    private String description;
    private String domainId;
    private Link link;
    Builder() {
    }
    private Builder(Group source) {
      this.id = source.id();
      this.name = source.name();
      this.description = source.description();
      this.domainId = source.domainId();
      this.link = source.link();
    }
    @Override
    public Group.Builder id(String id) {
      if (id == null) {
        throw new NullPointerException("Null id");
      }
      this.id = id;
      return this;
    }
    @Override
    public Group.Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("Null name");
      }
      this.name = name;
      return this;
    }
    @Override
    public Group.Builder description(@Nullable String description) {
      this.description = description;
      return this;
    }
    @Override
    public Group.Builder domainId(@Nullable String domainId) {
      this.domainId = domainId;
      return this;
    }
    @Override
    public Group.Builder link(@Nullable Link link) {
      this.link = link;
      return this;
    }
    @Override
    public Group build() {
      String missing = "";
      if (this.id == null) {
        missing += " id";
      }
      if (this.name == null) {
        missing += " name";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Group(
          this.id,
          this.name,
          this.description,
          this.domainId,
          this.link);
    }
  }

}
