
package org.jclouds.cloudwatch.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * Options use to get statistics for the specified metric.
 *
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_GetMetricStatistics.html" />
 */
@Beta
public class GetMetricStatistics {

   private final Set<Dimension> dimensions;
   private final Optional<Date> endTime;
   private final String metricName;
   private final String namespace;
   private final int period;
   private final Optional<Date> startTime;
   private final Set<Statistics> statistics;
   private final Optional<Unit> unit;

   protected GetMetricStatistics(Set<Dimension> dimensions, Date endTime, String metricName, String namespace, int period,
                               Date startTime, Set<Statistics> statistics, Unit unit) {
      this.dimensions = ImmutableSet.copyOf(checkNotNull(dimensions, "dimensions"));
      this.endTime = Optional.fromNullable(endTime);
      this.metricName = checkNotNull(metricName, "metricName");
      this.namespace = checkNotNull(namespace, "namespace");
      this.period = period;
      this.startTime = Optional.fromNullable(startTime);
      this.statistics = ImmutableSet.copyOf(checkNotNull(statistics, "statistics"));
      this.unit = Optional.fromNullable(unit);
   }

   public Set<Dimension> getDimensions() {
      return dimensions;
   }

   public Optional<Date> getEndTime() {
      return endTime;
   }

   public String getMetricName() {
      return metricName;
   }

   public String getNamespace() {
      return namespace;
   }

   public int getPeriod() {
      return period;
   }

   public Optional<Date> getStartTime() {
      return startTime;
   }

   public Set<Statistics> getStatistics() {
      return statistics;
   }

   public Optional<Unit> getUnit() {
      return unit;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {

      private Set<Dimension> dimensions = Sets.newLinkedHashSet();
      private Date endTime;
      private String metricName;
      private String namespace;
      private int period = 60;
      private Date startTime;
      private Set<Statistics> statistics = Sets.newLinkedHashSet();
      private Unit unit;

      public Builder dimensions(Set<Dimension> dimensions) {
         this.dimensions.addAll(checkNotNull(dimensions, "dimensions"));
         return this;
      }

      public Builder dimension(Dimension dimension) {
         this.dimensions.add(checkNotNull(dimension, "dimension"));
         return this;
      }

      public Builder endTime(Date endTime) {
         this.endTime = endTime;
         return this;
      }

      public Builder metricName(String metricName) {
         this.metricName = metricName;
         return this;
      }

      public Builder namespace(String namespace) {
         this.namespace = namespace;
         return this;
      }

      public Builder period(int period) {
         this.period = period;
         return this;
      }

      public Builder startTime(Date startTime) {
         this.startTime = startTime;
         return this;
      }

      public Builder statistics(Set<Statistics> statistics) {
         this.statistics.addAll(checkNotNull(statistics, "statistics"));
         return this;
      }

      public Builder statistic(Statistics statistic) {
         this.statistics.add(checkNotNull(statistic, "statistic"));
         return this;
      }

      public Builder unit(Unit unit) {
         this.unit = unit;
         return this;
      }

      public GetMetricStatistics build() {
         return new GetMetricStatistics(dimensions, endTime, metricName, namespace, period, startTime, statistics, unit);
      }
   }
}
