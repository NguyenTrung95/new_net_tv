package net.sunniwell.sz.mop4.sdk.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.regex.Pattern;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.util.StringUtil */
public class StringUtil {
    private static final String TAG = "StringUtil";
    private static Context mContext = null;

    public static void init(Context context) {
        mContext = context;
    }

    public static String getString(int resId) {
        if (mContext != null) {
            return mContext.getString(resId);
        }
        return null;
    }

    public static String[] getStringArray(int resId) {
        if (mContext != null) {
            return mContext.getResources().getStringArray(resId);
        }
        return null;
    }

    public static String getCacheDir() {
        String ret = null;
        if (mContext != null) {
            File file = mContext.getExternalCacheDir();
            if (file == null) {
                ret = new StringBuilder(String.valueOf(mContext.getCacheDir().getAbsolutePath())).append("/").toString();
            } else {
                ret = file.getAbsolutePath() + "/";
            }
        }
        if (ret == null) {
            ret = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/sunniwell/cache/").toString();
            File file2 = new File(ret);
            if (!file2.exists()) {
                file2.mkdirs();
            }
        }
        return ret;
    }

    public static String getFilesDir() {
        String ret = null;
        if (mContext != null) {
            File file = mContext.getExternalFilesDir(null);
            if (file == null) {
                ret = mContext.getFilesDir().getAbsolutePath();
            } else {
                ret = file.getAbsolutePath();
            }
        }
        if (ret == null) {
            ret = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/sunniwell/files/").toString();
            File file2 = new File(ret);
            if (!file2.exists()) {
                file2.mkdirs();
            }
        }
        return ret;
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static double string2double(String str) {
        double d = 0.0d;
        if (str == null) {
            return d;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, new StringBuilder(String.valueOf(str)).append(" is not a valid double string!").toString());
            return d;
        }
    }

    public static float string2float(String str) {
        float f = 0.0f;
        if (str == null) {
            return f;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, new StringBuilder(String.valueOf(str)).append(" is not a valid float string!").toString());
            return f;
        }
    }

    public static int string2int(String str) {
        int i = 0;
        if (str == null) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, new StringBuilder(String.valueOf(str)).append(" is not a valid int string!").toString());
            return i;
        }
    }

    public static long string2long(String str) {
        long j = 0;
        if (str == null) {
            return j;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, new StringBuilder(String.valueOf(str)).append(" is not a valid long string!").toString());
            return j;
        }
    }

    public static int getInt(String str) {
        String str2 = str.trim();
        String str22 = XmlPullParser.NO_NAMESPACE;
        if (str2 != null && !XmlPullParser.NO_NAMESPACE.equals(str2)) {
            for (int i = 0; i < str2.length(); i++) {
                if (str2.charAt(i) >= '0' && str2.charAt(i) <= '9') {
                    str22 = new StringBuilder(String.valueOf(str22)).append(str2.charAt(i)).toString();
                } else if (str22.length() > 0) {
                    break;
                }
            }
        }
        if (str22.length() > 0) {
            return Integer.valueOf(str22).intValue();
        }
        return -1;
    }
}
