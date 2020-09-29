package net.sunniwell.sz.mop4.sdk.ad;

import java.io.Serializable;

/* renamed from: net.sunniwell.sz.mop4.sdk.ad.AdBean */
public class AdBean implements Serializable {
    public static final int META_IMAGE = 2;
    public static final int META_TEXT = 3;
    public static final int META_VIDEO = 1;
    public static final int TYPE_COMMON = 5;
    public static final int TYPE_LOCATION = 6;
    public static final int TYPE_ONEND = 2;
    public static final int TYPE_ONPAUSE = 4;
    public static final int TYPE_ONPLAY = 3;
    public static final int TYPE_ONSTART = 1;
    private static final long serialVersionUID = 11111;
    private String content;
    private int duration;
    private String extend;

    /* renamed from: id */
    private int f335id;
    private int meta;
    private String title;
    private int type;

    public int getId() {
        return this.f335id;
    }

    public void setId(int id) {
        this.f335id = id;
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

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[AdBean=" + super.toString())).append("\n\tid=").append(this.f335id).toString())).append("\n\ttitle=").append(this.title).toString())).append("\n\ttype=").append(this.type).toString())).append("\n\tmeta=").append(this.meta).toString())).append("\n\tcontent=").append(this.content).toString())).append("\n\tduration=").append(this.duration).toString())).append("\n\textend=").append(this.extend).toString())).append("]").toString();
    }
}
