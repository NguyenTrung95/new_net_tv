package net.sunniwell.aidl.bean;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

public class TagBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    private int f305id = 0;
    private String title = XmlPullParser.NO_NAMESPACE;

    public void setId(int id) {
        this.f305id = id;
    }

    public int getId() {
        return this.f305id;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }
}
