package net.sunniwell.sz.mop4.sdk.sync;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

/* renamed from: net.sunniwell.sz.mop4.sdk.sync.SyncDataUtil */
public class SyncDataUtil {
    private static final String TAG = "SyncUtil";

    public static ArrayList<SyncBean> get() {
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "get(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/sync/get")).append("?token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "get() url =" + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
            Log.d(TAG, "get() StatusCode = " + response.getStatusLine().getStatusCode());
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
            Log.d(TAG, "get() jsonResult = " + jsonResult);
            if (jsonResult == null || jsonResult.equals(XmlPullParser.NO_NAMESPACE) || jsonResult.startsWith("invalid")) {
                return null;
            }
            return parseJsonToList(jsonResult);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "get() error !!!");
            return null;
        }
    }

    private static ArrayList<SyncBean> parseJsonToList(String json) {
        ArrayList<SyncBean> syncList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.optJSONObject(i);
                        if (item != null) {
                            SyncBean syncBean = new SyncBean();
                            syncBean.setId(item.getInt("id"));
                            syncBean.setSync(item.getInt("sync"));
                            syncList.add(syncBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "parseJsonToList error !!!");
            }
        }
        return syncList;
    }
}
