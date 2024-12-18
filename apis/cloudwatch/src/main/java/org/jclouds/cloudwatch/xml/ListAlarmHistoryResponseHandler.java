
package org.jclouds.cloudwatch.xml;

import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.jclouds.cloudwatch.domain.AlarmHistoryItem;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.IterableWithMarkers;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @see <a href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_DescribeAlarmHistory.html" />
 */
@Beta
public class ListAlarmHistoryResponseHandler
      extends ParseSax.HandlerForGeneratedRequestWithResult<IterableWithMarker<AlarmHistoryItem>> {

   private final AlarmHistoryItemHandler alarmHistoryItemHandler;

   private StringBuilder currentText = new StringBuilder();
   private Set<AlarmHistoryItem> alarmHistoryItems = Sets.newLinkedHashSet();
   private String nextToken;
   private boolean inAlarmHistoryItems;

   @Inject
   public ListAlarmHistoryResponseHandler(AlarmHistoryItemHandler alarmHistoryItemHandler) {
      this.alarmHistoryItemHandler = alarmHistoryItemHandler;
   }

   @Override
   public void startElement(String url, String name, String qName, Attributes attributes) throws SAXException {
      if (SaxUtils.equalsOrSuffix(qName, "AlarmHistoryItems")) {
         inAlarmHistoryItems = true;
      }
      if (inAlarmHistoryItems) {
         alarmHistoryItemHandler.startElement(url, name, qName, attributes);
      }
   }

   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      if (inAlarmHistoryItems) {
         handleAlarmHistoryItemsEnd(qName);
      } else if (qName.equals("NextToken")) {
         nextToken = SaxUtils.currentOrNull(currentText);
      }
      currentText.setLength(0);
   }

   private void handleAlarmHistoryItemsEnd(String qName) throws SAXException {
      if (qName.equals("AlarmHistoryItems")) {
         inAlarmHistoryItems = false;
      } else if (qName.equals("member")) {
         alarmHistoryItems.add(alarmHistoryItemHandler.getResult());
      } else {
         alarmHistoryItemHandler.endElement(null, null, qName);
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) {
      if (inAlarmHistoryItems) {
         alarmHistoryItemHandler.characters(ch, start, length);
      } else {
         currentText.append(ch, start, length);
      }
   }

   @Override
   public IterableWithMarker<AlarmHistoryItem> getResult() {
      IterableWithMarker<AlarmHistoryItem> result = IterableWithMarkers.from(alarmHistoryItems, nextToken);

      alarmHistoryItems.clear();
      nextToken = null;

      return result;
   }
}
