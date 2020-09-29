package net.sunniwell.app.linktaro.nettv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sunniwell.aidl.SDKRemoteManager;
import net.sunniwell.aidl.bean.AdBean;
import net.sunniwell.app.linktaro.nettv.Constants.C0412Constants;
import net.sunniwell.app.linktaro.nettv.manager.DataManager;
import net.sunniwell.app.linktaro.tools.JsonUtil;
import net.sunniwell.app.linktaro.tools.SharedPreUtil;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.common.tools.DateTime;
import org.xmlpull.p019v1.XmlPullParser;

public class SWUpdateStbDataService extends Service {
    private static final int CMD_UPDATE_CATEGORYLIST = 2;
    private static final int CMD_UPDATE_STB_ALL_AD_IMAGE_URL = 3;
    private static final int CMD_UPDATE_STB_ALL_DATA = 5;
    private static final int CMD_UPDATE_STB_FINISH_CALL_BACK = 4;
    private static final int CMD_UPDATE_STB_LIVELIST = 1;
    private static updateStbDataHandler mDataHandler;
    /* access modifiers changed from: private */
    public SWLogger LOG = SWLogger.getLogger(SWUpdateStbDataService.class);
    private List<AdBean> adBeans;
    private DataManager mDataManager;
    private HandlerThread mHandlerThread;
    private SDKRemoteManager mSdkRemoteManager;
    private SharedPreUtil mSharedPreUtil;

    class updateStbDataHandler extends Handler {
        public updateStbDataHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SWUpdateStbDataService.this.downAllLiveList();
                    return;
                case 2:
                    SWUpdateStbDataService.this.downAllCategoryList();
                    return;
                case 3:
                    SWUpdateStbDataService.this.downAllAdImageUrl();
                    return;
                case 4:
                    SWUpdateStbDataService.this.initDataFinishCallBack();
                    break;
                case 5:
                    break;
                default:
                    return;
            }
            SWUpdateStbDataService.this.initStbData();
        }
    }

    public void onCreate() {
        this.LOG.mo8825d("[onCreate]");
        this.mSharedPreUtil = SharedPreUtil.getSharedPreUtil(this);
        this.mHandlerThread = new HandlerThread("LoadData");
        this.mHandlerThread.start();
        mDataHandler = new updateStbDataHandler(this.mHandlerThread.getLooper());
        this.mDataManager = DataManager.getInstance(this);
        this.mSdkRemoteManager = SDKRemoteManager.getInstance(this, null);
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        initStbData();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        this.LOG.mo8825d("[onDestroy]");
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    /* access modifiers changed from: private */
    public void downAllLiveList() {
        this.LOG.mo8825d("[downAllLiveList]");
        this.mDataManager.initLiveDataFromNet();
        if (this.mDataManager.isLivePageHasData()) {
            this.LOG.mo8825d("[downAllLiveList]done");
        }
    }

    /* access modifiers changed from: private */
    public void downAllCategoryList() {
        this.LOG.mo8825d("[downAllCategoryList]");
        this.mDataManager.initAllCategoryList();
        if (this.mDataManager.isCategoryHasData()) {
            this.LOG.mo8825d("[downAllCategoryList]done");
        }
    }

    /* access modifiers changed from: private */
    public void downAllAdImageUrl() {
        this.LOG.mo8825d("[downAllAdImageUrl]");
        this.adBeans = this.mSdkRemoteManager.getAdData(0, "iptv");
        if (this.adBeans != null) {
            try {
                Collections.sort(this.adBeans, new Comparator<AdBean>() {
                    public int compare(AdBean lhs, AdBean rhs) {
                        String lextend = lhs.getExtend();
                        String rextend = rhs.getExtend();
                        SWUpdateStbDataService.this.LOG.mo8825d("lextend:" + lextend + " rextend:" + rextend);
                        if (StringUtils.isNotEmpty(lextend) && StringUtils.isNotEmpty(rextend)) {
                            if ("tv_buffer".equals(lextend)) {
                                return 1;
                            }
                            if ("tv_buffer".equals(rextend)) {
                                return -1;
                            }
                            if (StringUtils.isNumberStr(lextend.substring(6)) && StringUtils.isNumberStr(rextend.substring(6))) {
                                return Integer.valueOf(lextend.substring(6)).intValue() - Integer.valueOf(rextend.substring(6)).intValue();
                            }
                        }
                        return 0;
                    }
                });
                this.LOG.mo8825d("[adBeans]" + this.adBeans);
                initLivePageADImags();
                initBufferADImags();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public void initLivePageADImags() {
        this.LOG.mo8825d("[downLivePageADImags]");
        List<String> mList = new ArrayList<>();
        if (this.adBeans == null || this.adBeans.size() <= 0) {
            this.mSharedPreUtil.setStbImgs("StbLivePageAdImgs", XmlPullParser.NO_NAMESPACE);
            return;
        }
        for (int i = 0; i < this.adBeans.size(); i++) {
            String extend = ((AdBean) this.adBeans.get(i)).getExtend();
            if (StringUtils.isNotEmpty(extend) && (extend.contains(C0412Constants.TV_AD_DS) || extend.contains(C0412Constants.TV_AD_BS) || extend.contains(C0412Constants.TV_AD_CS))) {
                String adUrl = ((AdBean) this.adBeans.get(i)).getContent();
                mList.add(adUrl);
                this.LOG.mo8825d("[getLivePageADImags]" + adUrl);
            }
        }
        this.mSharedPreUtil.setStbImgs("StbLivePageAdImgs", JsonUtil.toJson(mList));
    }

    public void initBufferADImags() {
        this.LOG.mo8825d("[getBufferADImags]");
        List<String> mList = new ArrayList<>();
        if (this.adBeans == null || this.adBeans.size() <= 0) {
            this.mSharedPreUtil.setStbImgs("StbBufferImgs", XmlPullParser.NO_NAMESPACE);
            return;
        }
        for (int i = 0; i < this.adBeans.size(); i++) {
            String extend = ((AdBean) this.adBeans.get(i)).getExtend();
            if (StringUtils.isNotEmpty(extend) && extend.contains(C0412Constants.TV_AD_BUFFER)) {
                String adUrl = ((AdBean) this.adBeans.get(i)).getContent();
                mList.add(adUrl);
                this.LOG.mo8825d("[getBufferADImags]" + adUrl);
            }
        }
        this.mSharedPreUtil.setStbImgs("StbBufferImgs", JsonUtil.toJson(mList));
    }

    /* access modifiers changed from: private */
    public void initDataFinishCallBack() {
        this.LOG.mo8825d("[initDataFinishCallBack]");
        sendBroadcast(new Intent("net.sunniwell.action.IPTV_IS_OK"));
    }

    /* access modifiers changed from: private */
    public void initStbData() {
        this.LOG.mo8825d("[initStbData]");
        if (!this.mDataManager.isLivePageHasData()) {
            mDataHandler.sendEmptyMessage(1);
        }
        if (!this.mDataManager.isCategoryHasData()) {
            mDataHandler.sendEmptyMessage(2);
        }
        mDataHandler.sendEmptyMessage(3);
        mDataHandler.sendEmptyMessageDelayed(5, DateTime.MILLIS_PER_HOUR);
    }
}
