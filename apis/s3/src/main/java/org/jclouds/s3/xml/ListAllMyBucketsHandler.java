
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import java.util.Date;
import java.util.Set;

import jakarta.inject.Inject;

import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.BucketMetadata;
import org.jclouds.s3.domain.CanonicalUser;

import com.google.common.collect.Sets;

public class ListAllMyBucketsHandler extends ParseSax.HandlerWithResult<Set<BucketMetadata>> {

   private Set<BucketMetadata> buckets = Sets.newLinkedHashSet();
   private CanonicalUser currentOwner;
   private String currentDisplayName;
   private StringBuilder currentText = new StringBuilder();

   private final DateService dateParser;
   private String currentName;
   private Date currentCreationDate;

   @Inject
   public ListAllMyBucketsHandler(DateService dateParser) {
      this.dateParser = dateParser;
      this.currentOwner = new CanonicalUser();
   }

   public Set<BucketMetadata> getResult() {
      return buckets;
   }

   public void endElement(String uri, String name, String qName) {
      switch (qName) {
         case "ID":
            setOwnerId();
            break;
         case "DisplayName":
            setOwnerDisplayName();
            break;
         case "Bucket":
            addBucketMetadata();
            break;
         case "Name":
            setBucketName();
            break;
         case "CreationDate":
            setCreationDate();
            break;
      }
      currentText.setLength(0);
   }

   private void setOwnerId() {
      currentOwner.setId(currentOrNull(currentText));
   }

   private void setOwnerDisplayName() {
      currentOwner.setDisplayName(currentOrNull(currentText));
   }

   private void addBucketMetadata() {
      buckets.add(new BucketMetadata(currentName, currentCreationDate, currentOwner));
   }

   private void setBucketName() {
      currentName = currentOrNull(currentText);
   }

   private void setCreationDate() {
      currentCreationDate = dateParser.iso8601DateOrSecondsDateParse(currentOrNull(currentText));
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
