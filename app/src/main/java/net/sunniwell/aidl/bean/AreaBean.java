package net.sunniwell.aidl.bean;

import org.xmlpull.v1.XmlPullParser;

import java.io.Serializable;

public class AreaBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    private int f298id = 0;
    private String title = XmlPullParser.NO_NAMESPACE;

    public void setId(int id) {
        this.f298id = id;
    }

    public int getId() {
        return this.f298id;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }
}
