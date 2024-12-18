
package org.jclouds.s3.filters;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base16;
import static com.google.common.io.ByteStreams.readBytes;
import static org.jclouds.crypto.Macs.asByteProcessor;
import static org.jclouds.http.utils.Queries.queryParser;
import static org.jclouds.util.Strings2.toInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.InvalidKeyException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;

import jakarta.inject.Inject;

import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.escape.Escaper;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.net.HttpHeaders;
import com.google.common.net.PercentEscaper;
import com.google.inject.ImplementedBy;
import org.jclouds.crypto.Crypto;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.internal.SignatureWire;
import org.jclouds.io.Payload;
import org.jclouds.providers.ProviderMetadata;

public abstract class Aws4SignerBase {
   private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
   protected final DateFormat timestampFormat;
   protected final DateFormat dateFormat;

   private static final Escaper AWS_URL_PARAMETER_ESCAPER = new PercentEscaper("-_.~", false);
   private static final Escaper AWS_PATH_ESCAPER = new PercentEscaper("/-_.~", false);

   @ImplementedBy(ServiceAndRegion.AWSServiceAndRegion.class)
   public interface ServiceAndRegion {
      String service();
      String region(String host);

      final class AWSServiceAndRegion implements ServiceAndRegion {
         private final String service;

         @Inject
         AWSServiceAndRegion(ProviderMetadata provider) {
            this(provider.getEndpoint());
         }

         AWSServiceAndRegion(String endpoint) {
            this.service = AwsHostNameUtils.parseServiceName(URI.create(checkNotNull(endpoint, "endpoint")));
         }

         @Override
         public String service() {
            return service;
         }

         @Override
         public String region(String host) {
            return AwsHostNameUtils.parseRegionName(host, service());
         }
      }
   }

   protected final String headerTag;
   protected final ServiceAndRegion serviceAndRegion;
   protected final SignatureWire signatureWire;
   protected final Supplier<Credentials> creds;
   protected final Supplier<Date> timestampProvider;
   protected final Crypto crypto;

   protected Aws4SignerBase(SignatureWire signatureWire, String headerTag,
         Supplier<Credentials> creds, Supplier<Date> timestampProvider,
         ServiceAndRegion serviceAndRegion, Crypto crypto) {
      this.signatureWire = signatureWire;
      this.headerTag = headerTag;
      this.creds = creds;
      this.timestampProvider = timestampProvider;
      this.serviceAndRegion = serviceAndRegion;
      this.crypto = crypto;
      this.timestampFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
      timestampFormat.setTimeZone(GMT);
      this.dateFormat = new SimpleDateFormat("yyyyMMdd");
      dateFormat.setTimeZone(GMT);
   }

   protected static String hostHeaderFor(URI endpoint) {
      String scheme = endpoint.getScheme();
      String host = endpoint.getHost();
      int port = endpoint.getPort();
      if (port != -1) {
         if (("http".equalsIgnoreCase(scheme) && port != 80) ||
                 ("https".equalsIgnoreCase(scheme) && port != 443)) {
            host += ":" + port;
         }
      }
      return host;
   }

   protected String getContentType(HttpRequest request) {
      Payload payload = request.getPayload();
      String contentType = request.getFirstHeaderOrNull(HttpHeaders.CONTENT_TYPE);
      if (payload != null
            && payload.getContentMetadata() != null
            && payload.getContentMetadata().getContentType() != null) {
         contentType = payload.getContentMetadata().getContentType();
      }
      return contentType;
   }

   protected String getContentLength(HttpRequest request) {
      Payload payload = request.getPayload();
      String contentLength = request.getFirstHeaderOrNull(HttpHeaders.CONTENT_LENGTH);
      if (payload != null
            && payload.getContentMetadata() != null
            && payload.getContentMetadata().getContentType() != null) {
         Long length = payload.getContentMetadata().getContentLength();
         contentLength =
               length == null ? contentLength : String.valueOf(payload.getContentMetadata().getContentLength());
      }
      return contentLength;
   }

   protected void appendAmzHeaders(HttpRequest request,
         ImmutableMap.Builder<String, String> signedHeadersBuilder) {
      request.getHeaders().entries().stream()
             .filter(header -> header.getKey().startsWith("x-" + headerTag + "-"))
             .forEach(header -> signedHeadersBuilder.put(header.getKey().toLowerCase(), header.getValue()));
   }

   protected byte[] signatureKey(String secretKey, String datestamp, String region, String service) {
      byte[] kSecret = ("AWS4" + secretKey).getBytes(UTF_8);
      byte[] kDate = hmacSHA256(datestamp, kSecret);
      byte[] kRegion = hmacSHA256(region, kDate);
      byte[] kService = hmacSHA256(service, kRegion);
      byte[] kSigning = hmacSHA256("aws4_request", kService);
      return kSigning;
   }

   protected byte[] hmacSHA256(String toSign, byte[] key) {
      try {
         return readBytes(toInputStream(toSign), hmacSHA256(crypto, key));
      } catch (IOException | InvalidKeyException e) {
         throw new HttpException("Error during HMAC SHA256 operation", e);
      }
   }

   public static ByteProcessor<byte[]> hmacSHA256(Crypto crypto, byte[] signatureKey) throws InvalidKeyException {
      return asByteProcessor(crypto.hmacSHA256(signatureKey));
   }

   public static byte[] hash(InputStream input) throws HttpException {
      try (HashingInputStream his = new HashingInputStream(Hashing.sha256(), input)) {
         ByteStreams.copy(his, ByteStreams.nullOutputStream());
         return his.hash().asBytes();
      } catch (IOException e) {
         throw new HttpException("Unable to compute hash while signing request", e);
      }
   }

   public static byte[] hash(byte[] bytes) throws HttpException {
      try {
         return ByteSource.wrap(bytes).hash(Hashing.sha256()).asBytes();
      } catch (IOException e) {
         throw new HttpException("Unable to compute hash while signing request", e);
      }
   }

   public static byte[] hash(String input) throws HttpException {
      return hash(new ByteArrayInputStream(input.getBytes(UTF_8)));
   }

   protected String getCanonicalizedQueryString(String queryString) {
      Multimap<String, String> params = queryParser().apply(queryString);
      if (params == null) {
         return "";
      }
      SortedMap<String, String> sorted = Maps.newTreeMap();
      params.entries().forEach(pair -> sorted.put(urlEncode(pair.getKey()), urlEncode(pair.getValue())));
      return Joiner.on("&").withKeyValueSeparator("=").join(sorted);
   }

   public static String urlEncode(final String value) {
      return value == null ? "" : AWS_URL_PARAMETER_ESCAPER.escape(value);
   }

   public static String hex(final byte[] bytes) {
      return base16().lowerCase().encode(bytes);
   }

   protected String createStringToSign(String method, URI endpoint, Map<String, String> signedHeaders,
         String timestamp, String credentialScope, String hashedPayload) {

      Map<String, String> lowerCaseHeaders = lowerCaseNaturalOrderKeys(signedHeaders);

      StringBuilder canonicalRequest = new StringBuilder()
          .append(method).append("\n")
          .append(AWS_PATH_ESCAPER.escape(endpoint.getPath())).append("\n");

      if (endpoint.getQuery() != null) {
         canonicalRequest.append(getCanonicalizedQueryString(endpoint.getRawQuery()));
      }
      canonicalRequest.append("\n");

      lowerCaseHeaders.forEach((key, value) -> canonicalRequest.append(key).append(':').append(value).append('\n'));
      canonicalRequest.append("\n");
      canonicalRequest.append(Joiner.on(';').join(lowerCaseHeaders.keySet())).append('\n');
      canonicalRequest.append(hashedPayload);

      signatureWire.getWireLog().debug("<< " + canonicalRequest);

      return new StringBuilder()
          .append("AWS4-HMAC-SHA256").append('\n')
          .append(timestamp).append('\n')
          .append(credentialScope).append('\n')
          .append(hex(hash(canonicalRequest.toString()))).toString();
   }

   protected static Map<String, String> lowerCaseNaturalOrderKeys(Map<String, String> in) {
      checkNotNull(in, "input map");
      ImmutableSortedMap.Builder<String, String> returnVal = ImmutableSortedMap.naturalOrder();
      in.forEach((key, value) -> returnVal.put(key.toLowerCase(Locale.US), value));
      return returnVal.build();
   }
}
