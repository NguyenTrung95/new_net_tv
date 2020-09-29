package net.sunniwell.sz.mop4.sdk.media;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.PageEpgConstants;
import net.sunniwell.download.manager.DownLoadConfigUtil;
import net.sunniwell.sz.mop4.sdk.ad.AdBean;
import net.sunniwell.sz.mop4.sdk.param.Parameter;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.media.MediaDataUtil */
public class MediaDataUtil {
    private static final String TAG = "MediaDataUtil";

    public static MediaListBean get(int columnId, String meta, String category, String area, String tag, String year, String title, String pinyin, String actor, String director, String sort, int pageindex, int pagesize, String lang) {
        MediaListBean mlb = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "get(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/media/get")).append("?columnid=").append(columnId).toString();
            if (!TextUtils.isEmpty(meta)) {
                if (!meta.contains("-1")) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&meta=").append(meta).toString();
                }
            }
            if (category != null) {
                if (!category.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&category=").append(URLEncoder.encode(category, "UTF-8")).toString();
                }
            }
            if (area != null) {
                if (!area.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&area=").append(URLEncoder.encode(area, "UTF-8")).toString();
                }
            }
            if (tag != null) {
                if (!tag.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&tag=").append(URLEncoder.encode(tag, "UTF-8")).toString();
                }
            }
            if (year != null) {
                if (!year.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&year=").append(year).toString();
                }
            }
            if (title != null) {
                if (!title.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&title=").append(URLEncoder.encode(title, "UTF-8")).toString();
                }
            }
            if (pinyin != null) {
                if (!pinyin.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&pinyin=").append(pinyin).toString();
                }
            }
            if (actor != null) {
                if (!actor.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&actor=").append(URLEncoder.encode(actor, "UTF-8")).toString();
                }
            }
            if (director != null) {
                if (!director.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&director=").append(URLEncoder.encode(director, "UTF-8")).toString();
                }
            }
            if (sort != null) {
                if (!sort.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&sort=").append(sort).toString();
                }
            }
            String reqUri2 = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(reqUri)).append("&pageindex=").append(pageindex).toString())).append("&pagesize=").append(pagesize).toString())).append("&lang=").append(lang).toString())).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "get() reqUri = " + reqUri2);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpResponse response = client.execute(new HttpGet(reqUri2));
            Log.d(TAG, "get() StatusCode = " + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuffer sbResult = new StringBuffer();
                String str = XmlPullParser.NO_NAMESPACE;
                while (true) {
                    String data = br.readLine();
                    if (data == null) {
                        break;
                    }
                    sbResult.append(data);
                }
                inputStream.close();
                String jsonResult = sbResult.toString();
                Log.d(TAG, "get() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    mlb = parseJsonToMediaListBean(jsonResult);
                    if (mlb == null) {
                        mlb = new MediaListBean();
                    }
                    if (mlb.getList() == null) {
                        mlb.setList(new ArrayList());
                    }
                    mlb.setPageindex(pageindex);
                    mlb.setPagesize(pagesize);
                }
            }
            client.getConnectionManager().shutdown();
            return mlb;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MediaBean detail(int columnId, String mediaId, int pageindex, int pagesize, String provider, String lang) {
        MediaBean mediaBean = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "detail(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/media/detail")).append("?id=").append(mediaId).toString())).append("&columnid=").append(columnId).toString())).append("&pageindex=").append(pageindex).toString())).append("&pagesize=").append(pagesize).toString();
            if (provider != null) {
                if (!provider.equals(XmlPullParser.NO_NAMESPACE)) {
                    reqUri = new StringBuilder(String.valueOf(reqUri)).append("&provider=").append(URLEncoder.encode(provider, "UTF-8")).toString();
                }
            }
            String reqUri2 = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(reqUri)).append("&lang=").append(lang).toString())).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "detail() reqUri =" + reqUri2);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpResponse response = client.execute(new HttpGet(reqUri2));
            Log.d(TAG, "detail() StatusCode = " + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuffer sbResult = new StringBuffer();
                String str = XmlPullParser.NO_NAMESPACE;
                while (true) {
                    String data = br.readLine();
                    if (data == null) {
                        break;
                    }
                    sbResult.append(data);
                }
                inputStream.close();
                String jsonResult = sbResult.toString();
                Log.d(TAG, "detail() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    mediaBean = parseJsonToDetailMediaBean(jsonResult);
                }
            }
            client.getConnectionManager().shutdown();
            return mediaBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<MediaBean> getRelates(int columnId, String mediaId, int size, String lang) {
        ArrayList<MediaBean> mediaList = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "getRelates(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/media/relate")).append("?id=").append(mediaId).toString())).append("&columnid=").append(columnId).toString())).append("&size=").append(size).toString())).append("&lang=").append(lang).toString())).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "getRelates() reqUri =" + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpResponse response = client.execute(new HttpGet(reqUri));
            Log.d(TAG, "getRelates() StatusCode = " + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuffer sbResult = new StringBuffer();
                String str = XmlPullParser.NO_NAMESPACE;
                while (true) {
                    String data = br.readLine();
                    if (data == null) {
                        break;
                    }
                    sbResult.append(data);
                }
                inputStream.close();
                String jsonResult = sbResult.toString();
                Log.d(TAG, "getRelates() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    mediaList = parseJsonToSimpleMediaList(jsonResult);
                }
            }
            client.getConnectionManager().shutdown();
            return mediaList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MediaListBean parseJsonToMediaListBean(String json) {
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONObject jo = new JSONObject(json);
                    MediaListBean mlb = new MediaListBean();
                    mlb.setPagecount(jo.getInt("pagecount"));
                    mlb.setTotalcount(jo.getInt("totalcount"));
                    mlb.setList(parseJsonToSimpleMediaList(jo.getString("list")));
                    return mlb;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<MediaBean> parseJsonToSimpleMediaList(String json) {
        ArrayList<MediaBean> mediaList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.optJSONObject(i);
                        if (item != null) {
                            MediaBean mb = new MediaBean();
                            mb.setId(item.getString("id"));
                            mb.setMeta(item.getInt("meta"));
                            mb.setTitle(item.getString("title"));
                            mb.setImage(item.getString("image"));
                            mb.setThumbnail(item.getString("thumbnail"));
                            mb.setPoster(item.optString("poster"));
                            mb.setScore(item.getInt(MediaManager.SORT_BY_SCORE));
                            mb.setColumnId(item.getInt("columnId"));
                            int meta = mb.getMeta();
                            switch (meta) {
                                case 0:
                                case 5:
                                case 7:
                                    mb.setChannelNumber(item.getInt("channelNumber"));
                                    mb.setCategory(item.getString("category"));
                                    mb.setArea(item.getString("area"));
                                    mb.setPrice(item.getInt("price"));
                                    mb.setDuration(item.getInt("duration"));
                                    mb.setUrls(parseJsonToUrlList(item.getString("urls")));
                                    mb.setAds(parseJsonToAdList(item.getString("ads")));
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 6:
                                    try {
                                        mb.setProvider(item.getString("provider"));
                                    } catch (Exception e) {
                                        Log.d(TAG, mb.getTitle() + " get no provider");
                                    }
                                    mb.setActor(item.getString("actor"));
                                    if (meta == 2 || meta == 3 || meta == 4) {
                                        mb.setTotalSerial(item.getInt("totalSerial"));
                                        mb.setCurSerial(item.getInt("curSerial"));
                                    }
                                    mb.setCategory(item.getString("category"));
                                    mb.setArea(item.getString("area"));
                                    mb.setTag(item.getString(MediaManager.SORT_BY_TAG));
                                    mb.setPlayCount(item.getInt("playCount"));
                                    mb.setPrice(item.getInt("price"));
                                    mb.setDuration(item.getInt("duration"));
                                    break;
                                case 10:
                                    mb.setPrice(item.getInt("price"));
                                    mb.setDuration(item.getInt("duration"));
                                    break;
                                case 20:
                                    mb.setUrls(parseJsonToUrlList(item.getString("urls")));
                                    break;
                                case 30:
                                    mb.setVersionName(item.getString("versionName"));
                                    mb.setVersionCode(item.getString("versionCode"));
                                    mb.setPackageName(item.getString("packageName"));
                                    mb.setByteLen(item.getLong("byteLen"));
                                    mb.setPrice(item.getInt("price"));
                                    mb.setDuration(item.getInt("duration"));
                                    mb.setUrls(parseJsonToUrlList(item.getString("urls")));
                                    break;
                            }
                            mediaList.add(mb);
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mediaList;
    }

    private static MediaBean parseJsonToDetailMediaBean(String json) {
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONObject jo = new JSONObject(json);
                    MediaBean mb = new MediaBean();
                    mb.setId(jo.getString("id"));
                    mb.setMeta(jo.getInt("meta"));
                    mb.setTitle(jo.getString("title"));
                    mb.setImage(jo.getString("image"));
                    mb.setThumbnail(jo.getString("thumbnail"));
                    mb.setPoster(jo.optString("poster"));
                    mb.setCategory(jo.getString("category"));
                    mb.setArea(jo.getString("area"));
                    mb.setProvider(jo.getString("provider"));
                    mb.setTag(jo.getString(MediaManager.SORT_BY_TAG));
                    mb.setScore(jo.getInt(MediaManager.SORT_BY_SCORE));
                    mb.setPlayCount(jo.getInt("playCount"));
                    mb.setActor(jo.getString("actor"));
                    mb.setDirector(jo.getString("director"));
                    mb.setScreenwriter(jo.getString("screenwriter"));
                    mb.setReleasetimeUtcMs(jo.getLong("releasetime"));
                    mb.setYear(jo.getInt("year"));
                    mb.setDescription(jo.getString("description"));
                    mb.setDialogue(jo.getString("dialogue"));
                    mb.setTotalSerial(jo.getInt("totalSerial"));
                    mb.setCurSerial(jo.getInt("curSerial"));
                    mb.setByteLen(jo.getLong("byteLen"));
                    mb.setTimeLen(jo.getInt("timeLen"));
                    mb.setBitrate(jo.getInt("bitrate"));
                    mb.setRecommendLevel(jo.getInt("recommendLevel"));
                    mb.setLimitLevel(jo.getInt("limitLevel"));
                    mb.setChannelNumber(jo.getInt("channelNumber"));
                    mb.setSupportPlayback(jo.getInt("supportPlayback"));
                    mb.setVersionName(jo.getString("versionName"));
                    mb.setVersionCode(jo.getString("versionCode"));
                    mb.setPackageName(jo.getString("packageName"));
                    mb.setPrice(jo.getInt("price"));
                    mb.setDuration(jo.getInt("duration"));
                    mb.setColumnId(jo.getInt("columnId"));
                    mb.setPagecount(jo.getInt("pagecount"));
                    mb.setTotalcount(jo.getInt("totalcount"));
                    mb.setUrls(parseJsonToUrlList(jo.getString("urls")));
                    mb.setAds(parseJsonToAdList(jo.getString("ads")));
                    return mb;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<UrlBean> parseJsonToUrlList(String json) {
        ArrayList<UrlBean> urlList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.optJSONObject(i);
                        if (item != null) {
                            UrlBean urlBean = new UrlBean();
                            urlBean.setUrl(item.getString(DownLoadConfigUtil.KEY_URL));
                            urlBean.setTitle(item.getString("title"));
                            urlBean.setDescription(item.getString("description"));
                            urlBean.setSerial(item.getInt("serial"));
                            urlBean.setQuality(item.getInt("quality"));
                            urlBean.setProvider(item.getString("provider"));
                            urlBean.setIsfinal(item.getBoolean("isfinal"));
                            urlBean.setImage(item.getString("image"));
                            urlBean.setThumbnail(item.getString("thumbnail"));
                            urlList.add(urlBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    private static ArrayList<AdBean> parseJsonToAdList(String json) {
        ArrayList<AdBean> adList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.optJSONObject(i);
                        if (item != null) {
                            AdBean adBean = new AdBean();
                            adBean.setId(item.getInt("id"));
                            adBean.setMeta(item.getInt("meta"));
                            adBean.setType(item.getInt(MailDbHelper.TYPE));
                            adBean.setTitle(item.getString("title"));
                            adBean.setContent(item.getString(MailDbHelper.CONTENT));
                            adBean.setDuration(item.getInt("duration"));
                            adBean.setExtend(item.getString("extend"));
                            adList.add(adBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adList;
    }
}
