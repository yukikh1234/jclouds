
package org.jclouds.s3.blobstore.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.s3.domain.S3Object;

import com.google.common.base.Function;

@Singleton
public class BlobToObject implements Function<Blob, S3Object> {
    private final BlobToObjectMetadata blob2ObjectMd;
    private final S3Object.Factory objectProvider;

    @Inject
    BlobToObject(BlobToObjectMetadata blob2ObjectMd, S3Object.Factory objectProvider) {
        this.blob2ObjectMd = blob2ObjectMd;
        this.objectProvider = objectProvider;
    }

    public S3Object apply(Blob from) {
        if (from == null) {
            return null;
        }
        return createS3Object(from);
    }

    private S3Object createS3Object(Blob from) {
        S3Object object = objectProvider.create(blob2ObjectMd.apply(from.getMetadata()));
        setObjectPayload(object, from);
        setObjectHeaders(object, from);
        return object;
    }

    private void setObjectPayload(S3Object object, Blob from) {
        object.setPayload(checkNotNull(from.getPayload(), "payload: " + from));
    }

    private void setObjectHeaders(S3Object object, Blob from) {
        object.setAllHeaders(from.getAllHeaders());
    }
}
