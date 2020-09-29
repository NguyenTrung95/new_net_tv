package net.sunniwell.sz.mop4.sdk.log;

import android.support.p000v4.p002os.EnvironmentCompat;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ExceptionPlayBean */
public class ExceptionPlayBean extends ExceptionBean {
    private String mMediaId;
    private String mTitle;
    private String mUrl;

    public ExceptionPlayBean() {
        this.mMediaId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mTitle = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUrl = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mExtend = "play";
    }

    public ExceptionPlayBean(String description, String mediaId, String title, String url) {
        super(description);
        this.mMediaId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mTitle = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUrl = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mExtend = "play";
        this.mMediaId = mediaId;
        this.mTitle = title;
        this.mUrl = url;
    }

    public String getMediaId() {
        return this.mMediaId;
    }

    public void setMediaId(String mediaId) {
        this.mMediaId = mediaId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String toString() {
        this.mContent = String.format("{\"terminal_id\"=\"%s\",\"user_id\"=\"%s\",\"description\"=\"%s\",\"media_id\"=\"%s\",\"title\"=\"%s\",\"url\"=\"%s\"}", new Object[]{this.mTerminalId, this.mUserId, this.mDescription, this.mMediaId, this.mTitle, this.mUrl});
        return super.toString();
    }
}
