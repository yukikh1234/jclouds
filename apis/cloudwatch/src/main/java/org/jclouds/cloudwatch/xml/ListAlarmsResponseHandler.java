
package org.jclouds.cloudwatch.xml;

import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.jclouds.cloudwatch.domain.Alarm;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.IterableWithMarkers;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @see <a href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_DescribeAlarms.html" />
 */
@Beta
public class ListAlarmsResponseHandler
      extends ParseSax.HandlerForGeneratedRequestWithResult<IterableWithMarker<Alarm>> {

   private final MetricAlarmHandler metricAlarmHandler;

   private StringBuilder currentText = new StringBuilder();
   private Set<Alarm> alarms = Sets.newLinkedHashSet();
   private String nextToken;
   private boolean inMetricAlarms;

   @Inject
   public ListAlarmsResponseHandler(MetricAlarmHandler metricAlarmHandler) {
      this.metricAlarmHandler = metricAlarmHandler;
   }

   @Override
   public void startElement(String url, String name, String qName, Attributes attributes) throws SAXException {
      if (SaxUtils.equalsOrSuffix(qName, "MetricAlarms")) {
         inMetricAlarms = true;
      }
      if (inMetricAlarms) {
         metricAlarmHandler.startElement(url, name, qName, attributes);
      }
   }

   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      if (inMetricAlarms) {
         handleMetricAlarmsEndElement(uri, name, qName);
      } else if (qName.equals("NextToken")) {
         nextToken = SaxUtils.currentOrNull(currentText);
      }
      currentText.setLength(0);
   }

   private void handleMetricAlarmsEndElement(String uri, String name, String qName) throws SAXException {
      if (qName.equals("MetricAlarms")) {
         inMetricAlarms = false;
      } else if (qName.equals("member") && !metricAlarmHandler.shouldHandleMemberTag()) {
         alarms.add(metricAlarmHandler.getResult());
      } else {
         metricAlarmHandler.endElement(uri, name, qName);
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) {
      if (inMetricAlarms) {
         metricAlarmHandler.characters(ch, start, length);
      } else {
         currentText.append(ch, start, length);
      }
   }

   @Override
   public IterableWithMarker<Alarm> getResult() {
      IterableWithMarker<Alarm> result = IterableWithMarkers.from(alarms, nextToken);

      alarms = Sets.newLinkedHashSet();
      nextToken = null;

      return result;
   }
}
