package net.sunniwell.aidl.bean;

import java.io.Serializable;
import java.util.List;

public class EPGBean implements Serializable {
    private static final long serialVersionUID = 1;
    private String channelId;
    private String description;
    private long endUtc;
    private String title;
    private String type;
    private List<EPGUrlBean> urls;
    private long utc;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public long getUtc() {
        return this.utc;
    }

    public void setUtc(long time) {
        this.utc = time;
    }

    public String toString() {
        return "programName = " + this.title + " utc = " + this.utc;
    }

    public void setEndUtc(long endUtc2) {
        this.endUtc = endUtc2;
    }

    public long getEndUtc() {
        return this.endUtc;
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

    public List<EPGUrlBean> getUrls() {
        return this.urls;
    }

    public void setUrls(List<EPGUrlBean> urls2) {
        this.urls = urls2;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId2) {
        this.channelId = channelId2;
    }
}
