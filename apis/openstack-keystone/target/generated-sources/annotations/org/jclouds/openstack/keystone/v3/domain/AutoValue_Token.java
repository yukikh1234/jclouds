

package org.jclouds.openstack.keystone.v3.domain;

import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Token extends Token {

  private final String id;
  private final List<String> methods;
  private final Date expiresAt;
  private final Object extras;
  private final List<Catalog> catalog;
  private final List<String> auditIds;
  private final User user;
  private final Date issuedAt;

  private AutoValue_Token(
      @Nullable String id,
      List<String> methods,
      @Nullable Date expiresAt,
      @Nullable Object extras,
      @Nullable List<Catalog> catalog,
      @Nullable List<String> auditIds,
      User user,
      Date issuedAt) {
    this.id = id;
    this.methods = methods;
    this.expiresAt = expiresAt;
    this.extras = extras;
    this.catalog = catalog;
    this.auditIds = auditIds;
    this.user = user;
    this.issuedAt = issuedAt;
  }

  @Nullable
  @Override
  public String id() {
    return id;
  }

  @Override
  public List<String> methods() {
    return methods;
  }

  @Nullable
  @Override
  public Date expiresAt() {
    return expiresAt;
  }

  @Nullable
  @Override
  public Object extras() {
    return extras;
  }

  @Nullable
  @Override
  public List<Catalog> catalog() {
    return catalog;
  }

  @Nullable
  @Override
  public List<String> auditIds() {
    return auditIds;
  }

  @Override
  public User user() {
    return user;
  }

  @Override
  public Date issuedAt() {
    return issuedAt;
  }

  @Override
  public String toString() {
    return "Token{"
         + "id=" + id + ", "
         + "methods=" + methods + ", "
         + "expiresAt=" + expiresAt + ", "
         + "extras=" + extras + ", "
         + "catalog=" + catalog + ", "
         + "auditIds=" + auditIds + ", "
         + "user=" + user + ", "
         + "issuedAt=" + issuedAt
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Token) {
      Token that = (Token) o;
      return ((this.id == null) ? (that.id() == null) : this.id.equals(that.id()))
           && (this.methods.equals(that.methods()))
           && ((this.expiresAt == null) ? (that.expiresAt() == null) : this.expiresAt.equals(that.expiresAt()))
           && ((this.extras == null) ? (that.extras() == null) : this.extras.equals(that.extras()))
           && ((this.catalog == null) ? (that.catalog() == null) : this.catalog.equals(that.catalog()))
           && ((this.auditIds == null) ? (that.auditIds() == null) : this.auditIds.equals(that.auditIds()))
           && (this.user.equals(that.user()))
           && (this.issuedAt.equals(that.issuedAt()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= (id == null) ? 0 : id.hashCode();
    h$ *= 1000003;
    h$ ^= methods.hashCode();
    h$ *= 1000003;
    h$ ^= (expiresAt == null) ? 0 : expiresAt.hashCode();
    h$ *= 1000003;
    h$ ^= (extras == null) ? 0 : extras.hashCode();
    h$ *= 1000003;
    h$ ^= (catalog == null) ? 0 : catalog.hashCode();
    h$ *= 1000003;
    h$ ^= (auditIds == null) ? 0 : auditIds.hashCode();
    h$ *= 1000003;
    h$ ^= user.hashCode();
    h$ *= 1000003;
    h$ ^= issuedAt.hashCode();
    return h$;
  }

  @Override
  public Token.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends Token.Builder {
    private String id;
    private List<String> methods;
    private Date expiresAt;
    private Object extras;
    private List<Catalog> catalog;
    private List<String> auditIds;
    private User user;
    private Date issuedAt;
    Builder() {
    }
    private Builder(Token source) {
      this.id = source.id();
      this.methods = source.methods();
      this.expiresAt = source.expiresAt();
      this.extras = source.extras();
      this.catalog = source.catalog();
      this.auditIds = source.auditIds();
      this.user = source.user();
      this.issuedAt = source.issuedAt();
    }
    @Override
    public Token.Builder id(@Nullable String id) {
      this.id = id;
      return this;
    }
    @Override
    public Token.Builder methods(List<String> methods) {
      if (methods == null) {
        throw new NullPointerException("Null methods");
      }
      this.methods = methods;
      return this;
    }
    @Override
    List<String> methods() {
      if (methods == null) {
        throw new IllegalStateException("Property \"methods\" has not been set");
      }
      return methods;
    }
    @Override
    public Token.Builder expiresAt(@Nullable Date expiresAt) {
      this.expiresAt = expiresAt;
      return this;
    }
    @Override
    public Token.Builder extras(@Nullable Object extras) {
      this.extras = extras;
      return this;
    }
    @Override
    public Token.Builder catalog(@Nullable List<Catalog> catalog) {
      this.catalog = catalog;
      return this;
    }
    @Override
    @Nullable List<Catalog> catalog() {
      return catalog;
    }
    @Override
    public Token.Builder auditIds(@Nullable List<String> auditIds) {
      this.auditIds = auditIds;
      return this;
    }
    @Override
    @Nullable List<String> auditIds() {
      return auditIds;
    }
    @Override
    public Token.Builder user(User user) {
      if (user == null) {
        throw new NullPointerException("Null user");
      }
      this.user = user;
      return this;
    }
    @Override
    public Token.Builder issuedAt(Date issuedAt) {
      if (issuedAt == null) {
        throw new NullPointerException("Null issuedAt");
      }
      this.issuedAt = issuedAt;
      return this;
    }
    @Override
    Token autoBuild() {
      String missing = "";
      if (this.methods == null) {
        missing += " methods";
      }
      if (this.user == null) {
        missing += " user";
      }
      if (this.issuedAt == null) {
        missing += " issuedAt";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Token(
          this.id,
          this.methods,
          this.expiresAt,
          this.extras,
          this.catalog,
          this.auditIds,
          this.user,
          this.issuedAt);
    }
  }

}
