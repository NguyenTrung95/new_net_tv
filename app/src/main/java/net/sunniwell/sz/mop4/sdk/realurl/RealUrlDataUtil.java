package net.sunniwell.sz.mop4.sdk.realurl;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import net.sunniwell.app.linktaro.nettv.Constants.PageEpgConstants;
import net.sunniwell.download.manager.DownLoadConfigUtil;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.realurl.RealUrlDataUtil */
public class RealUrlDataUtil {
    private static final String TAG = "RealUrlDataUtil";

    public static RealUrlBean getRealUrl(String url, int quality) {
        RealUrlBean rub = null;
        try {
            if (TextUtils.isEmpty(SoapClient.getEpgs()) || TextUtils.isEmpty(SoapClient.getEpgsToken())) {
                Log.e(TAG, "getRealUrl(), epgs = " + SoapClient.getEpgs() + ", epgstoken = " + SoapClient.getEpgsToken());
                return null;
            }
            if (quality > 5 || quality <= 0) {
                quality = 3;
            }
            String reqUri = new StringBuilder(String.valueOf("http://" + SoapClient.getEpgs() + "/epgs/realurl/get")).append("?url=").append(URLEncoder.encode(url, "UTF-8")).append("&quality=").append(quality).append("&token=").append(SoapClient.getEpgsToken()).toString();
            Log.d(TAG, "getRealUrl() reqUri =" + reqUri);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(httpParams, PageEpgConstants.PZQB_HIDE_TIME);
            HttpResponse response = new DefaultHttpClient(httpParams).execute(new HttpGet(reqUri));
            Log.d(TAG, "getRealUrl() StatusCode = " + response.getStatusLine().getStatusCode());
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
                Log.d(TAG, "getRealUrl() jsonResult = " + jsonResult);
                if (jsonResult != null && !jsonResult.equals(XmlPullParser.NO_NAMESPACE) && !jsonResult.startsWith("invalid")) {
                    rub = parseJsonToBean(jsonResult);
                    if (!(rub == null || rub.getQualitys() == null)) {
                        if (rub.getQualitys().contains(quality)) {
                            rub.setQuality(quality);
                        } else {
                            String[] arrayQuality = rub.getQualitys().split("\\|");
                            rub.setQuality(Integer.valueOf(arrayQuality[arrayQuality.length - 1]).intValue());
                        }
                    }
                }
            }
            return rub;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static RealUrlBean parseJsonToBean(String json) {
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONObject jo = new JSONObject(json);
                    RealUrlBean rub = new RealUrlBean();
                    rub.setUrl(jo.getString(DownLoadConfigUtil.KEY_URL));
                    rub.setTitle(jo.getString("title"));
                    rub.setQualitys(jo.getString("qualitys"));
                    return rub;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
