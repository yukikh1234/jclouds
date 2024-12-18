

package org.jclouds.openstack.keystone.auth.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_TenantOrDomainAndCredentials<T> extends TenantOrDomainAndCredentials<T> {

  private final String tenantOrDomainId;
  private final String tenantOrDomainName;
  private final String scope;
  private final String projectDomainName;
  private final String projectDomainId;
  private final T credentials;

  private AutoValue_TenantOrDomainAndCredentials(
      @Nullable String tenantOrDomainId,
      @Nullable String tenantOrDomainName,
      @Nullable String scope,
      @Nullable String projectDomainName,
      @Nullable String projectDomainId,
      T credentials) {
    this.tenantOrDomainId = tenantOrDomainId;
    this.tenantOrDomainName = tenantOrDomainName;
    this.scope = scope;
    this.projectDomainName = projectDomainName;
    this.projectDomainId = projectDomainId;
    this.credentials = credentials;
  }

  @Nullable
  @Override
  public String tenantOrDomainId() {
    return tenantOrDomainId;
  }

  @Nullable
  @Override
  public String tenantOrDomainName() {
    return tenantOrDomainName;
  }

  @Nullable
  @Override
  public String scope() {
    return scope;
  }

  @Nullable
  @Override
  public String projectDomainName() {
    return projectDomainName;
  }

  @Nullable
  @Override
  public String projectDomainId() {
    return projectDomainId;
  }

  @Override
  public T credentials() {
    return credentials;
  }

  @Override
  public String toString() {
    return "TenantOrDomainAndCredentials{"
         + "tenantOrDomainId=" + tenantOrDomainId + ", "
         + "tenantOrDomainName=" + tenantOrDomainName + ", "
         + "scope=" + scope + ", "
         + "projectDomainName=" + projectDomainName + ", "
         + "projectDomainId=" + projectDomainId + ", "
         + "credentials=" + credentials
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof TenantOrDomainAndCredentials) {
      TenantOrDomainAndCredentials<?> that = (TenantOrDomainAndCredentials<?>) o;
      return ((this.tenantOrDomainId == null) ? (that.tenantOrDomainId() == null) : this.tenantOrDomainId.equals(that.tenantOrDomainId()))
           && ((this.tenantOrDomainName == null) ? (that.tenantOrDomainName() == null) : this.tenantOrDomainName.equals(that.tenantOrDomainName()))
           && ((this.scope == null) ? (that.scope() == null) : this.scope.equals(that.scope()))
           && ((this.projectDomainName == null) ? (that.projectDomainName() == null) : this.projectDomainName.equals(that.projectDomainName()))
           && ((this.projectDomainId == null) ? (that.projectDomainId() == null) : this.projectDomainId.equals(that.projectDomainId()))
           && (this.credentials.equals(that.credentials()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (tenantOrDomainId == null) ? 0 : tenantOrDomainId.hashCode();
    h$ *= 1000003;
    h$ ^= (tenantOrDomainName == null) ? 0 : tenantOrDomainName.hashCode();
    h$ *= 1000003;
    h$ ^= (scope == null) ? 0 : scope.hashCode();
    h$ *= 1000003;
    h$ ^= (projectDomainName == null) ? 0 : projectDomainName.hashCode();
    h$ *= 1000003;
    h$ ^= (projectDomainId == null) ? 0 : projectDomainId.hashCode();
    h$ *= 1000003;
    h$ ^= credentials.hashCode();
    return h$;
  }

  static final class Builder<T> extends TenantOrDomainAndCredentials.Builder<T> {
    private String tenantOrDomainId;
    private String tenantOrDomainName;
    private String scope;
    private String projectDomainName;
    private String projectDomainId;
    private T credentials;
    Builder() {
    }
    @Override
    public TenantOrDomainAndCredentials.Builder<T> tenantOrDomainId(@Nullable String tenantOrDomainId) {
      this.tenantOrDomainId = tenantOrDomainId;
      return this;
    }
    @Override
    public TenantOrDomainAndCredentials.Builder<T> tenantOrDomainName(@Nullable String tenantOrDomainName) {
      this.tenantOrDomainName = tenantOrDomainName;
      return this;
    }
    @Override
    public TenantOrDomainAndCredentials.Builder<T> scope(@Nullable String scope) {
      this.scope = scope;
      return this;
    }
    @Override
    public TenantOrDomainAndCredentials.Builder<T> projectDomainName(@Nullable String projectDomainName) {
      this.projectDomainName = projectDomainName;
      return this;
    }
    @Override
    public TenantOrDomainAndCredentials.Builder<T> projectDomainId(@Nullable String projectDomainId) {
      this.projectDomainId = projectDomainId;
      return this;
    }
    @Override
    public TenantOrDomainAndCredentials.Builder<T> credentials(T credentials) {
      if (credentials == null) {
        throw new NullPointerException("Null credentials");
      }
      this.credentials = credentials;
      return this;
    }
    @Override
    public TenantOrDomainAndCredentials<T> build() {
      String missing = "";
      if (this.credentials == null) {
        missing += " credentials";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_TenantOrDomainAndCredentials<T>(
          this.tenantOrDomainId,
          this.tenantOrDomainName,
          this.scope,
          this.projectDomainName,
          this.projectDomainId,
          this.credentials);
    }
  }

}
