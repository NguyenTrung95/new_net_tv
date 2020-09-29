package net.sunniwell.sz.mop4.sdk.log;

import android.support.p000v4.p002os.EnvironmentCompat;
import net.sunniwell.sz.mop4.sdk.param.Parameter;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ExceptionBean */
public class ExceptionBean extends LogBean1 {
    protected String mDescription;
    protected String mTerminalId;
    protected String mUserId;

    ExceptionBean() {
        this.mTerminalId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUserId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mDescription = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mType = 1;
        this.mSubType = 2;
        this.mTerminalId = Parameter.get("terminal_id");
        this.mUserId = Parameter.get("user");
    }

    ExceptionBean(String description) {
        this.mTerminalId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mUserId = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mDescription = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mType = 1;
        this.mSubType = 2;
        this.mTerminalId = Parameter.get("terminal_id");
        this.mUserId = Parameter.get("user");
        this.mDescription = description;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
