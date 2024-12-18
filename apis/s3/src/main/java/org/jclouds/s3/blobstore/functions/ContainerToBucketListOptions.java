
package org.jclouds.s3.blobstore.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.inject.Singleton;

import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.s3.options.ListBucketOptions;

import com.google.common.base.Function;

@Singleton
public class ContainerToBucketListOptions implements
         Function<ListContainerOptions, ListBucketOptions> {

   public ListBucketOptions apply(ListContainerOptions from) {
      checkNotNull(from, "set options to instance NONE instead of passing null");
      validateOptions(from);

      ListBucketOptions httpOptions = new ListBucketOptions();
      setDelimiter(from, httpOptions);
      setPrefix(from, httpOptions);
      setMarker(from, httpOptions);
      setMaxResults(from, httpOptions);

      return httpOptions;
   }

   private void validateOptions(ListContainerOptions from) {
      if (from.getPrefix() != null && from.getDir() != null) {
         throw new IllegalArgumentException("Cannot set both directory and prefix options");
      }
   }

   private void setDelimiter(ListContainerOptions from, ListBucketOptions httpOptions) {
      if (!from.isRecursive()) {
         String delimiter = from.getDelimiter() != null ? from.getDelimiter().toString() : "/";
         httpOptions.delimiter(delimiter);
      }
   }

   private void setPrefix(ListContainerOptions from, ListBucketOptions httpOptions) {
      if (from.getDir() != null) {
         String path = from.getDir();
         String delimiter = from.getDelimiter() != null ? from.getDelimiter().toString() : "/";
         if (!path.endsWith(delimiter)) {
            path += delimiter;
         }
         httpOptions.withPrefix(path);
      } else if (from.getPrefix() != null) {
         httpOptions.withPrefix(from.getPrefix());
      }
   }

   private void setMarker(ListContainerOptions from, ListBucketOptions httpOptions) {
      if (from.getMarker() != null) {
         httpOptions.afterMarker(from.getMarker());
      }
   }

   private void setMaxResults(ListContainerOptions from, ListBucketOptions httpOptions) {
      if (from.getMaxResults() != null) {
         httpOptions.maxResults(from.getMaxResults());
      }
   }
}
