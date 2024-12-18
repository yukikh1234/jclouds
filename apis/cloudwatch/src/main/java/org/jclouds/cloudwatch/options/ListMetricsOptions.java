
package org.jclouds.cloudwatch.options;

import java.util.Set;

import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.http.options.BaseHttpRequestOptions;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * Options used to list available metrics.
 *
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_ListMetrics.html" />
 */
public class ListMetricsOptions extends BaseHttpRequestOptions implements Cloneable {

   private Set<Dimension> dimensions = Sets.newLinkedHashSet();
   private String metricName;
   private String namespace;
   private Object afterMarker;

   public ListMetricsOptions namespace(String namespace) {
      this.namespace = namespace;
      return this;
   }

   public ListMetricsOptions metricName(String metricName) {
      this.metricName = metricName;
      return this;
   }

   public ListMetricsOptions dimensions(Iterable<Dimension> dimensions) {
      Iterables.addAll(this.dimensions, dimensions);
      return this;
   }

   public ListMetricsOptions dimension(Dimension dimension) {
      this.dimensions.add(dimension);
      return this;
   }

   public ListMetricsOptions afterMarker(Object afterMarker) {
      this.afterMarker = afterMarker;
      return this;
   }

   @Override
   public Multimap<String, String> buildFormParameters() {
      ImmutableMultimap.Builder<String, String> formParameters = ImmutableMultimap.builder();
      addNamespaceToFormParameters(formParameters);
      addMetricNameToFormParameters(formParameters);
      addDimensionsToFormParameters(formParameters);
      addAfterMarkerToFormParameters(formParameters);
      return formParameters.build();
   }

   private void addNamespaceToFormParameters(ImmutableMultimap.Builder<String, String> formParameters) {
      if (namespace != null) {
         formParameters.put("Namespace", namespace);
      }
   }

   private void addMetricNameToFormParameters(ImmutableMultimap.Builder<String, String> formParameters) {
      if (metricName != null) {
         formParameters.put("MetricName", metricName);
      }
   }

   private void addDimensionsToFormParameters(ImmutableMultimap.Builder<String, String> formParameters) {
      if (dimensions != null) {
         int dimensionIndex = 1;
         for (Dimension dimension : dimensions) {
            formParameters.put("Dimensions.member." + dimensionIndex + ".Name", dimension.getName());
            formParameters.put("Dimensions.member." + dimensionIndex + ".Value", dimension.getValue());
            dimensionIndex++;
         }
      }
   }

   private void addAfterMarkerToFormParameters(ImmutableMultimap.Builder<String, String> formParameters) {
      if (afterMarker != null) {
         formParameters.put("NextToken", afterMarker.toString());
      }
   }

   @Override
   public ListMetricsOptions clone() {
      return Builder.namespace(namespace).metricName(metricName).dimensions(dimensions).afterMarker(afterMarker);
   }
   
   public static class Builder {
      public static ListMetricsOptions namespace(String namespace) {
         return new ListMetricsOptions().namespace(namespace);
      }

      public static ListMetricsOptions metricName(String metricName) {
         return new ListMetricsOptions().metricName(metricName);
      }

      public static ListMetricsOptions dimensions(Iterable<Dimension> dimensions) {
         return new ListMetricsOptions().dimensions(dimensions);
      }

      public static ListMetricsOptions dimension(Dimension dimension) {
         return new ListMetricsOptions().dimension(dimension);
      }

      public static ListMetricsOptions afterMarker(Object afterMarker) {
         return new ListMetricsOptions().afterMarker(afterMarker);
      }
   }
}
