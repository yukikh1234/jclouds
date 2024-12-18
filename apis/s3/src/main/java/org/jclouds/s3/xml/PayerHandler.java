
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.Payer;

/**
 * Parses the response from Amazon S3 GET Request Payment
 * <p/>
 * RequestPaymentConfiguration is the document we expect to parse.
 */
public class PayerHandler extends ParseSax.HandlerWithResult<Payer> {
    private StringBuilder currentText = new StringBuilder();
    private Payer constraint;

    /**
     * Returns the parsed Payer result.
     * 
     * @return the Payer object or null if not set.
     */
    public Payer getResult() {
        return constraint;
    }

    /**
     * Handles the end of an XML element. Updates the constraint with the current text value.
     * 
     * @param uri    the URI of the element
     * @param name   the local name of the element
     * @param qName  the qualified name of the element
     */
    public void endElement(String uri, String name, String qName) {
        String value = currentOrNull(currentText);
        if (value != null) {
            constraint = Payer.fromValue(value);
        }
        currentText.setLength(0); // Reset the current text
    }

    /**
     * Accumulates the character data from the XML element.
     * 
     * @param ch     the character array
     * @param start  the starting position in the array
     * @param length the number of characters to read
     */
    public void characters(char[] ch, int start, int length) {
        currentText.append(ch, start, length);
    }
}
