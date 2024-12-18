
package org.jclouds.cloudwatch.options;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.google.common.annotations.Beta;
import org.jclouds.cloudwatch.domain.ComparisonOperator;
import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.cloudwatch.domain.Statistics;
import org.jclouds.cloudwatch.domain.Unit;
import org.jclouds.http.options.BaseHttpRequestOptions;

/**
 * Options used to create/update an alarm.
 *
 * @see <a href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_PutMetricAlarm.html" />
 */
@Beta
public class SaveAlarmOptions extends BaseHttpRequestOptions {

   private static final int MAX_ALARM_ACTIONS = 5;
   private static final int MAX_DIMENSIONS = 10;
   private static final int MAX_OK_ACTIONS = 5;
   private static final int MAX_INSUFFICIENT_DATA_ACTIONS = 5;

   private int alarmActionIndex = 1;
   private int dimensionIndex = 1;
   private int insufficientDataActionsIndex = 1;
   private int okActionsIndex = 1;

   public SaveAlarmOptions actionsEnabled(boolean actionsEnabled) {
      formParameters.put("ActionsEnabled", String.valueOf(actionsEnabled));
      return this;
   }

   public SaveAlarmOptions alarmActions(Set<String> alarmActions) {
      return processActions(alarmActions, "alarmActions", this::alarmAction);
   }

   public SaveAlarmOptions alarmAction(String alarmAction) {
      addFormParameter("AlarmActions.member.", alarmAction, "alarmAction", alarmActionIndex, MAX_ALARM_ACTIONS);
      alarmActionIndex++;
      return this;
   }

   public SaveAlarmOptions alarmDescription(String alarmDescription) {
      formParameters.put("AlarmDescription", checkNotNull(alarmDescription, "alarmDescription"));
      return this;
   }

   public SaveAlarmOptions alarmName(String alarmName) {
      formParameters.put("AlarmName", checkNotNull(alarmName, "alarmName"));
      return this;
   }

   public SaveAlarmOptions comparisonOperator(ComparisonOperator comparisonOperator) {
      checkNotNull(comparisonOperator, "comparisonOperator");
      checkArgument(comparisonOperator != ComparisonOperator.UNRECOGNIZED, "comparisonOperator unrecognized");
      formParameters.put("ComparisonOperator", comparisonOperator.toString());
      return this;
   }

   public SaveAlarmOptions dimensions(Set<Dimension> dimensions) {
      return processDimensions(dimensions, "dimensions", this::dimension);
   }

   public SaveAlarmOptions dimension(Dimension dimension) {
      checkNotNull(dimension, "dimension");
      addDimensionFormParameter(dimension, dimensionIndex, MAX_DIMENSIONS);
      dimensionIndex++;
      return this;
   }

   public SaveAlarmOptions evaluationPeriods(int evaluationPeriods) {
      formParameters.put("EvaluationPeriods", String.valueOf(evaluationPeriods));
      return this;
   }

   public SaveAlarmOptions insufficientDataActions(Set<String> insufficientDataActions) {
      return processActions(insufficientDataActions, "insufficientDataActions", this::insufficientDataAction);
   }

   public SaveAlarmOptions insufficientDataAction(String insufficientDataAction) {
      addFormParameter("InsufficientDataActions.member.", insufficientDataAction, "insufficientDataAction", insufficientDataActionsIndex, MAX_INSUFFICIENT_DATA_ACTIONS);
      insufficientDataActionsIndex++;
      return this;
   }

   public SaveAlarmOptions metricName(String metricName) {
      formParameters.put("MetricName", checkNotNull(metricName, "metricName"));
      return this;
   }

   public SaveAlarmOptions namespace(String namespace) {
      formParameters.put("Namespace", checkNotNull(namespace));
      return this;
   }

   public SaveAlarmOptions okActions(Set<String> okActions) {
      return processActions(okActions, "okActions", this::okAction);
   }

   public SaveAlarmOptions okAction(String okAction) {
      addFormParameter("OKActions.member.", okAction, "okAction", okActionsIndex, MAX_OK_ACTIONS);
      okActionsIndex++;
      return this;
   }

   public SaveAlarmOptions period(int period) {
      formParameters.put("Period", String.valueOf(period));
      return this;
   }

   public SaveAlarmOptions statistic(Statistics statistic) {
      checkNotNull(statistic, "statistic");
      checkArgument(statistic != Statistics.UNRECOGNIZED, "statistic unrecognized");
      formParameters.put("Statistic", statistic.toString());
      return this;
   }

   public SaveAlarmOptions threshold(double threshold) {
      formParameters.put("Threshold", String.valueOf(threshold));
      return this;
   }

   public SaveAlarmOptions unit(Unit unit) {
      checkNotNull(unit, "unit");
      checkArgument(unit != Unit.UNRECOGNIZED, "unit unrecognized");
      formParameters.put("Unit", unit.toString());
      return this;
   }

   private void addFormParameter(String prefix, String value, String parameterName, int index, int maxIndex) {
      checkArgument(index <= maxIndex, "maximum number of " + parameterName + " is " + maxIndex);
      formParameters.put(prefix + index, checkNotNull(value, parameterName));
   }

   private void addDimensionFormParameter(Dimension dimension, int index, int maxIndex) {
      checkArgument(index <= maxIndex, "maximum number of dimensions is " + maxIndex);
      formParameters.put("Dimensions.member." + index + ".Name", dimension.getName());
      formParameters.put("Dimensions.member." + index + ".Value", dimension.getValue());
   }

   private SaveAlarmOptions processActions(Set<String> actions, String parameterName, ActionProcessor actionProcessor) {
      for (String action : checkNotNull(actions, parameterName)) {
         actionProcessor.process(action);
      }
      return this;
   }

   private SaveAlarmOptions processDimensions(Set<Dimension> dimensions, String parameterName, DimensionProcessor dimensionProcessor) {
      for (Dimension dimension : checkNotNull(dimensions, parameterName)) {
         dimensionProcessor.process(dimension);
      }
      return this;
   }

   @FunctionalInterface
   private interface ActionProcessor {
      void process(String action);
   }

   @FunctionalInterface
   private interface DimensionProcessor {
      void process(Dimension dimension);
   }
}
