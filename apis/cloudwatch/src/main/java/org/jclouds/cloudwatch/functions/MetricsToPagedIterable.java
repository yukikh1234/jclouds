
package org.jclouds.cloudwatch.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.inject.Inject;

import org.jclouds.cloudwatch.CloudWatchApi;
import org.jclouds.cloudwatch.domain.Metric;
import org.jclouds.cloudwatch.features.MetricApi;
import org.jclouds.cloudwatch.options.ListMetricsOptions;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.internal.CallerArg0ToPagedIterable;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;

@Beta
public class MetricsToPagedIterable extends CallerArg0ToPagedIterable<Metric, MetricsToPagedIterable> {

   private final CloudWatchApi api;

   @Inject
   protected MetricsToPagedIterable(CloudWatchApi api) {
      this.api = checkNotNull(api, "api");
   }

   @Override
   protected Function<Object, IterableWithMarker<Metric>> markerToNextForCallingArg0(final String arg0) {
      final MetricApi metricApi = api.getMetricApiForRegion(arg0);
      return new MarkerToNextFunction(metricApi, arg0);
   }

   private static class MarkerToNextFunction implements Function<Object, IterableWithMarker<Metric>> {
      private final MetricApi metricApi;
      private final String region;

      MarkerToNextFunction(MetricApi metricApi, String region) {
         this.metricApi = metricApi;
         this.region = region;
      }

      @Override
      public IterableWithMarker<Metric> apply(Object input) {
         return metricApi.list(ListMetricsOptions.Builder.afterMarker(input.toString()));
      }

      @Override
      public String toString() {
         return "listMetricsInRegion(" + region + ")";
      }
   }
}
