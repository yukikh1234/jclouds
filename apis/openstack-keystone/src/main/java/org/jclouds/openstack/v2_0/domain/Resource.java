
package org.jclouds.openstack.v2_0.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.util.Objects;
import java.util.Set;

import org.jclouds.javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableSet;

/**
 * Resource found in a paginated collection
 *
 * @see <a href=
"http://docs.openstack.org/api/openstack-compute/1.1/content/Paginated_Collections-d1e664.html"
/>
 */
public class Resource implements Comparable<Resource> {

   private final String id;
   private final String name;
   private final Set<Link> links;

   @ConstructorProperties({
         "id", "name", "links"
   })
   protected Resource(String id, @Nullable String name, @Nullable Set<Link> links) {
      this.id = checkNotNull(id, "id");
      this.name = name;
      this.links = links == null ? null : ImmutableSet.copyOf(links);
   }

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   public Builder<?> toBuilder() {
      return new ConcreteBuilder().fromResource(this);
   }

   public abstract static class Builder<T extends Builder<T>>  {
      protected abstract T self();

      protected String id;
      protected String name;
      protected Set<Link> links;

      public T id(String id) {
         this.id = id;
         return self();
      }

      public T name(String name) {
         this.name = name;
         return self();
      }

      public T links(Set<Link> links) {
         this.links = links == null ? null : ImmutableSet.copyOf(links);
         return self();
      }

      public T links(Link... in) {
         return links(in == null ? null : ImmutableSet.copyOf(in));
      }

      public Resource build() {
         return new Resource(id, name, links);
      }

      public T fromResource(Resource in) {
         return this
               .id(in.getId())
               .name(in.getName())
               .links(in.getLinks());
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }

   /**
    * When providing an ID, it is assumed that the resource exists in the current OpenStack
    * deployment
    *
    * @return the id of the resource in the current OpenStack deployment
    */
   public String getId() {
      return this.id;
   }

   /**
    * @return the name of the resource
    */
   @Nullable
   public String getName() {
      return this.name;
   }

   /**
    * @return the links of the id address allocated to the new server
    */
   @Nullable
   public Set<Link> getLinks() {
      return this.links;
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name, links);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      Resource that = (Resource) obj;
      return Objects.equals(this.id, that.id) &&
             Objects.equals(this.name, that.name) &&
             Objects.equals(this.links, that.links);
   }

   protected ToStringHelper string() {
      return MoreObjects.toStringHelper(this)
            .add("id", id).add("name", name).add("links", links);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   @Override
   public int compareTo(Resource that) {
      if (that == null) return 1;
      return this.id.compareTo(that.id);
   }
}
