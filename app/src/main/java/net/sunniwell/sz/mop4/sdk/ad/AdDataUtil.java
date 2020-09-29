package net.sunniwell.sz.mop4.sdk.ad;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.PageEpgConstants;
import net.sunniwell.sz.mop4.sdk.param.Parameter;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.ad.AdDataUtil */
public class AdDataUtil {
    private static final String TAG = "AdDataUtil";

    public static ArrayList<AdBean> get(int columnId) {
        ArrayList<AdBean> adList = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "get(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/ad/get")).append("?columnid=").append(columnId).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "get() url = " + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
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
                    adList = parseJsonToList(jsonResult);
                }
            }
            return adList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, ArrayList<AdBean>> getMap() {
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "getMap(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/ad/map")).append("?token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "getMap() url=" + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
            Log.d(TAG, "getMap()  StatusCode=" + response.getStatusLine().getStatusCode());
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
            Log.d(TAG, "getMap() jsonResult=" + jsonResult);
            if (jsonResult == null || jsonResult.equals(XmlPullParser.NO_NAMESPACE) || jsonResult.startsWith("invalid")) {
                return null;
            }
            return parseJsonToMap(jsonResult);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<AdBean> parseJsonToList(String json) {
        ArrayList<AdBean> adList = new ArrayList<>();
        if (json == null) {
            return adList;
        }
        try {
            if (json.isEmpty()) {
                return adList;
            }
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
            return adList;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "parseJsonToList error adList.size()=" + adList.size());
            return null;
        }
    }

    private static HashMap<Integer, ArrayList<AdBean>> parseJsonToMap(String json) {
        HashMap hashMap = null;
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            JSONObject jo = new JSONObject(json);
            Iterator<String> it = jo.keys();
            HashMap hashMap2 = null;
            while (it.hasNext()) {
                try {
                    String key = (String) it.next();
                    if (key != null && !key.equals(XmlPullParser.NO_NAMESPACE)) {
                        ArrayList<AdBean> adList = parseJsonToList(jo.getString(key));
                        if (adList != null) {
                            if (hashMap2 == null) {
                                hashMap = new HashMap();
                            } else {
                                hashMap = hashMap2;
                            }
                            hashMap.put(Integer.valueOf(Integer.parseInt(key)), adList);
                            hashMap2 = hashMap;
                        }
                    }
                } catch (Exception e) {
                    e = e;
                    hashMap = hashMap2;
                    e.printStackTrace();
                    Log.d(TAG, "parseJsonToMap error !!");
                    return hashMap;
                }
            }
            return hashMap2;
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            Log.d(TAG, "parseJsonToMap error !!");
            return hashMap;
        }
    }
}
