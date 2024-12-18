
package org.jclouds.cloudwatch.domain;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.Set;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class MetricDatum {

   private final Set<Dimension> dimensions;
   private final String metricName;
   private final Optional<StatisticValues> statisticValues;
   private final Optional<Date> timestamp;
   private final Unit unit;
   private final Optional<Double> value;

   protected MetricDatum(MetricDatumParams params) {
      this.dimensions = ImmutableSet.copyOf(checkNotNull(params.dimensions, "dimensions"));
      this.metricName = checkNotNull(params.metricName, "metricName");
      this.statisticValues = checkNotNull(params.statisticValues, "statisticValues");
      this.timestamp = checkNotNull(params.timestamp, "timestamp");
      this.unit = checkNotNull(params.unit, "unit");
      this.value = checkNotNull(params.value, "value");
   }

   public Set<Dimension> getDimensions() {
      return dimensions;
   }

   public String getMetricName() {
      return metricName;
   }

   public Optional<StatisticValues> getStatisticValues() {
      return statisticValues;
   }

   public Optional<Date> getTimestamp() {
      return timestamp;
   }

   public Unit getUnit() {
      return unit;
   }

   public Optional<Double> getValue() {
      return value;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {

      private final MetricDatumParams params = new MetricDatumParams();

      public Builder dimensions(Iterable<Dimension> dimensions) {
         this.params.dimensions = dimensions;
         return this;
      }

      public Builder dimension(Dimension dimension) {
         this.params.dimensions = ImmutableList.<Dimension>builder().add(dimension).build();
         return this;
      }

      public Builder metricName(String metricName) {
         this.params.metricName = metricName;
         return this;
      }

      public Builder statisticValues(StatisticValues statisticValues) {
         this.params.statisticValues = Optional.fromNullable(statisticValues);
         return this;
      }

      public Builder timestamp(Date timestamp) {
         this.params.timestamp = Optional.fromNullable(timestamp);
         return this;
      }

      public Builder unit(Unit unit) {
         this.params.unit = unit;
         return this;
      }

      public Builder value(Double value) {
         this.params.value = Optional.fromNullable(value);
         return this;
      }

      public MetricDatum build() {
         return new MetricDatum(params);
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (!(o instanceof MetricDatum))
         return false;
      MetricDatum that = (MetricDatum) o;
      return Objects.equals(dimensions, that.dimensions) &&
             Objects.equals(metricName, that.metricName) &&
             Objects.equals(statisticValues, that.statisticValues) &&
             Objects.equals(timestamp, that.timestamp) &&
             unit == that.unit &&
             Objects.equals(value, that.value);
   }

   @Override
   public int hashCode() {
      return Objects.hash(dimensions, metricName, statisticValues, timestamp, unit, value);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   protected ToStringHelper string() {
      return MoreObjects.toStringHelper(this)
              .omitNullValues()
              .add("dimensions", dimensions)
              .add("metricName", metricName)
              .add("statisticValues", statisticValues.orNull())
              .add("timestamp", timestamp.orNull())
              .add("unit", unit)
              .add("value", value.orNull());
   }

   private static class MetricDatumParams {
      private Iterable<Dimension> dimensions = ImmutableList.of();
      private String metricName;
      private Optional<StatisticValues> statisticValues = Optional.absent();
      private Optional<Date> timestamp = Optional.absent();
      private Unit unit = Unit.NONE;
      private Optional<Double> value = Optional.absent();
   }
}
