package net.sunniwell.sz.mop4.sdk.epg;

import java.io.Serializable;
import java.util.ArrayList;

/* renamed from: net.sunniwell.sz.mop4.sdk.epg.EPGBean */
public class EPGBean implements Serializable {
    private static final long serialVersionUID = 1;
    private String description;
    private long endUtcMs;
    private String title;
    private String type;
    private ArrayList<EPGUrlBean> urls;
    private long utcMs;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public long getUtcMs() {
        return this.utcMs;
    }

    public void setUtcMs(long startUtcMs) {
        this.utcMs = startUtcMs;
    }

    public void setEndUtcMs(long endUtcMs2) {
        this.endUtcMs = endUtcMs2;
    }

    public long getEndUtcMs() {
        return this.endUtcMs;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public ArrayList<EPGUrlBean> getUrls() {
        return this.urls;
    }

    public void setUrls(ArrayList<EPGUrlBean> urls2) {
        this.urls = urls2;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[EPGBean=" + super.toString())).append("\n\ttitle=").append(this.title).toString())).append("\n\ttype=").append(this.type).toString())).append("\n\tdescription=").append(this.description).toString())).append("\n\tutcMs=").append(this.utcMs).toString())).append("\n\tendUtcMs=").append(this.endUtcMs).toString())).append("\n\turls=").append(this.urls).toString())).append("]").toString();
    }
}
