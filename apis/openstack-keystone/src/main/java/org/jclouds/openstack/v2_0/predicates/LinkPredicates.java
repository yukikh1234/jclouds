
package org.jclouds.openstack.v2_0.predicates;

import static com.google.common.base.Preconditions.checkNotNull;
import java.net.URI;
import org.jclouds.openstack.v2_0.domain.Link;
import org.jclouds.openstack.v2_0.domain.Link.Relation;
import com.google.common.base.Predicate;

/**
 * Predicates handy when working with Link Types
 */
public class LinkPredicates {

   private LinkPredicates() {
      // Private constructor to prevent instantiation
   }

   /**
    * Matches links of the given relation.
    *
    * @param rel relation of the link
    * @return predicate that will match links of the given rel
    */
   public static Predicate<Link> relationEquals(Relation rel) {
      checkNotNull(rel, "rel must be defined");
      return link -> rel.equals(link.getRelation());
   }

   /**
    * Matches links of the given href.
    *
    * @param href
    * @return predicate that will match links of the given href
    */
   public static Predicate<Link> hrefEquals(URI href) {
      checkNotNull(href, "href must be defined");
      return link -> href.equals(link.getHref());
   }

   /**
    * Matches links of the given type.
    *
    * @param type ex. application/pdf
    * @return predicate that will match links of the given type
    */
   public static Predicate<Link> typeEquals(String type) {
      checkNotNull(type, "type must be defined");
      return link -> type.equals(link.getType().orNull());
   }
}
