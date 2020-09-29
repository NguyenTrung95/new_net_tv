package net.sunniwell.sz.mop4.sdk.column;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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

/* renamed from: net.sunniwell.sz.mop4.sdk.column.ColumnDataUtil */
public class ColumnDataUtil {
    private static final String TAG = "ColumnDataUtil";

    public static ArrayList<ColumnBean> getSubColumns(int pid, String lang) {
        ArrayList<ColumnBean> columnList = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "getSubColumns(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/column/get")).append("?pid=").append(pid).append("&lang=").append(lang).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "getSubColumns() url = " + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
            Log.d(TAG, "getSubColumns() StatusCode = " + response.getStatusLine().getStatusCode());
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
                Log.d(TAG, "getSubColumns() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    columnList = parseJsonToList(jsonResult);
                }
            }
            return columnList;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getSubColumns() error !!!");
        }
    }

    public static ArrayList<ColumnBean> getList(String lang) {
        ArrayList<ColumnBean> columnList = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "getList(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/column/list")).append("?lang=").append(lang).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "getList() url = " + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
            Log.d(TAG, "getList() StatusCode = " + response.getStatusLine().getStatusCode());
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
                Log.d(TAG, "getList() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    columnList = parseJsonToList(jsonResult);
                }
            }
            return columnList;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getList() error !!!");
        }
    }

    public static HashMap<Integer, ArrayList<ColumnBean>> getMap(String lang) {
        HashMap<Integer, ArrayList<ColumnBean>> columnMap = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "getMap(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/column/map")).append("?lang=").append(lang).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "getMap() url = " + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
            Log.d(TAG, "getMap() StatusCode = " + response.getStatusLine().getStatusCode());
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
                Log.d(TAG, "getMap() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    columnMap = parseJsonToMap(jsonResult);
                }
            }
            return columnMap;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getMap() error !!!");
        }
    }

    public static ColumnBean query(int pid, String title) {
        ColumnBean columnBean = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "query(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/column/info")).append("?pid=").append(pid).append("&title=").append(URLEncoder.encode(title, "UTF-8")).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "query() url =" + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
            Log.d(TAG, "query() StatusCode = " + response.getStatusLine().getStatusCode());
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
                Log.d(TAG, "query() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    columnBean = parseJsonToBean(jsonResult);
                }
            }
            return columnBean;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "query() error !!!");
        }
    }

    private static ArrayList<ColumnBean> parseJsonToList(String json) {
        ArrayList<ColumnBean> columnList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.optJSONObject(i);
                        if (item != null) {
                            ColumnBean cb = new ColumnBean();
                            cb.setId(item.getInt("id"));
                            cb.setPid(item.getInt("pid"));
                            cb.setTitle(item.getString("title"));
                            cb.setType(item.getString(MailDbHelper.TYPE));
                            cb.setLeaf(item.getBoolean("leaf"));
                            try {
                                cb.setIcon(item.getString("icon"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            columnList.add(cb);
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.d(TAG, "parseJsonToList error !!!");
            }
        }
        return columnList;
    }

    private static HashMap<Integer, ArrayList<ColumnBean>> parseJsonToMap(String json) {
        HashMap<Integer, ArrayList<ColumnBean>> map = new HashMap<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONObject jo = new JSONObject(json);
                    Iterator<String> it = jo.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        if (key != null && !key.equals(XmlPullParser.NO_NAMESPACE)) {
                            ArrayList<ColumnBean> columnList = parseJsonToList(jo.getString(key));
                            if (columnList != null && columnList.size() > 0) {
                                map.put(Integer.valueOf(Integer.parseInt(key)), columnList);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "parseJsonToMap error !!!");
            }
        }
        return map;
    }

    private static ColumnBean parseJsonToBean(String json) {
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONObject jo = new JSONObject(json);
                    ColumnBean cb = new ColumnBean();
                    cb.setId(jo.getInt("id"));
                    cb.setPid(jo.getInt("pid"));
                    cb.setTitle(jo.getString("title"));
                    cb.setType(jo.getString(MailDbHelper.TYPE));
                    cb.setLeaf(jo.getBoolean("leaf"));
                    try {
                        cb.setIcon(jo.getString("icon"));
                        return cb;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return cb;
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.d(TAG, "parseJsonToBean error !!!");
            }
        }
        return null;
    }
}
