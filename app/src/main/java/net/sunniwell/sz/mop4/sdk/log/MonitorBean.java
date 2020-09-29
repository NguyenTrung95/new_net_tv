package net.sunniwell.sz.mop4.sdk.log;

import android.support.p000v4.p002os.EnvironmentCompat;
import net.sunniwell.sz.mop4.sdk.param.Parameter;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.MonitorBean */
public class MonitorBean extends LogBean1 {
    protected String mTerminalId;
    protected String mUserId;

    MonitorBean() {
        this.mTerminalId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUserId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mType = 1;
        this.mSubType = 8;
        this.mTerminalId = Parameter.get("terminal_id");
        this.mUserId = Parameter.get("user");
    }
}
