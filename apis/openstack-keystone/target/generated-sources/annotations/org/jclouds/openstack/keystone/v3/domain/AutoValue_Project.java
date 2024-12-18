

package org.jclouds.openstack.keystone.v3.domain;

import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Project extends Project {

  private final boolean isDomain;
  private final String description;
  private final String domainId;
  private final String domainName;
  private final boolean enabled;
  private final String id;
  private final String name;
  private final String parentId;
  private final List<String> tags;
  private final Link link;

  private AutoValue_Project(
      boolean isDomain,
      @Nullable String description,
      @Nullable String domainId,
      @Nullable String domainName,
      boolean enabled,
      @Nullable String id,
      String name,
      @Nullable String parentId,
      @Nullable List<String> tags,
      @Nullable Link link) {
    this.isDomain = isDomain;
    this.description = description;
    this.domainId = domainId;
    this.domainName = domainName;
    this.enabled = enabled;
    this.id = id;
    this.name = name;
    this.parentId = parentId;
    this.tags = tags;
    this.link = link;
  }

  @Override
  public boolean isDomain() {
    return isDomain;
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
  public String domainName() {
    return domainName;
  }

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Nullable
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
  public String parentId() {
    return parentId;
  }

  @Nullable
  @Override
  public List<String> tags() {
    return tags;
  }

  @Nullable
  @Override
  public Link link() {
    return link;
  }

  @Override
  public String toString() {
    return "Project{"
         + "isDomain=" + isDomain + ", "
         + "description=" + description + ", "
         + "domainId=" + domainId + ", "
         + "domainName=" + domainName + ", "
         + "enabled=" + enabled + ", "
         + "id=" + id + ", "
         + "name=" + name + ", "
         + "parentId=" + parentId + ", "
         + "tags=" + tags + ", "
         + "link=" + link
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Project) {
      Project that = (Project) o;
      return (this.isDomain == that.isDomain())
           && ((this.description == null) ? (that.description() == null) : this.description.equals(that.description()))
           && ((this.domainId == null) ? (that.domainId() == null) : this.domainId.equals(that.domainId()))
           && ((this.domainName == null) ? (that.domainName() == null) : this.domainName.equals(that.domainName()))
           && (this.enabled == that.enabled())
           && ((this.id == null) ? (that.id() == null) : this.id.equals(that.id()))
           && (this.name.equals(that.name()))
           && ((this.parentId == null) ? (that.parentId() == null) : this.parentId.equals(that.parentId()))
           && ((this.tags == null) ? (that.tags() == null) : this.tags.equals(that.tags()))
           && ((this.link == null) ? (that.link() == null) : this.link.equals(that.link()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= isDomain ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= (description == null) ? 0 : description.hashCode();
    h$ *= 1000003;
    h$ ^= (domainId == null) ? 0 : domainId.hashCode();
    h$ *= 1000003;
    h$ ^= (domainName == null) ? 0 : domainName.hashCode();
    h$ *= 1000003;
    h$ ^= enabled ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= (id == null) ? 0 : id.hashCode();
    h$ *= 1000003;
    h$ ^= name.hashCode();
    h$ *= 1000003;
    h$ ^= (parentId == null) ? 0 : parentId.hashCode();
    h$ *= 1000003;
    h$ ^= (tags == null) ? 0 : tags.hashCode();
    h$ *= 1000003;
    h$ ^= (link == null) ? 0 : link.hashCode();
    return h$;
  }

  @Override
  public Project.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends Project.Builder {
    private Boolean isDomain;
    private String description;
    private String domainId;
    private String domainName;
    private Boolean enabled;
    private String id;
    private String name;
    private String parentId;
    private List<String> tags;
    private Link link;
    Builder() {
    }
    private Builder(Project source) {
      this.isDomain = source.isDomain();
      this.description = source.description();
      this.domainId = source.domainId();
      this.domainName = source.domainName();
      this.enabled = source.enabled();
      this.id = source.id();
      this.name = source.name();
      this.parentId = source.parentId();
      this.tags = source.tags();
      this.link = source.link();
    }
    @Override
    public Project.Builder isDomain(boolean isDomain) {
      this.isDomain = isDomain;
      return this;
    }
    @Override
    public Project.Builder description(@Nullable String description) {
      this.description = description;
      return this;
    }
    @Override
    public Project.Builder domainId(@Nullable String domainId) {
      this.domainId = domainId;
      return this;
    }
    @Override
    public Project.Builder domainName(@Nullable String domainName) {
      this.domainName = domainName;
      return this;
    }
    @Override
    public Project.Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }
    @Override
    public Project.Builder id(@Nullable String id) {
      this.id = id;
      return this;
    }
    @Override
    public Project.Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("Null name");
      }
      this.name = name;
      return this;
    }
    @Override
    public Project.Builder parentId(@Nullable String parentId) {
      this.parentId = parentId;
      return this;
    }
    @Override
    public Project.Builder tags(@Nullable List<String> tags) {
      this.tags = tags;
      return this;
    }
    @Override
    @Nullable List<String> tags() {
      return tags;
    }
    @Override
    public Project.Builder link(@Nullable Link link) {
      this.link = link;
      return this;
    }
    @Override
    Project autoBuild() {
      String missing = "";
      if (this.isDomain == null) {
        missing += " isDomain";
      }
      if (this.enabled == null) {
        missing += " enabled";
      }
      if (this.name == null) {
        missing += " name";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Project(
          this.isDomain,
          this.description,
          this.domainId,
          this.domainName,
          this.enabled,
          this.id,
          this.name,
          this.parentId,
          this.tags,
          this.link);
    }
  }

}
