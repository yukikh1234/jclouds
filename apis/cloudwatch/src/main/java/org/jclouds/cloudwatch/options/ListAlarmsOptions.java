
package org.jclouds.cloudwatch.options;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.jclouds.cloudwatch.domain.Alarm;
import org.jclouds.http.options.BaseHttpRequestOptions;

/**
 * Options used to describe alarms.
 *
 * @see <a href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_DescribeAlarms.html" />
 */
public class ListAlarmsOptions extends BaseHttpRequestOptions {

   private int alarmIndex = 1;

   /**
    * The action name prefix.
    *
    * @param actionPrefix the action name prefix
    *
    * @return this {@code ListAlarmsOptions} object
    */
   public ListAlarmsOptions actionPrefix(String actionPrefix) {
      validateActionPrefix(actionPrefix);
      formParameters.put("ActionPrefix", actionPrefix);
      return this;
   }

   private void validateActionPrefix(String actionPrefix) {
      checkNotNull(actionPrefix, "actionPrefix");
      checkArgument(actionPrefix.length() <= 1024, "actionPrefix must be between 1 and 1024 characters in length");
   }

   /**
    * The alarm name prefix.
    *
    * @param alarmNamePrefix the alarm name prefix
    *
    * @return this {@code ListAlarmsOptions} object
    */
   public ListAlarmsOptions alarmNamePrefix(String alarmNamePrefix) {
      validateAlarmNamePrefix(alarmNamePrefix);
      formParameters.put("AlarmNamePrefix", alarmNamePrefix);
      return this;
   }

   private void validateAlarmNamePrefix(String alarmNamePrefix) {
      checkNotNull(alarmNamePrefix, "alarmNamePrefix");
      checkArgument(alarmNamePrefix.length() <= 255, "alarmNamePrefix must be between 1 and 255 characters in length");
   }

   /**
    * The list of alarm names to retrieve information for.
    *
    * @param alarmNames the alarm names
    *
    * @return this {@code ListAlarmsOptions} object
    */
   public ListAlarmsOptions alarmNames(Set<String> alarmNames) {
      checkNotNull(alarmNames, "alarmNames");
      alarmNames.forEach(this::alarmName);
      return this;
   }

   /**
    * The alarm name to retrieve information for.
    *
    * @param alarmName the alarm name
    *
    * @return this {@code ListAlarmsOptions} object
    */
   public ListAlarmsOptions alarmName(String alarmName) {
      validateAlarmName(alarmName);
      formParameters.put("AlarmNames.member." + alarmIndex++, alarmName);
      return this;
   }

   private void validateAlarmName(String alarmName) {
      checkArgument(alarmIndex <= 100, "maximum number of alarm names is 100");
      checkNotNull(alarmName, "alarmName");
   }

   /**
    * The maximum number of alarm descriptions to retrieve.
    *
    * @param maxRecords  maximum number of alarm descriptions to retrieve
    *
    * @return this {@code ListAlarmsOptions} object
    */
   public ListAlarmsOptions maxRecords(int maxRecords) {
      formParameters.put("MaxRecords", String.valueOf(maxRecords));
      return this;
   }

   /**
    * The state value to be used in matching alarms.
    *
    * @param state state value to be used in matching alarms
    *
    * @return this {@code ListAlarmsOptions} object
    */
   public ListAlarmsOptions state(Alarm.State state) {
      validateState(state);
      formParameters.put("StateValue", state.toString());
      return this;
   }

   private void validateState(Alarm.State state) {
      checkNotNull(state, "state");
      checkArgument(state != Alarm.State.UNRECOGNIZED, "state unrecognized");
   }
}
