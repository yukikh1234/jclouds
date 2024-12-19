
package org.jclouds.openstack.v2_0.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.net.URI;

import jakarta.inject.Named;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;

public class Link {
   
   public enum Relation {
      SELF, BOOKMARK, DESCRIBEDBY, NEXT, PREVIOUS, ALTERNATE, UNRECOGNIZED;

      public String value() {
         return name().toLowerCase();
      }

      public static Relation fromValue(String v) {
         try {
            return valueOf(v.toUpperCase());
         } catch (IllegalArgumentException e) {
            return UNRECOGNIZED;
         }
      }
   }

   public static Link create(Relation relation, URI href) {
      return new Link(relation, null, href);
   }

   public static Link create(Relation relation, String type, URI href) {
      return new Link(relation, Optional.fromNullable(type), href);
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromLink(this);
   }

   public static class Builder {
      private Link.Relation relation;
      private Optional<String> type = Optional.absent();
      private URI href;

      public Builder relation(Link.Relation relation) {
         this.relation = relation;
         return this;
      }

      public Builder type(String type) {
         this.type = Optional.fromNullable(type);
         return this;
      }

      public Builder href(URI href) {
         this.href = href;
         return this;
      }

      public Link build() {
         return new Link(relation, type, href);
      }

      public Builder fromLink(Link in) {
         return this.relation(in.getRelation()).type(in.getType().orNull()).href(in.getHref());
      }
   }

   @Named("rel")
   private final Link.Relation relation;
   private final Optional<String> type;
   private final URI href;

   @ConstructorProperties({ "rel", "type", "href" })
   protected Link(Link.Relation relation, Optional<String> type, URI href) {
      this.href = checkNotNull(href, "href");
      this.relation = checkNotNull(relation, "relation of %s", href);
      this.type = (type == null) ? Optional.<String>absent() : type;
   }

   public Link.Relation getRelation() {
      return this.relation;
   }

   public Optional<String> getType() {
      return this.type;
   }

   public URI getHref() {
      return this.href;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(relation, type, href);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      Link that = (Link) obj;
      return Objects.equal(this.relation, that.relation) && Objects.equal(this.type, that.type) && Objects.equal(this.href, that.href);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).omitNullValues().add("relation", relation).add("type", type.orNull()).add("href", href).toString();
   }
}
