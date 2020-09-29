package net.sunniwell.sz.mop4.sdk.log;

import android.support.p000v4.p002os.EnvironmentCompat;
import net.sunniwell.sz.mop4.sdk.param.Parameter;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.StatisticsBean */
public class StatisticsBean extends LogBean1 {
    protected long mEndUtcMs;
    protected long mStartUtcMs;
    protected String mTerminalId;
    protected String mUserId;

    StatisticsBean() {
        this.mTerminalId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUserId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mStartUtcMs = 0;
        this.mEndUtcMs = 0;
        this.mType = 1;
        this.mSubType = 7;
        this.mTerminalId = Parameter.get("terminal_id");
        this.mUserId = Parameter.get("user");
    }

    StatisticsBean(long startUtcMs, long endUtcMs) {
        this.mTerminalId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUserId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mStartUtcMs = 0;
        this.mEndUtcMs = 0;
        this.mType = 1;
        this.mSubType = 7;
        this.mTerminalId = Parameter.get("terminal_id");
        this.mUserId = Parameter.get("user");
        this.mStartUtcMs = startUtcMs;
        this.mEndUtcMs = endUtcMs;
    }
}
