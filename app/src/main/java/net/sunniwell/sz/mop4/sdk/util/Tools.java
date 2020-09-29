package net.sunniwell.sz.mop4.sdk.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.net.util.Base64;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.util.Tools */
public class Tools {
    private static final String TAG = "Tools";

    public static boolean save(Object obj, String path) {
        if (obj == null || path == null) {
            return false;
        }
        try {
            File dir = new File(path.substring(0, path.lastIndexOf("/")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(obj);
            oos.close();
            Runtime.getRuntime().exec("chmod 777 " + file.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Object read(String path) {
        if (path == null) {
            return null;
        }
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object readObject = ois.readObject();
            ois.close();
            return readObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getObject(Context context, String dbName, String key) {
        try {
            return str2Object(context.getSharedPreferences(dbName, 0).getString(key, XmlPullParser.NO_NAMESPACE));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveObject(Context context, String dbName, String key, Object bean) {
        try {
            Editor editor = context.getSharedPreferences(dbName, 0).edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(bean);
            editor.putString(key, new String(Base64.encodeBase64(baos.toByteArray())));
            editor.commit();
            oos.close();
            Runtime.getRuntime().exec("chmod 777 " + dbName.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object str2Object(String str) {
        String productBase64 = str;
        if (productBase64 != null) {
            try {
                if (!productBase64.equals(XmlPullParser.NO_NAMESPACE)) {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(Base64.decodeBase64(productBase64.getBytes())));
                    Object bean = ois.readObject();
                    ois.close();
                    return bean;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String parseNull(String str) {
        if (str == null || str.equals("null")) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return str;
    }
}
