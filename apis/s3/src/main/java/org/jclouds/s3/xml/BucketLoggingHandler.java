
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import java.net.URI;
import java.util.Set;

import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.AccessControlList.CanonicalUserGrantee;
import org.jclouds.s3.domain.AccessControlList.EmailAddressGrantee;
import org.jclouds.s3.domain.AccessControlList.Grant;
import org.jclouds.s3.domain.AccessControlList.Grantee;
import org.jclouds.s3.domain.AccessControlList.GroupGrantee;
import org.jclouds.s3.domain.BucketLogging;
import org.xml.sax.Attributes;

import com.google.common.collect.Sets;

public class BucketLoggingHandler extends ParseSax.HandlerWithResult<BucketLogging> {
   private Set<Grant> targetGrants = Sets.newHashSet();
   private StringBuilder currentText = new StringBuilder();

   private String currentId;
   private String currentDisplayName;
   private String currentGranteeType;
   private String currentPermission;
   private Grantee currentGrantee;

   private String targetBucket;
   private String targetPrefix;

   public BucketLogging getResult() {
      if (targetBucket == null)
         return null;
      return new BucketLogging(targetBucket, targetPrefix, targetGrants);
   }

   public void startElement(String uri, String name, String qName, Attributes attrs) {
      if (qName.equals("Grantee")) {
         currentGranteeType = attrs.getValue("xsi:type");
      }
   }

   public void endElement(String uri, String name, String qName) {
      switch (qName) {
         case "TargetBucket":
            this.targetBucket = currentOrNull(currentText);
            break;
         case "TargetPrefix":
            this.targetPrefix = currentOrNull(currentText);
            break;
         case "Grantee":
            handleGrantee();
            break;
         case "Grant":
            targetGrants.add(new Grant(currentGrantee, currentPermission));
            break;
         case "ID":
         case "EmailAddress":
         case "URI":
            currentId = currentOrNull(currentText);
            break;
         case "DisplayName":
            currentDisplayName = currentOrNull(currentText);
            break;
         case "Permission":
            currentPermission = currentOrNull(currentText);
            break;
      }
      currentText.setLength(0);
   }

   private void handleGrantee() {
      switch (currentGranteeType) {
         case "AmazonCustomerByEmail":
            currentGrantee = new EmailAddressGrantee(currentId);
            break;
         case "CanonicalUser":
            currentGrantee = new CanonicalUserGrantee(currentId, currentDisplayName);
            break;
         case "Group":
            currentGrantee = new GroupGrantee(URI.create(currentId));
            break;
      }
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
