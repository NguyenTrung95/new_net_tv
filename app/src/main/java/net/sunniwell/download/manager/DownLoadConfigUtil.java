package net.sunniwell.download.manager;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import net.sunniwell.download.common.BaseApplication;
import net.sunniwell.download.common.TAStringUtils;
import org.xmlpull.v1.XmlPullParser;

public class DownLoadConfigUtil {
    public static final String KEY_URL = "url";
    public static final String PREFERENCE_NAME = "com.yyxu.download";
    public static final int URL_COUNT = 3;

    public static void storeURL(int index, String url) {
        Log.d("DownLoadConfigUtil", "==index==" + index + "==url==" + url);
        BaseApplication.getApplication().setPreference(new StringBuilder(KEY_URL).append(index).toString(), url);
    }

    public static void clearURL(int index) {
        BaseApplication.getApplication().removePreference(new StringBuilder(KEY_URL).append(index).toString());
    }

    public static String getURL(int index) {
        return BaseApplication.getApplication().getPreference(new StringBuilder(KEY_URL).append(index).toString(), XmlPullParser.NO_NAMESPACE);
    }

    public static List<String> getURLArray() {
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (!TAStringUtils.isEmpty(getURL(i))) {
                urlList.add(getString(new StringBuilder(KEY_URL).append(i).toString()));
            }
        }
        return urlList;
    }

    private static String getString(String key) {
        return BaseApplication.getApplication().getPreference(key, XmlPullParser.NO_NAMESPACE);
    }
}
