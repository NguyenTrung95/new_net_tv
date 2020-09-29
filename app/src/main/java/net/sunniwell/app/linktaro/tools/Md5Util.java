package net.sunniwell.app.linktaro.tools;

import org.codehaus.jackson.smile.SmileConstants;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    protected static char[] mHexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected static MessageDigest messagedigest;

    static {
        messagedigest = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println(new StringBuilder(String.valueOf(Md5Util.class.getName())).append("初始化失败，MessageDigest不支持MD5Util。").toString());
            nsaex.printStackTrace();
        }
    }

    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        return getMD5String(password).equals(md5PwdStr);
    }

    public static String getFileMD5String(File file) throws IOException {
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        while (true) {
            int numRead = fis.read(buffer);
            if (numRead <= 0) {
                fis.close();
                return bufferToHex(messagedigest.digest());
            }
            messagedigest.update(buffer, 0, numRead);
        }
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(n * 2);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = mHexDigits[(bt & 240) >> 4];
        char c1 = mHexDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String byteToHex(byte[] b) {
        if (b == null) {
            throw new IllegalArgumentException("入参不能为空！");
        }
        String hs = XmlPullParser.NO_NAMESPACE;
        String str = XmlPullParser.NO_NAMESPACE;
        for (int n = 0; n < b.length; n++) {
            String stmp = Integer.toHexString(b[n] & SmileConstants.BYTE_MARKER_END_OF_CONTENT);
            if (stmp.length() == 1) {
                hs = new StringBuilder(String.valueOf(hs)).append("0").append(stmp).toString();
            } else {
                hs = new StringBuilder(String.valueOf(hs)).append(stmp).toString();
            }
            if (n < b.length - 1) {
                hs = new StringBuilder(String.valueOf(hs)).toString();
            }
        }
        return hs;
    }
}
