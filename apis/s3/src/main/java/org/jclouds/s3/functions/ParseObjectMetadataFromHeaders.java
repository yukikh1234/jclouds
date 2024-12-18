
package org.jclouds.s3.functions;

import static com.google.common.io.BaseEncoding.base16;
import static org.jclouds.blobstore.reference.BlobStoreConstants.PROPERTY_USER_METADATA_PREFIX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.functions.ParseSystemAndUserMetadataFromHeaders;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.rest.InvocationContext;
import org.jclouds.s3.blobstore.functions.BlobToObjectMetadata;
import org.jclouds.s3.domain.MutableObjectMetadata;
import org.jclouds.s3.domain.ObjectMetadata.StorageClass;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.net.HttpHeaders;

public class ParseObjectMetadataFromHeaders implements Function<HttpResponse, MutableObjectMetadata>,
        InvocationContext<ParseObjectMetadataFromHeaders> {
    private final ParseSystemAndUserMetadataFromHeaders blobMetadataParser;
    private final BlobToObjectMetadata blobToObjectMetadata;
    private final String userMdPrefix;

    @Inject
    public ParseObjectMetadataFromHeaders(ParseSystemAndUserMetadataFromHeaders blobMetadataParser,
            BlobToObjectMetadata blobToObjectMetadata, @Named(PROPERTY_USER_METADATA_PREFIX) String userMdPrefix) {
        this.blobMetadataParser = blobMetadataParser;
        this.blobToObjectMetadata = blobToObjectMetadata;
        this.userMdPrefix = userMdPrefix;
    }

    static final Pattern MD5_FROM_ETAG = Pattern.compile("^\"?([0-9a-f]+)\"?$");

    public MutableObjectMetadata apply(HttpResponse from) {
        BlobMetadata base = blobMetadataParser.apply(from);
        MutableObjectMetadata to = blobToObjectMetadata.apply(base);

        addETagToMetadata(from, to);
        setMD5FromETagIfAbsent(from, to);
        removeUnwantedUserMetadata(to);
        setCacheControlHeader(from, to);
        setStorageClassHeader(from, to);

        return to;
    }

    private void addETagToMetadata(HttpResponse from, MutableObjectMetadata metadata) {
        if (metadata.getETag() == null) {
            String eTagHeader = from.getFirstHeaderOrNull(userMdPrefix + "object-eTag");
            if (eTagHeader != null) {
                metadata.setETag(eTagHeader);
            }
        }
    }

    private void setMD5FromETagIfAbsent(HttpResponse from, MutableObjectMetadata metadata) {
        if (metadata.getContentMetadata().getContentMD5() == null && metadata.getETag() != null) {
            Matcher md5Matcher = MD5_FROM_ETAG.matcher(metadata.getETag());
            if (md5Matcher.find()) {
                byte[] md5 = base16().lowerCase().decode(md5Matcher.group(1));
                if (from.getPayload() != null) {
                    from.getPayload().getContentMetadata().setContentMD5(md5);
                }
                metadata.getContentMetadata().setContentMD5(md5);
            }
        }
    }

    private void removeUnwantedUserMetadata(MutableObjectMetadata metadata) {
        metadata.getUserMetadata().remove("object-etag");
    }

    private void setCacheControlHeader(HttpResponse from, MutableObjectMetadata metadata) {
        metadata.setCacheControl(from.getFirstHeaderOrNull(HttpHeaders.CACHE_CONTROL));
    }

    private void setStorageClassHeader(HttpResponse from, MutableObjectMetadata metadata) {
        String storageClass = from.getFirstHeaderOrNull("x-amz-storage-class");
        if (storageClass != null) {
            metadata.setStorageClass(StorageClass.valueOf(storageClass));
        }
    }

    @Override
    public ParseObjectMetadataFromHeaders setContext(HttpRequest request) {
        blobMetadataParser.setContext(request);
        blobToObjectMetadata.setContext(request);
        return this;
    }

    public ParseObjectMetadataFromHeaders setKey(String key) {
        blobMetadataParser.setName(key);
        return this;
    }
}
