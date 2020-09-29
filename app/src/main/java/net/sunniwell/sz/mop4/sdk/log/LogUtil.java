package net.sunniwell.sz.mop4.sdk.log;

import android.util.Log;
import net.sunniwell.sz.mop4.sdk.param.Parameter;
import net.sunniwell.sz.mop4.sdk.soap.SoapBase;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import net.sunniwell.sz.mop4.sdk.util.HttpHelper;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.LogUtil */
public class LogUtil {
    private static final String TAG = "LogUtil";

    public static String splitMessageBody(LogBean logBean) {
        String body;
        if (logBean == null) {
            return null;
        }
        int type = logBean.getType();
        int subtype = logBean.getSubtype();
        String extend = logBean.getExtend();
        String terminal_id = Parameter.get("terminal_id");
        String user_id = Parameter.get("user");
        String channel = Parameter.get(LogBean1.TERMINAL_STATISTICS_CHANNEL);
        String mac = Parameter.get("mac");
        String ois = SoapClient.getOis();
        String description = XmlPullParser.NO_NAMESPACE;
        String body2 = "<log type=\"" + type + "\" " + "subtype=" + "\"" + subtype + "\" " + "extend=" + "\"" + extend + "\" " + "content=" + "\"{" + "\"terminal_id\"" + ":\"" + terminal_id + "\"," + "\"user_id\"" + ":\"" + user_id + "\"," + "\"channel\"" + ":\"" + channel + "\",";
        Log.d(TAG, "splitMessageBody type=" + type + " subtype=" + subtype + " extend=" + extend);
        switch (subtype) {
            case 2:
                String description2 = new StringBuilder(String.valueOf(description)).append("failed ").append(extend).append(" errReason=").append(logBean.getExtra()).toString();
                if (extend.equalsIgnoreCase("play")) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description2)).append(" media_id=").append(logBean.getMedia_id()).append(" title=").append(logBean.getName()).append(" url=").append(logBean.getUrl()).toString()).append("\",").toString())).append("\"media_id\":\"").append(logBean.getMedia_id()).append("\",").toString())).append("\"title\":\"").append(logBean.getName()).append("\",").toString())).append("\"url\":\"").append(logBean.getUrl()).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase("upgrade")) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description2)).append(" upgrade_type=").append(logBean.getUpgrade_type()).append(" old_version=").append(logBean.getOld_version()).append(" new_version=").append(logBean.getNew_version()).append(" url=").append(logBean.getUrl()).toString()).append("\",").toString())).append("\"upgrade_type\":\"").append(logBean.getUpgrade_type()).append("\",").toString())).append("\"old_version\":\"").append(logBean.getOld_version()).append("\",").toString())).append("\"new_version\":\"").append(logBean.getNew_version()).append("\",").toString())).append("\"url\":\"").append(logBean.getUrl()).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase("install")) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description2)).append(" app_name=").append(logBean.getName()).append(" package_name=").append(logBean.getPackage_name()).toString()).append("\",").toString())).append("\"app_name\":\"").append(logBean.getName()).append("\",").toString())).append("\"package_name\":\"").append(logBean.getPackage_name()).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase(LogBean1.TERMINAL_EXCEPTION_FILE)) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description2)).append(" rwflag=").append(logBean.getRwflag()).append(" path=").append(logBean.getPath()).append(" file_name=").append(logBean.getName()).toString()).append("\",").toString())).append("\"rwflag\":\"").append(logBean.getRwflag()).append("\",").toString())).append("\"path\":\"").append(logBean.getPath()).append("\",").toString())).append("\"file_name\":\"").append(logBean.getName()).append("\"").toString();
                    break;
                } else {
                    Log.d(TAG, "splitMessageBody subtype=" + subtype + " extend=" + extend + " unKnow !!");
                    return null;
                }
            case 4:
                String description3 = new StringBuilder(String.valueOf(description)).append(extend).toString();
                if (extend.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_LOGIN) || extend.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_LOGOUT)) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description3)).append(" server=").append(ois).append(" mac=").append(mac).toString()).append("\",").toString())).append("\"server\":\"").append(ois).append("\",").toString())).append("\"mac\":\"").append(mac).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase("play") || extend.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_PAUSE) || extend.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_STOP) || extend.equalsIgnoreCase("resume")) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description3)).append(" media_id=").append(logBean.getMedia_id()).append(" title=").append(logBean.getName()).append(" url=").append(logBean.getUrl()).toString()).append("\",").toString())).append("\"media_id\":\"").append(logBean.getMedia_id()).append("\",").toString())).append("\"title\":\"").append(logBean.getName()).append("\",").toString())).append("\"url\":\"").append(logBean.getUrl()).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase("upgrade")) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description3)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(logBean.getExtra()).append(" upgrade_type=").append(logBean.getUpgrade_type()).append(" old_version=").append(logBean.getOld_version()).append(" new_version=").append(logBean.getNew_version()).append(" url=").append(logBean.getUrl()).toString()).append("\",").toString())).append("\"upgrade_type\":\"").append(logBean.getUpgrade_type()).append("\",").toString())).append("\"old_version\":\"").append(logBean.getOld_version()).append("\",").toString())).append("\"new_version\":\"").append(logBean.getNew_version()).append("\",").toString())).append("\"url\":\"").append(logBean.getUrl()).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_REBOOT) || extend.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_STANDBY)) {
                    body = new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(extend).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase("install")) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description3)).append(" app_name=").append(logBean.getName()).append(" package_name=").append(logBean.getPackage_name()).toString()).append("\",").toString())).append("\"app_name\":\"").append(logBean.getName()).append("\",").toString())).append("\"package_name\":\"").append(logBean.getPackage_name()).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_FAVORITE)) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"description\":\"").append(new StringBuilder(String.valueOf(description3)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(logBean.getExtra()).append(" media_id=").append(logBean.getMedia_id()).append(" title=").append(logBean.getName()).toString()).append("\",").toString())).append("\"media_id\":\"").append(logBean.getMedia_id()).append("\",").toString())).append("\"title\":\"").append(logBean.getName()).append("\"").toString();
                    break;
                } else {
                    Log.d(TAG, "splitMessageBody subtype=" + subtype + " extend=" + extend + " unKnow !!");
                    return null;
                }
                break;
            case 7:
                if (extend.equalsIgnoreCase(LogBean1.TERMINAL_STATISTICS_CHANNEL) || extend.equalsIgnoreCase(LogBean1.TERMINAL_STATISTICS_VOD)) {
                    if (extend.equalsIgnoreCase(LogBean1.TERMINAL_STATISTICS_VOD)) {
                        body2 = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"duration_total\":\"").append(logBean.getDuration_total()).append("\",").toString())).append("\"duration_percent\":\"").append(logBean.getDuration_percent()).append("\",").toString();
                    }
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"media_id\":\"").append(logBean.getMedia_id()).append("\",").toString())).append("\"url\":\"").append(logBean.getUrl()).append("\",").toString())).append("\"title\":\"").append(logBean.getName()).append("\",").toString())).append("\"duration_watch\":\"").append(logBean.getDuration_watch()).append("\",").toString())).append("\"start_utc\":\"").append(logBean.getStart_utc()).append("\",").toString())).append("\"end_utc\":\"").append(logBean.getEnd_utc()).append("\"").toString();
                    break;
                } else if (extend.equalsIgnoreCase(LogBean1.TERMINAL_STATISTICS_AD)) {
                    Log.d(TAG, "splitMessageBody subtype=" + subtype + " extend=" + extend + " is not support current !!");
                    return null;
                } else if (extend.equalsIgnoreCase(LogBean1.TERMINAL_STATISTICS_FLOW)) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"up_flow\":\"").append(logBean.getUp_flow()).append("\",").toString())).append("\"down_flow\":\"").append(logBean.getDown_flow()).append("\",").toString())).append("\"start_utc\":\"").append(logBean.getStart_utc()).append("\",").toString())).append("\"end_utc\":\"").append(logBean.getEnd_utc()).append("\"").toString();
                    break;
                } else {
                    Log.d(TAG, "splitMessageBody subtype=" + subtype + " extend=" + extend + " unKnow !!");
                    return null;
                }
            case 8:
                if (extend.equalsIgnoreCase(LogBean1.TERMINAL_MONITOR_SYSTEM)) {
                    body = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body2)).append("\"cpu\":\"").append(logBean.getCpu()).append("\",").toString())).append("\"mem\":\"").append(logBean.getMem()).append("\",").toString())).append("\"mem_total\":\"").append(logBean.getMem_total()).append("\",").toString())).append("\"mem_used\":\"").append(logBean.getMem_used()).append("\",").toString())).append("\"mem_free\":\"").append(logBean.getMem_free()).append("\",").toString())).append("\"upkb\":\"").append(logBean.getUpkb()).append("\",").toString())).append("\"downkb\":\"").append(logBean.getDownkb()).append("\",").toString())).append("\"disk_total\":\"").append(logBean.getDisk_total()).append("\",").toString())).append("\"disk_used\":\"").append(logBean.getDisk_used()).append("\",").toString())).append("\"disk_free\":\"").append(logBean.getDisk_free()).append("\",").toString())).append("\"io\":\"").append(logBean.getIo()).append("\"").toString();
                    break;
                } else {
                    Log.d(TAG, "splitMessageBody subtype=" + subtype + " extend=" + extend + " unKnow !!");
                    return null;
                }
            default:
                Log.d(TAG, "getQosParam log subtype=" + subtype + " unKnow or not support !!!");
                return null;
        }
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(body)).append("}\"").toString())).append(" />").toString();
    }

    public static void sendlog(String body) {
        String url = "https://" + SoapClient.getOis() + SoapBase.METHOD_SENDLOG;
        String content = SoapBase.getLogEntity(body);
        HttpHelper helper = new HttpHelper(true);
        helper.connect();
        helper.doPost(url, content);
        helper.disconnect();
    }
}
