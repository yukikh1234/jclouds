
package org.jclouds.cloudwatch.xml;

import java.util.Set;

import jakarta.inject.Inject;

import org.jclouds.cloudwatch.domain.Datapoint;
import org.jclouds.http.functions.ParseSax;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.google.common.collect.Sets;

public class GetMetricStatisticsResponseHandler extends ParseSax.HandlerWithResult<Set<Datapoint>> {

   private Set<Datapoint> datapoints = Sets.newLinkedHashSet();
   private final DatapointHandler datapointHandler;

   @Inject
   public GetMetricStatisticsResponseHandler(DatapointHandler datapointHandler) {
      this.datapointHandler = datapointHandler;
   }

   public Set<Datapoint> getResult() {
      return datapoints;
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      datapointHandler.startElement(uri, localName, qName, attributes);
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      handleEndElement(uri, localName, qName);
   }

   private void handleEndElement(String uri, String localName, String qName) throws SAXException {
      datapointHandler.endElement(uri, localName, qName);
      if (isMemberElement(qName)) {
         addCurrentDatapoint();
      }
   }

   private boolean isMemberElement(String qName) {
      return "member".equals(qName);
   }

   private void addCurrentDatapoint() {
      this.datapoints.add(datapointHandler.getResult());
   }

   public void characters(char[] ch, int start, int length) {
      datapointHandler.characters(ch, start, length);
   }
}
