
package org.jclouds.cloudwatch.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jclouds.javax.annotation.Nullable;

import java.util.Date;

/**
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference?DT_Datapoint.html"
 *      />
 */
public class Datapoint {

   private final Double average;
   private final Double maximum;
   private final Double minimum;
   private final Date timestamp;
   private final Double samples;
   private final Double sum;
   private final Unit unit;
   private final String customUnit;

   public Datapoint(@Nullable Double average, @Nullable Double maximum, @Nullable Double minimum,
            @Nullable Date timestamp, @Nullable Double samples, @Nullable Double sum, @Nullable Unit unit,
            @Nullable String customUnit) {
      this.average = average;
      this.maximum = maximum;
      this.minimum = minimum;
      this.timestamp = timestamp;
      this.samples = samples;
      this.sum = sum;
      this.unit = unit;
      this.customUnit = customUnit;
   }

   @Nullable
   public Double getAverage() {
      return average;
   }

   @Nullable
   public Double getMaximum() {
      return maximum;
   }

   @Nullable
   public Double getMinimum() {
      return minimum;
   }

   @Nullable
   public Date getTimestamp() {
      return timestamp;
   }

   @Nullable
   public Double getSamples() {
      return samples;
   }

   @Nullable
   public Double getSum() {
      return sum;
   }

   @Nullable
   public Unit getUnit() {
      return unit;
   }

   @Nullable
   public String getCustomUnit() {
      return customUnit;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(average, customUnit, maximum, minimum, samples, sum, timestamp, unit);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof Datapoint)) return false;
      Datapoint other = (Datapoint) obj;
      return Objects.equal(average, other.average) &&
             Objects.equal(customUnit, other.customUnit) &&
             Objects.equal(maximum, other.maximum) &&
             Objects.equal(minimum, other.minimum) &&
             Objects.equal(samples, other.samples) &&
             Objects.equal(sum, other.sum) &&
             Objects.equal(timestamp, other.timestamp) &&
             Objects.equal(unit, other.unit);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
                    .add("timestamp", timestamp)
                    .add("customUnit", customUnit)
                    .add("maximum", maximum)
                    .add("minimum", minimum)
                    .add("average", average)
                    .add("sum", sum)
                    .add("samples", samples)
                    .add("unit", unit).toString();
   }
}
