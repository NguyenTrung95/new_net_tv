package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.BaiduStatLable */
public class BaiduStatLable {
    public static final String EPG_SUFFIX = "_回看";
    public static final int MEDIABEAN_META_EPG = 37291;
    public static final String MEDIAPLAY_EPG = "回看";
    public static final String MEDIAPLAY_FILMS = "系列电影";
    public static final String MEDIAPLAY_GROUP = "综艺";
    public static final String MEDIAPLAY_LIVE = "直播";
    public static final String MEDIAPLAY_LIVE_ABR = "直播ABR";
    public static final String MEDIAPLAY_LUNBO = "轮播";
    public static final String MEDIAPLAY_SERIAL = "电视剧";
    public static final String MEDIAPLAY_SINGLE = "单集片源";
    public static final String MEDIAPLAY_UNKNOWN = "未知";
    public static final String MEDIAPLAY_VOD_ABR = "点播ABR";

    public static String getMediaMeta(int meta) {
        switch (meta) {
            case 0:
                return MEDIAPLAY_LIVE;
            case 1:
                return MEDIAPLAY_SINGLE;
            case 2:
                return MEDIAPLAY_SERIAL;
            case 3:
                return MEDIAPLAY_FILMS;
            case 4:
                return MEDIAPLAY_GROUP;
            case 5:
                return MEDIAPLAY_LIVE_ABR;
            case 6:
                return MEDIAPLAY_VOD_ABR;
            case 7:
                return MEDIAPLAY_LUNBO;
            case MEDIABEAN_META_EPG /*37291*/:
                return MEDIAPLAY_EPG;
            default:
                return MEDIAPLAY_UNKNOWN;
        }
    }
}
