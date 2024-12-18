
package org.jclouds.s3.options;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.jclouds.aws.reference.AWSConstants.PROPERTY_HEADER_TAG;

import java.util.Map.Entry;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.jclouds.http.options.BaseHttpRequestOptions;
import org.jclouds.s3.domain.CannedAccessPolicy;
import org.jclouds.s3.reference.S3Headers;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Contains options supported in the REST API for the PUT bucket operation.
 * <h2>Usage</h2> 
 * The recommended way to instantiate a PutBucketOptions object is to statically import
 * PutBucketOptions.Builder.* and invoke a static creation method followed by an instance mutator
 * (if needed):
 * <p/>
 * <code>
 * import static org.jclouds.s3.commands.options.PutBucketOptions.Builder.*;
 * import static org.jclouds.s3.domain.S3Bucket.Metadata.LocationConstraint.*;
 * import org.jclouds.s3.S3Client;
 * <p/>
 * S3Client connection = // get connection
 * boolean createdInEu = connection.putBucketIfNotExists("bucketName",createIn(EU));
 * <code>
 */
public class PutBucketOptions extends BaseHttpRequestOptions {
   private CannedAccessPolicy acl = CannedAccessPolicy.PRIVATE;

   private String headerTag;

   @Inject
   public void setHeaderTag(@Named(PROPERTY_HEADER_TAG) String headerTag) {
      this.headerTag = headerTag;
   }

   @Override
   public Multimap<String, String> buildRequestHeaders() {
      checkState(headerTag != null, "headerTag should have been injected!");
      return createHeadersWithReplacedTag();
   }

   private Multimap<String, String> createHeadersWithReplacedTag() {
      Multimap<String, String> modifiedHeaders = LinkedHashMultimap.create();
      for (Entry<String, String> entry : headers.entries()) {
         modifiedHeaders.put(entry.getKey().replace("aws", headerTag), entry.getValue());
      }
      return modifiedHeaders;
   }

   /**
    * Override the default ACL (private) with the specified one.
    * 
    * @see CannedAccessPolicy
    */
   public PutBucketOptions withBucketAcl(CannedAccessPolicy acl) {
      this.acl = checkNotNull(acl, "acl");
      if (!acl.equals(CannedAccessPolicy.PRIVATE)) {
         this.replaceHeader(S3Headers.CANNED_ACL, acl.toString());
      }
      return this;
   }

   /**
    * @see PutBucketOptions#withBucketAcl
    */
   public CannedAccessPolicy getAcl() {
      return acl;
   }

   public static class Builder {
      /**
       * @see PutBucketOptions#withBucketAcl
       */
      public static PutBucketOptions withBucketAcl(CannedAccessPolicy acl) {
         PutBucketOptions options = new PutBucketOptions();
         return options.withBucketAcl(acl);
      }
   }
}
