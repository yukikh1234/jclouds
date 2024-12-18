
package org.jclouds.cloudwatch.binders;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMultimap;
import org.jclouds.http.HttpRequest;

/**
 * Binds the alarm names request to the http request
 */
@Beta
public class AlarmNamesBinder implements org.jclouds.rest.Binder {

   @SuppressWarnings("unchecked")
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      Iterable<String> alarmNames = extractAlarmNames(input);
      ImmutableMultimap<String, String> formParameters = buildFormParameters(alarmNames);
      return buildRequestWithFormParams(request, formParameters);
   }

   private Iterable<String> extractAlarmNames(Object input) {
      return (Iterable<String>) checkNotNull(input, "alarm names must be set");
   }

   private ImmutableMultimap<String, String> buildFormParameters(Iterable<String> alarmNames) {
      ImmutableMultimap.Builder<String, String> formParameters = ImmutableMultimap.builder();
      int alarmNameIndex = 1;
      for (String alarmName : alarmNames) {
         formParameters.put("AlarmNames.member." + alarmNameIndex, alarmName);
         alarmNameIndex++;
      }
      return formParameters.build();
   }

   private <R extends HttpRequest> R buildRequestWithFormParams(R request, ImmutableMultimap<String, String> formParameters) {
      return (R) request.toBuilder().replaceFormParams(formParameters).build();
   }
}
