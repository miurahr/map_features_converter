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

public final class CustomizedXMLFilter extends XMLFilterImpl {    

    Stack elements;
    String propid;

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
        elements = new Stack();
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
        super.startElement(uri, localName, qName, atts);

        elements.push(qName);
        for (int i=0;i<atts.getLength();i++) {
            String aname = atts.getQName(i);
            if (aname.equals("propid")) {
                propid = atts.getValue(i);
            }
	}
    }
    
    /**
     * 文字列があったことをイベントとして受取るメソッド(SAXイベントハンドラ)のオーバーライド
     * @param ch 文字の配列
     * @param start 配列の中の開始位置を表すインデックス
     * @param length 配列の中での長さを表す
     * @throws SAXException
     */
    public final void characters(char[] ch, int start, int length) throws SAXException {
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<length; i++) {
            char c = ch[start + i];
            if (! Character.isWhitespace(c)) {
                buf.append(c);
            }
        }
        int size = buf.length();
        if (size > 0) {
            char modified[] = new char[size];
            buf.getChars(0, size, modified, 0);
            super.characters(modified, 0, size);
            // 加工したものを通す
        }
        else {
            // super.characters(ch, start, length) を呼び出さないと、文字列をスルーしない
        }
    }

    /**
     * 要素が終了したことをイベントとして受取るメソッド(SAXイベントハンドラ)のオーバーライド
     * 指定した要素が終わった所で、文字列を分割した後出力
     * @param uri 要素のnamespace name
     * @param localName 要素のローカル名
     * @param qName 要素の修飾名
     * @throws SAXException
     */
    public final void endElement(String uri, String localName, String qName) throws SAXException {
        elements.pop();
        super.endElement(uri, localName, qName);
    }
}
