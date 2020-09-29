package net.sunniwell.app.linktaro.tools;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.Properties;

public class SWUtil {
    private static final String CLASSNAME = "android.os.SystemProperties";
    private static final String TAG = SWUtil.class.getSimpleName();
    private static Method sGet;
    private static Method sSet;
    private static Class<?> sSystemPorperties;

    static {
        try {
            sSystemPorperties = Class.forName(CLASSNAME);
            sGet = sSystemPorperties.getDeclaredMethod("get", new Class[]{String.class});
            sSet = sSystemPorperties.getDeclaredMethod("set", new Class[]{String.class, String.class});
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    public static <T> boolean contain(T[] elements, T element) {
        for (T e : elements) {
            if (e.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContain(String[] elements, String element) {
        for (String name : elements) {
            if (name.equals(element) || element.contains(name)) {
                return true;
            }
        }
        return false;
    }

    public static int bitSetToInt(BitSet bitset) {
        return (int) bytesToLong(bitSetToBytes(bitset));
    }

    public static byte[] bitSetToBytes(BitSet bitset) {
        byte[] bytes = new byte[((bitset.length() / 8) + 1)];
        for (int i = 0; i < bitset.length(); i++) {
            if (bitset.get(i)) {
                int length = (bytes.length - (i / 8)) - 1;
                bytes[length] = (byte) (bytes[length] | (1 << (i % 8)));
            }
        }
        return bytes;
    }

    public static long bytesToLong(byte[] bytes) {
        if (bytes.length != 8) {
            throw new IllegalArgumentException("The byte array's length should be eight");
        }
        long number = 0;
        for (int i = 0; i < bytes.length; i++) {
            number += (((long) bytes[i]) & 255) << (((bytes.length - 1) - i) * 8);
        }
        return number;
    }

    public static String[] getSystemInfo(String... args) {
        String result = XmlPullParser.NO_NAMESPACE;
        try {
            ProcessBuilder cmd = new ProcessBuilder(args);
            try {
                InputStream in = cmd.start().getInputStream();
                byte[] re = new byte[1024];
                while (true) {
                    int count = in.read(re);
                    if (count == -1) {
                        break;
                    }
                    byte[] buf = new byte[count];
                    System.arraycopy(re, 0, buf, 0, count);
                    result = new StringBuilder(String.valueOf(result)).append(new String(buf)).toString();
                    Log.d(TAG, "==result===" + result);
                }
                in.close();
                ProcessBuilder processBuilder = cmd;
            } catch (IOException e) {
                ProcessBuilder processBuilder2 = cmd;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return result.split("\n");
        }
        return result.split("\n");
    }

    public static String getProperty(String name) throws Exception {
        Object systemProperties = sSystemPorperties.newInstance();
        Log.d(TAG, "==systemProperties===" + systemProperties);
        Log.d(TAG, "==get===" + name);
        return (String) sGet.invoke(systemProperties, new Object[]{name});
    }

    public static String setProperty(String name, String value) throws Exception {
        Object systemProperties = sSystemPorperties.newInstance();
        Log.d(TAG, "==setProperty==" + name + "==" + value);
        return (String) sSet.invoke(systemProperties, new Object[]{name, value});
    }

    public static void storePropertyFile(String filePath, String key, String value, String comment) {
        Properties pro = new Properties();
        try {
            pro.load(new FileInputStream(filePath));
            pro.put(key, value);
            FileOutputStream fos = new FileOutputStream(filePath);
            pro.store(fos, comment);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "===key==" + value + "==exception=" + e.getMessage());
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.d(TAG, "===key==" + value + "==exception=" + e2.getMessage());
        }
    }
}
