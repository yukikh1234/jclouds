
package org.jclouds.cloudwatch.xml;

import jakarta.inject.Inject;
import java.util.Date;
import com.google.common.annotations.Beta;
import org.jclouds.cloudwatch.domain.AlarmHistoryItem;
import org.jclouds.cloudwatch.domain.HistoryItemType;
import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.SAXException;

@Beta
public class AlarmHistoryItemHandler extends ParseSax.HandlerForGeneratedRequestWithResult<AlarmHistoryItem> {

   protected final DateService dateService;
   private StringBuilder currentText = new StringBuilder();
   private String alarmName;
   private String historyData;
   private HistoryItemType historyItemType;
   private String historySummary;
   private Date timestamp;

   @Inject
   public AlarmHistoryItemHandler(DateService dateService) {
      this.dateService = dateService;
   }

   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      switch (qName) {
         case "AlarmName":
            alarmName = SaxUtils.currentOrNull(currentText);
            break;
         case "HistoryData":
            extractHistoryData();
            break;
         case "HistoryItemType":
            extractHistoryItemType();
            break;
         case "HistorySummary":
            extractHistorySummary();
            break;
         case "Timestamp":
            extractTimestamp();
            break;
      }
      currentText.setLength(0);
   }

   private void extractHistoryData() {
      String rawJson = SaxUtils.currentOrNull(currentText);
      if (rawJson != null) {
         historyData = rawJson.trim();
      }
   }

   private void extractHistoryItemType() {
      historyItemType = HistoryItemType.fromValue(SaxUtils.currentOrNull(currentText));
   }

   private void extractHistorySummary() {
      historySummary = SaxUtils.currentOrNull(currentText);
   }

   private void extractTimestamp() {
      timestamp = dateService.iso8601DateParse(currentText.toString().trim());
   }

   @Override
   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }

   @Override
   public AlarmHistoryItem getResult() {
      AlarmHistoryItem result = new AlarmHistoryItem(alarmName, historyData, historyItemType, historySummary, timestamp);
      resetState();
      return result;
   }

   private void resetState() {
      alarmName = null;
      historyData = null;
      historyItemType = null;
      historySummary = null;
      timestamp = null;
   }
}
