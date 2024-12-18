
package org.jclouds.s3.xml;

import static com.google.common.io.BaseEncoding.base16;
import static org.jclouds.http.Uris.uriBuilder;
import static org.jclouds.util.SaxUtils.currentOrNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.inject.Inject;

import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.CanonicalUser;
import org.jclouds.s3.domain.ListBucketResponse;
import org.jclouds.s3.domain.ObjectMetadata;
import org.jclouds.s3.domain.ObjectMetadataBuilder;
import org.jclouds.s3.domain.internal.ListBucketResponseImpl;
import org.jclouds.util.Strings2;
import org.xml.sax.Attributes;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

public class ListBucketHandler extends ParseSax.HandlerWithResult<ListBucketResponse> {
   private Builder<ObjectMetadata> contents = ImmutableSet.builder();
   private Builder<String> commonPrefixes = ImmutableSet.builder();
   private CanonicalUser currentOwner;
   private StringBuilder currentText = new StringBuilder();
   private ObjectMetadataBuilder builder = new ObjectMetadataBuilder();
   private final DateService dateParser;
   private String bucketName;
   private String prefix;
   private String marker;
   private int maxResults;
   private String delimiter;
   private boolean isTruncated;
   private static final Pattern ETAG_CONTENT_MD5_PATTERN = Pattern.compile("\"([0-9a-f]+)\"");
   private boolean inCommonPrefixes;
   private String currentKey;
   private String nextMarker;

   @Inject
   public ListBucketHandler(DateService dateParser) {
      this.dateParser = dateParser;
      this.currentOwner = new CanonicalUser();
   }

   public ListBucketResponse getResult() {
      return new ListBucketResponseImpl(bucketName, contents.build(), prefix, marker,
               (isTruncated && nextMarker == null) ? currentKey : nextMarker, maxResults, delimiter, isTruncated,
               commonPrefixes.build());
   }

   public void startElement(String uri, String name, String qName, Attributes attrs) {
      if (qName.equals("CommonPrefixes")) {
         inCommonPrefixes = true;
      }
      currentText.setLength(0);
   }

   public void endElement(String uri, String name, String qName) {
      switch (qName) {
         case "ID":
            handleID();
            break;
         case "DisplayName":
            handleDisplayName();
            break;
         case "Key":
            handleKey(uri);
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
         case "Owner":
            handleOwner();
            break;
         case "StorageClass":
            handleStorageClass();
            break;
         case "Contents":
            handleContents();
            break;
         case "Name":
            handleName();
            break;
         case "Prefix":
            handlePrefix();
            break;
         case "Delimiter":
            handleDelimiter();
            break;
         case "Marker":
            handleMarker();
            break;
         case "NextMarker":
            handleNextMarker();
            break;
         case "MaxKeys":
            handleMaxKeys();
            break;
         case "IsTruncated":
            handleIsTruncated();
            break;
      }
   }

   private void handleID() {
      currentOwner.setId(currentOrNull(currentText));
   }

   private void handleDisplayName() {
      currentOwner.setDisplayName(currentOrNull(currentText));
   }

   private void handleKey(String uri) {
      if (currentText.length() == 0) {
         throw new RuntimeException("Object store returned empty key name");
      }
      currentKey = currentText.toString();
      builder.key(currentKey);
      builder.uri(uriBuilder(getRequest().getEndpoint()).clearQuery().appendPath(Strings2.urlEncode(currentKey)).build());
   }

   private void handleLastModified() {
      builder.lastModified(dateParser.iso8601DateOrSecondsDateParse(currentOrNull(currentText)));
   }

   private void handleETag() {
      String currentETag = currentOrNull(currentText);
      builder.eTag(currentETag);
      Matcher matcher = ETAG_CONTENT_MD5_PATTERN.matcher(currentETag);
      if (matcher.matches()) {
         builder.contentMD5(base16().lowerCase().decode(matcher.group(1)));
      }
   }

   private void handleSize() {
      builder.contentLength(Long.valueOf(currentOrNull(currentText)));
   }

   private void handleOwner() {
      builder.owner(currentOwner);
      currentOwner = new CanonicalUser();
   }

   private void handleStorageClass() {
      builder.storageClass(ObjectMetadata.StorageClass.valueOf(currentOrNull(currentText)));
   }

   private void handleContents() {
      contents.add(builder.build());
      builder = new ObjectMetadataBuilder().bucket(bucketName);
   }

   private void handleName() {
      this.bucketName = currentOrNull(currentText);
      builder.bucket(bucketName);
   }

   private void handlePrefix() {
      String prefix = currentOrNull(currentText);
      if (inCommonPrefixes)
         commonPrefixes.add(prefix);
      else
         this.prefix = prefix;
   }

   private void handleDelimiter() {
      this.delimiter = currentOrNull(currentText);
   }

   private void handleMarker() {
      this.marker = currentOrNull(currentText);
   }

   private void handleNextMarker() {
      this.nextMarker = currentOrNull(currentText);
   }

   private void handleMaxKeys() {
      this.maxResults = Integer.parseInt(currentOrNull(currentText));
   }

   private void handleIsTruncated() {
      this.isTruncated = Boolean.parseBoolean(currentOrNull(currentText));
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
