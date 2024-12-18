
package org.jclouds.s3.functions;

import java.net.URI;

import jakarta.inject.Inject;

import org.jclouds.location.functions.RegionToEndpointOrProviderIfNull;
import org.jclouds.s3.Bucket;

import com.google.common.base.Function;
import com.google.common.base.Optional;

public final class AssignCorrectHostnameForBucket implements Function<Object, URI> {

   private final RegionToEndpointOrProviderIfNull delegate;
   private final Function<String, Optional<String>> bucketToRegion;

   @Inject
   AssignCorrectHostnameForBucket(RegionToEndpointOrProviderIfNull delegate,
            @Bucket Function<String, Optional<String>> bucketToRegion) {
      this.bucketToRegion = bucketToRegion;
      this.delegate = delegate;
   }

   @Override
   public URI apply(Object from) {
      String bucketName = from.toString();
      Optional<String> region = getRegionForBucket(bucketName);
      return applyDelegate(region);
   }

   private Optional<String> getRegionForBucket(String bucketName) {
      return bucketToRegion.apply(bucketName);
   }

   private URI applyDelegate(Optional<String> region) {
      return delegate.apply(region.orNull());
   }
}
