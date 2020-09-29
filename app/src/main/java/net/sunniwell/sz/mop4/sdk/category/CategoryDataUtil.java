package net.sunniwell.sz.mop4.sdk.category;

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

/* renamed from: net.sunniwell.sz.mop4.sdk.category.CategoryDataUtil */
public class CategoryDataUtil {
    private static final String TAG = "CategoryDataUtil";

    public static ArrayList<CategoryBean> get(int columnId, String lang) {
        ArrayList<CategoryBean> categoryList = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "get(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/" + Parameter.get("epg") + "/category/get")).append("?columnid=").append(columnId).append("&lang=").append(lang).append("&token=").append(SoapClient.getEpgsToken()).toString();
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
                    categoryList = parseJsonToList(jsonResult);
                }
            }
            return categoryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<CategoryBean> parseJsonToList(String json) {
        ArrayList<CategoryBean> categoryList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.optJSONObject(i);
                        if (item != null) {
                            CategoryBean categoryBean = new CategoryBean();
                            categoryBean.setId(item.getInt("id"));
                            categoryBean.setTitle(item.getString("title"));
                            categoryBean.setTotal(item.getInt("total"));
                            categoryList.add(categoryBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "parseJsonToList error !!!");
            }
        }
        return categoryList;
    }
}
