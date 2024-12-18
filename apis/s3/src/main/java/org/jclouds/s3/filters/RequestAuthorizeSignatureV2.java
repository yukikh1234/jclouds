
package org.jclouds.s3.filters;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Iterables.get;
import static com.google.common.io.BaseEncoding.base64;
import static com.google.common.io.ByteStreams.readBytes;
import static org.jclouds.aws.reference.AWSConstants.PROPERTY_AUTH_TAG;
import static org.jclouds.aws.reference.AWSConstants.PROPERTY_HEADER_TAG;
import static org.jclouds.crypto.Macs.asByteProcessor;
import static org.jclouds.http.utils.Queries.queryParser;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_SERVICE_PATH;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;
import static org.jclouds.util.Strings2.toInputStream;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.aws.domain.SessionCredentials;
import org.jclouds.crypto.Crypto;
import org.jclouds.date.DateService;
import org.jclouds.date.TimeStamp;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.http.HttpUtils;
import org.jclouds.http.internal.SignatureWire;
import org.jclouds.logging.Logger;
import org.jclouds.rest.RequestSigner;
import org.jclouds.s3.reference.S3Constants;
import org.jclouds.s3.util.S3Utils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.io.ByteProcessor;
import com.google.common.net.HttpHeaders;

/**
 * AWS Sign V2
 */
@Singleton
public class RequestAuthorizeSignatureV2 implements RequestAuthorizeSignature, RequestSigner {
   private static final Collection<String> FIRST_HEADERS_TO_SIGN = ImmutableList.of(HttpHeaders.DATE);

   private static final Set<String> SIGNED_PARAMETERS = ImmutableSet.of("acl", "torrent", "logging", "location",
         "policy", "requestPayment", "versioning", "versions", "versionId", "notification", "uploadId", "uploads",
         "partNumber", "website", "response-content-type", "response-content-language", "response-expires",
         "response-cache-control", "response-content-disposition", "response-content-encoding", "delete");

   private final SignatureWire signatureWire;
   private final Supplier<Credentials> creds;
   private final Provider<String> timeStampProvider;
   private final Crypto crypto;
   private final HttpUtils utils;

   @Resource
   @Named(Constants.LOGGER_SIGNATURE)
   Logger signatureLog = Logger.NULL;

   private final String authTag;
   private final String headerTag;
   private final String servicePath;
   private final boolean isVhostStyle;
   private final DateService dateService;

   @Inject
   public RequestAuthorizeSignatureV2(SignatureWire signatureWire, @Named(PROPERTY_AUTH_TAG) String authTag,
         @Named(PROPERTY_S3_VIRTUAL_HOST_BUCKETS) boolean isVhostStyle,
         @Named(PROPERTY_S3_SERVICE_PATH) String servicePath, @Named(PROPERTY_HEADER_TAG) String headerTag,
         @org.jclouds.location.Provider Supplier<Credentials> creds,
         @TimeStamp Provider<String> timeStampProvider, Crypto crypto, HttpUtils utils,
         DateService dateService) {
      this.isVhostStyle = isVhostStyle;
      this.servicePath = servicePath;
      this.headerTag = headerTag;
      this.authTag = authTag;
      this.signatureWire = signatureWire;
      this.creds = creds;
      this.timeStampProvider = timeStampProvider;
      this.crypto = crypto;
      this.utils = utils;
      this.dateService = dateService;
   }

   public HttpRequest filter(HttpRequest request) throws HttpException {
      request = replaceDateHeader(request);
      Credentials current = creds.get();
      if (current instanceof SessionCredentials) {
         request = replaceSecurityTokenHeader(request, SessionCredentials.class.cast(current));
      }
      String signature = calculateSignature(createStringToSign(request));
      request = replaceAuthorizationHeader(request, signature);
      utils.logRequest(signatureLog, request, "<<");
      return request;
   }

   HttpRequest replaceSecurityTokenHeader(HttpRequest request, SessionCredentials current) {
      return request.toBuilder().replaceHeader("x-amz-security-token", current.getSessionToken()).build();
   }

   protected HttpRequest replaceAuthorizationHeader(HttpRequest request, String signature) {
      return request.toBuilder()
            .replaceHeader(HttpHeaders.AUTHORIZATION,
                  authTag + " " + creds.get().identity + ":" + signature).build();
   }

   HttpRequest replaceDateHeader(HttpRequest request) {
      return request.toBuilder().replaceHeader(HttpHeaders.DATE, timeStampProvider.get()).build();
   }

   public String createStringToSign(HttpRequest request) {
      utils.logRequest(signatureLog, request, ">>");
      SortedSetMultimap<String, String> canonicalizedHeaders = TreeMultimap.create();
      StringBuilder buffer = new StringBuilder();
      appendMethod(request, buffer);
      appendPayloadMetadata(request, buffer);
      appendHttpHeaders(request, canonicalizedHeaders);
      if (canonicalizedHeaders.containsKey("x-" + headerTag + "-date")) {
         canonicalizedHeaders.removeAll("date");
      }
      appendAmzHeaders(canonicalizedHeaders, buffer);
      appendBucketName(request, buffer);
      appendUriPath(request, buffer);
      if (signatureWire.enabled()) {
         signatureWire.output(buffer.toString());
      }
      return buffer.toString();
   }

   String calculateSignature(String toSign) throws HttpException {
      String signature = sign(toSign);
      if (signatureWire.enabled()) {
         signatureWire.input(toInputStream(signature));
      }
      return signature;
   }

   public String sign(String toSign) {
      try {
         ByteProcessor<byte[]> hmacSHA1 = asByteProcessor(crypto.hmacSHA1(creds.get().credential.getBytes(UTF_8)));
         return base64().encode(readBytes(toInputStream(toSign), hmacSHA1));
      } catch (Exception e) {
         throw new HttpException("error signing request", e);
      }
   }

   void appendMethod(HttpRequest request, StringBuilder toSign) {
      toSign.append(request.getMethod()).append("\n");
   }

   @VisibleForTesting
   void appendAmzHeaders(SortedSetMultimap<String, String> canonicalizedHeaders, StringBuilder toSign) {
      canonicalizedHeaders.entries().stream()
            .filter(header -> header.getKey().startsWith("x-" + headerTag + "-"))
            .forEach(header -> toSign.append(String.format("%s:%s\n", header.getKey().toLowerCase(), header.getValue())));
   }

   void appendPayloadMetadata(HttpRequest request, StringBuilder buffer) {
      buffer.append(request.getPayload() == null ? Strings.nullToEmpty(request.getFirstHeaderOrNull("Content-MD5")) :
            HttpUtils.nullToEmpty(request.getPayload().getContentMetadata().getContentMD5())).append("\n");
      buffer.append(Strings.nullToEmpty(request.getPayload() == null ? request.getFirstHeaderOrNull(HttpHeaders.CONTENT_TYPE)
            : request.getPayload().getContentMetadata().getContentType())).append("\n");
      FIRST_HEADERS_TO_SIGN.forEach(header -> buffer.append(HttpUtils.nullToEmpty(request.getHeaders().get(header))).append("\n"));
   }

   @VisibleForTesting
   void appendHttpHeaders(HttpRequest request, SortedSetMultimap<String, String> canonicalizedHeaders) {
      request.getHeaders().entries().stream()
            .filter(header -> header.getKey() != null)
            .forEach(header -> {
               String key = header.getKey().toString().toLowerCase(Locale.getDefault());
               if (key.equalsIgnoreCase(HttpHeaders.CONTENT_TYPE) || key.equalsIgnoreCase("Content-MD5")
                     || key.equalsIgnoreCase(HttpHeaders.DATE) || key.startsWith("x-" + headerTag + "-")) {
                  canonicalizedHeaders.put(key, header.getValue());
               }
            });
   }

   @VisibleForTesting
   void appendBucketName(HttpRequest req, StringBuilder toSign) {
      String bucketName = S3Utils.getBucketName(req);
      if (isVhostStyle && bucketName != null && bucketName.equals(bucketName.toLowerCase())) {
         toSign.append(servicePath).append(bucketName);
      }
   }

   @VisibleForTesting
   void appendUriPath(HttpRequest request, StringBuilder toSign) {
      toSign.append(request.getEndpoint().getRawPath());
      if (request.getEndpoint().getQuery() != null) {
         Multimap<String, String> params = queryParser().apply(request.getEndpoint().getQuery());
         char separator = '?';
         for (String paramName : Ordering.natural().sortedCopy(params.keySet())) {
            if (SIGNED_PARAMETERS.contains(paramName)) {
               toSign.append(separator).append(paramName);
               String paramValue = get(params.get(paramName), 0);
               if (paramValue != null) {
                  toSign.append("=").append(paramValue);
               }
               separator = '&';
            }
         }
      }
   }

   @Override
   public HttpRequest signForTemporaryAccess(HttpRequest request, long timeInSeconds) {
      String dateString = request.getFirstHeaderOrNull(HttpHeaders.DATE);
      if (dateString == null) {
         dateString = timeStampProvider.get();
      }
      Date date = dateService.rfc1123DateParse(dateString);
      String expiration = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(date.getTime()) + timeInSeconds);
      HttpRequest.Builder<?> builder = request.toBuilder()
            .removeHeader(HttpHeaders.AUTHORIZATION)
            .replaceHeader(HttpHeaders.DATE, expiration);
      String stringToSign = createStringToSign(builder.build());
      String signature = sign(stringToSign);
      return builder
            .addQueryParam(HttpHeaders.EXPIRES, expiration)
            .addQueryParam("AWSAccessKeyId", creds.get().identity)
            .addQueryParam(S3Constants.TEMPORARY_SIGNATURE_PARAM, signature)
            .removeHeader(HttpHeaders.DATE)
            .filters(ImmutableList.<HttpRequestFilter>of())
            .build();
   }
}
