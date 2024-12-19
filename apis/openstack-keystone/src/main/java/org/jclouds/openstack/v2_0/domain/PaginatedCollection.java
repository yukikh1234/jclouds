
package org.jclouds.openstack.v2_0.domain;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.openstack.v2_0.options.PaginationOptions;

import java.util.Iterator;
import java.util.stream.StreamSupport;

import static org.jclouds.http.utils.Queries.queryParser;

public class PaginatedCollection<T> extends IterableWithMarker<T> {
   private final Iterable<T> resources;
   private final Iterable<Link> links;
   private final Integer totalEntries;

   protected PaginatedCollection(@Nullable Iterable<T> resources, @Nullable Iterable<Link> links,
                                 @Nullable Integer totalEntries) {
      this.resources = resources != null ? resources : ImmutableSet.<T>of();
      this.links = links != null ? links : ImmutableSet.<Link>of();
      this.totalEntries = totalEntries;
   }

   protected PaginatedCollection(Iterable<T> resources, Iterable<Link> links) {
      this(resources, links, null);
   }

   @Override
   public Iterator<T> iterator() {
      return resources.iterator();
   }

   public Iterable<Link> getLinks() {
      return links;
   }

   public Optional<Integer> getTotalEntries() {
      return Optional.fromNullable(totalEntries);
   }

   public PaginationOptions nextPaginationOptions() {
      return PaginationOptions.class.cast(nextMarker().get());
   }

   @Override
   public Optional<Object> nextMarker() {
      return StreamSupport.stream(getLinks().spliterator(), false)
              .filter(link -> Link.Relation.NEXT == link.getRelation())
              .map(this::toPaginationOptions)
              .findFirst()
              .map(Optional::of)
              .orElse(Optional.absent());
   }

   private Object toPaginationOptions(Link link) {
      Multimap<String, String> queryParams = queryParser().apply(link.getHref().getRawQuery());
      return PaginationOptions.Builder.queryParameters(queryParams);
   }
}
