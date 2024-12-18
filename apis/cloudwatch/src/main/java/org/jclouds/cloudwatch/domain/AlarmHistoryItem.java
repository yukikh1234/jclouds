
package org.jclouds.cloudwatch.domain;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;

/**
 * @see <a href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_AlarmHistoryItem.html" />
 */
@Beta
public class AlarmHistoryItem {

    private final String alarmName;
    private final String historyData;
    private final HistoryItemType historyItemType;
    private final String historySummary;
    private final Date timestamp;

    public AlarmHistoryItem(String alarmName, String historyData, HistoryItemType historyItemType,
                            String historySummary, Date timestamp) {
        this.alarmName = checkNotNull(alarmName, "alarmName");
        this.historyData = checkNotNull(historyData, "historyData for %s", alarmName);
        this.historyItemType = checkNotNull(historyItemType, "historyItemType for %s", alarmName);
        this.historySummary = checkNotNull(historySummary, "historySummary for %s", alarmName);
        this.timestamp = checkNotNull(timestamp, "timestamp for %s", alarmName);
    }

    /**
     * return the descriptive name for the alarm
     */
    public String getAlarmName() {
        return alarmName;
    }

    /**
     * return the machine-readable data about the alarm in JSON format
     */
    public String getHistoryData() {
        return historyData;
    }

    /**
     * return the type of alarm history item
     */
    public HistoryItemType getHistoryItemType() {
        return historyItemType;
    }

    /**
     * return the human-readable summary of the alarm history
     */
    public String getHistorySummary() {
        return historySummary;
    }

    /**
     * return the time stamp for the alarm history item
     */
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(alarmName, historyData, historyItemType, historySummary, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AlarmHistoryItem other = (AlarmHistoryItem) obj;
        return areEqual(this, other);
    }

    private boolean areEqual(AlarmHistoryItem item1, AlarmHistoryItem item2) {
        return equal(item1.alarmName, item2.alarmName) &&
               equal(item1.historyData, item2.historyData) &&
               equal(item1.historyItemType, item2.historyItemType) &&
               equal(item1.historySummary, item2.historySummary) &&
               equal(item1.timestamp, item2.timestamp);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("alarmName", alarmName)
                .add("historyData", historyData)
                .add("historyItemType", historyItemType)
                .add("historySummary", historySummary)
                .add("timestamp", timestamp).toString();
    }
}
