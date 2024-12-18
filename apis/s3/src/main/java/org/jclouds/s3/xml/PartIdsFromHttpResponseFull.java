
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import java.util.Date;
import java.util.Map;

import jakarta.inject.Inject;

import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.ListMultipartUploadResponse;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;

@Beta
public final class PartIdsFromHttpResponseFull extends ParseSax.HandlerWithResult<Map<Integer, ListMultipartUploadResponse>> {
   private final StringBuilder currentText = new StringBuilder();
   private final DateService dateParser;

   private int partNumber;
   private Date lastModfied;
   private String eTag;
   private long size;

   private final ImmutableMap.Builder<Integer, ListMultipartUploadResponse> parts = ImmutableMap.builder();

   @Inject
   PartIdsFromHttpResponseFull(DateService dateParser) {
      this.dateParser = dateParser;
   }

   public Map<Integer, ListMultipartUploadResponse> getResult() {
      return parts.build();
   }

   public void endElement(String uri, String name, String qName) {
      switch (qName) {
         case "PartNumber":
            handlePartNumber();
            break;
         case "LastModified":
            handleLastModified();
            break;
         case "ETag":
            handleETag();
            break;
         case "Size":
            handleSize();
            break;
         case "Part":
            handlePart();
            break;
      }
      currentText.setLength(0);
   }

   private void handlePartNumber() {
      partNumber = Integer.parseInt(currentText.toString().trim());
   }

   private void handleLastModified() {
      lastModfied = dateParser.iso8601DateOrSecondsDateParse(currentOrNull(currentText));
   }

   private void handleETag() {
      eTag = currentText.toString().trim();
   }

   private void handleSize() {
      size = Long.parseLong(currentText.toString().trim());
   }

   private void handlePart() {
      parts.put(partNumber, ListMultipartUploadResponse.create(partNumber, lastModfied, eTag, size));
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
