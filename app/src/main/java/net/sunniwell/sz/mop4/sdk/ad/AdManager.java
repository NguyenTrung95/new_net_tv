package net.sunniwell.sz.mop4.sdk.ad;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import net.sunniwell.app.linktaro.nettv.Constants.PageLiveConstants;
import net.sunniwell.sz.mop4.sdk.column.ColumnManager;
import net.sunniwell.sz.mop4.sdk.sync.OnColumnSyncListener;
import net.sunniwell.sz.mop4.sdk.sync.SyncManager;

/* renamed from: net.sunniwell.sz.mop4.sdk.ad.AdManager */
public class AdManager {
    private static final String TAG = "AdManager";
    /* access modifiers changed from: private */
    public static HashMap<Integer, ArrayList<AdBean>> mAdMap = null;
    /* access modifiers changed from: private */
    public static boolean mGettingMap = false;
    /* access modifiers changed from: private */
    public static OnColumnSyncListener onSyncChangeListener = new OnColumnSyncListener() {
        public void onColumnSyncChange(int columnId, int oldSync, int newSync) {
            Log.d(AdManager.TAG, "onSyncChange columnId=" + columnId + " oldSync=" + oldSync + " newSync=" + newSync);
            ArrayList<AdBean> adList = AdDataUtil.get(columnId);
            if (adList != null) {
                if (AdManager.mAdMap == null) {
                    AdManager.mAdMap = AdDataUtil.getMap();
                }
                if (AdManager.mAdMap != null) {
                    AdManager.mAdMap.put(Integer.valueOf(columnId), adList);
                }
            }
        }
    };

    public static boolean init() {
        if (mAdMap == null && !mGettingMap) {
            mGettingMap = true;
            new Thread(new Runnable() {
                public void run() {
                    while (AdManager.mGettingMap) {
                        AdManager.mAdMap = AdDataUtil.getMap();
                        if (AdManager.mAdMap != null) {
                            break;
                        }
                        try {
                            Thread.sleep(PageLiveConstants.LIST_STAY_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    SyncManager.addOnColumnSyncListener(AdManager.class.getName(), AdManager.onSyncChangeListener);
                }
            }).start();
        }
        return true;
    }

    public static ArrayList<AdBean> getOnStartAds(int columnId) {
        ArrayList<AdBean> ads = getAds(columnId, 1);
        if (ads != null && ads.size() > 0) {
            return ads;
        }
        int pid = ColumnManager.getPid(columnId);
        if (pid >= 0) {
            return getOnStartAds(pid);
        }
        return ads;
    }

    public static ArrayList<AdBean> getOnEndAds(int columnId) {
        ArrayList<AdBean> ads = getAds(columnId, 2);
        if (ads != null && ads.size() > 0) {
            return ads;
        }
        int pid = ColumnManager.getPid(columnId);
        if (pid >= 0) {
            return getOnEndAds(pid);
        }
        return ads;
    }

    public static ArrayList<AdBean> getOnPlayAds(int columnId) {
        ArrayList<AdBean> ads = getAds(columnId, 3);
        if (ads != null && ads.size() > 0) {
            return ads;
        }
        int pid = ColumnManager.getPid(columnId);
        if (pid >= 0) {
            return getOnPlayAds(pid);
        }
        return ads;
    }

    public static ArrayList<AdBean> getOnPauseAds(int columnId) {
        ArrayList<AdBean> ads = getAds(columnId, 4);
        if (ads != null && ads.size() > 0) {
            return ads;
        }
        int pid = ColumnManager.getPid(columnId);
        if (pid >= 0) {
            return getOnPauseAds(pid);
        }
        return ads;
    }

    public static ArrayList<AdBean> getCommonAds(int columnId) {
        ArrayList<AdBean> ads = getAds(columnId, 5);
        if (ads != null && ads.size() > 0) {
            return ads;
        }
        int pid = ColumnManager.getPid(columnId);
        if (pid >= 0) {
            return getCommonAds(pid);
        }
        return ads;
    }

    public static ArrayList<AdBean> getLocationAds(int columnId) {
        ArrayList<AdBean> ads = getAds(columnId, 6);
        if (ads != null && ads.size() > 0) {
            return ads;
        }
        int pid = ColumnManager.getPid(columnId);
        if (pid >= 0) {
            return getLocationAds(pid);
        }
        return ads;
    }

    private static ArrayList<AdBean> getAds(int columnId, int type) {
        if (mAdMap != null) {
            ArrayList<AdBean> adList = (ArrayList) mAdMap.get(Integer.valueOf(columnId));
            if (adList != null && adList.size() > 0) {
                ArrayList<AdBean> ads = new ArrayList<>();
                for (int i = 0; i < adList.size(); i++) {
                    AdBean ad = (AdBean) adList.get(i);
                    if (type == 1) {
                        if (ad.getType() == 1 || ad.getType() == 5) {
                            ads.add(ad);
                        }
                    } else if (type == 2) {
                        if (ad.getType() == 2 || ad.getType() == 5) {
                            ads.add(ad);
                        }
                    } else if (type == 3) {
                        if (ad.getType() == 3 || ad.getType() == 5) {
                            ads.add(ad);
                        }
                    } else if (type == 4) {
                        if (ad.getType() == 4 || ad.getType() == 5) {
                            ads.add(ad);
                        }
                    } else if (type == 6 && ad.getType() == 6) {
                        ads.add(ad);
                    }
                }
                return ads;
            }
        } else {
            init();
        }
        return null;
    }
}
