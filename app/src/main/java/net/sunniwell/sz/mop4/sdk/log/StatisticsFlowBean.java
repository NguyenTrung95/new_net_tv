package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.StatisticsFlowBean */
public class StatisticsFlowBean extends StatisticsBean {
    private long mDownload;
    private long mUpload;

    public StatisticsFlowBean() {
        this.mUpload = 0;
        this.mDownload = 0;
        this.mExtend = LogBean1.TERMINAL_STATISTICS_FLOW;
    }

    public StatisticsFlowBean(long upload, long download, long startUtcMs, long endUtcMs) {
        super(startUtcMs, endUtcMs);
        this.mUpload = 0;
        this.mDownload = 0;
        this.mExtend = LogBean1.TERMINAL_STATISTICS_FLOW;
        this.mUpload = upload;
        this.mDownload = download;
    }

    public long getUpload() {
        return this.mUpload;
    }

    public void setUpload(long upload) {
        this.mUpload = upload;
    }

    public long getDownload() {
        return this.mDownload;
    }

    public void setDownload(long download) {
        this.mDownload = download;
    }

    public String toString() {
        this.mContent = String.format("{\"terminal_id\"=\"%s\",\"user_id\"=\"%s\",\"up_flow\"=\"%s\",\"down_flow\"=\"%s\",\"start_utc\"=\"%s\",\"end_utc\"=\"%s\"}", new Object[]{this.mTerminalId, this.mUserId, Long.valueOf(this.mUpload), Long.valueOf(this.mDownload), Long.valueOf(this.mStartUtcMs), Long.valueOf(this.mEndUtcMs)});
        return super.toString();
    }
}
