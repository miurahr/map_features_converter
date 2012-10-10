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

    public enum featureCategory {NONE, CATEGORY, INPUTSET, FEATURE};

    private Properties configuration;
    private Stack <String>elements;
    private String featureKey;
    private String elemName;
    private featureCategory featureCat;

    /**
     * Constructor
     * @param parent base XMLReader
     * @throws SAXException
     */
    public CustomizedXMLFilter(XMLReader parent, Properties configuration) throws SAXException {
        super(parent);
        this.configuration = configuration;
    }

    public void startDocument() throws SAXException
    {
        elements = new Stack<String>();
        featureKey = null;
        elemName = null;
        featureCat = featureCategory.NONE;
        super.startDocument();
    }

    public void endDocument() throws SAXException
    {
        super.endDocument();
    }

    private AttributesImpl replaceAtts(AttributesImpl newatts, String featureKey, String tag) {
        String localizedName = configuration.getProperty(featureKey + "." + tag);
        if (localizedName != null) {
            newatts.setValue(newatts.getIndex(tag), localizedName);
        }
        return newatts;
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
            if (aname.equals("id")) {
                if (qName.equals("category")) {
                    featureCat = featureCategory.CATEGORY;
                    featureKey = "category." + atts.getValue(i);
                    elemName = qName;
                } else if (qName.equals("input")) {
                    featureCat = featureCategory.INPUTSET;
                    featureKey = "input." + atts.getValue(i);
                    elemName = qName;
                } else if (qName.equals("feature")) {
                    featureCat = featureCategory.FEATURE;
                    featureKey = "feature." + atts.getValue(i);
                    elemName = qName;
                } else {
                    featureCat = featureCategory.NONE;
                }
            }
	    }

        AttributesImpl newatts = new AttributesImpl(atts);
        if (featureKey != null && elemName == qName) {
            if (featureCat == featureCategory.CATEGORY) {
                newatts = replaceAtts(newatts, featureKey, "name");
            } else if (featureCat == featureCategory.INPUTSET) {
                newatts = replaceAtts(newatts, featureKey, "name");
            } else if (featureCat == featureCategory.FEATURE) {
                newatts = replaceAtts(newatts, featureKey, "name");
            }
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
                featureKey = null;
                featureCat = featureCategory.NONE;
            }
        }
        super.endElement(uri, localName, qName);
    }
}
