
package org.jclouds.s3.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.MediaType;

import org.jclouds.http.HttpRequest;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.rest.Binder;

@Singleton
public class BindPartIdsAndETagsToRequest implements Binder {

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      validateInput(request, input);
      Map<Integer, String> partsMap = (Map<Integer, String>) input;
      String xmlContent = createMultipartUploadXml(partsMap);
      setPayload(request, xmlContent);
      return request;
   }

   private <R extends HttpRequest> void validateInput(R request, Object input) {
      checkNotNull(request, "request");
      checkNotNull(input, "input");
      checkArgument(input instanceof Map, "this binder is only valid for Map!");
      Map<?, ?> map = (Map<?, ?>) input;
      checkArgument(!map.isEmpty(), "Please send parts");
   }

   private String createMultipartUploadXml(Map<Integer, String> map) {
      StringBuilder content = new StringBuilder();
      content.append("<CompleteMultipartUpload>");
      for (Entry<Integer, String> entry : map.entrySet()) {
         content.append("<Part>");
         content.append("<PartNumber>").append(entry.getKey()).append("</PartNumber>");
         content.append("<ETag>").append(entry.getValue()).append("</ETag>");
         content.append("</Part>");
      }
      content.append("</CompleteMultipartUpload>");
      return content.toString();
   }

   private <R extends HttpRequest> void setPayload(R request, String xmlContent) {
      Payload payload = Payloads.newStringPayload(xmlContent);
      payload.getContentMetadata().setContentType(MediaType.TEXT_XML);
      request.setPayload(payload);
   }
}
