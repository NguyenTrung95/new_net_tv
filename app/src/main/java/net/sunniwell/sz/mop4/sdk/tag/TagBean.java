package net.sunniwell.sz.mop4.sdk.tag;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.tag.TagBean */
public class TagBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    private int f344id = 0;
    private String title = XmlPullParser.NO_NAMESPACE;

    public void setId(int id) {
        this.f344id = id;
    }

    public int getId() {
        return this.f344id;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[TagBean=" + super.toString())).append("\n\tid=").append(this.f344id).toString())).append("\n\ttitle=").append(this.title).toString())).append("]").toString();
    }
}
