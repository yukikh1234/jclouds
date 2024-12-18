
package org.jclouds.s3.binders;

import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_SERVICE_PATH;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;
import org.jclouds.rest.binders.BindAsHostPrefix;

import com.google.common.net.HttpHeaders;

@Singleton
public class BindAsHostPrefixIfConfigured implements Binder {

    protected final BindAsHostPrefix bindAsHostPrefix;
    protected final boolean isVhostStyle;
    protected final String servicePath;

    @Inject
    public BindAsHostPrefixIfConfigured(BindAsHostPrefix bindAsHostPrefix,
            @Named(PROPERTY_S3_VIRTUAL_HOST_BUCKETS) boolean isVhostStyle,
            @Named(PROPERTY_S3_SERVICE_PATH) String servicePath) {
        this.bindAsHostPrefix = bindAsHostPrefix;
        this.isVhostStyle = isVhostStyle;
        this.servicePath = servicePath;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends HttpRequest> R bindToRequest(R request, Object payload) {
        String payloadAsString = payload.toString();

        if (isVhostStyle && isLowerCase(payloadAsString)) {
            return bindAsHostPrefixAndSetHostHeader(request, payload);
        } else {
            return modifyRequestPath(request, payloadAsString);
        }
    }

    private boolean isLowerCase(String value) {
        return value.equals(value.toLowerCase());
    }

    private <R extends HttpRequest> R bindAsHostPrefixAndSetHostHeader(R request, Object payload) {
        request = bindAsHostPrefix.bindToRequest(request, payload);
        String host = request.getEndpoint().getHost();
        if (request.getEndpoint().getPort() != -1) {
            host += ":" + request.getEndpoint().getPort();
        }
        return (R) request.toBuilder().replaceHeader(HttpHeaders.HOST, host).build();
    }

    private <R extends HttpRequest> R modifyRequestPath(R request, String payloadAsString) {
        StringBuilder path = new StringBuilder(request.getEndpoint().getRawPath());
        if (servicePath.equals("/")) {
            appendOrInsertPayload(path, payloadAsString);
        } else {
            insertPayloadAtServicePath(path, payloadAsString);
        }
        return (R) request.toBuilder().replacePath(path.toString()).build();
    }

    private void appendOrInsertPayload(StringBuilder path, String payloadAsString) {
        if (path.toString().equals("/")) {
            path.append(payloadAsString);
        } else {
            path.insert(0, "/" + payloadAsString);
        }
    }

    private void insertPayloadAtServicePath(StringBuilder path, String payloadAsString) {
        int indexToInsert = path.indexOf(servicePath);
        indexToInsert = (indexToInsert == -1 ? 0 : indexToInsert) + servicePath.length();
        path.insert(indexToInsert, "/" + payloadAsString);
    }
}
