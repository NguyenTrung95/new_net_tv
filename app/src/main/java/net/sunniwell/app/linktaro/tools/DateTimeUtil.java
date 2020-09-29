package net.sunniwell.app.linktaro.tools;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import net.sunniwell.common.tools.DateTime;

public class DateTimeUtil {
    public static String getCurDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new StringBuilder(String.valueOf(String.valueOf(year).substring(2))).append("-").append(month < 10 ? "0" + month : new StringBuilder(String.valueOf(month)).toString()).append("-").append(day < 10 ? "0" + day : new StringBuilder(String.valueOf(day)).toString()).toString();
    }

    public static String getCurrentDay() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
    }

    public static String getLimitDay() {
        int leftTime = daysBetween(getCurrentDay(), SWSysProp.getStringParam("account_time", "2000-01-01"));
        return leftTime <= 0 ? "0" : String.valueOf(leftTime);
    }

    public static String getCurDateDetails() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        return new StringBuilder(String.valueOf(month)).append("月").append(calendar.get(Calendar.DAY_OF_MONTH)).append("日").append("(").append(intToWeek(calendar.get(Calendar.DAY_OF_WEEK))).append(")").append(calendar.get(Calendar.HOUR_OF_DAY)).append("時").append(calendar.get(Calendar.MINUTE)).append("分").toString();
    }

    private static String intToWeek(int week) {
        switch (week) {
            case 1:
                return "日";
            case 2:
                return "月";
            case 3:
                return "火";
            case 4:
                return "水";
            case 5:
                return "木";
            case 6:
                return "金";
            case 7:
                return "土";
            default:
                return null;
        }
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour == 0) {
            hour = 24;
        }
        return (hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute));
    }

    public static String getCurrentTime(String type) {
        return new SimpleDateFormat(type).format(new Date(System.currentTimeMillis()));
    }

    @SuppressLint({"SimpleDateFormat"})
    public static int getTimeMillions(String time) {
        int millions = 0;
        try {
            return Integer.parseInt(new SimpleDateFormat("HHmm").format(new SimpleDateFormat("HH:mm").parse(time)));
        } catch (Exception e) {
            e.printStackTrace();
            return millions;
        }
    }

    public static int daysBetween(String smdate, String bdate) {
        long between_days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            between_days = (cal.getTimeInMillis() - time1) / DateTime.MILLIS_PER_DAY;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static long daysToUtc(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long time = 0;
        try {
            cal.setTime(sdf.parse(date));
            return cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }

    public static long getUtcTime(String startTime) {
        long utcTime = 0;
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return utcTime;
        }
    }

    public static String UtcToDate(long utcTime) {
        Date dat = new Date(utcTime);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        return new SimpleDateFormat("HH:mm").format(gc.getTime());
    }
}
