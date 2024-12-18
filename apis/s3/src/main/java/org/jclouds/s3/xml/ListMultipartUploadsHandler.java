
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import java.util.Date;

import jakarta.inject.Inject;

import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.CanonicalUser;
import org.jclouds.s3.domain.ListMultipartUploadsResponse;
import org.jclouds.s3.domain.ObjectMetadata;
import org.xml.sax.Attributes;

import com.google.common.collect.ImmutableList;

public final class ListMultipartUploadsHandler extends ParseSax.HandlerWithResult<ListMultipartUploadsResponse> {
   private String bucket;
   private String keyMarker;
   private String uploadIdMarker;
   private String nextKeyMarker;
   private String nextUploadIdMarker;
   private int maxUploads;
   private boolean isTruncated;
   private final ImmutableList.Builder<ListMultipartUploadsResponse.Upload> uploads = ImmutableList.builder();

   private String key;
   private String uploadId;
   private String id;
   private String displayName;
   private CanonicalUser initiator;
   private CanonicalUser owner;
   private ObjectMetadata.StorageClass storageClass;
   private Date initiated;

   private final DateService dateParser;
   private final StringBuilder currentText = new StringBuilder();
   private boolean inUpload;
   private boolean inInitiator;
   private boolean inOwner;

   @Inject
   public ListMultipartUploadsHandler(DateService dateParser) {
      this.dateParser = dateParser;
   }

   public ListMultipartUploadsResponse getResult() {
      return ListMultipartUploadsResponse.create(bucket, keyMarker, uploadIdMarker, nextKeyMarker, nextUploadIdMarker, maxUploads, isTruncated, uploads.build());
   }

   public void startElement(String uri, String name, String qName, Attributes attrs) {
      switch (qName) {
         case "Upload":
            inUpload = true;
            break;
         case "Initiator":
            inInitiator = true;
            break;
         case "Owner":
            inOwner = true;
            break;
         default:
            break;
      }
      currentText.setLength(0);
   }

   public void endElement(String uri, String name, String qName) {
      switch (qName) {
         case "Bucket":
            bucket = currentOrNull(currentText);
            break;
         case "KeyMarker":
            keyMarker = currentOrNull(currentText);
            break;
         case "UploadIdMarker":
            uploadIdMarker = currentOrNull(currentText);
            break;
         case "NextKeyMarker":
            nextKeyMarker = currentOrNull(currentText);
            break;
         case "NextUploadIdMarker":
            nextUploadIdMarker = currentOrNull(currentText);
            break;
         case "MaxUploads":
            maxUploads = Integer.parseInt(currentOrNull(currentText));
            break;
         case "IsTruncated":
            isTruncated = Boolean.parseBoolean(currentOrNull(currentText));
            break;
         case "Key":
            key = currentOrNull(currentText);
            break;
         case "UploadId":
            uploadId = currentOrNull(currentText);
            break;
         case "StorageClass":
            storageClass = ObjectMetadata.StorageClass.valueOf(currentOrNull(currentText));
            break;
         case "Initiated":
            initiated = dateParser.iso8601DateOrSecondsDateParse(currentOrNull(currentText));
            break;
         case "Upload":
            addUpload();
            break;
         case "Initiator":
            initiator = createCanonicalUser();
            inInitiator = false;
            break;
         case "Owner":
            owner = createCanonicalUser();
            inOwner = false;
            break;
         default:
            break;
      }
   }

   private void addUpload() {
      uploads.add(ListMultipartUploadsResponse.Upload.create(key, uploadId, initiator, owner, storageClass, initiated));
      resetUploadFields();
   }

   private CanonicalUser createCanonicalUser() {
      CanonicalUser user = new CanonicalUser(id, displayName);
      id = null;
      displayName = null;
      return user;
   }

   private void resetUploadFields() {
      key = null;
      uploadId = null;
      id = null;
      displayName = null;
      initiator = null;
      owner = null;
      storageClass = null;
      initiated = null;
      inUpload = false;
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
