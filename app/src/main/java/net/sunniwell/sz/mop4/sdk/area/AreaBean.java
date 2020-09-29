package net.sunniwell.sz.mop4.sdk.area;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.area.AreaBean */
public class AreaBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    private int f336id = 0;
    private String title = XmlPullParser.NO_NAMESPACE;

    public void setId(int id) {
        this.f336id = id;
    }

    public int getId() {
        return this.f336id;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[AreaBean=" + super.toString())).append("\n\tid=").append(this.f336id).toString())).append("\n\ttitle=").append(this.title).toString())).append("]").toString();
    }
}
