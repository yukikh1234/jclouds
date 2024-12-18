
package org.jclouds.cloudwatch.xml;

import java.util.Date;
import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.jclouds.cloudwatch.domain.Alarm;
import org.jclouds.cloudwatch.domain.ComparisonOperator;
import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.cloudwatch.domain.Statistics;
import org.jclouds.cloudwatch.domain.Unit;
import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @see <a href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_MetricAlarm.html" />
 */
@Beta
public class MetricAlarmHandler extends ParseSax.HandlerForGeneratedRequestWithResult<Alarm> {

    protected final DateService dateService;
    protected final DimensionHandler dimensionHandler;

    private StringBuilder currentText = new StringBuilder();
    private Set<String> alarmActions = Sets.newLinkedHashSet();
    private Set<Dimension> dimensions = Sets.newLinkedHashSet();
    private Set<String> insufficientDataActions = Sets.newLinkedHashSet();
    private Set<String> okActions = Sets.newLinkedHashSet();
    private boolean inAlarmActions = false;
    private boolean inDimensions = false;
    private boolean inInsufficientDataActions = false;
    private boolean inOkActions = false;
    private boolean actionsEnabled;
    private String alarmARN;
    private Date alarmConfigurationUpdatedTimestamp;
    private String alarmDescription;
    private String alarmName;
    private ComparisonOperator comparisonOperator;
    private int evaluationPeriods;
    private String metricName;
    private String namespace;
    private int period;
    private String stateReason;
    private String stateReasonData;
    private Date stateUpdatedTimestamp;
    private Alarm.State state;
    private Statistics statistic;
    private double threshold;
    private Unit unit;

    @Inject
    public MetricAlarmHandler(DateService dateService, DimensionHandler dimensionHandler) {
        this.dateService = dateService;
        this.dimensionHandler = dimensionHandler;
    }

    public boolean shouldHandleMemberTag() {
        return inAlarmActions || inDimensions || inInsufficientDataActions || inOkActions;
    }

    @Override
    public void startElement(String url, String name, String qName, Attributes attributes) throws SAXException {
        if (SaxUtils.equalsOrSuffix(qName, "AlarmActions")) {
            inAlarmActions = true;
        } else if (SaxUtils.equalsOrSuffix(qName, "Dimensions")) {
            inDimensions = true;
        } else if (SaxUtils.equalsOrSuffix(qName, "InsufficientDataActions") ||
                SaxUtils.equalsOrSuffix(qName, "UnknownActions")) {
            inInsufficientDataActions = true;
        } else if (SaxUtils.equalsOrSuffix(qName, "OKActions")) {
            inOkActions = true;
        }
        if (inDimensions) {
            dimensionHandler.startElement(url, name, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String name, String qName) throws SAXException {
        if (inAlarmActions) {
            handleAlarmActions(qName);
        } else if (inDimensions) {
            handleDimensions(uri, name, qName);
        } else if (inInsufficientDataActions) {
            handleInsufficientDataActions(qName);
        } else if (inOkActions) {
            handleOkActions(qName);
        } else {
            handleGeneralElements(qName);
        }
        currentText.setLength(0);
    }

    private void handleAlarmActions(String qName) {
        if (qName.equals("AlarmActions")) {
            inAlarmActions = false;
        } else if (qName.equals("member")) {
            alarmActions.add(SaxUtils.currentOrNull(currentText));
        }
    }

    private void handleDimensions(String uri, String name, String qName) throws SAXException {
        if (qName.equals("Dimensions")) {
            inDimensions = false;
        } else if (qName.equals("member")) {
            dimensions.add(dimensionHandler.getResult());
        } else {
            dimensionHandler.endElement(uri, name, qName);
        }
    }

    private void handleInsufficientDataActions(String qName) {
        if (qName.equals("InsufficientDataActions") || qName.equals("UnknownActions")) {
            inInsufficientDataActions = false;
        } else if (qName.equals("member")) {
            insufficientDataActions.add(SaxUtils.currentOrNull(currentText));
        }
    }

    private void handleOkActions(String qName) {
        if (qName.equals("OKActions")) {
            inOkActions = false;
        } else if (qName.equals("member")) {
            okActions.add(SaxUtils.currentOrNull(currentText));
        }
    }

    private void handleGeneralElements(String qName) {
        switch (qName) {
            case "ActionsEnabled":
                actionsEnabled = Boolean.valueOf(SaxUtils.currentOrNull(currentText));
                break;
            case "AlarmArn":
                alarmARN = SaxUtils.currentOrNull(currentText);
                break;
            case "AlarmConfigurationUpdatedTimestamp":
                alarmConfigurationUpdatedTimestamp = dateService.iso8601DateParse(currentText.toString().trim());
                break;
            case "AlarmDescription":
                alarmDescription = SaxUtils.currentOrNull(currentText);
                break;
            case "AlarmName":
                alarmName = SaxUtils.currentOrNull(currentText);
                break;
            case "ComparisonOperator":
                comparisonOperator = ComparisonOperator.fromValue(SaxUtils.currentOrNull(currentText));
                break;
            case "EvaluationPeriods":
                evaluationPeriods = Integer.parseInt(SaxUtils.currentOrNull(currentText));
                break;
            case "MetricName":
                metricName = SaxUtils.currentOrNull(currentText);
                break;
            case "Namespace":
                namespace = SaxUtils.currentOrNull(currentText);
                break;
            case "Period":
                period = Integer.parseInt(SaxUtils.currentOrNull(currentText));
                break;
            case "StateReason":
                stateReason = SaxUtils.currentOrNull(currentText);
                break;
            case "StateReasonData":
                stateReasonData = extractStateReasonData();
                break;
            case "StateUpdatedTimestamp":
                stateUpdatedTimestamp = dateService.iso8601DateParse(currentText.toString().trim());
                break;
            case "StateValue":
                state = Alarm.State.fromValue(SaxUtils.currentOrNull(currentText));
                break;
            case "Statistic":
                statistic = Statistics.fromValue(SaxUtils.currentOrNull(currentText));
                break;
            case "Threshold":
                threshold = Double.valueOf(SaxUtils.currentOrNull(currentText));
                break;
            case "Unit":
                unit = Unit.fromValue(SaxUtils.currentOrNull(currentText));
                break;
        }
    }

    private String extractStateReasonData() {
        String rawJson = SaxUtils.currentOrNull(currentText);
        return rawJson != null ? rawJson.trim() : null;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (inDimensions) {
            dimensionHandler.characters(ch, start, length);
        } else {
            currentText.append(ch, start, length);
        }
    }

    @Override
    public Alarm getResult() {
        Alarm result = new Alarm(actionsEnabled, alarmActions, alarmARN, alarmConfigurationUpdatedTimestamp,
                alarmDescription, alarmName, comparisonOperator, dimensions,
                evaluationPeriods, insufficientDataActions, metricName, namespace, okActions,
                period, stateReason, Optional.fromNullable(stateReasonData),
                stateUpdatedTimestamp, state, statistic, threshold,
                Optional.fromNullable(unit));

        resetState();
        return result;
    }

    private void resetState() {
        actionsEnabled = false;
        alarmActions = Sets.newLinkedHashSet();
        alarmARN = null;
        alarmConfigurationUpdatedTimestamp = null;
        alarmDescription = null;
        alarmName = null;
        comparisonOperator = null;
        dimensions = Sets.newLinkedHashSet();
        evaluationPeriods = 0;
        insufficientDataActions = Sets.newLinkedHashSet();
        metricName = null;
        namespace = null;
        okActions = Sets.newLinkedHashSet();
        period = 0;
        stateReason = null;
        stateReasonData = null;
        stateUpdatedTimestamp = null;
        state = null;
        statistic = null;
        threshold = 0.0;
        unit = null;
    }
}
