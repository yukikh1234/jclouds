
package org.jclouds.openstack.keystone.v2_0.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.util.Set;

import org.jclouds.javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableSet;

public class User extends ForwardingSet<Role> {

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   public Builder<?> toBuilder() {
      return new ConcreteBuilder().fromUser(this);
   }

   public abstract static class Builder<T extends Builder<T>> {
      protected abstract T self();

      protected String id;
      protected String name;
      protected String email;
      protected Boolean enabled;
      protected String tenantId;
      protected ImmutableSet.Builder<Role> roles = ImmutableSet.builder();

      public T id(String id) {
         this.id = id;
         return self();
      }

      public T name(String name) {
         this.name = name;
         return self();
      }

      public T email(String email) {
         this.email = email;
         return self();
      }

      public T enabled(Boolean enabled) {
         this.enabled = enabled;
         return self();
      }

      public T tenantId(String tenantId) {
         this.tenantId = tenantId;
         return self();
      }

      public T role(Role role) {
         this.roles.add(role);
         return self();
      }

      public T roles(Iterable<Role> roles) {
         this.roles.addAll(roles);
         return self();
      }

      public User build() {
         return new User(id, name, email, enabled, tenantId, roles.build());
      }

      public T fromUser(User in) {
         return this.id(in.getId()).name(in.getName()).email(in.getEmail()).enabled(in.isEnabled())
               .tenantId(in.getTenantId()).roles(in);
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }

   private final String id;
   private final String name;
   private final String email;
   private final Boolean enabled;
   private final String tenantId;
   private final Set<Role> roles;

   @ConstructorProperties({ "id", "name", "email", "enabled", "tenantId", "roles" })
   protected User(String id, String name, @Nullable String email, @Nullable Boolean enabled, @Nullable String tenantId,
         @Nullable Set<Role> roles) {
      this.id = checkNotNull(id, "id");
      this.name = checkNotNull(name, "name");
      this.email = email;
      this.enabled = enabled;
      this.tenantId = tenantId;
      this.roles = roles == null ? ImmutableSet.of() : ImmutableSet.copyOf(roles);
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getEmail() {
      return this.email;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public String getTenantId() {
      return this.tenantId;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(id, name, email, enabled, tenantId, roles);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      User that = (User) obj;
      return equalAttributes(that);
   }

   private boolean equalAttributes(User that) {
      return Objects.equal(id, that.id) && Objects.equal(name, that.name)
            && Objects.equal(roles, that.roles) && Objects.equal(enabled, that.enabled)
            && Objects.equal(tenantId, that.tenantId) && Objects.equal(email, that.email);
   }

   protected ToStringHelper string() {
      return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("email", email)
            .add("enabled", enabled).add("roles", roles).add("tenanId", tenantId);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   @Override
   protected Set<Role> delegate() {
      return roles;
   }

}
