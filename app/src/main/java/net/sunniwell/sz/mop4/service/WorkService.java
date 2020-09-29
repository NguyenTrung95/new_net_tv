package net.sunniwell.sz.mop4.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import net.sunniwell.aidl.ISDKManagerService.Stub;
import net.sunniwell.app.linktaro.launcher.constans.Constans;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.PageEpgConstants;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.download.manager.DownLoadConfigUtil;
import net.sunniwell.sz.mop4.sdk.area.AreaBean;
import net.sunniwell.sz.mop4.sdk.area.AreaDataUtil;
import net.sunniwell.sz.mop4.sdk.category.CategoryBean;
import net.sunniwell.sz.mop4.sdk.category.CategoryDataUtil;
import net.sunniwell.sz.mop4.sdk.epg.EPGBean;
import net.sunniwell.sz.mop4.sdk.epg.EPGDataUtil;
import net.sunniwell.sz.mop4.sdk.log.LogBean1;
import net.sunniwell.sz.mop4.sdk.media.MediaBean;
import net.sunniwell.sz.mop4.sdk.media.MediaDataUtil;
import net.sunniwell.sz.mop4.sdk.media.MediaListBean;
import net.sunniwell.sz.mop4.sdk.ad.AdBean;
import net.sunniwell.sz.mop4.sdk.ad.AdDataUtil;
import net.sunniwell.sz.mop4.sdk.param.DefaultParam;
import net.sunniwell.sz.mop4.sdk.param.Parameter;
import net.sunniwell.sz.mop4.sdk.realurl.RealUrlBean;
import net.sunniwell.sz.mop4.sdk.realurl.RealUrlDataUtil;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient.SoapResponse;
import net.sunniwell.sz.mop4.sdk.soap.SoapListener;
import net.sunniwell.sz.mop4.sdk.soap.SubscribeBean;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.p019v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.service.WorkService */
public class WorkService extends Service {
    private static final int CMD_ENABLE = 2;
    private static final int CMD_LOGIN = 1;
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(WorkService.class);
    private boolean isFactory = true;
    private HandlerThread mHandlerThread;
    private SDKCallBack mSDKCallBack;
    /* access modifiers changed from: private */
    public WorkHandler mWorkHandler;

    /* renamed from: net.sunniwell.sz.mop4.service.WorkService$SDKCallBack */
    private class SDKCallBack implements SoapListener {
        private SDKCallBack() {
        }

        /* synthetic */ SDKCallBack(WorkService workService, SDKCallBack sDKCallBack) {
            this();
        }

        public void onEpgsChange() {
            WorkService.LOG.mo8825d("onEpgsChange------->");
        }

        public void onLoginSuccess() {
            WorkService.LOG.mo8825d("onLoginSuccess------------------------------------------>");
            Intent intent = new Intent(Constans.LOGIN_SUCEESS);
            String user = Parameter.get("user");
            String password = Parameter.get("password");
            intent.putExtra("user", user);
            intent.putExtra("password", password);
            intent.putExtra("oisUtc", SoapClient.getOisUtcMs());
            WorkService.this.sendBroadcast(intent);
            String ois = Parameter.get("ois");
            String epgs = Parameter.get("epgs");
            WorkService.LOG.mo8825d("ois------->" + ois + "--------epgs----->" + epgs);
            Secure.putString(WorkService.this.getContentResolver(), "ois", ois);
            Secure.putString(WorkService.this.getContentResolver(), "epgs", epgs);
        }

        public void onLoginFailed(int code) {
            WorkService.LOG.mo8825d("onLoginFailed------->" + code);
            if (code == 405 || code == 406) {
                WorkService.this.mWorkHandler.sendEmptyMessage(2);
            }
            Intent intent = new Intent(Constans.LOGIN_FAILED);
            intent.putExtra("error_code", code);
            WorkService.this.sendBroadcast(intent);
        }

        public void onGetParam(String key) {
            WorkService.LOG.mo8825d("onGetParam------->" + key);
        }

        public void onGetAllParam() {
            WorkService.LOG.mo8825d("onGetAllParam------->");
        }

        public void onSetParam(String key, String value) {
            WorkService.LOG.mo8825d("onSetParam------key->" + key + "------value---->" + value);
        }

        public void onSaveParam() {
            WorkService.LOG.mo8825d("onSaveParam------->");
        }

        public void onOpenUrl(String url) {
            WorkService.LOG.mo8825d("onOpenUrl------->" + url);
        }

        public void onMarquee(String title, String content, int fontSize, String fontColor, int speed, int loop, int duration) {
            WorkService.LOG.mo8825d("onMarquee------->");
            WorkService.LOG.mo8825d("title----->" + title + "-----content----->" + content + "------fontSize-->" + fontSize + "----fontColor---->" + fontColor + "----speed----->" + speed + "----loop--->" + loop + "-----duration---->" + duration);
            Intent intent = new Intent();
            intent.putExtra("text", content);
            intent.putExtra("point_x", HttpStatus.SC_PAYMENT_REQUIRED);
            intent.putExtra("point_y", 655);
            intent.putExtra("width", 880);
            intent.putExtra("hight", 55);
            intent.putExtra("speed", speed);
            intent.putExtra("loop_time", loop);
            intent.putExtra("font_color", fontColor);
            intent.putExtra("font_size", fontSize);
            intent.putExtra("padding_left", 60);
            intent.putExtra(MailDbHelper.FLAG, "show_marquee_play");
            intent.setAction(Constans.TEXT_MESSAGE);
            WorkService.this.sendBroadcast(intent);
        }

        public void onAlertmsg(String title, String content) {
            WorkService.LOG.mo8825d("onAlertmsg------title->" + title + "-------content--->" + content);
        }

        public void onMessage(String title, String content) {
            WorkService.LOG.mo8825d("onMessage------title->" + title + "-------content--->" + content);
            Intent intent = new Intent(Constans.INCOMING_MESSAGE);
            intent.putExtra("title", title);
            intent.putExtra(MailDbHelper.CONTENT, content);
            WorkService.this.sendBroadcast(intent);
        }

        public void onAssignUser(String user, String password) {
            WorkService.LOG.mo8825d("onAssignUser------user->" + user + "-----password------>" + password);
            Parameter.set("user", user);
            Parameter.set("password", password);
            Parameter.save();
            Intent intent = new Intent(Constans.ASSIGN_USER);
            intent.putExtra("user", user);
            intent.putExtra("password", password);
            WorkService.this.sendBroadcast(intent);
        }

        public void onMediaPlay(String url) {
            WorkService.LOG.mo8825d("onMediaPlay------->" + url);
        }

        public void onMediaStop() {
            WorkService.LOG.mo8825d("onMediaStop------->");
        }

        public void onUpgrade(String url) {
            WorkService.LOG.mo8825d("onUpgrade------->" + url);
            Intent intent = new Intent(Constans.UPGRADE_SOFTWARE);
            intent.putExtra(DownLoadConfigUtil.KEY_URL, url);
            WorkService.this.sendBroadcast(intent);
        }

        public void onRestart() {
            WorkService.LOG.mo8825d("onRestart------->");
        }

        public void onDelParam(String paramName) {
            WorkService.LOG.mo8825d("onDelParam------->" + paramName);
        }

        public void onStandby() {
            WorkService.LOG.mo8825d("onStandby------->");
        }

        public void onWakeup() {
            WorkService.LOG.mo8825d("onWakeup------->");
        }

        public void onShutDown() {
            WorkService.LOG.mo8825d("onShutDown------->");
        }
    }

    /* renamed from: net.sunniwell.sz.mop4.service.WorkService$SDKManager */
    private class SDKManager extends Stub {
        private SDKManager() {
        }

        /* synthetic */ SDKManager(WorkService workService, SDKManager sDKManager) {
            this();
        }

        public String getCurEpgUrl() throws RemoteException {
            WorkService.LOG.mo8825d("getCurEpgUrl------>");
            return SoapClient.getEpgs();
        }

        public String getCurOisUrl() throws RemoteException {
            WorkService.LOG.mo8825d("getCurOisUrl------>");
            return SoapClient.getOis();
        }

        public int enable() throws RemoteException {
            WorkService.LOG.mo8825d("enable------->");
            return SoapClient.enable(Parameter.get("terminal_id"), Parameter.get("mac"), "sunniwell", "888888", Parameter.get("soft_ver"), Parameter.get("hard_ver"), Parameter.get(LogBean1.TERMINAL_STATISTICS_CHANNEL));
        }

        public String getAdData(int type, String epg) throws RemoteException {
            WorkService.LOG.mo8825d("getAllAdData------->" + type);
            Parameter.set("epg", epg);
            Parameter.save();
            ArrayList<AdBean> allAdList = AdDataUtil.get(type);
            Comparator<? super AdBean> comparator = new Comparator<AdBean>() {
                public int compare(AdBean adL, AdBean adR) {
                    if (!StringUtils.isNumberStr(adL.getExtend()) || !StringUtils.isNumberStr(adR.getExtend())) {
                        return 0;
                    }
                    return Integer.parseInt(adL.getExtend()) - Integer.parseInt(adR.getExtend());
                }
            };
            if (allAdList != null && allAdList.size() > 0) {
                try {
                    Collections.sort(allAdList, comparator);
                    JSONArray arr = new JSONArray();
                    for (int i = 0; i < allAdList.size(); i++) {
                        AdBean adBean = (AdBean) allAdList.get(i);
                        WorkService.LOG.mo8825d("adBean------>" + adBean.getContent());
                        arr.put(WorkService.this.parseToJsonObj(adBean));
                    }
                    WorkService.LOG.mo8825d("arr.toString()------>" + arr.toString());
                    return arr.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public String getMediaList(int columnId, int meta, String category, String area, String tag, String year, String title, String pinyin, String actor, String director, String sort, int pageindex, int pagesize, String lang, String epg) throws RemoteException {
            WorkService.LOG.mo8825d("getLiveList------->" + columnId);
            Parameter.set("epg", epg);
            Parameter.save();
            MediaListBean mediaListBean = MediaDataUtil.get(columnId, String.valueOf(meta), category, area, tag, year, title, pinyin, actor, director, sort, pageindex, pagesize, lang);
            if (mediaListBean == null) {
                return null;
            }
            WorkService.LOG.mo8825d("mediaListBean--------->" + mediaListBean.getList());
            JSONObject jsonObj = WorkService.this.parseToJsonObj(mediaListBean);
            if (jsonObj == null) {
                return null;
            }
            WorkService.LOG.mo8825d("jsonObj-------->" + jsonObj.toString());
            return jsonObj.toString();
        }

        public String getRecordedProgram(String channelId, long utc, long endUtc, String lang, String epg) throws RemoteException {
            WorkService.LOG.mo8825d("getRecordedProgram------>");
            Parameter.set("epg", epg);
            Parameter.save();
            ArrayList<EPGBean> epgList = EPGDataUtil.get(channelId, utc, endUtc, lang);
            if (epgList == null || epgList.size() <= 0) {
                return null;
            }
            JSONArray arr = new JSONArray();
            for (int i = 0; i < epgList.size(); i++) {
                EPGBean epgBean = (EPGBean) epgList.get(i);
                WorkService.LOG.mo8825d("epgBean------>" + epgBean.getTitle());
                arr.put(WorkService.this.parseToJsonObj(epgBean));
            }
            WorkService.LOG.mo8825d("arr.toString()------>" + arr.toString());
            return arr.toString();
        }

        public String getCurentProgram(String channelId, long utc, String lang, String epg) throws RemoteException {
            Parameter.set("epg", epg);
            Parameter.save();
            ArrayList<EPGBean> epgList = EPGDataUtil.get(channelId, utc, lang);
            if (epgList == null || epgList.size() <= 0) {
                return null;
            }
            JSONArray arr = new JSONArray();
            for (int i = 0; i < epgList.size(); i++) {
                EPGBean epgBean = (EPGBean) epgList.get(i);
                WorkService.LOG.mo8825d("epgBean------>" + epgBean.getTitle());
                arr.put(WorkService.this.parseToJsonObj(epgBean));
            }
            WorkService.LOG.mo8825d("arr.toString()------>" + arr.toString());
            return arr.toString();
        }

        public int login() throws RemoteException {
            LogcatUtils.m321d("start do login");
            SoapResponse soapResponse = SoapClient.login(Parameter.get("user"), Parameter.get("password"), Parameter.get("terminal_id"), Parameter.get("terminal_type"), Parameter.get("mac"), Parameter.get("netmode"), Parameter.get("soft_ver"), Parameter.get("hard_ver"), Parameter.get("epg"));
            if (soapResponse == null) {
                return -1;
            }
            return soapResponse.statusCode;
        }

        public String getEpgsToken() throws RemoteException {
            return SoapClient.getEpgsToken();
        }

        public String getOisToken() throws RemoteException {
            return SoapClient.getOisToken();
        }

        public String getProgramByCategory(long utc, long endUtc, String lang, String type, String epg) throws RemoteException {
            Parameter.set("epg", epg);
            Parameter.save();
            String reqUri = "http://" + SoapClient.getEpgs() + "/epgs/channel/epg/get" + "?token=" + SoapClient.getEpgsToken() + "&type=" + type + "&utc=" + utc + "&endutc=" + endUtc + "&lang=" + lang;
            try {
                BasicHttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
                HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
                HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
                WorkService.LOG.mo8825d("getEpgInfo() url =" + reqUri + "  StatusCode=" + response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    InputStream inputStream = response.getEntity().getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    StringBuffer sbResult = new StringBuffer();
                    String str = XmlPullParser.NO_NAMESPACE;
                    while (true) {
                        String data = br.readLine();
                        if (data == null) {
                            inputStream.close();
                            String jsonResult = sbResult.toString();
                            WorkService.LOG.mo8825d("getEpgInfo() jsonResult=" + jsonResult);
                            return jsonResult;
                        }
                        sbResult.append(data);
                    }
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return null;
        }

        public String getCategoryList(int columnId, String lang, String epg) throws RemoteException {
            Parameter.set("epg", epg);
            Parameter.save();
            ArrayList<CategoryBean> categoryList = CategoryDataUtil.get(columnId, lang);
            if (categoryList == null || categoryList.size() <= 0) {
                return null;
            }
            JSONArray arr = new JSONArray();
            for (int i = 0; i < categoryList.size(); i++) {
                CategoryBean categoryBean = (CategoryBean) categoryList.get(i);
                WorkService.LOG.mo8825d("categoryBean------>" + categoryBean.getTitle());
                arr.put(WorkService.this.parseToJsonObj(categoryBean));
            }
            WorkService.LOG.mo8825d("arr.toString()------>" + arr.toString());
            return arr.toString();
        }

        public String getMediaDetail(int columnId, String mediaId, int pageindex, int pagesize, String provider, String lang, String epg) throws RemoteException {
            WorkService.LOG.mo8825d("getMediaDetail----->");
            Parameter.set("epg", epg);
            Parameter.save();
            MediaBean mediaBean = MediaDataUtil.detail(columnId, mediaId, pageindex, pagesize, provider, lang);
            if (mediaBean == null) {
                return null;
            }
            JSONObject mediaBeanObj = WorkService.this.parseToJsonObj(mediaBean);
            WorkService.LOG.mo8825d("mediaBeanObj---------->" + mediaBeanObj.toString());
            return mediaBeanObj.toString();
        }

        public String getRealUrlBean(String url, int quality, String token, String epg) throws RemoteException {
            WorkService.LOG.mo8825d("getRealUrlBean----->");
            Parameter.set("epg", epg);
            Parameter.save();
            RealUrlBean realUrlBean = RealUrlDataUtil.getRealUrl(url, quality);
            if (realUrlBean == null) {
                return null;
            }
            WorkService.LOG.mo8825d("realUrlBean---------->" + realUrlBean.toString());
            JSONObject mediaBeanObj = WorkService.this.parseToJsonObj(realUrlBean);
            WorkService.LOG.mo8825d("mediaBeanObj---------->" + mediaBeanObj.toString());
            return mediaBeanObj.toString();
        }

        public String getConnendMediaList(int columnId, String mediaId, int size, String lang, String epg) throws RemoteException {
            WorkService.LOG.mo8825d("getLiveList------->" + columnId);
            Parameter.set("epg", epg);
            Parameter.save();
            ArrayList<MediaBean> list = MediaDataUtil.getRelates(columnId, mediaId, size, lang);
            MediaListBean mlb = new MediaListBean();
            mlb.setList(list);
            mlb.setTotalcount(list.size());
            WorkService.LOG.mo8825d("mlb--------->" + mlb.getList());
            if (mlb != null) {
                JSONObject jsonObj = WorkService.this.parseToJsonObj(mlb);
                if (jsonObj != null) {
                    WorkService.LOG.mo8825d("jsonObj-------->" + jsonObj.toString());
                    return jsonObj.toString();
                }
            }
            return null;
        }

        public int authen(String user, String terminal_id, String mediaId) throws RemoteException {
            return SoapClient.authen(user, terminal_id, mediaId).statusCode;
        }

        public String getAreaList(String lang) throws RemoteException {
            ArrayList<AreaBean> arrayList = AreaDataUtil.get(lang);
            if (arrayList == null || arrayList.size() <= 0) {
                return null;
            }
            JSONArray arr = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                AreaBean areaBean = (AreaBean) arrayList.get(i);
                WorkService.LOG.mo8825d("areaBean------>" + areaBean.getTitle());
                arr.put(WorkService.this.parseToJsonObj(areaBean));
            }
            WorkService.LOG.mo8825d("arr.toString()------>" + arr.toString());
            return arr.toString();
        }

        public String getUserName(String user) throws RemoteException {
            WorkService.LOG.mo8825d("getUserName---user---->" + user);
            return SoapClient.getUserInfo(user).bean.getRealname();
        }

        public long getValidtoUtc(String user) throws RemoteException {
            return SoapClient.getUserInfo(user).bean.getValidtoUtcMs();
        }

        public String getUserSubscribe(String user) throws RemoteException {
            return ((SubscribeBean) SoapClient.getSubscribeList(user, 1).get(0)).getService_id();
        }
    }

    /* renamed from: net.sunniwell.sz.mop4.service.WorkService$WorkHandler */
    public class WorkHandler extends Handler {
        @SuppressLint({"HandlerLeak"})
        public WorkHandler(Looper mLooper) {
            super(mLooper);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    WorkService.LOG.mo8825d("CMD_LOGIN---------->");
                    SoapClient.login(Parameter.get("user"), Parameter.get("password"), Parameter.get("terminal_id"), Parameter.get("terminal_type"), Parameter.get("mac"), Parameter.get("netmode"), Parameter.get("soft_ver"), Parameter.get("hard_ver"), Parameter.get("epg"));
                    return;
                case 2:
                    WorkService.LOG.mo8825d("CMD_ENABLE---------->");
                    WorkService.LOG.mo8825d("resultCode------>" + SoapClient.enable(Parameter.get("terminal_id"), Parameter.get("mac"), "sunniwell", "888888", Parameter.get("soft_ver"), Parameter.get("hard_ver"), Parameter.get(LogBean1.TERMINAL_STATISTICS_CHANNEL)));
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        LOG.mo8825d("WorkService---------->onCreate");
        this.mHandlerThread = new HandlerThread("WorkThread");
        this.mHandlerThread.start();
        this.mWorkHandler = new WorkHandler(this.mHandlerThread.getLooper());
        this.mSDKCallBack = new SDKCallBack(this, null);
        SWSysProp.init(this);
        initSDK();
    }

    private void initSDK() {
        Parameter.init("/mnt/sdcard");
        Parameter.set("language", SystemProperties.get("persist.sys.language"));
        Parameter.set(LogBean1.TERMINAL_STATISTICS_CHANNEL, "linktaro");
        Parameter.set("terminal_id", SystemProperties.get("ro.serialno"));
        Parameter.set("terminal_type", "1");
        Parameter.set("project_id", "sunniwell_stb");
        String user = SWSysProp.getStringParam("user_name", XmlPullParser.NO_NAMESPACE);
        String password = SWSysProp.getStringParam("password", XmlPullParser.NO_NAMESPACE);
        LOG.mo8825d("user------->" + user);
        LOG.mo8825d("password------->" + password);
        if (!StringUtils.isEmpty(user) || !StringUtils.isEmpty(password)) {
            this.isFactory = false;
            Parameter.set("user", user);
            Parameter.set("password", password);
        } else {
            this.isFactory = true;
        }
        if (this.isFactory) {
            Parameter.set("user", "sunniwell");
            Parameter.set("password", "888888");
        }
        Parameter.set("soft_ver", SystemProperties.get("ro.build.version.incremental"));
        Parameter.set("hard_ver", SystemProperties.get("ro.hardware"));
        Parameter.set("netmode", Constans.AD_MEDIA);
        Parameter.set("mac", SystemProperties.get("ro.bootmac"));
        Parameter.set("is_login", "false");
        Parameter.set("auto_login", "false");
        Parameter.set("remember_password", "false");
        Parameter.set("authen_enable", "true");
        Parameter.set("ad_enable", "true");
        String ois = Secure.getString(getContentResolver(), "ois");
        String epgs = Secure.getString(getContentResolver(), "epgs");
        LOG.mo8825d("ois------>" + ois + "-----epgs----->" + epgs);
        if (StringUtils.isEmpty(ois)) {
            ois = DefaultParam.ois;
        }
        if (StringUtils.isEmpty(epgs)) {
            epgs = DefaultParam.epgs;
        }
        LOG.mo8825d("ois------>" + ois + "-----epgs----->" + epgs);
        Secure.putString(getContentResolver(), "ois", ois);
        Secure.putString(getContentResolver(), "epgs", epgs);
        Parameter.set("ois", ois);
        Parameter.set("epgs", epgs);
        Parameter.set("epg", "launcher");
        Parameter.set("upgrade_url", "http://upgrades.jhometv.net:8080/upgrade/config.ini");
        Parameter.set("protocol", "p2p");
        Parameter.save();
        SoapClient.init(Parameter.get("ois"), Parameter.get("epgs"), true, this.mSDKCallBack);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        LOG.mo8825d("onBind");
        return new SDKManager(this, null);
    }

    public boolean onUnbind(Intent intent) {
        LOG.mo8825d("onUnbind");
        return super.onUnbind(intent);
    }

    /* access modifiers changed from: private */
    public synchronized JSONObject parseToJsonObj(Object object) {
        JSONObject obj;
        obj = new JSONObject();
        Method[] methods = object.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            String str = XmlPullParser.NO_NAMESPACE;
            Class<?> returnType = methods[i].getReturnType();
            if (name != null && name.startsWith("get") && !"getClass".equals(name)) {
                String fieldsName = new StringBuilder(String.valueOf(name.substring(3, 4).toLowerCase())).append(name.substring(4)).toString();
                try {
                    if (returnType.getSimpleName().equals("ArrayList")) {
                        ArrayList list = (ArrayList) methods[i].invoke(object, null);
                        JSONArray arr = new JSONArray();
                        if (list != null && list.size() > 0) {
                            for (int j = 0; j < list.size(); j++) {
                                arr.put(parseToJsonObj(list.get(j)));
                            }
                            obj.put(fieldsName, arr);
                        }
                    } else {
                        MailDbHelper.CONTENT.equals(fieldsName);
                        obj.put(fieldsName, methods[i].invoke(object, null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
