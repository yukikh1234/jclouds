
package org.jclouds.s3.filters;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.hash.HashCode;
import com.google.common.io.BaseEncoding;
import com.google.common.net.HttpHeaders;
import com.google.inject.Inject;
import org.jclouds.aws.domain.SessionCredentials;
import org.jclouds.crypto.Crypto;
import org.jclouds.date.TimeStamp;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.internal.SignatureWire;
import org.jclouds.io.Payload;
import org.jclouds.location.Provider;
import org.jclouds.util.Closeables2;

import jakarta.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base16;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.google.common.net.HttpHeaders.CONTENT_MD5;
import static com.google.common.net.HttpHeaders.DATE;
import static org.jclouds.aws.reference.AWSConstants.PROPERTY_HEADER_TAG;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_ALGORITHM_HMAC_SHA256;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_CONTENT_SHA256_HEADER;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_DATE_HEADER;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_SECURITY_TOKEN_HEADER;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;

/**
 * AWS4 signer sign requests to Amazon S3 using an 'Authorization' header.
 */
public class Aws4SignerForAuthorizationHeader extends Aws4SignerBase {
   
   @Inject
   public Aws4SignerForAuthorizationHeader(SignatureWire signatureWire,
         @Named(PROPERTY_S3_VIRTUAL_HOST_BUCKETS) boolean isVhostStyle,
         @Named(PROPERTY_HEADER_TAG) String headerTag,
         @Provider Supplier<Credentials> creds, @TimeStamp Supplier<Date> timestampProvider,
         ServiceAndRegion serviceAndRegion, Crypto crypto) {
      super(signatureWire, headerTag, creds, timestampProvider, serviceAndRegion, crypto);
   }

   protected HttpRequest sign(HttpRequest request) throws HttpException {
      validateRequest(request);

      Payload payload = request.getPayload();
      String host = request.getEndpoint().getHost();

      Date date = timestampProvider.get();
      String timestamp = timestampFormat.format(date);
      String datestamp = dateFormat.format(date);

      String service = serviceAndRegion.service();
      String region = serviceAndRegion.region(host);
      String credentialScope = Joiner.on('/').join(datestamp, region, service, "aws4_request");

      HttpRequest.Builder<?> requestBuilder = prepareRequestBuilder(request, timestamp, host);

      ImmutableMap<String, String> signedHeaders = buildSignedHeaders(request, requestBuilder, payload, host);

      String stringToSign = createStringToSign(request.getMethod(), request.getEndpoint(), signedHeaders, timestamp,
            credentialScope, getPayloadHash(request));
      signatureWire.getWireLog().debug("<< " + stringToSign);

      byte[] signatureKey = signatureKey(creds.get().credential, datestamp, region, service);
      String signature = base16().lowerCase().encode(hmacSHA256(stringToSign, signatureKey));

      String authorizationHeader = buildAuthorizationHeader(creds.get(), credentialScope, signedHeaders, signature);
      return requestBuilder.replaceHeader(HttpHeaders.AUTHORIZATION, authorizationHeader).build();
   }

   private void validateRequest(HttpRequest request) {
      checkNotNull(request, "request is not ready to sign");
      checkNotNull(request.getEndpoint(), "request is not ready to sign, request.endpoint not present.");
   }

   private HttpRequest.Builder<?> prepareRequestBuilder(HttpRequest request, String timestamp, String host) {
      return request.toBuilder()
            .removeHeader(AUTHORIZATION)
            .removeHeader(DATE)
            .replaceHeader(AMZ_DATE_HEADER, timestamp)
            .replaceHeader(HttpHeaders.HOST, hostHeaderFor(request.getEndpoint()));
   }

   private ImmutableMap<String, String> buildSignedHeaders(HttpRequest request, HttpRequest.Builder<?> requestBuilder, Payload payload, String host) {
      ImmutableMap.Builder<String, String> signedHeadersBuilder = ImmutableSortedMap.naturalOrder();

      addHeaderIfNotEmpty(requestBuilder, signedHeadersBuilder, HttpHeaders.CONTENT_TYPE, getContentType(request));
      addHeaderIfNotEmpty(requestBuilder, signedHeadersBuilder, HttpHeaders.CONTENT_LENGTH, getContentLength(request));
      addHeaderIfNotEmpty(requestBuilder, signedHeadersBuilder, CONTENT_MD5, getContentMD5(request, payload));
      addHeaderIfNotEmpty(requestBuilder, signedHeadersBuilder, HttpHeaders.USER_AGENT, request.getFirstHeaderOrNull(HttpHeaders.USER_AGENT));

      appendAmzHeaders(request, signedHeadersBuilder);
      addSessionToken(requestBuilder, signedHeadersBuilder);
      String contentSha256 = getPayloadHash(request);
      requestBuilder.replaceHeader(AMZ_CONTENT_SHA256_HEADER, contentSha256);
      signedHeadersBuilder.put(AMZ_CONTENT_SHA256_HEADER.toLowerCase(), contentSha256);

      return signedHeadersBuilder.build();
   }

   private void addHeaderIfNotEmpty(HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder, String header, String value) {
      if (!Strings.isNullOrEmpty(value)) {
         requestBuilder.replaceHeader(header, value);
         signedHeadersBuilder.put(header.toLowerCase(), value);
      }
   }

   private String getContentMD5(HttpRequest request, Payload payload) {
      String contentMD5 = request.getFirstHeaderOrNull(CONTENT_MD5);
      if (payload != null) {
         HashCode md5 = payload.getContentMetadata().getContentMD5AsHashCode();
         if (md5 != null) {
            contentMD5 = BaseEncoding.base64().encode(md5.asBytes());
         }
      }
      return contentMD5;
   }

   private void addSessionToken(HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      Credentials credentials = creds.get();
      if (credentials instanceof SessionCredentials) {
         String token = ((SessionCredentials) credentials).getSessionToken();
         requestBuilder.replaceHeader(AMZ_SECURITY_TOKEN_HEADER, token);
         signedHeadersBuilder.put(AMZ_SECURITY_TOKEN_HEADER.toLowerCase(), token);
      }
   }

   private String buildAuthorizationHeader(Credentials credentials, String credentialScope, ImmutableMap<String, String> signedHeaders, String signature) {
      return new StringBuilder(AMZ_ALGORITHM_HMAC_SHA256)
            .append(" Credential=").append(Joiner.on("/").join(credentials.identity, credentialScope))
            .append(", SignedHeaders=").append(Joiner.on(";").join(signedHeaders.keySet()))
            .append(", Signature=").append(signature)
            .toString();
   }

   protected String getPayloadHash(HttpRequest request) {
      Payload payload = request.getPayload();
      if (payload == null || "0".equals(getContentLength(request))) {
         return getEmptyPayloadContentHash();
      }
      return calculatePayloadContentHash(payload);
   }

   protected String getEmptyPayloadContentHash() {
      return base16().lowerCase().encode(hash(new ByteArrayInputStream(new byte[0])));
   }

   protected String calculatePayloadContentHash(Payload payload) {
      try (InputStream payloadStream = payload.openStream()) {
         return base16().lowerCase().encode(hash(payloadStream));
      } catch (IOException e) {
         throw new HttpException("unable to open payload stream to calculate AWS4 signature.", e);
      }
   }

   void closeOrResetPayloadStream(InputStream payloadStream, boolean repeatable) {
      try {
         if (repeatable) {
            Closeables2.closeQuietly(payloadStream);
         } else {
            payloadStream.reset();
         }
      } catch (IOException e) {
         throw new HttpException("unable to reset unrepeatable payload stream after calculating AWS4 signature.", e);
      }
   }
}
