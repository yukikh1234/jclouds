
package org.jclouds.s3.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.MediaType;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;
import org.jclouds.s3.domain.Payer;

@Singleton
public class BindPayerToXmlPayload implements Binder {

    private static final String XML_NAMESPACE = "http://s3.amazonaws.com/doc/2006-03-01/";
    private static final String XML_TEMPLATE = "<RequestPaymentConfiguration xmlns=\"%s\"><Payer>%s</Payer></RequestPaymentConfiguration>";

    @Override
    public <R extends HttpRequest> R bindToRequest(R request, Object toBind) {
        checkArgument(checkNotNull(toBind, "toBind") instanceof Payer, "this binder is only valid for Payer!");
        String xmlPayload = createXmlPayload((Payer) toBind);
        request.setPayload(xmlPayload);
        request.getPayload().getContentMetadata().setContentType(MediaType.TEXT_XML);
        return request;
    }

    private String createXmlPayload(Payer payer) {
        return String.format(XML_TEMPLATE, XML_NAMESPACE, payer.value());
    }
}
