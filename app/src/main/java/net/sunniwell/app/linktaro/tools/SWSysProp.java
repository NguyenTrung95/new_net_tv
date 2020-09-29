package net.sunniwell.app.linktaro.tools;

import android.content.Context;
import android.provider.Settings.Secure;
import net.sunniwell.common.log.SWLogger;
import org.xmlpull.v1.XmlPullParser;

public class SWSysProp {
    private static SWLogger LOG = SWLogger.getLogger(SWSysProp.class);
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static String getStringParam(String param, String def) {
        String str = XmlPullParser.NO_NAMESPACE;
        try {
            String temp = Secure.getString(mContext.getContentResolver(), param);
            if (temp == null) {
                return def;
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    public static int getIntParam(String param, int def) {
        try {
            LOG.mo8825d("[getIntParam]" + param);
            return Secure.getInt(mContext.getContentResolver(), param, 0);
        } catch (Exception e) {
            return def;
        }
    }

    public static void setIntParam(String param, int value) {
        try {
            LOG.mo8825d("[setIntParam]" + param);
            Secure.putInt(mContext.getContentResolver(), param, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setStringParam(String param, String value) {
        try {
            LOG.mo8825d("[setStringParam]" + param);
            Secure.putString(mContext.getContentResolver(), param, value);
        } catch (Exception e) {
        }
    }
}
