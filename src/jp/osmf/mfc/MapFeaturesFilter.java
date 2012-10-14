package jp.osmf.mfc;

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

public final class MapFeaturesFilter extends XMLFilterImpl {    

    public enum featureCategory {NONE, CATEGORY, INPUTSET, FEATURE};

    private Properties configuration;
    private Stack <String>elements;
    private String featureKey;
    private String elemName;
    private featureCategory featureCat;

    private boolean help_processed = false;

    /**
     * Constructor
     * @param parent base XMLReader
     * @throws SAXException
     */
    public MapFeaturesFilter(XMLReader parent, Properties configuration) throws SAXException {
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

    private String getAttsValue(Attributes atts, String name) {
        String result = null;

        for (int i=0;i<atts.getLength();i++) {
            String aname = atts.getQName(i);
            if (aname.equals("key")) {
                result = atts.getValue(i);
            }
        }

        return result;
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
                } else if (qName.equals("inputSet")) {
                    featureCat = featureCategory.INPUTSET;
                    featureKey = "input." + atts.getValue(i);
                    elemName = qName;
                } else if (qName.equals("feature")) {
                    featureCat = featureCategory.FEATURE;
                    featureKey = "feature." + atts.getValue(i);
                    elemName = qName;
                }
            }
        }

        AttributesImpl newatts = new AttributesImpl(atts);
        if (featureKey != null) {
            if (elemName == qName) {
                if (featureCat == featureCategory.CATEGORY) {
                    newatts = replaceAtts(newatts, featureKey, "name");
                } else if (featureCat == featureCategory.FEATURE) {
                    newatts = replaceAtts(newatts, featureKey, "name");
                }
            } else if (featureCat == featureCategory.INPUTSET) {
                if (qName.equals("input")) {
                    String inputKey = getAttsValue(newatts, "key");
                    if (inputKey != null) {
                        featureKey = featureKey + "." + inputKey;
                        newatts = replaceAtts(newatts, featureKey, "name");
                        newatts = replaceAtts(newatts, featureKey, "description");
                    }
                } else if (qName.equals("choice")) {
                    String choiceKey = getAttsValue(newatts, "value");
                    if (choiceKey != null) {
                        newatts = replaceAtts(newatts, featureKey + "." + choiceKey, "text");
                    }
                }
            } else if (featureCat == featureCategory.FEATURE) {
                if (qName.equals("icon")) {
                    newatts = replaceAtts(newatts, featureKey + ".icon", "image");
                }
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

        if (currentElem.equals("help")) {
            String helpuri = configuration.getProperty(featureKey + ".help");
            if (helpuri != null && !help_processed) {
                // replace chars to newchars
                char[] newch = helpuri.toCharArray();
                super.characters(newch, 0, newch.length);
                help_processed = true;
            } else {
                // just pass to output
                super.characters(ch, start, length);
            }
        } else {
            // just pass to output
            super.characters(ch, start, length);
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
            if (featureCat == featureCategory.FEATURE && elemName.equals("help") && popedElem.equals("help")) {
                help_processed = false;
            }
            if (elemName.equals(popedElem)) {
                elemName = null;
                featureKey = null;
                featureCat = featureCategory.NONE;
            }
        }
        super.endElement(uri, localName, qName);
    }
}
