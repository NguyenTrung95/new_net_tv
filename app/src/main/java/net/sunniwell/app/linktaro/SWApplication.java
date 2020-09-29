package net.sunniwell.app.linktaro;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.download.common.BaseApplication;
import net.sunniwell.sz.mop4.sdk.epg.EPGDataUtil;

public class SWApplication extends BaseApplication {
    private static final String FIRST_START = "first_start";
    private static final int FIRST_START_STATUS = -1;
    private static final String FIRST_STATUS = "first_status";
    public static SWApplication INSTANCE;
    private static final int START_STATUS = 0;
    public static Context mContext;
    public static MailDbHelper mMailHelper;
    private boolean isHotKey;
    private Editor mEditor;
    public boolean mLogined = false;
    private SharedPreferences mPrefence;

    public void onCreate() {
        super.onCreate();
        EPGDataUtil.initHttpClient();
        INSTANCE = this;
        mContext = getApplicationContext();
        mMailHelper = new MailDbHelper(this, null, null, 1);
        this.mPrefence = getSharedPreferences(FIRST_START, 0);
        this.mEditor = this.mPrefence.edit();
        SWSysProp.init(this);
        initData();
    }

    public void initData() {
        if (-1 == this.mPrefence.getInt(FIRST_STATUS, -1)) {
            SWSysProp.setStringParam("transport_way", "transport_hls");
            SWSysProp.setIntParam("play_quality", 3);
            this.mEditor.putInt(FIRST_STATUS, 0);
            this.mEditor.apply();
        } else if (getVersionCode() == 14) {
            if (TextUtils.equals("transport_p2p", SWSysProp.getStringParam("transport_way", "transport_p2p"))) {
                SWSysProp.setStringParam("transport_way", "transport_hls");
            }
        }
    }

    public int getVersionCode() {
        int versionCode = 0;
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return versionCode;
        }
    }

    public boolean isHotKey() {
        return this.isHotKey;
    }

    public void setHotKey(boolean isHotKey2) {
        this.isHotKey = isHotKey2;
    }
}
