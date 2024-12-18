
package org.jclouds.cloudwatch.xml;

import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.SAXException;

/**
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_Dimension.html" />
 */
public class DimensionHandler extends ParseSax.HandlerForGeneratedRequestWithResult<Dimension> {

    private StringBuilder currentText = new StringBuilder();
    private String name;
    private String value;

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getResult() {
        Dimension dimension = new Dimension(name, value);

        // Reset since this handler is created once but produces N results
        resetFields();

        return dimension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endElement(String uri, String name, String qName) throws SAXException {
        processEndElement(qName);
        currentText.setLength(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void characters(char[] ch, int start, int length) {
        currentText.append(ch, start, length);
    }

    private void resetFields() {
        name = null;
        value = null;
    }

    private void processEndElement(String qName) {
        if ("Name".equals(qName)) {
            this.name = SaxUtils.currentOrNull(currentText);
        } else if ("Value".equals(qName)) {
            value = SaxUtils.currentOrNull(currentText);
        }
    }
}
