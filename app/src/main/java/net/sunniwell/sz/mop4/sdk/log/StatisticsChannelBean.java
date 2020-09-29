package net.sunniwell.sz.mop4.sdk.log;

import android.support.p000v4.p002os.EnvironmentCompat;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.StatisticsChannelBean */
public class StatisticsChannelBean extends StatisticsBean {
    protected String mMediaId;
    protected String mTitle;
    protected String mUrl;
    protected long mWatchedS;

    public StatisticsChannelBean() {
        this.mMediaId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mTitle = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUrl = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mWatchedS = 0;
        this.mExtend = LogBean1.TERMINAL_STATISTICS_CHANNEL;
    }

    public StatisticsChannelBean(String mediaId, String title, String url, long watchS, long startUtcMs, long endUtcMs) {
        super(startUtcMs, endUtcMs);
        this.mMediaId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mTitle = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUrl = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mWatchedS = 0;
        this.mExtend = LogBean1.TERMINAL_STATISTICS_CHANNEL;
        this.mMediaId = mediaId;
        this.mTitle = title;
        this.mUrl = url;
        this.mWatchedS = watchS;
    }

    public String getediaId() {
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

    public long getWatchedS() {
        return this.mWatchedS;
    }

    public void setWatchedS(long watchedS) {
        this.mWatchedS = watchedS;
    }

    public String toString() {
        this.mContent = String.format("{\"terminal_id\"=\"%s\",\"user_id\"=\"%s\",\"media_id\"=\"%s\",\"title\"=\"%s\",\"url\"=\"%s\",\"duration_watch\"=\"%s\",\"start_utc\"=\"%s\",\"end_utc\"=\"%s\"}", new Object[]{this.mTerminalId, this.mUserId, this.mMediaId, this.mTitle, this.mUrl, Long.valueOf(this.mWatchedS), Long.valueOf(this.mStartUtcMs), Long.valueOf(this.mEndUtcMs)});
        return super.toString();
    }
}
