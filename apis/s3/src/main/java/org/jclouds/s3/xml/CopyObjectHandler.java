
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import java.util.Date;

import jakarta.inject.Inject;

import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.ObjectMetadata;
import org.jclouds.s3.domain.internal.CopyObjectResult;

/**
 * Parses the response from Amazon S3 COPY Object command.
 * <p/>
 * CopyObjectResult is the document we expect to parse.
 */
public class CopyObjectHandler extends ParseSax.HandlerWithResult<ObjectMetadata> {

   private CopyObjectResult metadata;
   private StringBuilder currentText = new StringBuilder();
   @Inject
   private DateService dateParser;
   private Date currentLastModified;
   private String currentETag;

   public ObjectMetadata getResult() {
      return metadata;
   }

   public void endElement(String uri, String name, String qName) {
      handleETag(qName);
      handleLastModified(qName);
      handleCopyObjectResult(qName);
      currentText.setLength(0);
   }

   private void handleETag(String qName) {
      if (qName.equals("ETag")) {
         this.currentETag = currentOrNull(currentText);
      }
   }

   private void handleLastModified(String qName) {
      if (qName.equals("LastModified")) {
         this.currentLastModified = dateParser
             .iso8601DateOrSecondsDateParse(currentOrNull(currentText));
      }
   }

   private void handleCopyObjectResult(String qName) {
      if (qName.equals("CopyObjectResult")) {
         metadata = new CopyObjectResult(currentLastModified, currentETag);
      }
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
