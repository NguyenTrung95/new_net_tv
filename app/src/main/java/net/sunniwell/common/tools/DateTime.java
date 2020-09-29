package net.sunniwell.common.tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.p019v1.XmlPullParser;

public class DateTime {

    /* renamed from: AM */
    public static final int f334AM = 0;
    public static final int AM_PM = 9;
    public static final int DATE = 5;
    public static final int DAY_OF_MONTH = 5;
    public static final int DAY_OF_WEEK = 7;
    public static final int DAY_OF_WEEK_IN_MONTH = 8;
    public static final int DAY_OF_YEAR = 6;
    public static final int DST_OFFSET = 16;
    public static final int ERA = 0;
    public static final int HOUR = 10;
    public static final int HOUR_OF_DAY = 11;
    public static final int MILLISECOND = 14;
    public static final long MILLIS_PER_DAY = 86400000;
    public static final long MILLIS_PER_HOUR = 3600000;
    public static final long MILLIS_PER_MINUTE = 60000;
    public static final int MINUTE = 12;
    public static final int MONTH = 2;
    public static final int SECOND = 13;
    public static final int WEEK_OF_MONTH = 4;
    public static final int WEEK_OF_YEAR = 3;
    public static final int YEAR = 1;
    public static final int ZONE_OFFSET = 15;
    private Calendar calendar;

    public DateTime() {
        this.calendar = null;
        this.calendar = Calendar.getInstance();
    }

    public DateTime(long lMillis) {
        this();
        this.calendar.setTimeInMillis(lMillis);
    }

    public DateTime(int year, int month, int day) {
        this();
        this.calendar.set(year, month, day);
    }

    public DateTime(int year, int month, int day, int hourOfDay, int minute) {
        this();
        this.calendar.set(year, month, day, hourOfDay, minute);
    }

    public DateTime(int year, int month, int day, int hourOfDay, int minute, int second) {
        this();
        this.calendar.set(year, month, day, hourOfDay, minute, second);
    }

    public DateTime(String strDateTime) {
        this.calendar = null;
        if (strDateTime == null || strDateTime.trim().length() == 0) {
            this.calendar = null;
            return;
        }
        String strDateTime2 = checkStr(strDateTime);
        this.calendar = Calendar.getInstance();
        this.calendar.set(getInt(strDateTime2, 0, 4), getInt(strDateTime2, 4, 2) - 1, getInt(strDateTime2, 6, 2), getInt(strDateTime2, 8, 2), getInt(strDateTime2, 10, 2), getInt(strDateTime2, 12, 2));
    }

    public int getYear() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(1);
    }

    public int getMonth() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(2) + 1;
    }

    public int getDay() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(5);
    }

    public int getWeek() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(7);
    }

    public int getHour() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(11);
    }

    public int getMinute() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(12);
    }

    public int getSecond() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(13);
    }

    public int getMillisecond() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(14);
    }

    public long getTime() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.getTime().getTime();
    }

    public int getTimeZone() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.get(15) / 3600000;
    }

    public String formatDefault() {
        return format("yyyy-MM-dd hh:mm:ss.SSS");
    }

    public String format(String format) {
        if (format == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return new SimpleDateFormat(format).format(new Date(getTime()));
    }

    public Timestamp getTimestamp() {
        return new Timestamp(this.calendar.getTimeInMillis());
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public long getDateTime() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.getTimeInMillis();
    }

    public long getDateTimeInMillis() {
        if (this.calendar == null) {
            return -1;
        }
        return this.calendar.getTimeInMillis();
    }

    public String getFormattedDateTime() {
        return getFormattedDateTime("yyyy年MM月dd日HH时mm分ss秒");
    }

    public String getFormatDateTime() {
        return getFormattedDateTime("yyyy-MM-dd HH:mm:ss");
    }

    public String getFormattedDateTime(String strPattern) {
        if (this.calendar == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        if (strPattern == null || strPattern.trim().length() == 0) {
            return null;
        }
        return new SimpleDateFormat(strPattern).format(this.calendar.getTime());
    }

    public DateTime getFirstDayOfMouth() {
        DateTime dt = new DateTime(getDateTimeInMillis());
        dt.set(5, this.calendar.getActualMinimum(5));
        return dt;
    }

    public DateTime getLastDayOfMouth() {
        DateTime dt = new DateTime(getDateTimeInMillis());
        dt.set(5, this.calendar.getActualMaximum(5));
        return dt;
    }

    public DateTime getFirstDayOfWeek() {
        int iDayOfWeek = this.calendar.get(7);
        DateTime dt = new DateTime(getDateTimeInMillis());
        dt.addDay(1 - iDayOfWeek);
        return dt;
    }

    public DateTime getLastDayOfWeek() {
        DateTime dt = getFirstDayOfWeek();
        dt.addDay(6);
        return dt;
    }

    public void setDateTime(long lMillis) {
        if (this.calendar != null) {
            this.calendar.setTimeInMillis(lMillis);
        }
    }

    public void setDateTime(String strDateTime) {
        if (this.calendar != null) {
            String strDateTime2 = checkStr(strDateTime);
            this.calendar.set(getInt(strDateTime2, 0, 4), getInt(strDateTime2, 4, 2) - 1, getInt(strDateTime2, 6, 2), getInt(strDateTime2, 8, 2), getInt(strDateTime2, 10, 2), getInt(strDateTime2, 12, 2));
        }
    }

    public String getDateTimeString() {
        if (this.calendar == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.calendar.get(1));
        buffer.append(padChar(this.calendar.get(2) + 1, 2));
        buffer.append(padChar(this.calendar.get(5), 2));
        buffer.append(padChar(this.calendar.get(11), 2));
        buffer.append(padChar(this.calendar.get(12), 2));
        buffer.append(padChar(this.calendar.get(13), 2));
        return buffer.toString();
    }

    public int get(int field) {
        return this.calendar.get(field);
    }

    public void set(int field, int value) {
        this.calendar.set(field, value);
    }

    public int getActualMaximum(int field) {
        return this.calendar.getActualMaximum(field);
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    public DateTime addYear(int iYear) {
        if (this.calendar == null) {
            return null;
        }
        this.calendar.add(1, iYear);
        return this;
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    public DateTime addMonth(int iMonth) {
        if (this.calendar == null) {
            return null;
        }
        this.calendar.add(2, iMonth);
        return this;
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    public DateTime addDay(int iDay) {
        if (this.calendar == null) {
            return null;
        }
        this.calendar.add(5, iDay);
        return this;
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    public DateTime addHour(int iHour) {
        if (this.calendar == null) {
            return null;
        }
        this.calendar.add(11, iHour);
        return this;
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    public DateTime addMinute(int iMinute) {
        if (this.calendar == null) {
            return null;
        }
        this.calendar.add(12, iMinute);
        return this;
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    public DateTime addSecond(int iSecond) {
        if (this.calendar == null) {
            return null;
        }
        this.calendar.add(13, iSecond);
        return this;
    }

    public static double diffDays(String strStartDate, String strEndDate) {
        return ((double) (new DateTime(strEndDate).getDateTime() - new DateTime(strStartDate).getDateTime())) / 8.64E7d;
    }

    public static double diffDays(DateTime startDate, DateTime endDate) {
        return ((double) (endDate.getDateTime() - startDate.getDateTime())) / 8.64E7d;
    }

    public static double diffHours(String strStartDate, String strEndDate) {
        return diffHours(new DateTime(strStartDate), new DateTime(strEndDate));
    }

    public static double diffHours(DateTime startDate, DateTime endDate) {
        return (((double) (endDate.getDateTime() - startDate.getDateTime())) * 1.0d) / 3600000.0d;
    }

    public static double diffMinutes(String strStartDate, String strEndDate) {
        return diffMinutes(new DateTime(strStartDate), new DateTime(strEndDate));
    }

    public static double diffMinutes(DateTime startDate, DateTime endDate) {
        return (((double) (endDate.getDateTime() - startDate.getDateTime())) * 1.0d) / 60000.0d;
    }

    public String toString() {
        return getDateTimeString();
    }

    private int getInt(String str, int iStart, int iWidth) {
        int iLen = str.length();
        if (iLen < 1 || iLen < iStart || iLen < iStart + iWidth) {
            return 0;
        }
        return new Integer(str.substring(iStart, iStart + iWidth)).intValue();
    }

    private String checkStr(String strDateTime) {
        return padChar(strDateTime.replaceAll("-", XmlPullParser.NO_NAMESPACE).replaceAll(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE).replaceAll(":", XmlPullParser.NO_NAMESPACE).replaceAll("/", XmlPullParser.NO_NAMESPACE).trim(), 14, false);
    }

    private String padChar(int iInfo, int iWidth) {
        String strInfo = String.valueOf(iInfo);
        StringBuffer sbTmp = new StringBuffer(strInfo);
        if (strInfo == null || strInfo.length() > iWidth) {
            return strInfo;
        }
        for (int i = 0; i < iWidth - strInfo.length(); i++) {
            sbTmp.insert(0, '0');
        }
        return sbTmp.toString();
    }

    private String padChar(String strInfo, int iWidth, boolean bAtBegin) {
        StringBuffer sbTmp = new StringBuffer(strInfo);
        if (strInfo == null || strInfo.length() > iWidth) {
            return strInfo;
        }
        for (int i = 0; i < iWidth - strInfo.length(); i++) {
            sbTmp.insert(bAtBegin ? 0 : sbTmp.length(), '0');
        }
        return sbTmp.toString();
    }
}
