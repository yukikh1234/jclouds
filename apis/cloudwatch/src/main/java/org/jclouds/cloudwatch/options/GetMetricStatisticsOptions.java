
package org.jclouds.cloudwatch.options;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.jclouds.aws.util.AWSUtils;
import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.cloudwatch.domain.Unit;
import org.jclouds.http.options.BaseHttpRequestOptions;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Options used to control metric statistics are returned
 * 
 * @see <a
 *      href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/index.html?API_GetMetricStatistics.html"
 *      />
 */
public class GetMetricStatisticsOptions extends BaseHttpRequestOptions {

   private Set<Dimension> dimensions;

   /**
    * Adds a dimension describing qualities of the metric.
    *
    * @param dimension the dimension describing the qualities of the metric
    *
    * @return this {@code GetMetricStatisticsOptions} object
    */
   public GetMetricStatisticsOptions addDimension(Dimension dimension) {
      if (dimension == null) {
         throw new NullPointerException("dimension cannot be null");
      }
      if (dimensions == null) {
         dimensions = Sets.newLinkedHashSet();
      }
      this.dimensions.add(dimension);
      return this;
   }
   
   /**
    * Sets a list of dimensions describing qualities of the metric.
    *
    * @param dimensions the dimensions describing the qualities of the metric
    *
    * @return this {@code GetMetricStatisticsOptions} object
    */
   public GetMetricStatisticsOptions dimensions(Set<Dimension> dimensions) {
      if (dimensions == null) {
         throw new NullPointerException("dimensions cannot be null");
      }
      this.dimensions = dimensions;
      return this;
   }
   
   /**
    * @param instanceId
    *           filter metrics by instance Id
    */
   public GetMetricStatisticsOptions instanceId(String instanceId) {
      String[] parts = AWSUtils.parseHandle(instanceId);
      this.formParameters.put("Dimensions.member.1.Name", "InstanceId");
      this.formParameters.put("Dimensions.member.1.Value", checkNotNull(parts[1]));
      return this;
   }

   /**
    * @param unit
    *          the unit of the metric
    */
   public GetMetricStatisticsOptions unit(Unit unit) {
      this.formParameters.put("Unit", unit.toString());
      return this;
   }

   public static class Builder {

      /**
       * @see GetMetricStatisticsOptions#instanceId
       */
      public static GetMetricStatisticsOptions instanceId(String instanceId) {
         GetMetricStatisticsOptions options = new GetMetricStatisticsOptions();
         return options.instanceId(instanceId);
      }

      /**
       * @see GetMetricStatisticsOptions#unit
       */
      public static GetMetricStatisticsOptions unit(Unit unit) {
         GetMetricStatisticsOptions options = new GetMetricStatisticsOptions();
         return options.unit(unit);
      }
      
      /**
       * @see GetMetricStatisticsOptions#addDimension
       */
      public static GetMetricStatisticsOptions addDimension(Dimension dimension) {
         GetMetricStatisticsOptions options = new GetMetricStatisticsOptions();
         return options.addDimension(dimension);
      }
      
      /**
       * @see GetMetricStatisticsOptions#dimensions
       */
      public static GetMetricStatisticsOptions dimensions(Set<Dimension> dimensions) {
         GetMetricStatisticsOptions options = new GetMetricStatisticsOptions();
         return options.dimensions(dimensions);
      }
    }

   @Override
   public Multimap<String, String> buildFormParameters() {
      Multimap<String, String> formParameters = super.buildFormParameters();
      handleDimensions(formParameters);
      return formParameters;
   }

   private void handleDimensions(Multimap<String, String> formParameters) {
      if (dimensions != null) {
         int dimensionIndex = 1;
         for (Dimension dimension : dimensions) {
            formParameters.put("Dimensions.member." + dimensionIndex + ".Name", dimension.getName());
            formParameters.put("Dimensions.member." + dimensionIndex + ".Value", dimension.getValue());
            dimensionIndex++;
         }
      }
   }
}
