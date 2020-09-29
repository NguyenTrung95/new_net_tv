package net.sunniwell.common;

import android.os.SystemProperties;

public class SWSystemProperties {
    public static int PROP_NAME_MAX = 31;
    public static int PROP_VALUE_MAX = 91;

    public static String get(String key) {
        return SystemProperties.get(key);
    }

    public static String get(String key, String def) {
        return SystemProperties.get(key, def);
    }

    public static int getInt(String key, int def) {
        return SystemProperties.getInt(key, def);
    }

    public static long getLong(String key, long def) {
        return SystemProperties.getLong(key, def);
    }

    public static boolean getBoolean(String key, boolean def) {
        return SystemProperties.getBoolean(key, def);
    }

    public static void set(String key, String val) {
        SystemProperties.set(key, val);
    }
}
