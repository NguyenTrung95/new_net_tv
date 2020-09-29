package net.sunniwell.app.linktaro.tools;

import android.util.Log;

public class LogcatUtils {
    public static final int DEBUG = 2;
    public static final int ERROR = 5;
    public static final int INFO = 3;
    public static int LEVEL = 5;
    public static final String SEPARATOR = " -> ";
    private static final String TAG = "LinkTaro";
    public static final int VERBOSE = 1;
    public static final int WARN = 4;
    public static boolean isDebug = true;

    /* renamed from: v */
    public static void m324v(String message) {
        if (LEVEL >= 1 && isDebug) {
            Log.v(TAG, getLogInfo() + message);
        }
    }

    /* renamed from: d */
    public static void m321d(String message) {
        if (LEVEL >= 2 && isDebug) {
            Log.d(TAG, getLogInfo() + message);
        }
    }

    /* renamed from: i */
    public static void m323i(String message) {
        if (LEVEL >= 3 && isDebug) {
            Log.i(TAG, getLogInfo() + message);
        }
    }

    /* renamed from: w */
    public static void m325w(String message) {
        if (LEVEL >= 4 && isDebug) {
            Log.w(TAG, getLogInfo() + message);
        }
    }

    /* renamed from: e */
    public static void m322e(String message) {
        if (LEVEL >= 5 && isDebug) {
            Log.e(TAG, getLogInfo() + message);
        }
    }

    private static String getLogInfo() {
        StackTraceElement traceElement = new Exception().getStackTrace()[2];
        return new StringBuffer().append("[").append(traceElement.getFileName()).append("-> Method:").append(traceElement.getMethodName()).append(" -> Line:").append(traceElement.getLineNumber()).append("]").toString();
    }
}
