package net.sunniwell.app.linktaro.radio.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.sz.mop4.sdk.log.LogBean1;
import org.apache.http.protocol.HTTP;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLResFile extends DefaultHandler {
    private StringBuffer buffer = new StringBuffer();
    private Map<String, Object> file;
    private List<Map<String, Object>> files;
    private Map<String, String> link;
    private List<Map<String, String>> links;

    public List<Map<String, Object>> getFiles() {
        return this.files;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        this.buffer.append(ch, start, length);
        super.characters(ch, start, length);
    }

    public void startDocument() throws SAXException {
        this.files = new ArrayList();
        super.startDocument();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals(LogBean1.TERMINAL_EXCEPTION_FILE)) {
            this.file = new HashMap();
        } else if (localName.equals("links")) {
            this.links = new ArrayList();
        } else if (localName.equals("link")) {
            this.link = new HashMap();
        }
        super.startElement(uri, localName, qName, attributes);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("name")) {
            this.file.put("name", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals("link")) {
            this.links.add(this.link);
        } else if (localName.equalsIgnoreCase("links")) {
            this.file.put("links", this.links);
        } else if (localName.equals("filmname")) {
            this.link.put("filmname", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals("filmid")) {
            this.link.put("filmid", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals("format")) {
            this.link.put("format", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals(MailDbHelper.TYPE)) {
            this.link.put(MailDbHelper.TYPE, this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals(HTTP.SERVER_HEADER)) {
            this.file.put("server", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals("Img")) {
            this.file.put("img", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals("Membership")) {
            this.file.put("membership", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals("channelnumber")) {
            this.file.put("channelnum", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals("newname")) {
            this.file.put("newname", this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equals(LogBean1.TERMINAL_EXCEPTION_FILE)) {
            this.files.add(this.file);
        }
        super.endElement(uri, localName, qName);
    }

    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
