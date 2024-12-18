
package org.jclouds.s3.functions;

import java.net.URI;

import jakarta.inject.Inject;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.s3.Bucket;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.cache.LoadingCache;

public final class DefaultEndpointThenInvalidateRegion implements Function<Object, URI> {

   private final Function<Object, URI> delegate;
   private final LoadingCache<String, Optional<String>> bucketToRegionCache;

   @Inject
   public DefaultEndpointThenInvalidateRegion(AssignCorrectHostnameForBucket delegate,
         @Bucket LoadingCache<String, Optional<String>> bucketToRegionCache) {
      this.delegate = delegate;
      this.bucketToRegionCache = bucketToRegionCache;
   }

   @Override
   public URI apply(@Nullable Object from) {
      URI result = delegate.apply(from);
      bucketToRegionCache.invalidate(from.toString());
      return result;
   }
}
