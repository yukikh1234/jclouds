
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import java.net.URI;

import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.AccessControlList;
import org.jclouds.s3.domain.AccessControlList.CanonicalUserGrantee;
import org.jclouds.s3.domain.AccessControlList.EmailAddressGrantee;
import org.jclouds.s3.domain.AccessControlList.Grantee;
import org.jclouds.s3.domain.AccessControlList.GroupGrantee;
import org.jclouds.s3.domain.CanonicalUser;
import org.xml.sax.Attributes;

public class AccessControlListHandler extends ParseSax.HandlerWithResult<AccessControlList> {
   private AccessControlList acl = new AccessControlList();
   private StringBuilder currentText = new StringBuilder();

   public AccessControlListHandler() {
   }

   public AccessControlList getResult() {
      return acl;
   }

   private String currentId;
   private String currentDisplayName;
   private String currentGranteeType;
   private String currentPermission;
   private Grantee currentGrantee;

   public void startElement(String uri, String name, String qName, Attributes attrs) {
      if (qName.equals("Grantee")) {
         currentGranteeType = attrs.getValue("xsi:type");
      }
   }

   public void endElement(String uri, String name, String qName) {
      switch (qName) {
         case "Owner":
            handleOwnerEndElement();
            break;
         case "Grantee":
            handleGranteeEndElement();
            break;
         case "Grant":
            handleGrantEndElement();
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
         default:
            break;
      }
      currentText.setLength(0);
   }

   private void handleOwnerEndElement() {
      CanonicalUser owner = new CanonicalUser(currentId);
      owner.setDisplayName(currentDisplayName);
      acl.setOwner(owner);
   }

   private void handleGranteeEndElement() {
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
         default:
            break;
      }
   }

   private void handleGrantEndElement() {
      acl.addPermission(currentGrantee, currentPermission);
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
