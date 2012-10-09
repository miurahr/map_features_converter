//package jp.osmf.potlatch2;
//
// Utility to generate localized map_features.xml and map_features/feature.xml
//
// Copyright 2012 Hiroshi Miura <miurahr@osmf.jp>
// Copyright 2012 OpenStreetMap Foundation Japan
//

import java.util.*;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public final class CustomizedXMLFilter extends XMLFilterImpl {    

    Stack <String>elements;
    String propid;
    String elemName;

    /**
     * Constructor
     * @param parent base XMLReader
     * @throws SAXException
     */
    public CustomizedXMLFilter(XMLReader parent) throws SAXException {
        super(parent);
    }

    public void startDocument()
    {
        elements = new Stack<String>();
        propid = null;
        elemName = null;
    }

   public void endDocument() throws SAXException
   {
   }

    /**
     * startElement handler
     * @param uri namespace
     * @param localName
     * @param qName
     * @param atts attributes
     * @throws SAXException
     */
    public final void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {


        elements.push(qName);
        for (int i=0;i<atts.getLength();i++) {
            String aname = atts.getQName(i);
            if (aname.equals("propid")) {
                propid = atts.getValue(i);
                elemName = qName;
            }
	    }
        AttributesImpl newatts = new AttributesImpl(atts);
        if ((propid != null) && propid.equals("category.roads")) {
            newatts.setValue(newatts.getIndex("name"), "道路");
        }
        super.startElement(uri, localName, qName, newatts);
    }
    
    /**
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    public final void characters(char[] ch, int start, int length) throws SAXException {
        StringBuffer buf = new StringBuffer();
        String currentElem = elements.peek();
          for (int i=0; i<length; i++) {
            char c = ch[start + i];
            buf.append(c);
          }
        // pass to output
        int size = buf.length();
        if (size > 0) {
            char modified[] = new char[size];
            buf.getChars(0, size, modified, 0);
            super.characters(modified, 0, size);
        }
    }

    /**
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    public final void endElement(String uri, String localName, String qName) throws SAXException {
        String popedElem = elements.pop();
        if (elemName != null) {
            if (elemName.equals(popedElem)) {
                propid = null;
            }
        }
        super.endElement(uri, localName, qName);
    }
}
