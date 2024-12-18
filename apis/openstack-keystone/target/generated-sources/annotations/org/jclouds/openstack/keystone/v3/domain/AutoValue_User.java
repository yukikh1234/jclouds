

package org.jclouds.openstack.keystone.v3.domain;

import java.util.Date;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_User extends User {

  private final String id;
  private final String name;
  private final Date passwordExpiresAt;
  private final User.Domain domain;
  private final String domainId;
  private final String defaultProjectId;
  private final Boolean enabled;
  private final Link link;

  private AutoValue_User(
      String id,
      String name,
      @Nullable Date passwordExpiresAt,
      @Nullable User.Domain domain,
      @Nullable String domainId,
      @Nullable String defaultProjectId,
      @Nullable Boolean enabled,
      @Nullable Link link) {
    this.id = id;
    this.name = name;
    this.passwordExpiresAt = passwordExpiresAt;
    this.domain = domain;
    this.domainId = domainId;
    this.defaultProjectId = defaultProjectId;
    this.enabled = enabled;
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
  public Date passwordExpiresAt() {
    return passwordExpiresAt;
  }

  @Nullable
  @Override
  public User.Domain domain() {
    return domain;
  }

  @Nullable
  @Override
  public String domainId() {
    return domainId;
  }

  @Nullable
  @Override
  public String defaultProjectId() {
    return defaultProjectId;
  }

  @Nullable
  @Override
  public Boolean enabled() {
    return enabled;
  }

  @Nullable
  @Override
  public Link link() {
    return link;
  }

  @Override
  public String toString() {
    return "User{"
         + "id=" + id + ", "
         + "name=" + name + ", "
         + "passwordExpiresAt=" + passwordExpiresAt + ", "
         + "domain=" + domain + ", "
         + "domainId=" + domainId + ", "
         + "defaultProjectId=" + defaultProjectId + ", "
         + "enabled=" + enabled + ", "
         + "link=" + link
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof User) {
      User that = (User) o;
      return (this.id.equals(that.id()))
           && (this.name.equals(that.name()))
           && ((this.passwordExpiresAt == null) ? (that.passwordExpiresAt() == null) : this.passwordExpiresAt.equals(that.passwordExpiresAt()))
           && ((this.domain == null) ? (that.domain() == null) : this.domain.equals(that.domain()))
           && ((this.domainId == null) ? (that.domainId() == null) : this.domainId.equals(that.domainId()))
           && ((this.defaultProjectId == null) ? (that.defaultProjectId() == null) : this.defaultProjectId.equals(that.defaultProjectId()))
           && ((this.enabled == null) ? (that.enabled() == null) : this.enabled.equals(that.enabled()))
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
    h$ ^= (passwordExpiresAt == null) ? 0 : passwordExpiresAt.hashCode();
    h$ *= 1000003;
    h$ ^= (domain == null) ? 0 : domain.hashCode();
    h$ *= 1000003;
    h$ ^= (domainId == null) ? 0 : domainId.hashCode();
    h$ *= 1000003;
    h$ ^= (defaultProjectId == null) ? 0 : defaultProjectId.hashCode();
    h$ *= 1000003;
    h$ ^= (enabled == null) ? 0 : enabled.hashCode();
    h$ *= 1000003;
    h$ ^= (link == null) ? 0 : link.hashCode();
    return h$;
  }

  @Override
  public User.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends User.Builder {
    private String id;
    private String name;
    private Date passwordExpiresAt;
    private User.Domain domain;
    private String domainId;
    private String defaultProjectId;
    private Boolean enabled;
    private Link link;
    Builder() {
    }
    private Builder(User source) {
      this.id = source.id();
      this.name = source.name();
      this.passwordExpiresAt = source.passwordExpiresAt();
      this.domain = source.domain();
      this.domainId = source.domainId();
      this.defaultProjectId = source.defaultProjectId();
      this.enabled = source.enabled();
      this.link = source.link();
    }
    @Override
    public User.Builder id(String id) {
      if (id == null) {
        throw new NullPointerException("Null id");
      }
      this.id = id;
      return this;
    }
    @Override
    public User.Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("Null name");
      }
      this.name = name;
      return this;
    }
    @Override
    public User.Builder passwordExpiresAt(@Nullable Date passwordExpiresAt) {
      this.passwordExpiresAt = passwordExpiresAt;
      return this;
    }
    @Override
    public User.Builder domain(@Nullable User.Domain domain) {
      this.domain = domain;
      return this;
    }
    @Override
    public User.Builder domainId(@Nullable String domainId) {
      this.domainId = domainId;
      return this;
    }
    @Override
    public User.Builder defaultProjectId(@Nullable String defaultProjectId) {
      this.defaultProjectId = defaultProjectId;
      return this;
    }
    @Override
    public User.Builder enabled(@Nullable Boolean enabled) {
      this.enabled = enabled;
      return this;
    }
    @Override
    public User.Builder link(@Nullable Link link) {
      this.link = link;
      return this;
    }
    @Override
    public User build() {
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
      return new AutoValue_User(
          this.id,
          this.name,
          this.passwordExpiresAt,
          this.domain,
          this.domainId,
          this.defaultProjectId,
          this.enabled,
          this.link);
    }
  }

}
