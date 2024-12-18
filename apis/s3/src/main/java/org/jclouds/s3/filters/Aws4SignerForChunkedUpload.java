
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.s3.filters;

import static org.jclouds.aws.reference.AWSConstants.PROPERTY_HEADER_TAG;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.*;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_JCLOUDS_S3_CHUNKED_SIZE;
import static org.jclouds.util.Strings2.toInputStream;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.ByteStreams.readBytes;
import static com.google.common.net.HttpHeaders.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.Date;

import jakarta.inject.Named;

import org.jclouds.aws.domain.SessionCredentials;
import org.jclouds.crypto.Crypto;
import org.jclouds.date.TimeStamp;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.internal.SignatureWire;
import org.jclouds.io.Payload;
import org.jclouds.location.Provider;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.hash.HashCode;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteProcessor;
import com.google.inject.Inject;

/**
 * AWS4 signer for 'chunked' uploads.
 */
public class Aws4SignerForChunkedUpload extends Aws4SignerBase {

   private final int userDataBlockSize;

   @Inject
   public Aws4SignerForChunkedUpload(SignatureWire signatureWire,
         @Named(PROPERTY_HEADER_TAG) String headerTag,
         @Named(PROPERTY_JCLOUDS_S3_CHUNKED_SIZE) int userDataBlockSize,
         @Provider Supplier<Credentials> creds, @TimeStamp Supplier<Date> timestampProvider,
         ServiceAndRegion serviceAndRegion, Crypto crypto) {
      super(signatureWire, headerTag, creds, timestampProvider, serviceAndRegion, crypto);
      this.userDataBlockSize = userDataBlockSize;
   }

   protected HttpRequest sign(HttpRequest request) throws HttpException {
      validateRequest(request);
      Payload payload = request.getPayload();
      Long contentLength = payload.getContentMetadata().getContentLength();
      checkNotNull(contentLength, "request is not ready to sign, payload contentLength not present.");

      String host = request.getEndpoint().getHost();
      Date date = timestampProvider.get();
      String timestamp = timestampFormat.format(date);
      String datestamp = dateFormat.format(date);
      String service = serviceAndRegion.service();
      String region = serviceAndRegion.region(host);
      String credentialScope = Joiner.on('/').join(datestamp, region, service, "aws4_request");

      HttpRequest.Builder<?> requestBuilder = initializeRequestBuilder(request);

      ImmutableMap.Builder<String, String> signedHeadersBuilder = ImmutableSortedMap.naturalOrder();
      addContentEncodingHeaders(payload, requestBuilder, signedHeadersBuilder);
      addContentLengthHeaders(contentLength, requestBuilder, signedHeadersBuilder);
      addContentMD5Headers(request, payload, requestBuilder, signedHeadersBuilder);
      addContentTypeHeaders(request, requestBuilder, signedHeadersBuilder);
      addHostHeaders(request, host, requestBuilder, signedHeadersBuilder);
      addOptionalHeaders(request, signedHeadersBuilder);
      addAmzHeaders(request, signedHeadersBuilder);

      Credentials credentials = creds.get();
      addSecurityTokenHeader(credentials, requestBuilder, signedHeadersBuilder);

      String contentSha256 = getPayloadHash();
      addContentSha256Header(contentSha256, requestBuilder, signedHeadersBuilder);

      addDateHeader(timestamp, requestBuilder, signedHeadersBuilder);
      ImmutableMap<String, String> signedHeaders = signedHeadersBuilder.build();

      String stringToSign = createStringToSign(request.getMethod(), request.getEndpoint(), signedHeaders, timestamp,
            credentialScope, contentSha256);
      signatureWire.getWireLog().debug("<< " + stringToSign);

      byte[] signatureKey = signatureKey(credentials.credential, datestamp, region, service);

      ByteProcessor<byte[]> hmacSHA256 = initializeHmacSHA256(signatureKey);
      String signature = calculateSeedSignature(stringToSign, hmacSHA256);

      String authorization = buildAuthorizationHeader(credentials, credentialScope, signedHeaders, signature);

      ChunkedUploadPayload chunkedPayload = new ChunkedUploadPayload(payload, userDataBlockSize, timestamp,
            credentialScope, hmacSHA256, signature);
      chunkedPayload.getContentMetadata().setContentEncoding(null);

      return requestBuilder
            .replaceHeader(AUTHORIZATION, authorization)
            .payload(chunkedPayload)
            .build();
   }

   private void validateRequest(HttpRequest request) {
      checkNotNull(request, "request is not ready to sign");
      checkNotNull(request.getEndpoint(), "request is not ready to sign, request.endpoint not present.");
   }

   private HttpRequest.Builder<?> initializeRequestBuilder(HttpRequest request) {
      return request.toBuilder()
            .removeHeader(AUTHORIZATION)
            .removeHeader(DATE)
            .removeHeader(CONTENT_LENGTH);
   }

   private void addContentEncodingHeaders(Payload payload, HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      String contentEncoding = CONTENT_ENCODING_HEADER_AWS_CHUNKED;
      String originalContentEncoding = payload.getContentMetadata().getContentEncoding();
      if (originalContentEncoding != null) {
         contentEncoding += "," + originalContentEncoding;
      }
      requestBuilder.replaceHeader(CONTENT_ENCODING, contentEncoding);
      signedHeadersBuilder.put(CONTENT_ENCODING.toLowerCase(), contentEncoding);
   }

   private void addContentLengthHeaders(Long contentLength, HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      requestBuilder.replaceHeader(AMZ_DECODED_CONTENT_LENGTH_HEADER, contentLength.toString());
      signedHeadersBuilder.put(AMZ_DECODED_CONTENT_LENGTH_HEADER.toLowerCase(), contentLength.toString());

      long totalLength = calculateChunkedContentLength(contentLength, userDataBlockSize);
      requestBuilder.replaceHeader(CONTENT_LENGTH, Long.toString(totalLength));
      signedHeadersBuilder.put(CONTENT_LENGTH.toLowerCase(), Long.toString(totalLength));
   }

   private void addContentMD5Headers(HttpRequest request, Payload payload, HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      String contentMD5 = request.getFirstHeaderOrNull(CONTENT_MD5);
      if (payload != null) {
         HashCode md5 = payload.getContentMetadata().getContentMD5AsHashCode();
         if (md5 != null) {
            contentMD5 = BaseEncoding.base64().encode(md5.asBytes());
         }
      }
      if (contentMD5 != null) {
         requestBuilder.replaceHeader(CONTENT_MD5, contentMD5);
         signedHeadersBuilder.put(CONTENT_MD5.toLowerCase(), contentMD5);
      }
   }

   private void addContentTypeHeaders(HttpRequest request, HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      String contentType = getContentType(request);
      if (!Strings.isNullOrEmpty(contentType)) {
         requestBuilder.replaceHeader(CONTENT_TYPE, contentType);
         signedHeadersBuilder.put(CONTENT_TYPE.toLowerCase(), contentType);
      } else {
         requestBuilder.removeHeader(CONTENT_TYPE);
      }
   }

   private void addHostHeaders(HttpRequest request, String host, HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      host = hostHeaderFor(request.getEndpoint());
      requestBuilder.replaceHeader(HOST, host);
      signedHeadersBuilder.put(HOST.toLowerCase(), host);
   }

   private void addOptionalHeaders(HttpRequest request, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      String userAgent = request.getFirstHeaderOrNull(USER_AGENT);
      if (userAgent != null) {
         signedHeadersBuilder.put(USER_AGENT.toLowerCase(), userAgent);
      }
   }

   private void addAmzHeaders(HttpRequest request, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      appendAmzHeaders(request, signedHeadersBuilder);
   }

   private void addSecurityTokenHeader(Credentials credentials, HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      if (credentials instanceof SessionCredentials) {
         String token = SessionCredentials.class.cast(credentials).getSessionToken();
         requestBuilder.replaceHeader(AMZ_SECURITY_TOKEN_HEADER, token);
         signedHeadersBuilder.put(AMZ_SECURITY_TOKEN_HEADER.toLowerCase(), token);
      }
   }

   private void addContentSha256Header(String contentSha256, HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      requestBuilder.replaceHeader(AMZ_CONTENT_SHA256_HEADER, contentSha256);
      signedHeadersBuilder.put(AMZ_CONTENT_SHA256_HEADER.toLowerCase(), contentSha256);
   }

   private void addDateHeader(String timestamp, HttpRequest.Builder<?> requestBuilder, ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      requestBuilder.replaceHeader(AMZ_DATE_HEADER, timestamp);
      signedHeadersBuilder.put(AMZ_DATE_HEADER.toLowerCase(), timestamp);
   }

   private ByteProcessor<byte[]> initializeHmacSHA256(byte[] signatureKey) throws ChunkedUploadException {
      try {
         return hmacSHA256(crypto, signatureKey);
      } catch (InvalidKeyException e) {
         throw new ChunkedUploadException("Invalid key", e);
      }
   }

   private String calculateSeedSignature(String stringToSign, ByteProcessor<byte[]> hmacSHA256) throws ChunkedUploadException {
      try {
         return hex(readBytes(toInputStream(stringToSign), hmacSHA256));
      } catch (IOException e) {
         throw new ChunkedUploadException("HMAC SHA256 seed signature error", e);
      }
   }

   private String buildAuthorizationHeader(Credentials credentials, String credentialScope, ImmutableMap<String, String> signedHeaders, String signature) {
      return AMZ_ALGORITHM_HMAC_SHA256 + " " +
            "Credential=" + Joiner.on("/").join(credentials.identity, credentialScope) + ", " +
            "SignedHeaders=" + Joiner.on(";").join(signedHeaders.keySet()) + ", " +
            "Signature=" + signature;
   }

   protected String getPayloadHash() {
      return STREAMING_BODY_SHA256;
   }

   public static long calculateChunkedContentLength(long originalLength, long chunkSize) {
      checkArgument(originalLength > 0, "Nonnegative content length expected.");

      long maxSizeChunks = originalLength / chunkSize;
      long remainingBytes = originalLength % chunkSize;
      return maxSizeChunks * calculateChunkHeaderLength(chunkSize)
            + (remainingBytes > 0 ? calculateChunkHeaderLength(remainingBytes) : 0)
            + calculateChunkHeaderLength(0);
   }

   private static long calculateChunkHeaderLength(long chunkDataSize) {
      return Long.toHexString(chunkDataSize).length()
            + CHUNK_SIGNATURE_HEADER.length()
            + SIGNATURE_LENGTH
            + CLRF.length()
            + chunkDataSize
            + CLRF.length();
   }
}
