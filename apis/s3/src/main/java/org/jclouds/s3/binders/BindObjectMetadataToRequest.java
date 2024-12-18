
package org.jclouds.s3.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base64;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.blobstore.binders.BindMapToHeadersWithPrefix;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;
import org.jclouds.s3.domain.ObjectMetadata;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.net.HttpHeaders;

@Singleton
public class BindObjectMetadataToRequest implements Binder {
   protected final BindMapToHeadersWithPrefix metadataPrefixer;

   @Inject
   public BindObjectMetadataToRequest(BindMapToHeadersWithPrefix metadataPrefixer) {
      this.metadataPrefixer = checkNotNull(metadataPrefixer, "metadataPrefixer");
   }

   @SuppressWarnings("unchecked")
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      checkArgument(checkNotNull(input, "input") instanceof ObjectMetadata,
               "this binder is only valid for ObjectMetadata!");
      checkNotNull(request, "request");

      ObjectMetadata md = ObjectMetadata.class.cast(input);
      checkArgument(md.getKey() != null, "objectMetadata.getKey() must be set!");

      request = metadataPrefixer.bindToRequest(request, md.getUserMetadata());

      Builder<String, String> headers = ImmutableMultimap.builder();
      addHeaderIfNotNull(headers, HttpHeaders.CACHE_CONTROL, md.getContentMetadata().getCacheControl());
      addHeaderIfNotNull(headers, "Content-Disposition", md.getContentMetadata().getContentDisposition());
      addHeaderIfNotNull(headers, "Content-Encoding", md.getContentMetadata().getContentEncoding());
      addHeaderIfNotNull(headers, HttpHeaders.CONTENT_LANGUAGE, md.getContentMetadata().getContentLanguage());
      addContentTypeHeader(headers, md);
      addContentMD5Header(headers, md);
      addStorageClassHeader(headers, md);

      return (R) request.toBuilder().replaceHeaders(headers.build()).build();
   }

   private void addHeaderIfNotNull(Builder<String, String> headers, String headerName, String headerValue) {
      if (headerValue != null) {
         headers.put(headerName, headerValue);
      }
   }

   private void addContentTypeHeader(Builder<String, String> headers, ObjectMetadata md) {
      String contentType = md.getContentMetadata().getContentType();
      if (contentType != null) {
         headers.put(HttpHeaders.CONTENT_TYPE, contentType);
      } else {
         headers.put(HttpHeaders.CONTENT_TYPE, "binary/octet-stream");
      }
   }

   private void addContentMD5Header(Builder<String, String> headers, ObjectMetadata md) {
      if (md.getContentMetadata().getContentMD5() != null) {
         headers.put("Content-MD5", base64().encode(md.getContentMetadata().getContentMD5()));
      }
   }

   private void addStorageClassHeader(Builder<String, String> headers, ObjectMetadata md) {
      ObjectMetadata.StorageClass storageClass = md.getStorageClass();
      if (storageClass != ObjectMetadata.StorageClass.STANDARD) {
         headers.put("x-amz-storage-class", storageClass.toString());
      }
   }
}
