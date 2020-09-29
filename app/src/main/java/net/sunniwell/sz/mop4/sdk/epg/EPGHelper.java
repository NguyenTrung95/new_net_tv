package net.sunniwell.sz.mop4.sdk.epg;

import java.util.ArrayList;
import java.util.HashMap;
import net.sunniwell.common.tools.DateTime;
import net.sunniwell.sz.mop4.sdk.sync.OnEPGSyncListener;
import net.sunniwell.sz.mop4.sdk.sync.SyncManager;

/* renamed from: net.sunniwell.sz.mop4.sdk.epg.EPGHelper */
public class EPGHelper {
    /* access modifiers changed from: private */
    public static HashMap<String, HashMap<Long, HashMap<String, ArrayList<EPGBean>>>> epgMap;
    private static OnEPGSyncListener epgSyncListener = new OnEPGSyncListener() {
        public void onEPGSyncChange(String oldSync, String newSync) {
            for (String channelId : EPGHelper.epgMap.keySet()) {
                HashMap<Long, HashMap<String, ArrayList<EPGBean>>> datemap = (HashMap) EPGHelper.epgMap.get(channelId);
                for (Long utc : datemap.keySet()) {
                    HashMap<String, ArrayList<EPGBean>> langmap = (HashMap) datemap.get(utc);
                    for (String lang : langmap.keySet()) {
                        ArrayList<EPGBean> epglist = EPGManager.get(channelId, utc.longValue(), (utc.longValue() + DateTime.MILLIS_PER_DAY) - 1000, lang);
                        if (epglist != null) {
                            langmap.put(lang, epglist);
                        }
                    }
                }
            }
        }
    };

    public static boolean init() {
        if (epgMap == null) {
            epgMap = new HashMap<>();
            SyncManager.setOnEPGSyncListener(epgSyncListener);
        }
        return true;
    }

    public static ArrayList<EPGBean> getThisDay(String channelId, long utcMs, String lang) {
        ArrayList<EPGBean> epglist;
        long utcMs2 = utcMs - ((((utcMs % 24) * 60) * 60) * 1000);
        long startUtc = utcMs2;
        long endUtc = (DateTime.MILLIS_PER_DAY + utcMs2) - 1000;
        HashMap<Long, HashMap<String, ArrayList<EPGBean>>> datemap = (HashMap) epgMap.get(channelId);
        if (datemap == null) {
            epglist = EPGManager.get(channelId, startUtc, endUtc, lang);
            if (epglist != null) {
                HashMap<String, ArrayList<EPGBean>> langmap = new HashMap<>();
                langmap.put(lang, epglist);
                HashMap<Long, HashMap<String, ArrayList<EPGBean>>> datemap2 = new HashMap<>();
                datemap2.put(Long.valueOf(utcMs2), langmap);
                epgMap.put(channelId, datemap2);
            }
        } else {
            HashMap<String, ArrayList<EPGBean>> langmap2 = (HashMap) datemap.get(Long.valueOf(utcMs2));
            if (langmap2 == null) {
                epglist = EPGManager.get(channelId, startUtc, endUtc, lang);
                if (epglist != null) {
                    HashMap<String, ArrayList<EPGBean>> langmap3 = new HashMap<>();
                    langmap3.put(lang, epglist);
                    datemap.put(Long.valueOf(utcMs2), langmap3);
                }
            } else {
                epglist = (ArrayList) langmap2.get(lang);
                if (epglist == null) {
                    epglist = EPGManager.get(channelId, startUtc, endUtc, lang);
                    if (epglist != null) {
                        langmap2.put(lang, epglist);
                    }
                }
            }
        }
        return epglist;
    }
}
