
package org.jclouds.cloudwatch.xml;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.cloudwatch.domain.Metric;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Set;

public class MetricHandler extends ParseSax.HandlerForGeneratedRequestWithResult<Metric> {

   private final DimensionHandler dimensionHandler;

   private StringBuilder currentText = new StringBuilder();
   private Set<Dimension> dimensions = Sets.newLinkedHashSet();
   private boolean inDimensions;
   private String metricName;
   private String namespace;

   @Inject
   public MetricHandler(DimensionHandler dimensionHandler) {
      this.dimensionHandler = dimensionHandler;
   }

   public boolean inDimensions() {
      return inDimensions;
   }

   @Override
   public Metric getResult() {
      Metric metric = new Metric(metricName, namespace, dimensions);
      resetState();
      return metric;
   }

   private void resetState() {
      dimensions = Sets.newLinkedHashSet();
      metricName = null;
      namespace = null;
   }

   @Override
   public void startElement(String url, String name, String qName, Attributes attributes) throws SAXException {
      if (shouldEnterDimensions(qName)) {
         inDimensions = true;
      }
      if (inDimensions) {
         dimensionHandler.startElement(url, name, qName, attributes);
      }
   }

   private boolean shouldEnterDimensions(String qName) {
      return !inDimensions && SaxUtils.equalsOrSuffix(qName, "member");
   }

   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      if (inDimensions) {
         handleEndElementInDimensions(uri, name, qName);
      } else {
         handleEndElementOutsideDimensions(qName);
      }
      currentText.setLength(0);
   }

   private void handleEndElementInDimensions(String uri, String name, String qName) throws SAXException {
      if (qName.equals("Dimensions")) {
         inDimensions = false;
      } else if (qName.equals("member")) {
         dimensions.add(dimensionHandler.getResult());
      } else {
         dimensionHandler.endElement(uri, name, qName);
      }
   }

   private void handleEndElementOutsideDimensions(String qName) {
      if (qName.equals("MetricName")) {
         metricName = SaxUtils.currentOrNull(currentText);
      } else if (qName.equals("Namespace")) {
         namespace = SaxUtils.currentOrNull(currentText);
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) {
      if (inDimensions) {
         dimensionHandler.characters(ch, start, length);
      } else {
         currentText.append(ch, start, length);
      }
   }
}
