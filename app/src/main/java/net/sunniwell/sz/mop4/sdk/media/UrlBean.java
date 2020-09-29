package net.sunniwell.sz.mop4.sdk.media;

import java.io.Serializable;

/* renamed from: net.sunniwell.sz.mop4.sdk.media.UrlBean */
public class UrlBean implements Serializable {
    public static final int QUALITY_720P = 5;
    public static final int QUALITY_HIGH = 3;
    public static final int QUALITY_LOW = 1;
    public static final int QUALITY_STANDARD = 2;
    public static final int QUALITY_SUPER = 4;
    public static final int QUALITY_UNKNOWN = 0;
    private static final long serialVersionUID = 1;
    private String description;
    private String image;
    private boolean isfinal;
    private String provider;
    private int quality = 3;
    private int serial;
    private String thumbnail;
    private String title;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public int getSerial() {
        return this.serial;
    }

    public void setSerial(int serial2) {
        this.serial = serial2;
    }

    public boolean isIsfinal() {
        return this.isfinal;
    }

    public void setIsfinal(boolean isfinal2) {
        this.isfinal = isfinal2;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProvider(String provider2) {
        this.provider = provider2;
    }

    public int getQuality() {
        return this.quality;
    }

    public void setQuality(int quality2) {
        this.quality = quality2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setImage(String image2) {
        this.image = image2;
    }

    public String getImage() {
        return this.image;
    }

    public void setThumbnail(String thumbnail2) {
        this.thumbnail = thumbnail2;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[UrlBean=" + super.toString())).append("\n\turl=").append(this.url).toString())).append("\n\tserial=").append(this.serial).toString())).append("\n\tisfinal=").append(this.isfinal).toString())).append("\n\tprovider=").append(this.provider).toString())).append("\n\tquality=").append(this.quality).toString())).append("\n\ttitle=").append(this.title).toString())).append("\n\tdescription=").append(this.description).toString())).append("\n\timage=").append(this.image).toString())).append("\n\tthumbnail=").append(this.thumbnail).toString())).append("]").toString();
    }
}
