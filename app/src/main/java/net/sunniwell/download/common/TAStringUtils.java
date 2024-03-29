package net.sunniwell.download.common;

import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import net.sunniwell.common.tools.DateTime;
import org.apache.commons.net.SocketClient;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class TAStringUtils {
    private static final String _BR = "<br/>";
    private static final ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        /* access modifiers changed from: protected */
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static final ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        /* access modifiers changed from: protected */
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private static final Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    public static String subString(String str, int length) throws Exception {
        byte[] bytes = str.getBytes("Unicode");
        int n = 0;
        int i = 2;
        while (i < bytes.length && n < length) {
            if (i % 2 == 1) {
                n++;
            } else if (bytes[i] != 0) {
                n++;
            }
            i++;
        }
        if (i % 2 == 1) {
            if (bytes[i - 1] != 0) {
                i--;
            } else {
                i++;
            }
        }
        return new String(bytes, 0, i, "Unicode");
    }

    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = ' ';
            } else if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    public static long calculateWeiboLength(CharSequence c) {
        double d;
        double len = 0.0d;
        for (int i = 0; i < c.length(); i++) {
            int temp = c.charAt(i);
            if (temp <= 0 || temp >= 127) {
                d = 1.0d;
            } else {
                d = 0.5d;
            }
            len += d;
        }
        return Math.round(len);
    }

    public static String[] split(String str, String splitsign) {
        if (str == null || splitsign == null) {
            return null;
        }
        ArrayList<String> al = new ArrayList<>();
        while (true) {
            int index = str.indexOf(splitsign);
            if (index == -1) {
                al.add(str);
                return (String[]) al.toArray(new String[0]);
            }
            al.add(str.substring(0, index));
            str = str.substring(splitsign.length() + index);
        }
    }

    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null) {
            return null;
        }
        StringBuffer bf = new StringBuffer(XmlPullParser.NO_NAMESPACE);
        while (true) {
            int index = source.indexOf(from);
            if (index == -1) {
                bf.append(source);
                return bf.toString();
            }
            bf.append(source.substring(0, index) + to);
            source = source.substring(from.length() + index);
            int index2 = source.indexOf(from);
        }
    }

    public static String htmlencode(String str) {
        if (str == null) {
            return null;
        }
        return replace("\"", "&quot;", replace("<", "&lt;", str));
    }

    public static String htmldecode(String str) {
        if (str == null) {
            return null;
        }
        return replace("&quot;", "\"", replace("&lt;", "<", str));
    }

    public static String htmlshow(String str) {
        if (str == null) {
            return null;
        }
        return replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", replace("\n", _BR, replace(SocketClient.NETASCII_EOL, _BR, replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, "&nbsp;", replace("<", "&lt;", str)))));
    }

    public static String toLength(String str, int length) {
        if (str == null) {
            return null;
        }
        if (length <= 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        try {
            if (str.getBytes("GBK").length <= length) {
                return str;
            }
        } catch (Exception e) {
        }
        StringBuffer buff = new StringBuffer();
        int index = 0;
        int length2 = length - 3;
        while (length2 > 0) {
            char c = str.charAt(index);
            if (c >= 128) {
                length2--;
            }
            length2--;
            buff.append(c);
            index++;
        }
        buff.append("...");
        return buff.toString();
    }

    public static String getUrlFileName(String urlString) {
        String fileName = urlString.substring(urlString.lastIndexOf("/"));
        String fileName2 = fileName.substring(1, fileName.length());
        if (!fileName2.equalsIgnoreCase(XmlPullParser.NO_NAMESPACE)) {
            return fileName2;
        }
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.MINUTE) + "";
    }

    public static String replaceSomeString(String str) {
        String dest = XmlPullParser.NO_NAMESPACE;
        if (str == null) {
            return dest;
        }
        try {
            return str.replaceAll("\r", XmlPullParser.NO_NAMESPACE).replaceAll("&gt;", ">").replaceAll("&ldquo;", "“").replaceAll("&rdquo;", "”").replaceAll("&#39;", "'").replaceAll("&nbsp;", XmlPullParser.NO_NAMESPACE).replaceAll("<br\\s*/>", "\n").replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&lsquo;", "《").replaceAll("&rsquo;", "》").replaceAll("&middot;", "·").replace("&mdash;", "—").replace("&hellip;", "…").replace("&amp;", "×").replaceAll("\\s*", XmlPullParser.NO_NAMESPACE).trim().replaceAll("<p>", "\n      ").replaceAll("</p>", XmlPullParser.NO_NAMESPACE).replaceAll("<div.*?>", "\n      ").replaceAll("</div>", XmlPullParser.NO_NAMESPACE);
        } catch (Exception e) {
            return dest;
        }
    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        String regEx_html = "<[^>]+>";
        Log.v("htmlStr", htmlStr);
        try {
            return Pattern.compile(regEx_html, 2).matcher(Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE).matcher(Pattern.compile(regEx_script, 2).matcher(htmlStr).replaceAll(XmlPullParser.NO_NAMESPACE)).replaceAll(XmlPullParser.NO_NAMESPACE)).replaceAll(XmlPullParser.NO_NAMESPACE);
        } catch (Exception e) {
            return htmlStr;
        }
    }

    public static String delSpace(String str) {
        if (str != null) {
            return str.replaceAll("\r", XmlPullParser.NO_NAMESPACE).replaceAll("\n", XmlPullParser.NO_NAMESPACE).replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
        }
        return str;
    }

    public static boolean isNotNull(String str) {
        return str != null && !XmlPullParser.NO_NAMESPACE.equalsIgnoreCase(str.trim());
    }

    public static Date toDate(String sdate) {
        try {
            return ((SimpleDateFormat) dateFormater.get()).parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String str = XmlPullParser.NO_NAMESPACE;
        Calendar cal = Calendar.getInstance();
        if (((SimpleDateFormat) dateFormater2.get()).format(cal.getTime()).equals(((SimpleDateFormat) dateFormater2.get()).format(time))) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / DateTime.MILLIS_PER_HOUR);
            if (hour == 0) {
                return new StringBuilder(String.valueOf(Math.max((cal.getTimeInMillis() - time.getTime()) / DateTime.MILLIS_PER_MINUTE, 1))).append("分钟前").toString();
            }
            return new StringBuilder(String.valueOf(hour)).append("小时前").toString();
        }
        int days = (int) ((cal.getTimeInMillis() / DateTime.MILLIS_PER_DAY) - (time.getTime() / DateTime.MILLIS_PER_DAY));
        if (days == 0) {
            int hour2 = (int) ((cal.getTimeInMillis() - time.getTime()) / DateTime.MILLIS_PER_HOUR);
            if (hour2 == 0) {
                return new StringBuilder(String.valueOf(Math.max((cal.getTimeInMillis() - time.getTime()) / DateTime.MILLIS_PER_MINUTE, 1))).append("分钟前").toString();
            }
            return new StringBuilder(String.valueOf(hour2)).append("小时前").toString();
        } else if (days == 1) {
            return "昨天";
        } else {
            if (days == 2) {
                return "前天";
            }
            if (days > 2 && days <= 10) {
                return new StringBuilder(String.valueOf(days)).append("天前").toString();
            }
            if (days > 10) {
                return ((SimpleDateFormat) dateFormater2.get()).format(time);
            }
            return str;
        }
    }

    public static String trimmy(String str) {
        String dest = XmlPullParser.NO_NAMESPACE;
        if (str != null) {
            return str.replaceAll("-", XmlPullParser.NO_NAMESPACE).replaceAll("\\+", XmlPullParser.NO_NAMESPACE);
        }
        return dest;
    }

    public static String replaceBlank(String str) {
        String dest = XmlPullParser.NO_NAMESPACE;
        if (str != null) {
            return Pattern.compile("\r").matcher(str).replaceAll(XmlPullParser.NO_NAMESPACE);
        }
        return dest;
    }

    public static boolean isToday(String sdate) {
        Date time = toDate(sdate);
        Date today = new Date();
        if (time == null || !((SimpleDateFormat) dateFormater2.get()).format(today).equals(((SimpleDateFormat) dateFormater2.get()).format(time))) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String input) {
        if (input == null || XmlPullParser.NO_NAMESPACE.equals(input)) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != 9 && c != 13 && c != 10) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return false;
        }
        return emailer.matcher(email).matches();
    }

    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static int toInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        return toInt(obj.toString(), 0);
    }

    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isHandset(String handset) {
        try {
            if (handset.substring(0, 1).equals("1") && handset != null && handset.length() == 11 && Pattern.compile("^[0123456789]+$").matcher(handset).matches()) {
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean isChinese(String str) {
        return Pattern.compile("[Α-￥]+$").matcher(str).matches();
    }

    public static boolean isNumeric(String str) {
        if (!Pattern.compile("[0-9]*").matcher(str).matches()) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        return Pattern.compile("^[-\\+]?[\\d]*$").matcher(str).matches();
    }

    public static boolean isDouble(String str) {
        return Pattern.compile("^[-\\+]?[.\\d]*$").matcher(str).matches();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isLenghtStrLentht(String text, int lenght) {
        if (text.length() <= lenght) {
            return true;
        }
        return false;
    }

    public static boolean isSMSStrLentht(String text) {
        if (text.length() <= 70) {
            return true;
        }
        return false;
    }

    public static boolean checkEmail(String email) {
        if (Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$").matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isShareStrLentht(String text, int lenght) {
        if (text.length() <= 120) {
            return true;
        }
        return false;
    }

    public static String getFileNameFromUrl(String url) {
        String extName;
        String str = XmlPullParser.NO_NAMESPACE;
        int index = url.lastIndexOf(63);
        if (index > 1) {
            extName = url.substring(url.lastIndexOf(46) + 1, index);
        } else {
            extName = url.substring(url.lastIndexOf(46) + 1);
        }
        return hashKeyForDisk(url) + "." + extName;
    }

    public static String hashKeyForDisk(String key) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            return bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(key.hashCode());
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & SmileConstants.BYTE_MARKER_END_OF_CONTENT);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
