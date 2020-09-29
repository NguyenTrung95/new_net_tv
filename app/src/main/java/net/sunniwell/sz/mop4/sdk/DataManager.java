package net.sunniwell.sz.mop4.sdk;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import net.sunniwell.download.manager.DownLoadConfigUtil;
import net.sunniwell.sz.mop4.sdk.area.AreaBean;
import net.sunniwell.sz.mop4.sdk.area.AreaManager;
import net.sunniwell.sz.mop4.sdk.category.CategoryBean;
import net.sunniwell.sz.mop4.sdk.category.CategoryManager;
import net.sunniwell.sz.mop4.sdk.column.ColumnBean;
import net.sunniwell.sz.mop4.sdk.column.ColumnManager;
import net.sunniwell.sz.mop4.sdk.epg.EPGBean;
import net.sunniwell.sz.mop4.sdk.epg.EPGManager;
import net.sunniwell.sz.mop4.sdk.log.LogBean1;
import net.sunniwell.sz.mop4.sdk.media.MediaBean;
import net.sunniwell.sz.mop4.sdk.media.MediaListBean;
import net.sunniwell.sz.mop4.sdk.media.MediaManager;
import net.sunniwell.sz.mop4.sdk.media.UrlBean;
import net.sunniwell.sz.mop4.sdk.ad.AdBean;
import net.sunniwell.sz.mop4.sdk.ad.AdManager;
import net.sunniwell.sz.mop4.sdk.param.Parameter;
import net.sunniwell.sz.mop4.sdk.realurl.RealUrlBean;
import net.sunniwell.sz.mop4.sdk.realurl.RealUrlManager;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient.AuthenResponse;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient.PayUrlResponse;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient.UserBeanResponse;
import net.sunniwell.sz.mop4.sdk.soap.SoapListener;
import net.sunniwell.sz.mop4.sdk.soap.SubscribeBean;
import net.sunniwell.sz.mop4.sdk.soap.UserBean;
import net.sunniwell.sz.mop4.sdk.sync.SyncManager;
import net.sunniwell.sz.mop4.sdk.util.StringUtil;
import org.apache.http.HttpStatus;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.DataManager */
public class DataManager {
    public static final int INIT_DONE = 0;
    public static final int INIT_ERROR = 1;
    private static final String TAG = "DataManager";
    /* access modifiers changed from: private */
    public static Handler mCallback = null;
    /* access modifiers changed from: private */
    public static Context mContext = null;
    /* access modifiers changed from: private */
    public static boolean mIsInit = false;
    private static SoapListener mSoapListener = new SoapListener() {
        public void onLoginSuccess() {
            if (!DataManager.mIsInit) {
                String path = StringUtil.getFilesDir();
                SyncManager.init(path);
                ColumnManager.init(path, Parameter.get("language"));
                CategoryManager.init(path, Parameter.get("language"));
                AdManager.init();
                MediaManager.init(path);
            }
            if (!DataManager.mIsInit && DataManager.mCallback != null) {
                DataManager.mCallback.sendEmptyMessage(0);
            }
            DataManager.mIsInit = true;
        }

        public void onLoginFailed(int code) {
            switch (code) {
                case 400:
                    Log.d(DataManager.TAG, "Illegal request!");
                    break;
                case HttpStatus.SC_PAYMENT_REQUIRED /*402*/:
                    Log.d(DataManager.TAG, "User not exsist!");
                    break;
                case HttpStatus.SC_FORBIDDEN /*403*/:
                    Log.d(DataManager.TAG, "User not enabled!");
                    break;
                case HttpStatus.SC_NOT_FOUND /*404*/:
                    Log.d(DataManager.TAG, "Username or password wrong!");
                    break;
                case HttpStatus.SC_METHOD_NOT_ALLOWED /*405*/:
                    Log.d(DataManager.TAG, "Illegal terminal");
                    SoapClient.enable(Parameter.get("terminal_id"), Parameter.get("mac"), XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, Parameter.get("soft_ver"), Parameter.get("hard_ver"), Parameter.get(LogBean1.TERMINAL_STATISTICS_CHANNEL));
                    return;
                case HttpStatus.SC_NOT_ACCEPTABLE /*406*/:
                    Log.d(DataManager.TAG, "Terminal not enabled!");
                    SoapClient.enable(Parameter.get("terminal_id"), Parameter.get("mac"), XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, Parameter.get("soft_ver"), Parameter.get("hard_ver"), Parameter.get(LogBean1.TERMINAL_STATISTICS_CHANNEL));
                    return;
                case HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED /*407*/:
                    Log.d(DataManager.TAG, "Out of soft terminal limit!");
                    break;
                case HttpStatus.SC_REQUEST_TIMEOUT /*408*/:
                    Log.d(DataManager.TAG, "Terminal out of date!");
                    break;
                case HttpStatus.SC_CONFLICT /*409*/:
                    Log.d(DataManager.TAG, "Invalid epg template!");
                    break;
                case HttpStatus.SC_GONE /*410*/:
                    Log.d(DataManager.TAG, "OIS overload!");
                    SoapClient.switchOIS();
                    return;
            }
            if (!DataManager.mIsInit && DataManager.mCallback != null) {
                DataManager.mCallback.sendEmptyMessage(1);
            }
        }

        public void onGetParam(String key) {
            Log.d(DataManager.TAG, "OnNmpListener onGetParam key=" + key);
            SoapClient.postParam(Parameter.get("terminal_id"), key, Parameter.get(key));
        }

        public void onGetAllParam() {
            Log.d(DataManager.TAG, "OnNmpListener onGetAllParam");
            SoapClient.postAllParam(Parameter.get("terminal_id"), Parameter.getAll(), 5000);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:32:0x00ba, code lost:
            if (r16.equals("protocol") != false) goto L_0x00bc;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onSetParam(java.lang.String r16, java.lang.String r17) {
            /*
                r15 = this;
                java.lang.String r12 = "DataManager"
                java.lang.StringBuilder r13 = new java.lang.StringBuilder
                java.lang.String r14 = "OnNmpListener onSetParam key="
                r13.<init>(r14)
                r0 = r16
                java.lang.StringBuilder r13 = r13.append(r0)
                java.lang.String r14 = " value="
                java.lang.StringBuilder r13 = r13.append(r14)
                r0 = r17
                java.lang.StringBuilder r13 = r13.append(r0)
                java.lang.String r13 = r13.toString()
                android.util.Log.d(r12, r13)
                if (r16 == 0) goto L_0x00d8
                java.lang.String r12 = ""
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00d8
                if (r17 == 0) goto L_0x00d8
                java.lang.String r12 = "user"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "password"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "is_login"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "auto_login"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "remember_password"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "language"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "project_id"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "authen_enable"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "ad_enable"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "upgrade_url"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "ois"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "epgs"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "epg"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 != 0) goto L_0x00bc
                java.lang.String r12 = "protocol"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 == 0) goto L_0x00bf
            L_0x00bc:
                net.sunniwell.p008sz.mop4.sdk.param.Parameter.set(r16, r17)
            L_0x00bf:
                java.lang.String r12 = "upgrade_url"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 == 0) goto L_0x00d9
                android.content.Context r12 = net.sunniwell.p008sz.mop4.sdk.DataManager.mContext
                android.content.ContentResolver r12 = r12.getContentResolver()
                java.lang.String r13 = "upgrade_url"
                r0 = r17
                android.provider.Settings.Secure.putString(r12, r13, r0)
            L_0x00d8:
                return
            L_0x00d9:
                java.lang.String r12 = "language"
                r0 = r16
                boolean r12 = r0.equals(r12)
                if (r12 == 0) goto L_0x00d8
                r9 = 0
                java.lang.String r5 = "cn"
                java.lang.String r12 = "zh"
                r0 = r17
                boolean r12 = r0.equals(r12)
                if (r12 == 0) goto L_0x0148
                java.lang.String r5 = "cn"
            L_0x00f2:
                java.util.Locale r9 = new java.util.Locale
                r0 = r17
                r9.<init>(r0, r5)
                java.lang.String r12 = "android.app.ActivityManagerNative"
                java.lang.Class r2 = java.lang.Class.forName(r12)     // Catch:{ Exception -> 0x0143 }
                java.lang.String r12 = "getDefault"
                r13 = 0
                java.lang.Class[] r13 = new java.lang.Class[r13]     // Catch:{ Exception -> 0x0143 }
                java.lang.reflect.Method r8 = r2.getDeclaredMethod(r12, r13)     // Catch:{ Exception -> 0x0143 }
                r12 = 0
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x0143 }
                java.lang.Object r1 = r8.invoke(r2, r12)     // Catch:{ Exception -> 0x0143 }
                java.lang.Class r12 = r1.getClass()     // Catch:{ Exception -> 0x0143 }
                java.lang.String r13 = "getConfiguration"
                r14 = 0
                java.lang.Class[] r14 = new java.lang.Class[r14]     // Catch:{ Exception -> 0x0143 }
                java.lang.reflect.Method r7 = r12.getDeclaredMethod(r13, r14)     // Catch:{ Exception -> 0x0143 }
                r12 = 0
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x0143 }
                java.lang.Object r4 = r7.invoke(r1, r12)     // Catch:{ Exception -> 0x0143 }
                android.content.res.Configuration r4 = (android.content.res.Configuration) r4     // Catch:{ Exception -> 0x0143 }
                java.lang.String r12 = "android.content.res.Configuration"
                java.lang.Class r3 = java.lang.Class.forName(r12)     // Catch:{ Exception -> 0x0143 }
                java.lang.String r12 = "locale"
                java.lang.reflect.Field r10 = r3.getDeclaredField(r12)     // Catch:{ Exception -> 0x0143 }
                r10.set(r4, r9)     // Catch:{ Exception -> 0x0143 }
                java.lang.String r12 = "userSetLocale"
                java.lang.reflect.Field r11 = r3.getDeclaredField(r12)     // Catch:{ Exception -> 0x0143 }
                r12 = 1
                java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)     // Catch:{ Exception -> 0x0143 }
                r11.set(r4, r12)     // Catch:{ Exception -> 0x0143 }
                goto L_0x00d8
            L_0x0143:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x00d8
            L_0x0148:
                java.lang.String r12 = "zh_hk"
                r0 = r17
                boolean r12 = r0.equals(r12)
                if (r12 == 0) goto L_0x0155
                java.lang.String r5 = "hk"
                goto L_0x00f2
            L_0x0155:
                java.lang.String r12 = "en"
                r0 = r17
                boolean r12 = r0.equals(r12)
                if (r12 == 0) goto L_0x00f2
                java.lang.String r5 = "us"
                goto L_0x00f2
            */
            throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.DataManager.C04871.onSetParam(java.lang.String, java.lang.String):void");
        }

        public void onSaveParam() {
            Log.d(DataManager.TAG, "OnNmpListener onSaveParam");
            Parameter.save();
        }

        public void onOpenUrl(String url) {
            Log.d(DataManager.TAG, "OnNmpListener onOpenUrl url=" + url);
            if (url != null) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                if (intent != null) {
                    try {
                        DataManager.mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Log.d(DataManager.TAG, "onItemClick ActivityNotFoundException intent = " + intent.toString());
                        e.printStackTrace();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }

        public void onAlertmsg(String title, String content) {
            Log.d(DataManager.TAG, "OnNmpListener onAlertmsg");
        }

        public void onMessage(String title, String content) {
            Log.d(DataManager.TAG, "OnNmpListener onMessage");
        }

        public void onAssignUser(String user, String password) {
            Parameter.set("user", user);
            Parameter.set("password", password);
            Parameter.save();
        }

        public void onMediaPlay(String url) {
            Log.d(DataManager.TAG, "OnNmpListener onMediaPlay url=" + url);
        }

        public void onMediaStop() {
            Log.d(DataManager.TAG, "OnNmpListener onMediaStop");
        }

        public void onUpgrade(String url) {
            Log.d(DataManager.TAG, "OnNmpListener onUpgrade url = " + url);
            if (url != null && url != XmlPullParser.NO_NAMESPACE) {
                String newUpgradeUrl = new StringBuilder(String.valueOf(url)).append("?serial=").append(Parameter.get("terminal_id")).append("&mac=").append(Parameter.get("mac")).append("&soft_ver=").append(Parameter.get("soft_ver")).append("&hard_ver=").append(Parameter.get("hard_ver")).toString();
                Intent intent = new Intent("net.sunniwell.action.CHECK_UPGRADE_ACTION");
                Bundle bundle = new Bundle();
                bundle.putString(DownLoadConfigUtil.KEY_URL, newUpgradeUrl);
                bundle.putInt("recycle", 0);
                bundle.putInt("ms", 0);
                intent.putExtras(bundle);
                DataManager.mContext.sendBroadcast(intent);
                Log.d(DataManager.TAG, "sendBroadcast net.sunniwell.action.CHECK_UPGRADE_ACTION url= " + newUpgradeUrl);
            }
        }

        public void onRestart() {
            Log.d(DataManager.TAG, "OnNmpListener onRestart");
        }

        public void onDelParam(String paramName) {
            Log.d(DataManager.TAG, "OnNmpListener onDelParam: " + paramName);
        }

        public void onStandby() {
            Log.d(DataManager.TAG, "OnNmpListener onStandby");
        }

        public void onWakeup() {
            Log.d(DataManager.TAG, "OnNmpListener onWakeup");
        }

        public void onShutDown() {
            Log.d(DataManager.TAG, "OnNmpListener onShutDown");
        }

        public void onMarquee(String title, String content, int fontSize, String fontColor, int speed, int loop, int duration) {
            Log.d(DataManager.TAG, "OnNmpListener onMarquee__" + title + "__" + content + "__" + speed + "__" + loop + "__" + duration + "__");
        }

        public void onEpgsChange() {
        }
    };

    public static void init(Context context, String terminal_id, Handler callback) {
        mContext = context;
        mCallback = callback;
        StringUtil.init(mContext);
        initParam(StringUtil.getFilesDir(), terminal_id);
        if (!SoapClient.isInit()) {
            SoapClient.init(Parameter.get("ois"), Parameter.get("epgs"), true, mSoapListener);
        }
        SoapClient.setSoapListener(mSoapListener);
        SoapClient.setAutoProcess(true);
    }

    private static void initParam(String path, String terminal_id) {
        Parameter.init(path);
        Parameter.set(LogBean1.TERMINAL_STATISTICS_CHANNEL, "VST");
        Parameter.set("terminal_id", terminal_id);
        try {
            String mac = ((WifiManager) mContext.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            if (mac != null) {
                Parameter.set("mac", mac);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Parameter.set("terminal_type", "1");
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            Parameter.set("app_versionCode", String.valueOf(packageInfo.versionCode));
            Parameter.set("app_verionName", String.valueOf(packageInfo.versionName));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String language = mContext.getResources().getConfiguration().locale.getLanguage();
        if (language.endsWith("zh-CN")) {
            Parameter.set("language", "zh");
        } else if (language.endsWith("zh-TW")) {
            Parameter.set("language", "zh_hk");
        } else if (language.endsWith("en")) {
            Parameter.set("language", "en");
        } else {
            Parameter.set("language", "zh");
        }
        Parameter.save();
    }

    public static void deInit() {
        mIsInit = false;
        SoapClient.setAutoProcess(false);
    }

    public static ArrayList<AdBean> getOnPlayAds(int columnId) {
        return AdManager.getOnPlayAds(columnId);
    }

    public static ArrayList<AdBean> getOnPauseAds(int columnId) {
        return AdManager.getOnPauseAds(columnId);
    }

    public static ArrayList<AdBean> getOnFieldAds(int columnId) {
        return AdManager.getLocationAds(columnId);
    }

    public static ArrayList<AreaBean> getArea() {
        return AreaManager.get(Parameter.get("language"));
    }

    public static ArrayList<CategoryBean> getCategoryById(int columnId) {
        return CategoryManager.get(columnId);
    }

    public static ArrayList<ColumnBean> getColumnByPid(int pid) {
        return ColumnManager.get(pid);
    }

    public static ArrayList<EPGBean> getEpg(String mediaId, long startUtc, long endUtc) {
        return EPGManager.get(mediaId, startUtc, endUtc, Parameter.get("language"));
    }

    public static MediaListBean getMediaList(int columnId, String meta, String category, String area, String tag, String year, String title, String pinyin, String actor, String director, String sort, int pageIndex, int pageSize) {
        return MediaManager.get(columnId, meta, category, area, tag, year, title, pinyin, actor, director, sort, pageIndex, pageSize, Parameter.get("language"));
    }

    public static MediaBean getMediaDetail(int columnId, String mediaId, int pageIndex, int pageSize, String provider) {
        return MediaManager.detail(columnId, mediaId, pageIndex, pageSize, provider, Parameter.get("language"));
    }

    public static List<MediaBean> getMediaRelates(int columnId, String mediaId, int size) {
        return MediaManager.getRelates(columnId, mediaId, size, Parameter.get("language"));
    }

    public static RealUrlBean getRealUrl(MediaBean mediaBean, UrlBean urlBean, int quality) {
        String ret;
        String ret2;
        if (mediaBean == null || urlBean == null) {
            return null;
        }
        RealUrlBean realUrlBean = new RealUrlBean();
        realUrlBean.setQuality(urlBean.getQuality());
        realUrlBean.setTitle(urlBean.getTitle());
        String ois = SoapClient.getOis();
        String sid = mediaBean.getId();
        String url = urlBean.getUrl();
        if (url.startsWith("mop://")) {
            String[] tmp = ois.split(":");
            if (tmp.length != 2) {
                return null;
            }
            ret = "http://" + tmp[0] + ":" + (Integer.parseInt(tmp[1]) - 1) + "/" + url.substring("mop://".length()) + ".m3u8";
        } else if (!urlBean.isIsfinal()) {
            return RealUrlManager.getRealUrl(url, quality);
        } else {
            ret = url;
        }
        if (ret.indexOf("?") > 0) {
            ret2 = new StringBuilder(String.valueOf(ret)).append("&").toString();
        } else {
            ret2 = new StringBuilder(String.valueOf(ret)).append("?").toString();
        }
        realUrlBean.setUrl(new StringBuilder(String.valueOf(ret2)).append("user=").append(Parameter.get("user")).append("&tid=").append(Parameter.get("terminal_id")).append("&sid=").append(sid).append("&type=stb&token=").append(SoapClient.getOisToken()).toString());
        return realUrlBean;
    }

    public static AuthenResponse authen(String mediaId) {
        return SoapClient.authen(Parameter.get("user"), Parameter.get("terminal_id"), mediaId);
    }

    public static UserBean getUserInfo() {
        UserBeanResponse ret = SoapClient.getUserInfo(Parameter.get("user"));
        if (ret == null) {
            return null;
        }
        return ret.bean;
    }

    public static int setUserInfo(String realname, String country, String email, String addr, String phone, String mobile, String postcode, String birthday) {
        return SoapClient.setUserInfo(Parameter.get("user"), realname, country, email, addr, phone, mobile, postcode, birthday);
    }

    public static String getTenBuyUrl(String mediaId, String description, boolean renew) {
        PayUrlResponse ret = SoapClient.getWAPTenPayBuyUrl(Parameter.get("user"), mediaId, renew, description);
        if (ret == null) {
            return null;
        }
        return ret.url;
    }

    public static String getTenRechargeUrl(int recharge, String description) {
        PayUrlResponse ret = SoapClient.getWAPTenPayRechargeUrl(Parameter.get("user"), recharge, description);
        if (ret == null) {
            return null;
        }
        return ret.url;
    }

    public static String getPaypalBuyUrl(String mediaId, String description, boolean renew) {
        PayUrlResponse ret = SoapClient.getPayPalPayBuyUrl(Parameter.get("user"), mediaId, renew, description);
        if (ret == null) {
            return null;
        }
        return ret.url;
    }

    public static String getPaypalRechargeUrl(int recharge, String description) {
        PayUrlResponse ret = SoapClient.getPaypalRechargeUrl(Parameter.get("user"), recharge, description);
        if (ret == null) {
            return null;
        }
        return ret.url;
    }

    public static ArrayList<SubscribeBean> getSubscribeList(int type) {
        return SoapClient.getSubscribeList(Parameter.get("user"), type);
    }
}
