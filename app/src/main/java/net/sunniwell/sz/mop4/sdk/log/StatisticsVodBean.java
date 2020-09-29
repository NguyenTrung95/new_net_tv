package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.StatisticsVodBean */
public class StatisticsVodBean extends StatisticsChannelBean {
    private int mWatchPercent;
    private long mWatchTotalS;

    public StatisticsVodBean() {
        this.mWatchTotalS = 0;
        this.mWatchPercent = 0;
        this.mExtend = LogBean1.TERMINAL_STATISTICS_VOD;
    }

    public StatisticsVodBean(String mediaId, String title, String url, long watchS, long watchTotalS, int wathcPercent, long startUtcMs, long endUtcMs) {
        super(mediaId, title, url, watchS, startUtcMs, endUtcMs);
        this.mWatchTotalS = 0;
        this.mWatchPercent = 0;
        this.mExtend = LogBean1.TERMINAL_STATISTICS_VOD;
        this.mWatchTotalS = watchTotalS;
        this.mWatchPercent = wathcPercent;
    }

    public long getWatchTotalS() {
        return this.mWatchTotalS;
    }

    public void setWatchTotalS(long watchTotalS) {
        this.mWatchTotalS = watchTotalS;
    }

    public int getWatchPercent() {
        return this.mWatchPercent;
    }

    public void setWatchPercent(int watchPercent) {
        this.mWatchPercent = watchPercent;
    }

    public String toString() {
        this.mContent = String.format("{\"terminal_id\"=\"%s\",\"user_id\"=\"%s\",\"media_id\"=\"%s\",\"title\"=\"%s\",\"url\"=\"%s\",\"duration_watch\"=\"%s\",\"duration_total\"=\"%s\",\"duration_percent\"=\"%s\",\"start_utc\"=\"%s\",\"end_utc\"=\"%s\"}", new Object[]{this.mTerminalId, this.mUserId, this.mMediaId, this.mTitle, this.mUrl, Long.valueOf(this.mWatchedS), Long.valueOf(this.mWatchTotalS), Integer.valueOf(this.mWatchPercent), Long.valueOf(this.mStartUtcMs), Long.valueOf(this.mEndUtcMs)});
        return super.toString();
    }
}
