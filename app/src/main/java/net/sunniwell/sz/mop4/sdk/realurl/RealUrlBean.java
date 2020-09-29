package net.sunniwell.sz.mop4.sdk.realurl;

import java.io.Serializable;

/* renamed from: net.sunniwell.sz.mop4.sdk.realurl.RealUrlBean */
public class RealUrlBean implements Serializable {
    private static final long serialVersionUID = 1;
    private int quality;
    private String qualitys;
    private String title;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setQualitys(String qualitys2) {
        this.qualitys = qualitys2;
    }

    public String getQualitys() {
        return this.qualitys;
    }

    public void setQuality(int quality2) {
        this.quality = quality2;
    }

    public int getQuality() {
        return this.quality;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[RealUrlBean=" + super.toString())).append("\n\turl=").append(this.url).toString())).append("\n\ttitle=").append(this.title).toString())).append("\n\tquality=").append(this.quality).toString())).append("\n\tqualitys=").append(this.qualitys).toString())).append("]").toString();
    }
}
