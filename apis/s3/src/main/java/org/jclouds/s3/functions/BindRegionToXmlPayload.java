
package org.jclouds.s3.functions;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.MediaType;

import org.jclouds.http.HttpRequest;
import org.jclouds.location.Region;
import org.jclouds.logging.Logger;
import org.jclouds.rest.binders.BindToStringPayload;
import org.jclouds.s3.Bucket;

import com.google.common.base.Supplier;

/**
 * Depending on your latency and legal requirements, you can specify a location
 * constraint that will affect where your data physically resides.
 */
@Singleton
public class BindRegionToXmlPayload extends BindToStringPayload {
   @Resource
   protected Logger logger = Logger.NULL;

   private final Supplier<String> defaultRegionForEndpointSupplier;
   private final Supplier<String> defaultRegionForServiceSupplier;
   private final Supplier<Set<String>> regionsSupplier;

   @Inject
   public BindRegionToXmlPayload(@Region Supplier<String> defaultRegionForEndpointSupplier,
         @Bucket Supplier<String> defaultRegionForServiceSupplier, @Region Supplier<Set<String>> regionsSupplier) {
      this.defaultRegionForEndpointSupplier = defaultRegionForEndpointSupplier;
      this.defaultRegionForServiceSupplier = defaultRegionForServiceSupplier;
      this.regionsSupplier = checkNotNull(regionsSupplier, "regions");
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      String defaultRegionForEndpoint = defaultRegionForEndpointSupplier.get();
      if (defaultRegionForEndpoint == null) {
         return request;
      }
      input = input == null ? defaultRegionForEndpoint : input;
      checkArgument(input instanceof String, "this binder is only valid for Region!");
      String constraint = (String) input;
      String value = determineRegionValue(constraint);
      String payload = createPayload(value);
      request = super.bindToRequest(request, payload);
      request.getPayload().getContentMetadata().setContentType(MediaType.TEXT_XML);
      return request;
   }

   private String determineRegionValue(String constraint) {
      String defaultRegionForService = defaultRegionForServiceSupplier.get();
      Set<String> regions = regionsSupplier.get();
      if ((defaultRegionForService == null && constraint == null)
            || (defaultRegionForService != null && defaultRegionForService.equals(constraint))) {
         return null; // nothing to bind as this is default.
      } else if (regions.contains(constraint)) {
         return constraint;
      } else {
         logger.warn("region %s not in %s ", constraint, regions);
         return constraint;
      }
   }

   private String createPayload(String value) {
      return String.format(
            "<CreateBucketConfiguration><LocationConstraint>%s</LocationConstraint></CreateBucketConfiguration>",
            value);
   }
}
