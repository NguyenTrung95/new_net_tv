package net.sunniwell.aidl.bean;

import java.io.Serializable;

public class AdBean implements Serializable {
    public static final int META_IMAGE = 2;
    public static final int META_TEXT = 3;
    public static final int META_VIDEO = 1;
    public static final int TYPE_ONFIELD = 4;
    public static final int TYPE_ONPAUSE = 2;
    public static final int TYPE_ONPLAY = 1;
    public static final int TYPE_ONPLAYPAUSE = 3;
    private static final long serialVersionUID = 1;
    private String content;
    private int duration;
    private String extend;

    /* renamed from: id */
    private int f297id;
    private int meta;
    private String title;
    private int type;

    public int getId() {
        return this.f297id;
    }

    public void setId(int id) {
        this.f297id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public int getMeta() {
        return this.meta;
    }

    public void setMeta(int meta2) {
        this.meta = meta2;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content2) {
        this.content = content2;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration2) {
        this.duration = duration2;
    }

    public String getExtend() {
        return this.extend;
    }

    public void setExtend(String extend2) {
        this.extend = extend2;
    }
}
