package net.sunniwell.app.linktaro.radio.processor;

import android.content.Context;
import android.util.Xml;

import net.sunniwell.app.linktaro.radio.bean.Category;
import net.sunniwell.app.linktaro.radio.xml.XMLCategory;
import net.sunniwell.app.linktaro.radio.xml.XMLResFile;
import net.sunniwell.app.linktaro.tools.HttpClient4Utils;
import net.sunniwell.app.linktaro.tools.XmlParse;
import net.sunniwell.common.log.SWLogger;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParserProcessor {
    private static final SWLogger LOG = SWLogger.getLogger(ParserProcessor.class);
    private HttpClient4Utils mHttpClient4Utils = new HttpClient4Utils();
    private XmlParse mXmlParse = new XmlParse();

    public ParserProcessor(Context context) {
    }

    public List<Map<String, Object>> getRadioListData(String url, int categoryInx) {
        List<Map<String, Object>> radioList = null;
        try {
            List<Map<String, Object>> radioList2 = new ArrayList<>();
            try {
                List<Category> live = getCategoryList(url);
                if (live == null || live.size() == 0) {
                    radioList = radioList2;
                } else {
                    radioList = getRadioList(((Category) live.get(categoryInx)).getUrl());
                }
                LOG.mo8825d("...live_biil===" + radioList.size());
            } catch (Exception e) {
                e = e;
                radioList = radioList2;
                LOG.mo8825d("...Exception...===" + e.toString());
                return radioList;
            }
        } catch (Exception e2) {
            LOG.mo8825d("...Exception...===" + e2.toString());
            return radioList;
        }
        return radioList;
    }

    private List<Map<String, Object>> getRadioList(String url) {
        LOG.mo8825d("...getRadioList  url===" + url);
        List<Map<String, Object>> files = null;
        String str = XmlPullParser.NO_NAMESPACE;
        try {
            List<Map<String, Object>> files2 = new ArrayList<>();
            try {
                XMLResFile mXmlResFile = new XMLResFile();
                Xml.parse(this.mHttpClient4Utils.getHttpResponseResult(url, null), mXmlResFile);
                return mXmlResFile.getFiles();
            } catch (SAXException e) {
                e = e;
                files = files2;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return files;
        }
        return null;
    }

    private List<Category> getCategoryList(String url) {
        List<Category> categories = null;
        String str = XmlPullParser.NO_NAMESPACE;
        try {
            List<Category> categories2 = new ArrayList<>();
            try {
                XMLCategory mXmlCategory = new XMLCategory();
                Xml.parse(this.mHttpClient4Utils.getHttpResponseResult(url.trim(), null), mXmlCategory);
                return mXmlCategory.getCategories();
            } catch (SAXException e) {
                e = e;
                categories = categories2;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return categories;
        }
        return null;
    }

}
