
package org.jclouds.cloudwatch.xml;

import java.util.Set;

import org.jclouds.cloudwatch.domain.Metric;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.IterableWithMarkers;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_ListMetrics.html" />
 */
public class ListMetricsResponseHandler extends ParseSax.HandlerForGeneratedRequestWithResult<IterableWithMarker<Metric>> {

   private final MetricHandler metricHandler;
   private StringBuilder currentText = new StringBuilder();
   private Set<Metric> metrics = Sets.newLinkedHashSet();
   private boolean inMetrics;
   private String nextToken;

   @Inject
   public ListMetricsResponseHandler(MetricHandler metricHandler) {
      this.metricHandler = metricHandler;
   }

   @Override
   public IterableWithMarker<Metric> getResult() {
      return IterableWithMarkers.from(metrics, nextToken);
   }

   @Override
   public void startElement(String url, String name, String qName, Attributes attributes) throws SAXException {
      if (SaxUtils.equalsOrSuffix(qName, "Metrics")) {
         handleStartMetrics();
      }
      if (inMetrics) {
         metricHandler.startElement(url, name, qName, attributes);
      }
   }

   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      if (inMetrics) {
         handleEndMetrics(uri, name, qName);
      } else if (qName.equals("NextToken")) {
         handleEndNextToken();
      }
      currentText.setLength(0);
   }

   @Override
   public void characters(char[] ch, int start, int length) {
      if (inMetrics) {
         metricHandler.characters(ch, start, length);
      } else {
         currentText.append(ch, start, length);
      }
   }

   private void handleStartMetrics() {
      inMetrics = true;
   }

   private void handleEndMetrics(String uri, String name, String qName) throws SAXException {
      if (qName.equals("Metrics")) {
         inMetrics = false;
      } else if (qName.equals("member") && !metricHandler.inDimensions()) {
         metrics.add(metricHandler.getResult());
      } else {
         metricHandler.endElement(uri, name, qName);
      }
   }

   private void handleEndNextToken() {
      nextToken = SaxUtils.currentOrNull(currentText);
   }
}
