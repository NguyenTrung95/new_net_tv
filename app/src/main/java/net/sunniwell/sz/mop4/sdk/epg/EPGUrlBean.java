package net.sunniwell.sz.mop4.sdk.epg;

/* renamed from: net.sunniwell.sz.mop4.sdk.epg.EPGUrlBean */
public class EPGUrlBean {
    private long duration;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration2) {
        this.duration = duration2;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[EPGUrlBean=" + super.toString())).append("\n\turl=").append(this.url).toString())).append("\n\tduration=").append(this.duration).toString())).append("]").toString();
    }
}
