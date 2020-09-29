package net.sunniwell.app.linktaro.radio.xml;

import java.util.ArrayList;
import java.util.List;
import net.sunniwell.app.linktaro.radio.bean.Category;
import net.sunniwell.download.manager.DownLoadConfigUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLCategory extends DefaultHandler {
    private StringBuffer buffer = new StringBuffer();
    private List<Category> categories;
    private Category category;

    public List<Category> getCategories() {
        return this.categories;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        this.buffer.append(ch, start, length);
        super.characters(ch, start, length);
    }

    public void startDocument() throws SAXException {
        this.categories = new ArrayList();
        super.startDocument();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equalsIgnoreCase("Category") && attributes.getLength() > 0 && attributes.getLocalName(0).equalsIgnoreCase("name")) {
            this.category = new Category();
            this.category.setName(attributes.getValue(0));
        }
        super.startElement(uri, localName, qName, attributes);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equalsIgnoreCase(DownLoadConfigUtil.KEY_URL)) {
            this.category.setUrl(this.buffer.toString().trim());
            this.buffer.setLength(0);
        } else if (localName.equalsIgnoreCase("Category") && this.category != null) {
            this.categories.add(this.category);
            this.category = null;
        }
        super.endElement(uri, localName, qName);
    }
}
