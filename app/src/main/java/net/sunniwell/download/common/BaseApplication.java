package net.sunniwell.download.common;

import android.app.Application;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private static BaseApplication application;
    public final String PERMISSION_DENIED = "1";

    public void onCreate() {
        Log.d(TAG, "...onCreate...");
        super.onCreate();
        application = this;
    }

    public static BaseApplication getApplication() {
        return application;
    }

    public String getPreference(String key, String defaultValue) {
        return getSharedPreferences("down_load", 0).getString(key, defaultValue);
    }

    public void setPreference(String key, String value) {
        Editor editor = getSharedPreferences("down_load", 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void removePreference(String key) {
        Editor editor = getSharedPreferences("down_load", 0).edit();
        editor.putString(key, XmlPullParser.NO_NAMESPACE);
        editor.commit();
    }
}
