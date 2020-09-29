package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.MonitorSystemBean */
public class MonitorSystemBean extends MonitorBean {
    private int mCpu;
    private int mDiskFree;
    private int mDiskTotal;
    private int mDiskUsed;
    private int mDownloadKb;
    private int mIO;
    private int mMemory;
    private int mMemoryFree;
    private int mMemoryTotal;
    private int mMemoryUsed;
    private int mUploadKb;

    public MonitorSystemBean() {
        this.mCpu = 0;
        this.mMemory = 0;
        this.mMemoryTotal = 0;
        this.mMemoryUsed = 0;
        this.mMemoryFree = 0;
        this.mUploadKb = 0;
        this.mDownloadKb = 0;
        this.mDiskTotal = 0;
        this.mDiskUsed = 0;
        this.mDiskFree = 0;
        this.mIO = 0;
        this.mExtend = LogBean1.TERMINAL_MONITOR_SYSTEM;
    }

    public String toString() {
        this.mContent = String.format("{\"terminal_id\"=\"%s\",\"user_id\"=\"%s\",\"cpu\"=\"%s\",\"mem\"=\"%s\",\"mem_total\"=\"%s\",\"mem_used\"=\"%s\",\"mem_free\"=\"%s\",\"upkb\"=\"%s\",\"downkb\"=\"%s\",\"disk_total\"=\"%s\",\"disk_used\"=\"%s\",\"disk_free\"=\"%s\",\"io\"=\"%s\"}", new Object[]{this.mTerminalId, this.mUserId, Integer.valueOf(this.mCpu), Integer.valueOf(this.mMemory), Integer.valueOf(this.mMemoryTotal), Integer.valueOf(this.mMemoryUsed), Integer.valueOf(this.mMemoryFree), Integer.valueOf(this.mUploadKb), Integer.valueOf(this.mDownloadKb), Integer.valueOf(this.mDiskTotal), Integer.valueOf(this.mDiskUsed), Integer.valueOf(this.mDiskFree), Integer.valueOf(this.mIO)});
        return super.toString();
    }
}
