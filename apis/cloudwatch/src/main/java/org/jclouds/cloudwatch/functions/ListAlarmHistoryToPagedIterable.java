
package org.jclouds.cloudwatch.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.inject.Inject;
import org.jclouds.cloudwatch.CloudWatchApi;
import org.jclouds.cloudwatch.domain.AlarmHistoryItem;
import org.jclouds.cloudwatch.features.AlarmApi;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.internal.CallerArg0ToPagedIterable;

@Beta
public class ListAlarmHistoryToPagedIterable
      extends CallerArg0ToPagedIterable<AlarmHistoryItem, ListAlarmHistoryToPagedIterable> {

   private final CloudWatchApi api;

   @Inject
   ListAlarmHistoryToPagedIterable(CloudWatchApi api) {
      this.api = checkNotNull(api, "api");
   }

   @Override
   protected Function<Object, IterableWithMarker<AlarmHistoryItem>> markerToNextForCallingArg0(final String arg0) {
      final AlarmApi alarmApi = api.getAlarmApiForRegion(arg0);
      return new AlarmHistoryFunction(alarmApi, arg0);
   }

   private static class AlarmHistoryFunction implements Function<Object, IterableWithMarker<AlarmHistoryItem>> {
      private final AlarmApi alarmApi;
      private final String region;

      AlarmHistoryFunction(AlarmApi alarmApi, String region) {
         this.alarmApi = alarmApi;
         this.region = region;
      }

      @Override
      public IterableWithMarker<AlarmHistoryItem> apply(Object input) {
         return alarmApi.listHistoryAt(input.toString());
      }

      @Override
      public String toString() {
         return "listHistory(" + region + ")";
      }
   }
}
