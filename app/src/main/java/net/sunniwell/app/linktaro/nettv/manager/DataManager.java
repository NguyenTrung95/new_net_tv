package net.sunniwell.app.linktaro.nettv.manager;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sunniwell.aidl.SDKRemoteManager;
import net.sunniwell.aidl.bean.AdBean;
import net.sunniwell.aidl.bean.EPGBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.aidl.bean.MediaListBean;
import net.sunniwell.app.linktaro.nettv.Constants.C0412Constants;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.sz.mop4.sdk.media.MediaManager;

public class DataManager {
    private static final SWLogger LOG = SWLogger.getLogger(DataManager.class);
    private static DataManager mDataManager;
    private int autoQuality;
    private boolean isAutoQuality;
    private ArrayList<MediaBean> mBsChannelData;
    private ArrayList<String> mCategoryList;
    private Context mContext;
    private ArrayList<MediaBean> mCsChannelData;
    private ArrayList<MediaBean> mDsChannelData;
    private SDKRemoteManager mSdkRemoteManager;
    private MediaBean mediaBean;

    public static DataManager getInstance(Context context) {
        if (mDataManager == null) {
            mDataManager = new DataManager(context);
        }
        return mDataManager;
    }

    private DataManager(Context context) {
        this.mContext = context;
        this.mSdkRemoteManager = SDKRemoteManager.getInstance(context, null);
    }

    public void initLiveDataFromNet() {
        LOG.mo8825d("initLiveDataFromNet---->");
        this.mDsChannelData = initLiveDataFromNet(3);
        this.mBsChannelData = initLiveDataFromNet(1);
        this.mCsChannelData = initLiveDataFromNet(2);
    }

    public int login() {
        return this.mSdkRemoteManager.login();
    }

    private ArrayList<MediaBean> initLiveDataFromNet(int columnInx) {
        MediaListBean liveList = this.mSdkRemoteManager.getMediaList(columnInx, -1, null, null, null, null, null, null, null, null, MediaManager.SORT_BY_CHNLNUM, 0, 100, "zh", "iptv");
        if (liveList == null) {
            return null;
        }
        LOG.mo8825d("liveList------>" + liveList);
        return liveList.getList();
    }

    public boolean isLivePageHasData() {
        boolean flag = false;
        if (this.mDsChannelData != null && this.mDsChannelData.size() > 0 && this.mBsChannelData != null && this.mBsChannelData.size() > 0 && this.mCsChannelData != null && this.mCsChannelData.size() > 0) {
            flag = true;
        }
        LOG.mo8825d("isLivePageHasData---->" + flag);
        return flag;
    }

    public ArrayList<MediaBean> getLiveData(int columnInx) {
        LOG.mo8825d("getLiveData---->" + columnInx);
        switch (columnInx) {
            case 1:
                return this.mBsChannelData;
            case 2:
                return this.mCsChannelData;
            case 3:
                return this.mDsChannelData;
            default:
                return this.mDsChannelData;
        }
    }

    public int getTvFormtByChannel(MediaBean mediaBean2) {
        int tvFormat = 0;
        if (!isLivePageHasData()) {
            return 0;
        }
        if (this.mDsChannelData.contains(mediaBean2)) {
            tvFormat = 3;
        }
        if (this.mBsChannelData.contains(mediaBean2)) {
            tvFormat = 1;
        }
        if (this.mCsChannelData.contains(mediaBean2)) {
            return 2;
        }
        return tvFormat;
    }

    private EPGBean getFirstProgramByChannelInx(int columnInx, int channelInx) {
        EPGBean epgBean = null;
        ArrayList<MediaBean> channelData = null;
        if (isLivePageHasData()) {
            channelData = getLiveData(columnInx);
        }
        if (channelData != null && channelData.size() > channelInx) {
            List<EPGBean> curProgramList = this.mSdkRemoteManager.getCurentProgram(((MediaBean) channelData.get(channelInx)).getId(), System.currentTimeMillis(), "zh", "iptv");
            if (curProgramList == null || curProgramList.size() <= 0) {
                LOG.mo8825d("current program == null !!!");
                return null;
            }
            epgBean = (EPGBean) curProgramList.get(0);
        }
        return epgBean;
    }

    public Map<MediaBean, EPGBean> getFirstChannelProgramMapFromNet(int columnInx, int channelInx) {
        LOG.mo8825d("getFirstChannelProgramMapFromNet----columnInx--->" + columnInx + "-------channelInx------->" + channelInx);
        MediaBean mediaBean2 = null;
        ArrayList<MediaBean> channelData = null;
        if (isLivePageHasData()) {
            channelData = getLiveData(columnInx);
        }
        Map<MediaBean, EPGBean> channelProgramMap = new HashMap<>();
        if (channelData != null && channelData.size() > channelInx) {
            mediaBean2 = (MediaBean) channelData.get(channelInx);
        }
        channelProgramMap.put(mediaBean2, getFirstProgramByChannelInx(columnInx, channelInx));
        return channelProgramMap;
    }

    public List<EPGBean> getProgramListByChannel(int columnInx, int channelInx, long startUtcTime, long endUtcTime, boolean isFirstDay) {
        LOG.mo8825d("getProgramListByChannel----columnInx--->" + columnInx + "-------channelInx------->" + channelInx + "------startUtcTime---->" + startUtcTime + "--------endUtcTime--------->" + endUtcTime);
        if (!isFirstDay) {
            return getProgramListFromNet(columnInx, channelInx, startUtcTime, endUtcTime);
        }
        List<EPGBean> programListFromNet = getProgramListFromNet(columnInx, channelInx, startUtcTime, endUtcTime);
        if (programListFromNet == null || programListFromNet.size() <= 0) {
            List<EPGBean> programListFromNet2 = new ArrayList<>();
            EPGBean firstEPG = getFirstProgramByChannelInx(columnInx, channelInx);
            if (firstEPG == null) {
                return programListFromNet2;
            }
            programListFromNet2.add(firstEPG);
            return programListFromNet2;
        }
        EPGBean firstEPG2 = getFirstProgramByChannelInx(columnInx, channelInx);
        if (firstEPG2 == null) {
            return programListFromNet;
        }
        programListFromNet.add(0, firstEPG2);
        return programListFromNet;
    }

    private List<EPGBean> getProgramListFromNet(int columnInx, int channelInx, long startUtcTime, long endUtcTime) {
        MediaBean mediaBean2 = null;
        ArrayList<MediaBean> channelData = null;
        if (!isLivePageHasData()) {
            initLiveDataFromNet();
        }
        if (isLivePageHasData()) {
            channelData = getLiveData(columnInx);
        }
        if (channelData != null) {
            if (channelData.size() <= channelInx) {
                channelInx = channelData.size() - 1;
            }
            mediaBean2 = (MediaBean) channelData.get(channelInx);
        }
        if (mediaBean2 == null) {
            return null;
        }
        return this.mSdkRemoteManager.getRecordedProgram(mediaBean2.getId(), startUtcTime, endUtcTime, "zh", "iptv");
    }

    public MediaBean getPlayingChannel() {
        return this.mediaBean;
    }

    public void setPlayingChannel(MediaBean mediaBean2) {
        this.mediaBean = mediaBean2;
    }

    public void initAllCategoryList() {
        this.mCategoryList = new ArrayList<>();
        this.mCategoryList.add("ドラマ");
        this.mCategoryList.add("情報/ワイドショー");
        this.mCategoryList.add("ドキュメンタリー/教養");
        this.mCategoryList.add("ニュース/報道");
        this.mCategoryList.add("スポーツ");
        this.mCategoryList.add("趣味/教育");
        this.mCategoryList.add("アニメ/特撮");
        this.mCategoryList.add("福祉");
    }

    public boolean isCategoryHasData() {
        if (this.mCategoryList == null || this.mCategoryList.size() <= 0) {
            return false;
        }
        return true;
    }

    public ArrayList<String> getAllCategory() {
        return this.mCategoryList;
    }

    private ArrayList<EPGBean> getCategoryProgramByColumn(int columnInx, long startUtcTime, long endUtcTime, String categoryName) {
        List<EPGBean> programByCategory = this.mSdkRemoteManager.getProgramByCategory(startUtcTime, endUtcTime, "zh", categoryName, "iptv");
        ArrayList<EPGBean> allCategoryProgram = new ArrayList<>();
        allCategoryProgram.addAll(programByCategory);
        return allCategoryProgram;
    }

    public ArrayList<EPGBean> getCategoryProgramByColumn(int columnInx, long startUtcTime, long endUtcTime, String categoryName, Comparator<EPGBean> comparator) {
        LOG.mo8825d("getCategoryProgramByColumn------columnInx--------->" + columnInx + "-------startUtcTime------->" + startUtcTime + "-------endUtcTime------> " + endUtcTime + "-------categoryName--------->" + categoryName);
        ArrayList<EPGBean> categoryProgramByColumn = getCategoryProgramByColumn(columnInx, startUtcTime, endUtcTime, categoryName);
        Collections.sort(categoryProgramByColumn, comparator);
        return categoryProgramByColumn;
    }

    public MediaBean getChannelById(String id, int columnInx) {
        LOG.mo8825d("getChannelById-------id----->" + id + "------columnInx--------->" + columnInx);
        if (StringUtils.isNotEmpty(id)) {
            ArrayList<MediaBean> dsChannelData = getLiveData(3);
            ArrayList<MediaBean> bsChannelData = getLiveData(1);
            ArrayList<MediaBean> csChannelData = getLiveData(2);
            for (int i = 0; i < dsChannelData.size(); i++) {
                if (id.equals(((MediaBean) dsChannelData.get(i)).getId())) {
                    return (MediaBean) dsChannelData.get(i);
                }
            }
            for (int i2 = 0; i2 < bsChannelData.size(); i2++) {
                if (id.equals(((MediaBean) bsChannelData.get(i2)).getId())) {
                    return (MediaBean) bsChannelData.get(i2);
                }
            }
            for (int i3 = 0; i3 < csChannelData.size(); i3++) {
                if (id.equals(((MediaBean) csChannelData.get(i3)).getId())) {
                    return (MediaBean) csChannelData.get(i3);
                }
            }
        }
        return null;
    }

    public int getQuality() {
        int quality = SWSysProp.getIntParam("play_quality", 0);
        if (quality != 0) {
            return quality;
        }
        SWSysProp.setIntParam("play_quality", 3);
        return 3;
    }

    public void setQuality(int quality) {
        SWSysProp.setIntParam("play_quality", quality);
    }

    public int getAutoQuality() {
        return this.autoQuality;
    }

    public void setAutoQuality(int autoQuality2) {
        this.autoQuality = autoQuality2;
    }

    public boolean isAutoQuality() {
        return this.isAutoQuality;
    }

    public void setAutoQuality(boolean isAutoQuality2) {
        this.isAutoQuality = isAutoQuality2;
    }

    private EPGBean getFirstProgramByChannelInx2(MediaBean mediaBean2, int channelInx) {
        EPGBean epgBean = null;
        if (mediaBean2 != null) {
            List<EPGBean> curProgramList = this.mSdkRemoteManager.getCurentProgram(mediaBean2.getId(), System.currentTimeMillis(), "zh", "iptv");
            if (curProgramList == null || curProgramList.size() <= 0) {
                LOG.mo8825d("current program == null !!!");
                return null;
            }
            epgBean = (EPGBean) curProgramList.get(0);
        }
        return epgBean;
    }

    public EPGBean getTextEPGBean(MediaBean mediaBean2, int channelInx) {
        return getFirstProgramByChannelInx2(mediaBean2, channelInx);
    }

    public HashMap<String, AdBean> downAdData() {
        HashMap<String, AdBean> mAdHashMap = new HashMap<>();
        if (this.mSdkRemoteManager != null) {
            List<AdBean> mAllAdList = this.mSdkRemoteManager.getAdData(0, "iptv");
            if (mAllAdList != null && mAllAdList.size() > 0) {
                for (AdBean ad : mAllAdList) {
                    if (ad != null) {
                        if (ad.getExtend().equals(C0412Constants.TV_AD_DS)) {
                            mAdHashMap.put(C0412Constants.TV_AD_DS, ad);
                        } else if (ad.getExtend().equals(C0412Constants.TV_AD_BS)) {
                            mAdHashMap.put(C0412Constants.TV_AD_BS, ad);
                        } else if (ad.getExtend().equals(C0412Constants.TV_AD_CS)) {
                            mAdHashMap.put(C0412Constants.TV_AD_CS, ad);
                        }
                    }
                }
            }
        }
        return mAdHashMap;
    }
}
