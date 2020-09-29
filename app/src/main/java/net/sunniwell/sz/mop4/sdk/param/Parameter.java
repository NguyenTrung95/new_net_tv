package net.sunniwell.sz.mop4.sdk.param;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.param.Parameter */
public class Parameter {
    private static final String TAG = "Parameter";
    private static String mConfigDir = null;
    private static String mConfigPath = null;
    private static Properties mProperty = null;

    public static void init(String path) {
        if (path != null) {
            mConfigDir = path;
            mConfigDir = mConfigDir.replaceAll("//", "/");
            mConfigPath = new StringBuilder(String.valueOf(path)).append("/config.txt").toString();
            File dir = new File(mConfigDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            load();
        }
        Log.d(TAG, "Parameter mConfigDir=" + mConfigDir + " mConfigPath=" + mConfigPath);
    }

    public static void reload() {
        load();
    }

    public static String get(String key) {
        if (mProperty == null) {
            load();
        }
        String ret = mProperty.getProperty(key);
        return ret == null ? XmlPullParser.NO_NAMESPACE : ret;
    }

    public static void set(String key, String value) {
        if (mProperty == null) {
            load();
        }
        mProperty.setProperty(key, value);
    }

    public static String getAll() {
        if (mProperty == null) {
            load();
        }
        String allParam = XmlPullParser.NO_NAMESPACE;
        for (String key : mProperty.keySet()) {
            allParam = new StringBuilder(String.valueOf(allParam)).append(key).append("=").append(mProperty.get(key)).append("\n").toString();
        }
        return allParam;
    }

    public static void clearCache() {
        save();
    }

    public static void save() {
        if (mConfigPath != null) {
            try {
                FileOutputStream fos = new FileOutputStream(mConfigPath);
                mProperty.store(fos, null);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private static boolean load() {
        mProperty = new Properties();
        default_param();
        if (mConfigPath == null || mConfigDir == null) {
            return false;
        }
        try {
            File file = new File(mConfigPath);
            if (file == null || !file.exists()) {
                File dir = new File(mConfigDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                Runtime.getRuntime().exec("chmod -R 777 " + dir.toString());
                file.createNewFile();
                Runtime.getRuntime().exec("chmod 777 " + file.toString());
            }
            FileInputStream fis = new FileInputStream(mConfigPath);
            mProperty.load(fis);
            fis.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private static void default_param() {
        mProperty.setProperty("language", DefaultParam.language);
        mProperty.setProperty("terminal_type", DefaultParam.terminal_type);
        mProperty.setProperty("project_id", DefaultParam.project_id);
        mProperty.setProperty("user", DefaultParam.user);
        mProperty.setProperty("password", DefaultParam.password);
        mProperty.setProperty("soft_ver", DefaultParam.soft_ver);
        mProperty.setProperty("hard_ver", DefaultParam.hard_ver);
        mProperty.setProperty("netmode", DefaultParam.netmode);
        mProperty.setProperty("is_login", DefaultParam.is_login);
        mProperty.setProperty("auto_login", DefaultParam.auto_login);
        mProperty.setProperty("remember_password", DefaultParam.remember_password);
        mProperty.setProperty("authen_enable", DefaultParam.authen_enable);
        mProperty.setProperty("ad_enable", DefaultParam.ad_enable);
        mProperty.setProperty("ois", DefaultParam.ois);
        mProperty.setProperty("epgs", DefaultParam.epgs);
        mProperty.setProperty("epg", DefaultParam.epg);
        mProperty.setProperty("upgrade_url", DefaultParam.upgrade_url);
        mProperty.setProperty("protocol", DefaultParam.protocol);
        mProperty.setProperty("apk_versioncode", DefaultParam.apk_versioncode);
    }
}
