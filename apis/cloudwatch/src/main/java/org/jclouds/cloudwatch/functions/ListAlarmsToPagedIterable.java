
package org.jclouds.cloudwatch.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.inject.Inject;
import org.jclouds.cloudwatch.CloudWatchApi;
import org.jclouds.cloudwatch.domain.Alarm;
import org.jclouds.cloudwatch.features.AlarmApi;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.internal.CallerArg0ToPagedIterable;

@Beta
public class ListAlarmsToPagedIterable extends CallerArg0ToPagedIterable<Alarm, ListAlarmsToPagedIterable> {

    private final CloudWatchApi api;

    @Inject
    ListAlarmsToPagedIterable(CloudWatchApi api) {
        this.api = checkNotNull(api, "api");
    }

    @Override
    protected Function<Object, IterableWithMarker<Alarm>> markerToNextForCallingArg0(final String arg0) {
        return new ListAlarmsFunction(arg0, api);
    }

    private static class ListAlarmsFunction implements Function<Object, IterableWithMarker<Alarm>> {
        private final String region;
        private final AlarmApi alarmApi;

        ListAlarmsFunction(String region, CloudWatchApi api) {
            this.region = region;
            this.alarmApi = api.getAlarmApiForRegion(region);
        }

        @Override
        public IterableWithMarker<Alarm> apply(Object input) {
            return alarmApi.listAt(input.toString());
        }

        @Override
        public String toString() {
            return "listAlarms(" + region + ")";
        }
    }
}
