package net.sunniwell.sz.mop4.sdk.epg;

import android.text.TextUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.PageEpgConstants;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.download.manager.DownLoadConfigUtil;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.epg.EPGDataUtil */
public class EPGDataUtil {
    public static OkHttpClient mOkHttpClient;

    public static void initHttpClient() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
    }

    public static String sync() {
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                LogcatUtils.m322e("sync(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = "http://" + SoapClient.getEpgs() + "/epgs/channel/epg/sync" + "?token=" + SoapClient.getEpgsToken();
            LogcatUtils.m321d("sync() url = " + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
            LogcatUtils.m321d("sync() StatusCode = " + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
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
            LogcatUtils.m321d("sync() jsonResult = " + jsonResult);
            if (jsonResult == null || jsonResult.equals(XmlPullParser.NO_NAMESPACE) || jsonResult.startsWith("invalid")) {
                return null;
            }
            return new JSONObject(jsonResult).getString("sync");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<EPGBean> get(String channelId, long utcMs, long endUtcMs, String lang) {
        ArrayList<EPGBean> epgList = null;
        if (utcMs > endUtcMs) {
            try {
                LogcatUtils.m323i("utc > endUtc, make sure the given params is correct!");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
            LogcatUtils.m322e("get(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
            return null;
        } else {
            String reqUri = "http://" + SoapClient.getEpgs() + "/epgs/channel/epg/get" + "?token=" + SoapClient.getEpgsToken() + "&id=" + channelId + "&utc=" + utcMs + "&endutc=" + endUtcMs + "&lang=" + lang;
            LogcatUtils.m321d("get() url = " + reqUri);
            Response response = mOkHttpClient.newCall(new Builder().url(reqUri).get().build()).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                LogcatUtils.m321d("get() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    epgList = parseJsonToList(jsonResult);
                }
            } else {
                LogcatUtils.m321d("request failed");
            }
            return epgList;
        }
    }

    public static ArrayList<EPGBean> get(String channelId, long utcMs, String lang) {
        ArrayList<EPGBean> epgList = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                LogcatUtils.m322e("get(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = "http://" + SoapClient.getEpgs() + "/epgs/channel/epg/get" + "?token=" + SoapClient.getEpgsToken() + "&id=" + channelId + "&utc=" + utcMs + "&lang=" + lang;
            LogcatUtils.m321d("get() url = " + reqUri);
            Response response = mOkHttpClient.newCall(new Builder().url(reqUri).get().build()).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                LogcatUtils.m321d("get() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    epgList = parseJsonToList(jsonResult);
                }
            } else {
                LogcatUtils.m321d("request failed");
            }
            return epgList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<EPGBean> parseJsonToList(String json) {
        ArrayList<EPGBean> epgList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        if (item != null) {
                            EPGBean epgBean = new EPGBean();
                            epgBean.setUtcMs(item.getLong("utc"));
                            epgBean.setEndUtcMs(item.getLong("endUtc"));
                            epgBean.setTitle(item.getString("title"));
                            epgBean.setType(item.getString(MailDbHelper.TYPE));
                            epgBean.setDescription(item.getString("description"));
                            epgBean.setUrls(parseJsonToUrlList(item.getString("urls")));
                            epgList.add(epgBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return epgList;
    }

    private static ArrayList<EPGUrlBean> parseJsonToUrlList(String json) {
        ArrayList<EPGUrlBean> urlList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.optJSONObject(i);
                        if (item != null) {
                            EPGUrlBean urlBean = new EPGUrlBean();
                            urlBean.setUrl(item.getString(DownLoadConfigUtil.KEY_URL));
                            urlBean.setDuration((long) item.getInt("duration"));
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
}
