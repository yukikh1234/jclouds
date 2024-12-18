
package org.jclouds.s3.blobstore.functions;

import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.get;
import static org.jclouds.location.predicates.LocationPredicates.idEquals;

import java.util.NoSuchElementException;
import java.util.Set;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.domain.Location;
import org.jclouds.logging.Logger;
import org.jclouds.s3.Bucket;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;

@Singleton
public class LocationFromBucketName implements Function<String, Location> {
   private final Supplier<Set<? extends Location>> locations;
   private final Function<String, Optional<String>> bucketToRegion;

   @Resource
   protected Logger logger = Logger.NULL;

   @Inject
   LocationFromBucketName(@Bucket Function<String, Optional<String>> bucketToRegion,
            @Memoized Supplier<Set<? extends Location>> locations) {
      this.bucketToRegion = bucketToRegion;
      this.locations = locations;
   }

   public Location apply(String bucket) {
      Set<? extends Location> locations = this.locations.get();
      if (locations.size() == 1) {
         return get(locations, 0);
      }

      Optional<String> region = bucketToRegion.apply(bucket);
      return getLocationForRegion(locations, region, bucket);
   }

   private Location getLocationForRegion(Set<? extends Location> locations, Optional<String> region, String bucket) {
      if (region.isPresent()) {
         return findLocationByRegion(locations, region.get());
      }

      logger.debug("could not get region for %s", bucket);
      return null;
   }

   private Location findLocationByRegion(Set<? extends Location> locations, String region) {
      try {
         return find(locations, idEquals(region));
      } catch (NoSuchElementException e) {
         logger.debug("could not get location for region %s in %s", region, locations);
         return null;
      }
   }
}
