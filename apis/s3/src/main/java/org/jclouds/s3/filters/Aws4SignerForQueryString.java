
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
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_ALGORITHM_PARAM;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_CONTENT_SHA256_HEADER;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_CREDENTIAL_PARAM;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_DATE_HEADER;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_DATE_PARAM;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_EXPIRES_PARAM;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_SECURITY_TOKEN_PARAM;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_SIGNATURE_PARAM;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AMZ_SIGNEDHEADERS_PARAM;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.AUTHORIZATION_HEADER;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.UNSIGNED_PAYLOAD;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base16;

import java.util.Date;

import jakarta.inject.Named;

import org.jclouds.aws.domain.SessionCredentials;
import org.jclouds.crypto.Crypto;
import org.jclouds.date.TimeStamp;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.Uris;
import org.jclouds.http.internal.SignatureWire;
import org.jclouds.location.Provider;

import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.inject.Inject;

/**
 * AWS4 signer sign requests to Amazon S3 using query string parameters.
 */
public class Aws4SignerForQueryString extends Aws4SignerBase {
   @Inject
   public Aws4SignerForQueryString(SignatureWire signatureWire,
         @Named(PROPERTY_S3_VIRTUAL_HOST_BUCKETS) boolean isVhostStyle,
         @Named(PROPERTY_HEADER_TAG) String headerTag,
         @Provider Supplier<Credentials> creds, @TimeStamp Supplier<Date> timestampProvider,
         ServiceAndRegion serviceAndRegion, Crypto crypto) {
      super(signatureWire, headerTag, creds, timestampProvider, serviceAndRegion, crypto);
   }

   protected HttpRequest sign(HttpRequest request, long timeInSeconds) throws HttpException {
      checkNotNull(request, "request is not ready to sign");
      checkNotNull(request.getEndpoint(), "request is not ready to sign, request.endpoint not present.");

      String host = request.getEndpoint().getHost();
      Date date = timestampProvider.get();
      String timestamp = timestampFormat.format(date);
      String datestamp = dateFormat.format(date);
      String service = serviceAndRegion.service();
      String region = serviceAndRegion.region(host);
      String credentialScope = Joiner.on('/').join(datestamp, region, service, "aws4_request");

      HttpRequest.Builder<?> requestBuilder = initializeRequestBuilder(request);
      ImmutableMap<String, String> signedHeaders = constructCanonicalHeaders(request, host);
      Uris.UriBuilder endpointBuilder = constructUriBuilder(request, signedHeaders);

      Credentials credentials = creds.get();
      addSecurityTokenIfNeeded(endpointBuilder, credentials);

      addQueryParameters(endpointBuilder, credentials, credentialScope, timestamp, timeInSeconds, signedHeaders);

      String stringToSign = createStringToSign(request.getMethod(), endpointBuilder.build(), signedHeaders, timestamp, credentialScope,
            getPayloadHash());

      signatureWire.getWireLog().debug("<< " + stringToSign);

      byte[] signatureKey = signatureKey(credentials.credential, datestamp, region, service);
      String signature = base16().lowerCase().encode(hmacSHA256(stringToSign, signatureKey));

      endpointBuilder.replaceQuery(AMZ_SIGNATURE_PARAM, signature);

      return requestBuilder.endpoint(endpointBuilder.build()).build();
   }

   private HttpRequest.Builder<?> initializeRequestBuilder(HttpRequest request) {
      return request.toBuilder()
            .removeHeader(AUTHORIZATION_HEADER)
            .removeHeader(AMZ_CONTENT_SHA256_HEADER)
            .removeHeader(AMZ_DATE_HEADER);
   }

   private ImmutableMap<String, String> constructCanonicalHeaders(HttpRequest request, String host) {
      ImmutableMap.Builder<String, String> signedHeadersBuilder = ImmutableSortedMap.<String, String>naturalOrder();
      String hostHeader = hostHeaderFor(request.getEndpoint());
      signedHeadersBuilder.put("host", hostHeader);
      return signedHeadersBuilder.build();
   }

   private Uris.UriBuilder constructUriBuilder(HttpRequest request, ImmutableMap<String, String> signedHeaders) {
      return Uris.uriBuilder(request.getEndpoint());
   }

   private void addSecurityTokenIfNeeded(Uris.UriBuilder endpointBuilder, Credentials credentials) {
      if (credentials instanceof SessionCredentials) {
         String token = SessionCredentials.class.cast(credentials).getSessionToken();
         endpointBuilder.replaceQuery(AMZ_SECURITY_TOKEN_PARAM, token);
      }
   }

   private void addQueryParameters(Uris.UriBuilder endpointBuilder, Credentials credentials, String credentialScope, String timestamp,
                                   long timeInSeconds, ImmutableMap<String, String> signedHeaders) {
      endpointBuilder
            .replaceQuery(AMZ_ALGORITHM_PARAM, AwsSignatureV4Constants.AMZ_ALGORITHM_HMAC_SHA256)
            .replaceQuery(AMZ_CREDENTIAL_PARAM, Joiner.on("/").join(credentials.identity, credentialScope))
            .replaceQuery(AMZ_DATE_PARAM, timestamp)
            .replaceQuery(AMZ_EXPIRES_PARAM, String.valueOf(timeInSeconds))
            .replaceQuery(AMZ_SIGNEDHEADERS_PARAM, Joiner.on(';').join(signedHeaders.keySet()));
   }

   protected String getPayloadHash() {
      return UNSIGNED_PAYLOAD;
   }
}
