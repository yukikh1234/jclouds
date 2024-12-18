
package org.jclouds.s3.binders;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.hash.Hashing.md5;
import static org.jclouds.s3.binders.XMLHelper.asString;
import static org.jclouds.s3.binders.XMLHelper.createDocument;
import static org.jclouds.s3.binders.XMLHelper.elem;
import static org.jclouds.s3.binders.XMLHelper.elemWithText;

import jakarta.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;

import org.jclouds.http.HttpRequest;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.rest.Binder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BindIterableAsPayloadToDeleteRequest implements Binder {

   @SuppressWarnings("unchecked")
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      validateInput(request, input);

      Iterable<String> keys = (Iterable<String>) input;
      String content = createXMLContent(keys);

      Payload payload = createPayload(content);
      request.setPayload(payload);
      return request;
   }

   private void validateInput(Object request, Object input) {
      checkArgument(checkNotNull(input, "input is null") instanceof Iterable,
         "this binder is only valid for an Iterable");
      checkNotNull(request, "request is null");
      checkArgument(!Iterables.isEmpty((Iterable<?>) input), "The list of keys should not be empty.");
   }

   private String createXMLContent(Iterable<String> keys) {
      try {
         Document document = createDocument();
         Element rootNode = elem(document, "Delete", document);
         for (String key : keys) {
            Element objectNode = elem(rootNode, "Object", document);
            elemWithText(objectNode, "Key", key, document);
         }
         return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + asString(document);
      } catch (ParserConfigurationException | TransformerException e) {
         throw Throwables.propagate(e);
      }
   }

   private Payload createPayload(String content) {
      Payload payload = Payloads.newStringPayload(content);
      payload.getContentMetadata().setContentType(MediaType.TEXT_XML);
      byte[] md5 = md5().hashString(content, UTF_8).asBytes();
      payload.getContentMetadata().setContentMD5(md5);
      return payload;
   }
}
