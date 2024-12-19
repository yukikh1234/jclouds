
package org.jclouds.openstack.v2_0.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.Date;
import java.util.Set;

import org.jclouds.javax.annotation.Nullable;
import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;

public class Extension extends Resource {

   public static Builder builder() { 
      return new ConcreteBuilder();
   }
   
   public Builder toBuilder() { 
      return new ConcreteBuilder().fromExtension(this);
   }

   public abstract static class Builder<T extends Builder<T>> extends Resource.Builder<T>  {
      protected URI namespace;
      protected String alias;
      protected Date updated;
      protected String description;
   
      public T namespace(URI namespace) {
         this.namespace = namespace;
         return self();
      }

      public T alias(String alias) {
         this.alias = alias;
         return self();
      }

      @Override
      public T id(String id) {
         return alias(id);
      }

      public T updated(Date updated) {
         this.updated = updated;
         return self();
      }

      public T description(String description) {
         this.description = description;
         return self();
      }

      public Extension build() {
         return new Extension(name, links, namespace, alias, updated, description);
      }
      
      public T fromExtension(Extension in) {
         return super.fromResource(in)
                  .namespace(in.getNamespace())
                  .alias(in.getAlias())
                  .updated(in.getUpdated())
                  .description(in.getDescription());
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }

   private final URI namespace;
   private final String alias;
   private final Date updated;
   private final String description;

   @ConstructorProperties({
      "name", "links", "namespace", "alias", "updated", "description"
   })
   protected Extension(@Nullable String name, Set<Link> links, @Nullable URI namespace, String alias,
         @Nullable Date updated, String description) {
      super(alias, name, links);
      this.namespace = namespace;
      this.alias = checkNotNull(alias, "alias");
      this.updated = updated;
      this.description = checkNotNull(description, "description");
   }

   @Nullable
   public URI getNamespace() {
      return this.namespace;
   }

   public String getAlias() {
      return this.alias;
   }

   @Nullable
   public Date getUpdated() {
      return this.updated;
   }

   public String getDescription() {
      return this.description;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(namespace, alias, updated, description);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof Extension)) return false;
      Extension that = (Extension) obj;
      return super.equals(that) && Objects.equal(namespace, that.namespace)
               && Objects.equal(alias, that.alias)
               && Objects.equal(updated, that.updated)
               && Objects.equal(description, that.description);
   }
   
   protected ToStringHelper string() {
      return super.string()
            .add("namespace", namespace)
            .add("alias", alias)
            .add("updated", updated)
            .add("description", description);
   }
}
