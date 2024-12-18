
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import jakarta.inject.Inject;

import org.jclouds.aws.domain.Region;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.s3.Bucket;

import com.google.common.base.Optional;
import com.google.common.cache.LoadingCache;

/**
 * Parses the response from Amazon S3 GET Bucket Location
 * <p/>
 * Region is the document we expect to parse.
 */
public class LocationConstraintHandler extends ParseSax.HandlerWithResult<String> {
   private final LoadingCache<String, Optional<String>> bucketToRegion;
   private StringBuilder currentText = new StringBuilder();
   private String region;
   private String bucket;

   @Inject
   public LocationConstraintHandler(@Bucket LoadingCache<String, Optional<String>> bucketToRegion) {
      this.bucketToRegion = bucketToRegion;
   }

   @Override
   public String getResult() {
      return region;
   }

   @Override
   public void endElement(String uri, String localName, String qName) {
      // Parse the region from the current text
      region = fromValue(currentOrNull(currentText));
      // Update the cache with the parsed region
      bucketToRegion.put(bucket, Optional.fromNullable(region));
   }

   @Override
   public LocationConstraintHandler setContext(HttpRequest request) {
      super.setContext(request);
      // Set the bucket name from the request context
      setBucket(GeneratedHttpRequest.class.cast(getRequest()).getInvocation().getArgs().get(0).toString());
      return this;
   }

   void setBucket(String bucket) {
      this.bucket = bucket;
   }

   /**
    * Parses the value expected in XML documents from the S3 service.
    * <p/>
    * {@code US_STANDARD} is returned as empty string in XML documents.
    */
   public static String fromValue(String value) {
      if (value == null || value.isEmpty()) {
         return Region.US_STANDARD;
      }
      // Simplified conditional checks for region mapping
      switch (value) {
         case "EU":
            return "eu-west-1";
         default:
            return value;
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}
