package net.sunniwell.aidl.bean;

import java.io.Serializable;
import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaBean implements Serializable {
    public static final int META_APK = 30;
    public static final int META_DLNA_IMAGE = -4;
    public static final int META_DLNA_MUSIC = -6;
    public static final int META_DLNA_VIDEO = -5;
    public static final int META_HTML = 20;
    public static final int META_IMAGE = 40;
    public static final int META_LIVE_ABR = 5;
    public static final int META_LIVE_CHANNEL = 0;
    public static final int META_LOCAL_IMAGE = -1;
    public static final int META_LOCAL_MUSIC = -3;
    public static final int META_LOCAL_VIDEO = -2;
    public static final int META_SERVICE_PACKAGE = 10;
    public static final int META_TEXT = 50;
    public static final int META_VOD_ABR = 6;
    public static final int META_VOD_FILMS = 3;
    public static final int META_VOD_GROUP = 4;
    public static final int META_VOD_PROGRAMS = 2;
    public static final int META_VOD_SINGLE = 1;
    public static final int STATE_DOWNLOADED = 2;
    public static final int STATE_EMPTY = 1;
    public static final int STATE_INSTALLED = 3;
    public static final int STATE_UNKNOWN = 0;
    private static final long serialVersionUID = 1;
    private String actor;
    private ArrayList<AdBean> ads;
    private String area;
    private int bitrate;
    private long byteLen;
    private String category;
    private int channelNumber;
    private int columnId;
    private int curSerial;
    private String description;
    private String dialogue;
    private String director;

    /* renamed from: id */
    private String f303id;
    private String image;
    private int limitLevel;
    private int meta;
    private String packageName;
    private int pagecount;
    private int playCount;
    private int priceDay;
    private int priceMonth;
    private int priceYear;
    private String provider;
    private int recommendLevel;
    private long releasetime;
    private int score;
    private String screenwriter;
    private int state = 0;
    private int supportPlayback;
    private String tag;
    private String thumbnail;
    private int timeLen;
    private String title;
    private int totalSerial;
    private int totalcount;
    private ArrayList<UrlBean> urls;
    private String versionCode;
    private String versionName;
    private int year;

    public String getId() {
        return this.f303id;
    }

    public void setId(String id) {
        this.f303id = id;
    }

    public int getMeta() {
        return this.meta;
    }

    public void setMeta(int meta2) {
        this.meta = meta2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image2) {
        this.image = image2;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail2) {
        this.thumbnail = thumbnail2;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category2) {
        this.category = category2;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area2) {
        this.area = area2;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProvider(String provider2) {
        this.provider = provider2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score2) {
        this.score = score2;
    }

    public int getPlayCount() {
        return this.playCount;
    }

    public void setPlayCount(int playCount2) {
        this.playCount = playCount2;
    }

    public String getActor() {
        return this.actor;
    }

    public void setActor(String actor2) {
        this.actor = actor2;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director2) {
        this.director = director2;
    }

    public String getScreenwriter() {
        return this.screenwriter;
    }

    public void setScreenwriter(String screenwriter2) {
        this.screenwriter = screenwriter2;
    }

    public long getReleasetime() {
        return this.releasetime;
    }

    public void setReleasetime(long releasetime2) {
        this.releasetime = releasetime2;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year2) {
        this.year = year2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(String dialogue2) {
        this.dialogue = dialogue2;
    }

    public ArrayList<UrlBean> getUrls() {
        return this.urls;
    }

    public void setUrls(ArrayList<UrlBean> urls2) {
        this.urls = urls2;
    }

    public int getTotalSerial() {
        return this.totalSerial;
    }

    public void setTotalSerial(int totalSerial2) {
        this.totalSerial = totalSerial2;
    }

    public int getCurSerial() {
        return this.curSerial;
    }

    public void setCurSerial(int curSerial2) {
        this.curSerial = curSerial2;
    }

    public long getByteLen() {
        return this.byteLen;
    }

    public void setByteLen(long byteLen2) {
        this.byteLen = byteLen2;
    }

    public int getTimeLen() {
        return this.timeLen;
    }

    public void setTimeLen(int timeLen2) {
        this.timeLen = timeLen2;
    }

    public int getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(int bitrate2) {
        this.bitrate = bitrate2;
    }

    public int getRecommendLevel() {
        return this.recommendLevel;
    }

    public void setRecommendLevel(int recommendLevel2) {
        this.recommendLevel = recommendLevel2;
    }

    public int getLimitLevel() {
        return this.limitLevel;
    }

    public void setLimitLevel(int limitLevel2) {
        this.limitLevel = limitLevel2;
    }

    public int getChannelNumber() {
        return this.channelNumber;
    }

    public void setChannelNumber(int channelNumber2) {
        this.channelNumber = channelNumber2;
    }

    public void setSupportPlayback(int supportPlayback2) {
        this.supportPlayback = supportPlayback2;
    }

    public int getSupportPlayback() {
        return this.supportPlayback;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName2) {
        this.versionName = versionName2;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(String versionCode2) {
        this.versionCode = versionCode2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName2) {
        this.packageName = packageName2;
    }

    public int getPriceDay() {
        return this.priceDay;
    }

    public void setPriceDay(int priceDay2) {
        this.priceDay = priceDay2;
    }

    public int getPriceMonth() {
        return this.priceMonth;
    }

    public void setPriceMonth(int priceMonth2) {
        this.priceMonth = priceMonth2;
    }

    public int getPriceYear() {
        return this.priceYear;
    }

    public void setPriceYear(int priceYear2) {
        this.priceYear = priceYear2;
    }

    public int getColumnId() {
        return this.columnId;
    }

    public void setColumnId(int columnId2) {
        this.columnId = columnId2;
    }

    public int getPagecount() {
        return this.pagecount;
    }

    public void setPagecount(int pagecount2) {
        this.pagecount = pagecount2;
    }

    public int getTotalcount() {
        return this.totalcount;
    }

    public void setTotalcount(int totalcount2) {
        this.totalcount = totalcount2;
    }

    public void setAds(ArrayList<AdBean> ads2) {
        this.ads = ads2;
    }

    public ArrayList<AdBean> getAds() {
        return this.ads;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state2) {
        this.state = state2;
    }

    public String toString() {
        return "MediaBean [id=" + this.f303id + ", meta=" + this.meta + ", title=" + this.title + ", image=" + this.image + ", thumbnail=" + this.thumbnail + ", category=" + this.category + ", area=" + this.area + ", provider=" + this.provider + ", tag=" + this.tag + ", score=" + this.score + ", playCount=" + this.playCount + ", actor=" + this.actor + ", director=" + this.director + ", screenwriter=" + this.screenwriter + ", releasetime=" + this.releasetime + ", year=" + this.year + ", description=" + this.description + ", dialogue=" + this.dialogue + ", urls=" + this.urls + ", ads=" + this.ads + ", totalSerial=" + this.totalSerial + ", curSerial=" + this.curSerial + ", byteLen=" + this.byteLen + ", timeLen=" + this.timeLen + ", bitrate=" + this.bitrate + ", recommendLevel=" + this.recommendLevel + ", limitLevel=" + this.limitLevel + ", channelNumber=" + this.channelNumber + ", supportPlayback=" + this.supportPlayback + ", versionName=" + this.versionName + ", versionCode=" + this.versionCode + ", packageName=" + this.packageName + ", state=" + this.state + ", priceDay=" + this.priceDay + ", priceMonth=" + this.priceMonth + ", priceYear=" + this.priceYear + ", columnId=" + this.columnId + ", pagecount=" + this.pagecount + ", totalcount=" + this.totalcount + "]";
    }
}
