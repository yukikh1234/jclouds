
package org.jclouds.s3.functions;

import jakarta.inject.Inject;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.rest.InvocationContext;
import org.jclouds.s3.domain.MutableObjectMetadata;
import org.jclouds.s3.domain.S3Object;
import com.google.common.base.Function;

public class ParseObjectFromHeadersAndHttpContent implements Function<HttpResponse, S3Object>,
      InvocationContext<ParseObjectFromHeadersAndHttpContent> {

   private final ParseObjectMetadataFromHeaders metadataParser;
   private final S3Object.Factory objectProvider;

   @Inject
   public ParseObjectFromHeadersAndHttpContent(ParseObjectMetadataFromHeaders metadataParser,
         S3Object.Factory objectProvider) {
      this.metadataParser = metadataParser;
      this.objectProvider = objectProvider;
   }

   @Override
   public S3Object apply(HttpResponse from) {
      return createS3Object(from);
   }
   
   private S3Object createS3Object(HttpResponse response) {
      MutableObjectMetadata metadata = metadataParser.apply(response);
      S3Object object = objectProvider.create(metadata);
      object.getAllHeaders().putAll(response.getHeaders());
      object.setPayload(response.getPayload());
      return object;
   }

   @Override
   public ParseObjectFromHeadersAndHttpContent setContext(HttpRequest request) {
      metadataParser.setContext(request);
      return this;
   }
}
