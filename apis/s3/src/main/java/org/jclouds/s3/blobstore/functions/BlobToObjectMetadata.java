
package org.jclouds.s3.blobstore.functions;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map.Entry;

import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpUtils;
import org.jclouds.rest.InvocationContext;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.s3.domain.MutableObjectMetadata;
import org.jclouds.s3.domain.ObjectMetadata.StorageClass;
import org.jclouds.s3.domain.internal.MutableObjectMetadataImpl;

import com.google.common.base.Function;

public class BlobToObjectMetadata implements Function<BlobMetadata, MutableObjectMetadata>,
        InvocationContext<BlobToObjectMetadata> {
    private String bucket;

    public MutableObjectMetadata apply(BlobMetadata from) {
        if (from == null) return null;
        MutableObjectMetadata to = new MutableObjectMetadataImpl();
        copyContentMetadata(from, to);
        to.setUri(from.getUri());
        to.setETag(from.getETag());
        to.setKey(from.getName());
        to.setBucket(bucket);
        to.setLastModified(from.getLastModified());
        copyUserMetadata(from, to);
        setStorageClass(from, to);
        return to;
    }

    private void copyContentMetadata(BlobMetadata from, MutableObjectMetadata to) {
        HttpUtils.copy(from.getContentMetadata(), to.getContentMetadata());
    }

    private void copyUserMetadata(BlobMetadata from, MutableObjectMetadata to) {
        if (from.getUserMetadata() != null) {
            for (Entry<String, String> entry : from.getUserMetadata().entrySet()) {
                to.getUserMetadata().put(entry.getKey().toLowerCase(), entry.getValue());
            }
        }
    }

    private void setStorageClass(BlobMetadata from, MutableObjectMetadata to) {
        if (from.getTier() != null) {
            to.setStorageClass(StorageClass.fromTier(from.getTier()));
        }
    }

    @Override
    public BlobToObjectMetadata setContext(HttpRequest request) {
        checkArgument(request instanceof GeneratedHttpRequest, "note this handler requires a GeneratedHttpRequest");
        return setBucket(GeneratedHttpRequest.class.cast(request).getInvocation().getArgs().get(0).toString());
    }

    private BlobToObjectMetadata setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
}
