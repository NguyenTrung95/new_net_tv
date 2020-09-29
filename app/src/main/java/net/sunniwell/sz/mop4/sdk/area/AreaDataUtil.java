package net.sunniwell.sz.mop4.sdk.area;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import net.sunniwell.app.linktaro.nettv.Constants.PageEpgConstants;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.area.AreaDataUtil */
public class AreaDataUtil {
    private static final String TAG = "AreaUtil";

    public static ArrayList<AreaBean> get(String lang) {
        ArrayList<AreaBean> areaList = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "get(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = "http://" + SoapClient.getEpgs() + "/epgs/area/get?lang=" + lang + "&token=" + SoapClient.getEpgsToken();
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
                    areaList = parseJsonToList(jsonResult);
                }
            }
            return areaList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<AreaBean> parseJsonToList(String json) {
        ArrayList<AreaBean> areaList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        if (item != null) {
                            AreaBean areaBean = new AreaBean();
                            areaBean.setId(item.getInt("id"));
                            areaBean.setTitle(item.getString("title"));
                            areaList.add(areaBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "AreaUtil parseJsonToList error !!!");
            }
        }
        return areaList;
    }
}
