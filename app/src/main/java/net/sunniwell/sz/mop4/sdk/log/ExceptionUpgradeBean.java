package net.sunniwell.sz.mop4.sdk.log;

import android.support.p000v4.p002os.EnvironmentCompat;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ExceptionUpgradeBean */
public class ExceptionUpgradeBean extends ExceptionBean {
    private String mNewVersion;
    private String mOldVersion;
    private String mUrl;

    public ExceptionUpgradeBean() {
        this.mOldVersion = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mNewVersion = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUrl = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mExtend = "upgrade";
    }

    public ExceptionUpgradeBean(String description, String oldVersion, String newVersion, String url) {
        super(description);
        this.mOldVersion = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mNewVersion = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUrl = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mExtend = "upgrade";
        this.mOldVersion = oldVersion;
        this.mNewVersion = newVersion;
        this.mUrl = url;
    }

    public String getOldVersion() {
        return this.mOldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.mOldVersion = oldVersion;
    }

    public String getNewVersion() {
        return this.mNewVersion;
    }

    public void setNewVersion(String newVersion) {
        this.mNewVersion = newVersion;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String toString() {
        this.mContent = String.format("{\"terminal_id\"=\"%s\",\"user_id\"=\"%s\",\"description\"=\"%s\",\"url\"=\"%s\",\"old_version\"=\"%s\",\"new_version\"=\"%s\"}", new Object[]{this.mTerminalId, this.mUserId, this.mDescription, this.mUrl, this.mOldVersion, this.mNewVersion});
        return super.toString();
    }
}
