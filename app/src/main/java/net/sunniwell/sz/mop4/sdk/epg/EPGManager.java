package net.sunniwell.sz.mop4.sdk.epg;

import java.util.ArrayList;

/* renamed from: net.sunniwell.sz.mop4.sdk.epg.EPGManager */
public class EPGManager {
    public static String sync() {
        return EPGDataUtil.sync();
    }

    public static ArrayList<EPGBean> get(String channelId, long utcMs, long endUtcMs, String lang) {
        return EPGDataUtil.get(channelId, utcMs, endUtcMs, lang);
    }

    public static ArrayList<EPGBean> get(String channelId, long utcMs, String lang) {
        return EPGDataUtil.get(channelId, utcMs, lang);
    }
}
