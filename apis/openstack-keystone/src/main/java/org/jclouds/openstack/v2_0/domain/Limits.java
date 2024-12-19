
package org.jclouds.openstack.v2_0.domain;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.util.Map;

import jakarta.inject.Named;

import com.google.common.base.Objects;

public final class Limits {

   @Named("rate")
   private final Iterable<RateLimit> rateLimits;
   @Named("absolute")
   private final Map<String, Integer> absoluteLimits;

   @ConstructorProperties({ "rate", "absolute" })
   private Limits(Iterable<RateLimit> rateLimits, Map<String, Integer> absoluteLimits) {
      this.rateLimits = checkNotNull(rateLimits, "rateLimits");
      this.absoluteLimits = checkNotNull(absoluteLimits, "absoluteLimits");
   }

   public Iterable<RateLimit> getRateLimits() {
      return rateLimits;
   }

   public Map<String, Integer> getAbsoluteLimits() {
      return absoluteLimits;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(rateLimits, absoluteLimits);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!(obj instanceof Limits)) {
         return false;
      }
      Limits that = (Limits) obj;
      return equal(rateLimits, that.rateLimits) && equal(absoluteLimits, that.absoluteLimits);
   }

   @Override
   public String toString() {
      return toStringHelper(this)
            .add("rateLimits", rateLimits)
            .add("absoluteLimits", absoluteLimits)
            .toString();
   }
}
