
package org.jclouds.cloudwatch.domain;

import java.util.Iterator;
import java.util.Objects;

import org.jclouds.javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

/**
 *  the list of {@link Datapoint} for the metric
 *  
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_GetMetricStatistics.html" />
 */
public class GetMetricStatisticsResponse extends FluentIterable<Datapoint> {

   private final ImmutableSet<Datapoint> datapoints;
   private final String label;

   public GetMetricStatisticsResponse(@Nullable Iterable<Datapoint> datapoints, String label) {
      if (label == null || label.isEmpty()) {
         throw new IllegalArgumentException("Label cannot be null or empty");
      }
      this.datapoints = (datapoints == null) ? ImmutableSet.of() : ImmutableSet.copyOf(datapoints);
      this.label = label;
   }

   /**
    * return the label describing the specified metric
    */
   public String getLabel() {
      return label;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      return Objects.hash(datapoints, label);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof GetMetricStatisticsResponse)) return false;
      GetMetricStatisticsResponse other = (GetMetricStatisticsResponse) obj;
      return Objects.equals(this.datapoints, other.datapoints) &&
             Objects.equals(this.label, other.label);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
                        .add("label", label)
                        .add("datapoints", datapoints)
                        .toString();
   }

   @Override
   public Iterator<Datapoint> iterator() {
      return datapoints.iterator();
   }
}
