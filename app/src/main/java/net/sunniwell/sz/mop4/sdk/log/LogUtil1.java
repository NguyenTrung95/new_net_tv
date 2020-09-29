package net.sunniwell.sz.mop4.sdk.log;

import net.sunniwell.sz.mop4.sdk.soap.SoapBase;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import net.sunniwell.sz.mop4.sdk.util.HttpHelper;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.LogUtil1 */
public class LogUtil1 {
    public static void sendLog(LogBean1 logItem) {
        String url = "https://" + SoapClient.getOis() + SoapBase.METHOD_SENDLOG;
        String content = logItem.toString();
        HttpHelper helper = new HttpHelper(true);
        helper.connect();
        helper.doPost(url, content);
        helper.disconnect();
    }
}
