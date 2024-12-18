
package org.jclouds.cloudwatch.xml;

import com.google.common.collect.Sets;
import org.jclouds.cloudwatch.domain.Datapoint;
import org.jclouds.cloudwatch.domain.GetMetricStatisticsResponse;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import jakarta.inject.Inject;
import java.util.Set;

/**
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_GetMetricStatistics.html" />
 */
public class GetMetricStatisticsResponseHandlerV2 extends ParseSax.HandlerWithResult<GetMetricStatisticsResponse> {

   private StringBuilder currentText = new StringBuilder();
   private Set<Datapoint> datapoints = Sets.newLinkedHashSet();
   private String label;
   private final DatapointHandler datapointHandler;
   private boolean inDatapoints;

   @Inject
   public GetMetricStatisticsResponseHandlerV2(DatapointHandler datapointHandler) {
      this.datapointHandler = datapointHandler;
   }

   public GetMetricStatisticsResponse getResult() {
      return new GetMetricStatisticsResponse(datapoints, label);
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (qName.equals("Datapoints")) {
         enterDatapoints();
      } else if (inDatapoints) {
         delegateStartElement(uri, localName, qName, attributes);
      }
   }

   private void enterDatapoints() {
      inDatapoints = true;
   }

   private void delegateStartElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      datapointHandler.startElement(uri, localName, qName, attributes);
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      if (inDatapoints) {
         processDatapointEndElement(uri, localName, qName);
      } else if (qName.equals("Label")) {
         label = SaxUtils.currentOrNull(currentText);
      }
      currentText.setLength(0);
   }

   private void processDatapointEndElement(String uri, String localName, String qName) throws SAXException {
      if (qName.equals("Datapoints")) {
         inDatapoints = false;
      } else {
         datapointHandler.endElement(uri, localName, qName);
         if (qName.equals("member")) {
            datapoints.add(datapointHandler.getResult());
         }
      }
   }

   public void characters(char[] ch, int start, int length) {
      if (inDatapoints) {
         datapointHandler.characters(ch, start, length);
      } else {
         currentText.append(ch, start, length);
      }
   }
}
