
package org.jclouds.s3.filters;

import com.google.common.reflect.TypeToken;
import com.google.inject.Singleton;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.io.Payload;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.s3.S3Client;

import jakarta.inject.Inject;

@Singleton
public class RequestAuthorizeSignatureV4 implements RequestAuthorizeSignature {

   private static final String PUT_OBJECT_METHOD = "putObject";
   private static final TypeToken<S3Client> S3_CLIENT_TYPE = new TypeToken<S3Client>() {
   };

   private final Aws4SignerForAuthorizationHeader signerForAuthorizationHeader;
   private final Aws4SignerForChunkedUpload signerForChunkedUpload;
   private final Aws4SignerForQueryString signerForQueryString;

   @Inject
   public RequestAuthorizeSignatureV4(Aws4SignerForAuthorizationHeader signerForAuthorizationHeader,
         Aws4SignerForChunkedUpload signerForChunkedUpload,
         Aws4SignerForQueryString signerForQueryString) {
      this.signerForAuthorizationHeader = signerForAuthorizationHeader;
      this.signerForChunkedUpload = signerForChunkedUpload;
      this.signerForQueryString = signerForQueryString;
   }

   @Override
   public HttpRequest filter(HttpRequest request) throws HttpException {
      return useChunkedUpload(request) ? signForChunkedUpload(request) : signForAuthorizationHeader(request);
   }

   protected boolean useChunkedUpload(HttpRequest request) {
      if (!isGeneratedHttpRequest(request)) {
         return false;
      }
      GeneratedHttpRequest req = (GeneratedHttpRequest) request;
      if (!isS3PutObjectMethod(req)) {
         return false;
      }
      return isValidPayloadForChunkedUpload(req.getPayload());
   }

   private boolean isGeneratedHttpRequest(HttpRequest request) {
      return GeneratedHttpRequest.class.isAssignableFrom(request.getClass());
   }

   private boolean isS3PutObjectMethod(GeneratedHttpRequest req) {
      return S3_CLIENT_TYPE.equals(req.getInvocation().getInvokable().getOwnerType()) &&
             PUT_OBJECT_METHOD.equals(req.getInvocation().getInvokable().getName());
   }

   private boolean isValidPayloadForChunkedUpload(Payload payload) {
      if (payload == null || payload.getContentMetadata() == null) {
         return false;
      }
      Long contentLength = payload.getContentMetadata().getContentLength();
      return contentLength != null && contentLength > 0L && !payload.isRepeatable();
   }

   protected HttpRequest signForAuthorizationHeader(HttpRequest request) {
      return signerForAuthorizationHeader.sign(request);
   }

   protected HttpRequest signForChunkedUpload(HttpRequest request) {
      return signerForChunkedUpload.sign(request);
   }

   @Override
   public HttpRequest signForTemporaryAccess(HttpRequest request, long timeInSeconds) {
      return signerForQueryString.sign(request, timeInSeconds);
   }
}
