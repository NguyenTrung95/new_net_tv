package net.sunniwell.sz.mop4.sdk.log;

import android.support.p000v4.p002os.EnvironmentCompat;
import net.sunniwell.sz.mop4.sdk.soap.SoapBase;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean1 */
public class LogBean1 {
    public static final int LOG_ACTION = 4;
    public static final int LOG_ERROR = 3;
    public static final int LOG_EXCEPTION = 2;
    public static final int LOG_MONITOR = 8;
    public static final int LOG_NORMAL = 1;
    public static final int LOG_STATISTICS = 7;
    public static final int LOG_WARNING = 9;
    public static final String TERMINAL_ACTION_FAVORITE = "favorite";
    public static final String TERMINAL_ACTION_INSTALL = "install";
    public static final String TERMINAL_ACTION_LOGIN = "login";
    public static final String TERMINAL_ACTION_LOGOUT = "logout";
    public static final String TERMINAL_ACTION_PAUSE = "pause";
    public static final String TERMINAL_ACTION_PLAY = "play";
    public static final String TERMINAL_ACTION_REBOOT = "reboot";
    public static final String TERMINAL_ACTION_STANDBY = "standby";
    public static final String TERMINAL_ACTION_STOP = "stop";
    public static final String TERMINAL_ACTION_UPGRADE = "upgrade";
    public static final String TERMINAL_EXCEPTION_FILE = "file";
    public static final String TERMINAL_EXCEPTION_INSTALL = "install";
    public static final String TERMINAL_EXCEPTION_PLAY = "play";
    public static final String TERMINAL_EXCEPTION_UPGRADE = "upgrade";
    public static final String TERMINAL_MONITOR_SYSTEM = "system";
    public static final String TERMINAL_STATISTICS_AD = "ad";
    public static final String TERMINAL_STATISTICS_CHANNEL = "channel";
    public static final String TERMINAL_STATISTICS_FLOW = "flow";
    public static final String TERMINAL_STATISTICS_VOD = "vod";
    public static final int TYPE_SERVER = 2;
    public static final int TYPE_TERMINAL = 1;
    protected String mContent = EnvironmentCompat.MEDIA_UNKNOWN;
    protected String mExtend = EnvironmentCompat.MEDIA_UNKNOWN;
    protected int mSubType = 999;
    protected int mType = 1;

    LogBean1() {
    }

    LogBean1(int type) {
        this.mType = type;
    }

    LogBean1(int type, int subtype) {
        this.mType = type;
        this.mSubType = subtype;
    }

    LogBean1(int type, int subtype, String extend) {
        this.mType = type;
        this.mSubType = subtype;
        this.mExtend = extend;
    }

    LogBean1(int type, int subtype, String extend, String content) {
        this.mType = type;
        this.mSubType = subtype;
        this.mExtend = extend;
        this.mContent = content;
    }

    public String toString() {
        return SoapBase.getLogEntity(String.format("\t\t<log type=\"%s\" subtype=\"%s\" extend=\"%s\" content=\"%s\" />", new Object[]{Integer.valueOf(this.mType), Integer.valueOf(this.mSubType), this.mExtend, this.mContent}));
    }
}
