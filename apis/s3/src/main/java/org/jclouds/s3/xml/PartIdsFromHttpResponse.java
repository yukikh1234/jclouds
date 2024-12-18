
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import jakarta.inject.Inject;

import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;

import com.google.common.collect.ImmutableMap;

/**
 * Parses the following XML document:
 * <p/>
 * ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01"
 *
 * @deprecated see PartIdsFromHttpResponseFull
 */
@Deprecated
public class PartIdsFromHttpResponse extends ParseSax.HandlerWithResult<Map<Integer, String>> {
   private final StringBuilder currentText = new StringBuilder();

   private final DateService dateParser;

   private int partNumber;
   private Date lastModfied;
   private String eTag;
   private long size;

   private final ImmutableMap.Builder<Integer, String> parts = ImmutableMap.builder();

   /** Some blobs have a non-hex suffix when created by multi-part uploads such Amazon S3. */
   private static final Pattern ETAG_CONTENT_MD5_PATTERN = Pattern.compile("\"([0-9a-f]+)\"");

   @Inject
   public PartIdsFromHttpResponse(DateService dateParser) {
      this.dateParser = dateParser;
   }

   public Map<Integer, String> getResult() {
      return parts.build();
   }

   public void endElement(String uri, String name, String qName) {
      switch (qName) {
         case "PartNumber":
            parsePartNumber();
            break;
         case "LastModified":
            parseLastModified();
            break;
         case "ETag":
            parseETag();
            break;
         case "Size":
            parseSize();
            break;
         case "Part":
            addPart();
            break;
      }
      currentText.setLength(0);
   }

   private void parsePartNumber() {
      partNumber = Integer.parseInt(currentText.toString().trim());
   }

   private void parseLastModified() {
      lastModfied = dateParser.iso8601DateOrSecondsDateParse(currentOrNull(currentText));
   }

   private void parseETag() {
      eTag = currentText.toString().trim();
   }

   private void parseSize() {
      size = Long.parseLong(currentText.toString().trim());
   }

   private void addPart() {
      parts.put(partNumber, eTag);
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
