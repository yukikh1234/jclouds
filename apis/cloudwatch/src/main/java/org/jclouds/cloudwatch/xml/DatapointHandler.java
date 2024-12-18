
package org.jclouds.cloudwatch.xml;

import org.jclouds.cloudwatch.domain.Datapoint;
import org.jclouds.cloudwatch.domain.Unit;
import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;

import jakarta.inject.Inject;
import java.util.Date;

public class DatapointHandler extends ParseSax.HandlerForGeneratedRequestWithResult<Datapoint> {
    private StringBuilder currentText = new StringBuilder();

    protected final DateService dateService;
    private Double average;
    private Double maximum;
    private Double minimum;
    private Date timestamp;
    private Double samples;
    private Double sum;
    private Unit unit;
    private String customUnit;

    @Inject
    public DatapointHandler(DateService dateService) {
        this.dateService = dateService;
    }

    public Datapoint getResult() {
        Datapoint datapoint = new Datapoint(average, maximum, minimum, timestamp, samples, sum, unit, customUnit);
        resetFields();
        return datapoint;
    }

    private void resetFields() {
        this.average = null;
        this.maximum = null;
        this.minimum = null;
        this.timestamp = null;
        this.samples = null;
        this.sum = null;
        this.unit = null;
        this.customUnit = null;
    }

    public void endElement(String uri, String name, String qName) {
        switch (qName) {
            case "Average":
                average = doubleOrNull();
                break;
            case "Maximum":
                maximum = doubleOrNull();
                break;
            case "Minimum":
                minimum = doubleOrNull();
                break;
            case "Timestamp":
                timestamp = parseTimestamp();
                break;
            case "SampleCount":
                samples = doubleOrNull();
                break;
            case "Sum":
                sum = doubleOrNull();
                break;
            case "Unit":
                unit = parseUnit();
                break;
            case "CustomUnit":
                customUnit = parseCustomUnit();
                break;
            default:
                break;
        }
        currentText.setLength(0);
    }

    private Double doubleOrNull() {
        String text = currentText.toString().trim();
        return text.isEmpty() ? null : Double.valueOf(text);
    }

    private Date parseTimestamp() {
        return dateService.iso8601SecondsDateParse(currentText.toString().trim());
    }

    private Unit parseUnit() {
        return Unit.fromValue(currentText.toString().trim());
    }

    private String parseCustomUnit() {
        return currentText.toString().trim();
    }

    public void characters(char[] ch, int start, int length) {
        currentText.append(ch, start, length);
    }
}
