package com.github.hinsteny.commons.warp.io.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author Hinsteny
 * @version CustomXMLStreamWriter: CustomXMLStreamWriter 2019-05-10 09:38 All rights reserved.$
 */
public class CustomXMLStreamWriter extends DelegatingXMLStreamWriter {

    private boolean cData = false;

    public CustomXMLStreamWriter(XMLStreamWriter writer) {
        super(writer);
    }

    public void setcData(boolean cData) {
        this.cData = cData;
    }

    /**
     * Write text to the output
     *
     * @param text the value to write
     */
    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        if (!"".equals(text) && cData) {
            super.writeCData(text);
        } else {
            super.writeCharacters(text);
        }
    }

}
