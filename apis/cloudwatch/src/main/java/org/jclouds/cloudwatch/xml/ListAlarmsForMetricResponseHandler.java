
package org.jclouds.cloudwatch.xml;

import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.jclouds.cloudwatch.domain.Alarm;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

@Beta
public class ListAlarmsForMetricResponseHandler
      extends ParseSax.HandlerForGeneratedRequestWithResult<Iterable<Alarm>> {

   private final MetricAlarmHandler metricAlarmHandler;
   private StringBuilder currentText = new StringBuilder();
   private Set<Alarm> alarms = Sets.newLinkedHashSet();
   private boolean inMetricAlarms;

   @Inject
   public ListAlarmsForMetricResponseHandler(MetricAlarmHandler metricAlarmHandler) {
      this.metricAlarmHandler = metricAlarmHandler;
   }

   @Override
   public void startElement(String url, String name, String qName, Attributes attributes) throws SAXException {
      if (isMetricAlarmsElement(qName)) {
         inMetricAlarms = true;
      }
      if (inMetricAlarms) {
         metricAlarmHandler.startElement(url, name, qName, attributes);
      }
   }

   private boolean isMetricAlarmsElement(String qName) {
      return SaxUtils.equalsOrSuffix(qName, "MetricAlarms");
   }

   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      if (inMetricAlarms) {
         handleEndElement(qName, uri, name);
      }
      currentText.setLength(0);
   }

   private void handleEndElement(String qName, String uri, String name) throws SAXException {
      if (qName.equals("MetricAlarms")) {
         inMetricAlarms = false;
      } else if (isMemberElement(qName) && !metricAlarmHandler.shouldHandleMemberTag()) {
         alarms.add(metricAlarmHandler.getResult());
      } else {
         metricAlarmHandler.endElement(uri, name, qName);
      }
   }

   private boolean isMemberElement(String qName) {
      return qName.equals("member");
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
   public FluentIterable<Alarm> getResult() {
      FluentIterable<Alarm> result = FluentIterable.from(alarms);
      alarms = Sets.newLinkedHashSet();
      return result;
   }
}
