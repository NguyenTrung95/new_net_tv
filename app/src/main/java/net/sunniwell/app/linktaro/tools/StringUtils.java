package net.sunniwell.app.linktaro.tools;

import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class StringUtils {
    public static final int INDEX_NOT_FOUND = -1;

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static int indexOf(String str, char searchChar) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.indexOf(searchChar);
    }

    public static int indexOf(String str, char searchChar, int startPos) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.indexOf(searchChar, startPos);
    }

    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }

    public static int ordinalIndexOf(String str, String searchStr, int ordinal) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return 0;
        }
        int found = 0;
        int index = -1;
        do {
            index = str.indexOf(searchStr, index + 1);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    public static int indexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (searchStr.length() != 0 || startPos < str.length()) {
            return str.indexOf(searchStr, startPos);
        }
        return str.length();
    }

    public static int lastIndexOf(String str, char searchChar) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(searchChar);
    }

    public static int lastIndexOf(String str, char searchChar, int startPos) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(searchChar, startPos);
    }

    public static int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr);
    }

    public static int lastIndexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr, startPos);
    }

    public static boolean contains(String str, char searchChar) {
        if (!isEmpty(str) && str.indexOf(searchChar) >= 0) {
            return true;
        }
        return false;
    }

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null || str.indexOf(searchStr) < 0) {
            return false;
        }
        return true;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return contains(str.toUpperCase(), searchStr.toUpperCase());
    }

    public static String capitalize(String str) {
        if (str == null) {
            return str;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return str;
        }
        return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    public static String subStringByByte(String str, int bytes) {
        int cutLength = 0;
        int byteNum = bytes;
        byte[] bt = str.getBytes();
        if (bytes > 1) {
            for (int i = 0; i < byteNum; i++) {
                if (bt[i] < 0) {
                    cutLength++;
                }
            }
            if (cutLength % 2 == 0) {
                cutLength /= 2;
            } else {
                cutLength = 0;
            }
        }
        int result = cutLength + (byteNum - 1);
        if (result > bytes) {
            result = bytes;
        }
        if (bytes == 1) {
            if (bt[0] < 0) {
                result += 2;
            } else {
                result++;
            }
        }
        return new String(bt, 0, result);
    }

    public static String subStringByByte_(String str, int end) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char[] c = str.toCharArray();
        String result = XmlPullParser.NO_NAMESPACE;
        int curLen = 0;
        for (int i = 0; i < c.length; i++) {
            curLen += String.valueOf(c[i]).getBytes().length;
            if (end >= curLen) {
                result = new StringBuilder(String.valueOf(result)).append(String.valueOf(c[i])).toString();
            }
        }
        return result;
    }

    public static boolean isNumberStr(String str) {
        if (str != null) {
            return Pattern.compile("^-?\\d+|(\\d+.{1}\\d+)$").matcher(str).matches();
        }
        return false;
    }

    public static String getTimeStr(long pos) {
        int second;
        int minute;
        int hour;
        String str = "00:00:00";
        if (pos == 0 || pos / 60 < 1) {
            second = (int) pos;
            minute = 0;
            hour = 0;
        } else {
            second = ((int) pos) % 60;
            long min = pos / 60;
            if (min / 60 >= 1) {
                minute = ((int) min) % 60;
                hour = ((int) min) / 60;
            } else {
                minute = (int) min;
                hour = 0;
            }
        }
        return new StringBuilder(String.valueOf(hour < 10 ? "0" + hour + ":" : new StringBuilder(String.valueOf(hour)).append(":").toString())).append(minute < 10 ? "0" + minute + ":" : new StringBuilder(String.valueOf(minute)).append(":").toString()).append(second < 10 ? "0" + second : new StringBuilder(String.valueOf(second)).toString()).toString();
    }

    public static String getDateStr(String date) {
        String str = XmlPullParser.NO_NAMESPACE;
        if (date == null || date.isEmpty()) {
            return XmlPullParser.NO_NAMESPACE;
        }
        int mon = Math.abs(Integer.parseInt(date.substring(0, 2)));
        int day = Math.abs(Integer.parseInt(date.substring(2)));
        return new StringBuilder(String.valueOf(mon)).append("月").append(day < 10 ? "0" + day : Integer.valueOf(day)).append("日").toString();
    }

    public static String getDateStr2(String date) {
        String str = XmlPullParser.NO_NAMESPACE;
        if (date == null || date.isEmpty()) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return date.substring(0, 2) + "-" + date.substring(2) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
    }

    public static long getTimeMillions(String time) {
        long millions = 0;
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return millions;
        }
    }
}
