
package org.jclouds.cloudwatch.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.jclouds.javax.annotation.Nullable;

import java.util.Set;

/**
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_Metric.html" />
 */
public class Metric {

   private final Set<Dimension> dimensions;
   private final String metricName;
   private final String namespace;

   public Metric(String metricName, String namespace, @Nullable Set<Dimension> dimensions) {
      this.dimensions = initializeDimensions(dimensions);
      this.metricName = metricName;
      this.namespace = namespace;
   }

   private Set<Dimension> initializeDimensions(@Nullable Set<Dimension> dimensions) {
      return (dimensions == null) ? Sets.newLinkedHashSet() : dimensions;
   }

   /**
    * return the metric name for the metric.
    */
   public String getMetricName() {
      return metricName;
   }

   /**
    * return the namespace for the metric
    */
   public String getNamespace() {
      return namespace;
   }

   /**
    * return the available dimensions for the metric
    */
   @Nullable
   public Set<Dimension> getDimensions() {
      return dimensions;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      return Objects.hashCode(dimensions, metricName, namespace);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Metric other = (Metric) obj;
      return areDimensionsEqual(other) && areMetricNamesEqual(other) && areNamespacesEqual(other);
   }

   private boolean areDimensionsEqual(Metric other) {
      return Objects.equal(this.dimensions, other.dimensions);
   }

   private boolean areMetricNamesEqual(Metric other) {
      return Objects.equal(this.metricName, other.metricName);
   }

   private boolean areNamespacesEqual(Metric other) {
      return Objects.equal(this.namespace, other.namespace);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .add("namespace", namespace)
            .add("metricName", metricName)
            .add("dimension", dimensions).toString();
   }

}
