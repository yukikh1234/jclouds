
package org.jclouds.cloudwatch.options;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import com.google.common.annotations.Beta;
import org.jclouds.cloudwatch.domain.HistoryItemType;
import org.jclouds.date.DateService;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.http.options.BaseHttpRequestOptions;

/**
 * Options used to describe alarm history.
 *
 * @see <a href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_DescribeAlarmHistory.html" />
 */
@Beta
public class ListAlarmHistoryOptions extends BaseHttpRequestOptions {

   private static final DateService dateService = new SimpleDateFormatDateService();

   private void putFormParameter(String key, Object value) {
      formParameters.put(key, String.valueOf(value));
   }

   /**
    * The name of the alarm you want to filter against.
    *
    * @param alarmName the name of the alarm you want to filter against
    *
    * @return this {@code ListAlarmHistoryOptions} object
    */
   public ListAlarmHistoryOptions alarmName(String alarmName) {
      checkNotNull(alarmName, "alarmName");
      checkArgument(alarmName.length() <= 255, "alarmName must be between 1 and 255 characters in length");
      putFormParameter("AlarmName", alarmName);
      return this;
   }

   /**
    * The ending date to retrieve alarm history.
    *
    * @param endDate the ending date to retrieve alarm history
    *
    * @return this {@code ListAlarmHistoryOptions} object
    */
   public ListAlarmHistoryOptions endDate(Date endDate) {
      putFormParameter("EndDate", dateService.iso8601DateFormat(checkNotNull(endDate, "endDate")));
      return this;
   }

   /**
    * The type of alarm histories to retrieve.
    *
    * @param historyItemType type of alarm histories to retrieve
    *
    * @return this {@code ListAlarmHistoryOptions} object
    */
   public ListAlarmHistoryOptions historyItemType(HistoryItemType historyItemType) {
      checkNotNull(historyItemType, "historyItemType");
      checkArgument(historyItemType != HistoryItemType.UNRECOGNIZED, "historyItemType unrecognized");
      putFormParameter("HistoryItemType", historyItemType);
      return this;
   }

   /**
    * The maximum number of alarm history records to retrieve.
    *
    * @param maxRecords maximum number of alarm history records to retrieve
    *
    * @return this {@code ListAlarmHistoryOptions} object
    */
   public ListAlarmHistoryOptions maxRecords(int maxRecords) {
      putFormParameter("MaxRecords", maxRecords);
      return this;
   }

   /**
    * The starting date to retrieve alarm history.
    *
    * @param startDate the starting date to retrieve alarm history
    *
    * @return this {@code ListAlarmHistoryOptions} object
    */
   public ListAlarmHistoryOptions startDate(Date startDate) {
      putFormParameter("StartDate", dateService.iso8601DateFormat(checkNotNull(startDate, "startDate")));
      return this;
   }

}
