
package org.jclouds.s3.binders;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;

@Singleton
public class BindNoBucketLoggingToXmlPayload implements Binder {

    private static final String XML_PAYLOAD = "<BucketLoggingStatus xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"/>";
    private final BindAsHostPrefixIfConfigured bindAsHostPrefixIfConfigured;

    @Inject
    BindNoBucketLoggingToXmlPayload(BindAsHostPrefixIfConfigured bindAsHostPrefixIfConfigured) {
        this.bindAsHostPrefixIfConfigured = bindAsHostPrefixIfConfigured;
    }

    @Override
    public <R extends HttpRequest> R bindToRequest(R request, Object payload) {
        request = bindAsHostPrefixIfConfigured.bindToRequest(request, payload);
        request.setPayload(XML_PAYLOAD);
        return request;
    }
}
