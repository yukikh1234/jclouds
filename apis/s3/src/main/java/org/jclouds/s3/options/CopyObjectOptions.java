
package org.jclouds.s3.options;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.jclouds.aws.reference.AWSConstants.PROPERTY_HEADER_TAG;
import static org.jclouds.blobstore.reference.BlobStoreConstants.PROPERTY_USER_METADATA_PREFIX;
import static org.jclouds.s3.reference.S3Headers.CANNED_ACL;
import static org.jclouds.s3.reference.S3Headers.COPY_SOURCE_IF_MATCH;
import static org.jclouds.s3.reference.S3Headers.COPY_SOURCE_IF_MODIFIED_SINCE;
import static org.jclouds.s3.reference.S3Headers.COPY_SOURCE_IF_NO_MATCH;
import static org.jclouds.s3.reference.S3Headers.COPY_SOURCE_IF_UNMODIFIED_SINCE;
import static org.jclouds.s3.reference.S3Headers.DEFAULT_AMAZON_HEADERTAG;
import static org.jclouds.s3.reference.S3Headers.METADATA_DIRECTIVE;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.jclouds.date.DateService;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.http.options.BaseHttpRequestOptions;
import org.jclouds.s3.domain.CannedAccessPolicy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.net.HttpHeaders;

public class CopyObjectOptions extends BaseHttpRequestOptions {
    private static final DateService dateService = new SimpleDateFormatDateService();
    public static final CopyObjectOptions NONE = new CopyObjectOptions();
    private String cacheControl;
    private String contentDisposition;
    private String contentEncoding;
    private String contentLanguage;
    private String contentType;
    private Map<String, String> metadata;
    private CannedAccessPolicy acl = CannedAccessPolicy.PRIVATE;
    private String metadataPrefix;
    private String headerTag;

    @Inject
    public void setMetadataPrefix(@Named(PROPERTY_USER_METADATA_PREFIX) String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
    }

    @Inject
    public void setHeaderTag(@Named(PROPERTY_HEADER_TAG) String headerTag) {
        this.headerTag = headerTag;
    }

    public CopyObjectOptions overrideAcl(CannedAccessPolicy acl) {
        this.acl = checkNotNull(acl, "acl");
        if (!acl.equals(CannedAccessPolicy.PRIVATE))
            this.replaceHeader(CANNED_ACL, acl.toString());
        return this;
    }

    public CannedAccessPolicy getAcl() {
        return acl;
    }

    public String getIfModifiedSince() {
        return getFirstHeaderOrNull(COPY_SOURCE_IF_MODIFIED_SINCE);
    }

    public String getIfUnmodifiedSince() {
        return getFirstHeaderOrNull(COPY_SOURCE_IF_UNMODIFIED_SINCE);
    }

    public String getIfMatch() {
        return getFirstHeaderOrNull(COPY_SOURCE_IF_MATCH);
    }

    public String getIfNoneMatch() {
        return getFirstHeaderOrNull(COPY_SOURCE_IF_NO_MATCH);
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public CopyObjectOptions ifSourceModifiedSince(Date ifModifiedSince) {
        checkState(getIfMatch() == null, "ifETagMatches() is not compatible with ifModifiedSince()");
        checkState(getIfUnmodifiedSince() == null, "ifUnmodifiedSince() is not compatible with ifModifiedSince()");
        replaceHeader(COPY_SOURCE_IF_MODIFIED_SINCE, dateService.rfc822DateFormat(checkNotNull(ifModifiedSince, "ifModifiedSince")));
        return this;
    }

    public CopyObjectOptions ifSourceUnmodifiedSince(Date ifUnmodifiedSince) {
        checkState(getIfNoneMatch() == null, "ifETagDoesntMatch() is not compatible with ifUnmodifiedSince()");
        checkState(getIfModifiedSince() == null, "ifModifiedSince() is not compatible with ifUnmodifiedSince()");
        replaceHeader(COPY_SOURCE_IF_UNMODIFIED_SINCE, dateService.rfc822DateFormat(checkNotNull(ifUnmodifiedSince, "ifUnmodifiedSince")));
        return this;
    }

    public CopyObjectOptions ifSourceETagMatches(String eTag) {
        checkState(getIfNoneMatch() == null, "ifETagDoesntMatch() is not compatible with ifETagMatches()");
        checkState(getIfModifiedSince() == null, "ifModifiedSince() is not compatible with ifETagMatches()");
        replaceHeader(COPY_SOURCE_IF_MATCH, maybeQuoteETag(checkNotNull(eTag, "eTag")));
        return this;
    }

    public CopyObjectOptions ifSourceETagDoesntMatch(String eTag) {
        checkState(getIfMatch() == null, "ifETagMatches() is not compatible with ifETagDoesntMatch()");
        checkState(getIfUnmodifiedSince() == null, "ifUnmodifiedSince() is not compatible with ifETagDoesntMatch()");
        replaceHeader(COPY_SOURCE_IF_NO_MATCH, maybeQuoteETag(checkNotNull(eTag, "ifETagDoesntMatch")));
        return this;
    }

    @Override
    public Multimap<String, String> buildRequestHeaders() {
        checkState(headerTag != null, "headerTag should have been injected!");
        checkState(metadataPrefix != null, "metadataPrefix should have been injected!");
        ImmutableMultimap.Builder<String, String> returnVal = ImmutableMultimap.builder();
        for (Entry<String, String> entry : headers.entries()) {
            returnVal.put(entry.getKey().replace(DEFAULT_AMAZON_HEADERTAG, headerTag), entry.getValue());
        }
        boolean replace = addOptionalHeaders(returnVal);
        if (metadata != null) {
            addMetadataHeaders(returnVal);
            replace = true;
        }
        if (replace) {
            returnVal.put(METADATA_DIRECTIVE.replace(DEFAULT_AMAZON_HEADERTAG, headerTag), "REPLACE");
        }
        return returnVal.build();
    }
    
    private boolean addOptionalHeaders(ImmutableMultimap.Builder<String, String> returnVal) {
        boolean replace = false;
        if (cacheControl != null) {
            returnVal.put(HttpHeaders.CACHE_CONTROL, cacheControl);
            replace = true;
        }
        if (contentDisposition != null) {
            returnVal.put(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
            replace = true;
        }
        if (contentEncoding != null) {
            returnVal.put(HttpHeaders.CONTENT_ENCODING, contentEncoding);
            replace = true;
        }
        if (contentLanguage != null) {
            returnVal.put(HttpHeaders.CONTENT_LANGUAGE, contentLanguage);
            replace = true;
        }
        if (contentType != null) {
            returnVal.put(HttpHeaders.CONTENT_TYPE, contentType);
            replace = true;
        }
        return replace;
    }

    private void addMetadataHeaders(ImmutableMultimap.Builder<String, String> returnVal) {
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            String key = entry.getKey();
            returnVal.put(key.startsWith(metadataPrefix) ? key : metadataPrefix + key, entry.getValue());
        }
    }

    public CopyObjectOptions cacheControl(String cacheControl) {
        this.cacheControl = checkNotNull(cacheControl, "cacheControl");
        return this;
    }

    public CopyObjectOptions contentDisposition(String contentDisposition) {
        this.contentDisposition = checkNotNull(contentDisposition, "contentDisposition");
        return this;
    }

    public CopyObjectOptions contentEncoding(String contentEncoding) {
        this.contentEncoding = checkNotNull(contentEncoding, "contentEncoding");
        return this;
    }

    public CopyObjectOptions contentLanguage(String contentLanguage) {
        this.contentLanguage = checkNotNull(contentLanguage, "contentLanguage");
        return this;
    }

    public CopyObjectOptions contentType(String contentType) {
        this.contentType = checkNotNull(contentType, "contentType");
        return this;
    }

    public CopyObjectOptions overrideMetadataWith(Map<String, String> metadata) {
        checkNotNull(metadata, "metadata");
        this.metadata = metadata;
        return this;
    }

    public static class Builder {
        public static CopyObjectOptions overrideAcl(CannedAccessPolicy acl) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.overrideAcl(acl);
        }

        public static CopyObjectOptions ifSourceModifiedSince(Date ifModifiedSince) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.ifSourceModifiedSince(ifModifiedSince);
        }

        public static CopyObjectOptions ifSourceUnmodifiedSince(Date ifUnmodifiedSince) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.ifSourceUnmodifiedSince(ifUnmodifiedSince);
        }

        public static CopyObjectOptions ifSourceETagMatches(String eTag) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.ifSourceETagMatches(eTag);
        }

        public static CopyObjectOptions ifSourceETagDoesntMatch(String eTag) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.ifSourceETagDoesntMatch(eTag);
        }

        public static CopyObjectOptions cacheControl(String cacheControl) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.cacheControl(cacheControl);
        }

        public static CopyObjectOptions contentDisposition(String contentDisposition) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.contentDisposition(contentDisposition);
        }

        public static CopyObjectOptions contentEncoding(String contentEncoding) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.contentEncoding(contentEncoding);
        }

        public static CopyObjectOptions contentLanguage(String contentLanguage) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.contentLanguage(contentLanguage);
        }

        public static CopyObjectOptions contentType(String contentType) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.contentType(contentType);
        }

        public static CopyObjectOptions overrideMetadataWith(Map<String, String> metadata) {
            CopyObjectOptions options = new CopyObjectOptions();
            return options.overrideMetadataWith(metadata);
        }
    }

    private static String maybeQuoteETag(String eTag) {
        if (!eTag.startsWith("\"") && !eTag.endsWith("\"")) {
            eTag = "\"" + eTag + "\"";
        }
        return eTag;
    }
}
