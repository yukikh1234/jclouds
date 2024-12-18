
package org.jclouds.s3.options;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.jclouds.http.options.BaseHttpRequestOptions;

/**
 * Contains options supported in the REST API for the GET bucket operation. <h2>
 * Usage</h2> The recommended way to instantiate a ListBucketOptions object is to statically import
 * ListBucketOptions.Builder.* and invoke a static creation method followed by an instance mutator
 * (if needed):
 * <p/>
 * <code>
 * import static org.jclouds.s3.options.ListBucketOptions.Builder.*
 * <p/>
 * S3Client connection = // get connection
 * ListBucketResponse bucket = connection.listBucket("bucketName", withPrefix("home/users").maxResults(1000));
 * <code>
 */
public class ListBucketOptions extends BaseHttpRequestOptions implements Cloneable {
   public static final ListBucketOptions NONE = new ListBucketOptions();

   /**
    * Limits the response to keys which begin with the indicated prefix. You can use prefixes to
    * separate a bucket into different sets of keys in a way similar to how a file system uses
    * folders.
    */
   public ListBucketOptions withPrefix(String prefix) {
      queryParameters.put("prefix", checkNotNull(prefix, "prefix"));
      return this;
   }

   public String getPrefix() {
      return getFirstQueryOrNull("prefix");
   }

   /**
    * Indicates where in the bucket to begin listing. The list will only include keys that occur
    * lexicographically after marker. This is convenient for pagination: To get the next page of
    * results use the last key of the current page as the marker.
    */
   public ListBucketOptions afterMarker(String marker) {
      queryParameters.put("marker", checkNotNull(marker, "marker"));
      return this;
   }

   public String getMarker() {
      return getFirstQueryOrNull("marker");
   }

   /**
    * The maximum number of keys you'd like to see in the response body. The server might return
    * fewer than this many keys, but will not return more.
    */
   public ListBucketOptions maxResults(int maxResults) {
      checkState(maxResults >= 0, "maxResults must be >= 0");
      queryParameters.put("max-keys", Long.toString(maxResults));
      return this;
   }

   public Integer getMaxResults() {
      String returnVal = getFirstQueryOrNull("max-keys");
      return (returnVal != null) ? Integer.valueOf(returnVal) : null;
   }

   /**
    * Causes keys that contain the same string between the prefix and the first occurrence of the
    * delimiter to be rolled up into a single result element in the CommonPrefixes collection. These
    * rolled-up keys are not returned elsewhere in the response.
    */
   public ListBucketOptions delimiter(String delimiter) {
      queryParameters.put("delimiter", checkNotNull(delimiter, "delimiter"));
      return this;
   }

   public String getDelimiter() {
      return getFirstQueryOrNull("delimiter");
   }

   public static class Builder {

      /**
       * @see ListBucketOptions#withPrefix(String)
       */
      public static ListBucketOptions withPrefix(String prefix) {
         ListBucketOptions options = new ListBucketOptions();
         return options.withPrefix(prefix);
      }

      /**
       * @see ListBucketOptions#afterMarker(String)
       */
      public static ListBucketOptions afterMarker(String marker) {
         ListBucketOptions options = new ListBucketOptions();
         return options.afterMarker(marker);
      }

      /**
       * @see ListBucketOptions#maxResults(int)
       */
      public static ListBucketOptions maxResults(int maxResults) {
         ListBucketOptions options = new ListBucketOptions();
         return options.maxResults(maxResults);
      }

      /**
       * @see ListBucketOptions#delimiter(String)
       */
      public static ListBucketOptions delimiter(String delimiter) {
         ListBucketOptions options = new ListBucketOptions();
         return options.delimiter(delimiter);
      }
   }

   @Override
   public ListBucketOptions clone() {
      ListBucketOptions newOptions = new ListBucketOptions();
      newOptions.queryParameters.putAll(queryParameters);
      return newOptions;
   }
}
