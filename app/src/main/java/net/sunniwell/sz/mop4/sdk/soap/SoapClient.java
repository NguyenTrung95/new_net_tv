package net.sunniwell.sz.mop4.sdk.soap;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.sunniwell.sz.mop4.sdk.log.LogBean1;
import net.sunniwell.sz.mop4.sdk.param.Parameter;
import net.sunniwell.sz.mop4.sdk.util.HttpHelper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapClient */
public class SoapClient {
    private static final String TAG = "SoapClient";
    /* access modifiers changed from: private */
    public static SoapClient mInstance;
    /* access modifiers changed from: private */
    public boolean autoProcess = false;
    private int curOisIdx = 0;
    private String epgsStr;
    private String epgsToken;
    private SoapListener listener = null;
    private int loginFailedCount = 0;
    /* access modifiers changed from: private */
    public HttpHelper mHelper;
    private long mLoginSuccesTimeUtcMs;
    /* access modifiers changed from: private */
    public long mLoginTime;
    private Object mSyncObj = new Object();
    private Thread monitorThread = new Thread(new Runnable() {
        public void run() {
            LoginRunnable loginProc = new LoginRunnable();
            new Thread(loginProc).start();
            while (SoapClient.mInstance.runFlag) {
                if (SoapClient.mInstance.autoProcess && SoapClient.mInstance.mLoginTime != 0 && System.currentTimeMillis() > SoapClient.mInstance.mLoginTime + 30000) {
                    try {
                        SoapClient.mInstance.mHelper.disconnect();
                        SoapClient.mInstance.mHelper = new HttpHelper(true);
                        SoapClient.mInstance.mHelper.connect();
                        loginProc.isExit = true;
                        LoginRunnable loginProc2 = new LoginRunnable();
                        try {
                            Thread loginThread = new Thread(loginProc2);
                            try {
                                loginThread.start();
                                Thread thread = loginThread;
                                loginProc = loginProc2;
                            } catch (Exception e) {
                                e = e;
                                Thread thread2 = loginThread;
                                loginProc = loginProc2;
                                e.printStackTrace();
                                SoapClient.delay(1000);
                            }
                        } catch (Exception e2) {
                            e = e2;
                            loginProc = loginProc2;
                            e.printStackTrace();
                            SoapClient.delay(1000);
                        }
                    } catch (Exception e3) {
                        e = e3;
                        e.printStackTrace();
                        SoapClient.delay(1000);
                    }
                }
                SoapClient.delay(1000);
            }
        }
    });
    private String[] oisList;
    private String oisStr;
    private String oisToken;
    private long oisUtc;
    /* access modifiers changed from: private */
    public boolean runFlag = false;

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapClient$AuthenResponse */
    public static class AuthenResponse {
        public int allow_watch_duration = HttpStatus.SC_MULTIPLE_CHOICES;
        public boolean result = false;
        public int statusCode;
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapClient$BankAccountBeanResponse */
    public static class BankAccountBeanResponse {
        public BankAccountBean bean = null;
        public int statusCode;
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapClient$LoginRunnable */
    class LoginRunnable implements Runnable {
        boolean isExit;

        LoginRunnable() {
        }

        public void run() {
            SoapResponse res = null;
            boolean hasNMP = false;
            while (SoapClient.mInstance.runFlag && !this.isExit) {
                if (SoapClient.mInstance.autoProcess) {
                    res = SoapClient.this.getLoginResponse();
                    if (res != null && 200 == res.statusCode && res.body != null && !res.body.isEmpty()) {
                        hasNMP = SoapClient.parseNmpCommand(res.body, Parameter.get("terminal_id"), Parameter.get("user"));
                    }
                }
                if (res == null || 200 != res.statusCode) {
                    SoapClient.delay(3000);
                } else if (hasNMP) {
                    hasNMP = false;
                    SoapClient.delay(100);
                } else {
                    SoapClient.delay(10000);
                }
            }
        }
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapClient$PayUrlResponse */
    public static class PayUrlResponse {
        public int statusCode;
        public String url = null;
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapClient$SoapResponse */
    public static class SoapResponse {
        public String body;
        public String epgs;
        public String epgsToken;
        public String location;
        public String ois;
        public String oisToken;
        public int statusCode;
        public long utc;
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapClient$UserBeanResponse */
    public static class UserBeanResponse {
        public UserBean bean = null;
        public int statusCode;
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapClient$VideoCardBeanResponse */
    public static class VideoCardBeanResponse {
        public VideoCardBean bean = null;
        public int statusCode;
    }

    public SoapResponse getLoginResponse() {
        return login(Parameter.get("user"), Parameter.get("password"), Parameter.get("terminal_id"), Parameter.get("terminal_type"), Parameter.get("mac"), Parameter.get("netmode"), Parameter.get("soft_ver"), Parameter.get("hard_ver"), Parameter.get("epg"));
    }

    public static boolean init(String ois, String epgs, boolean auto, SoapListener lsn) {
        if (mInstance == null) {
            mInstance = new SoapClient();
        }
        if (mInstance.runFlag) {
            return true;
        }
        mInstance.autoProcess = auto;
        mInstance.listener = lsn;
        if (mInstance.mHelper == null) {
            mInstance.mHelper = new HttpHelper(true);
            if (!mInstance.mHelper.connect()) {
                return false;
            }
        }
        mInstance.oisStr = ois;
        mInstance.epgsStr = epgs;
        if (mInstance.oisStr == null || mInstance.oisStr.equals(XmlPullParser.NO_NAMESPACE)) {
            return false;
        }
        mInstance.oisList = mInstance.oisStr.split("\\|");
        mInstance.curOisIdx = 0;
        mInstance.monitorThread.start();
        mInstance.runFlag = true;
        return true;
    }

    public static boolean isInit() {
        try {
            return mInstance.runFlag;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setAutoProcess(boolean auto) {
        mInstance.autoProcess = auto;
    }

    public static SoapResponse login(String user, String password, String terminal_id, String terminal_type, String mac, String netmode, String soft_ver, String hard_ver, String epg) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        mInstance.mLoginTime = System.currentTimeMillis();
        Log.d(TAG, "SoapClient login...");
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_LOGIN, SoapBase.getLoginEntity(user, password, terminal_id, terminal_type, mac, netmode, soft_ver, hard_ver, epg, getOisToken()));
        if (res == null) {
            Log.d(TAG, "Soapclient login failed ! connect to OIS failed! mInstance.loginFailedCount = " + mInstance.loginFailedCount);
            SoapClient soapClient = mInstance;
            soapClient.loginFailedCount++;
            if (mInstance.loginFailedCount >= 3) {
                switchOIS();
                mInstance.loginFailedCount = 0;
            }
        } else if (res.statusCode != 200) {
            Log.d(TAG, "Soapclient login failed ! statusCode = " + res.statusCode);
            if (mInstance.listener != null) {
                mInstance.listener.onLoginFailed(res.statusCode);
            }
            if (res.statusCode == 410) {
                switchOIS();
            }
        } else {
            Log.d(TAG, "Soapclient login success !");
            Log.d(TAG, "mInstance.oisToken = " + res.oisToken);
            Log.d(TAG, "mInstance.epgsToken = " + res.epgsToken);
            Log.d(TAG, "ois = " + res.ois);
            Log.d(TAG, "epgs = " + res.epgs);
            Log.d(TAG, "utc = " + res.utc);
            mInstance.mLoginSuccesTimeUtcMs = System.currentTimeMillis();
            mInstance.oisToken = res.oisToken;
            mInstance.epgsToken = res.epgsToken;
            mInstance.oisUtc = res.utc * 1000;
            if (mInstance.listener != null) {
                mInstance.listener.onLoginSuccess();
            }
            if (res.epgs != null && !res.epgs.equals(XmlPullParser.NO_NAMESPACE) && res.epgs.length() > 8 && !res.epgs.equals(mInstance.epgsStr)) {
                Log.d(TAG, "=======================  epgs change !!! ===================");
                mInstance.epgsStr = res.epgs;
                Parameter.set("epgs", mInstance.epgsStr);
                Parameter.save();
                if (mInstance.listener != null) {
                    mInstance.listener.onEpgsChange();
                }
            }
            if (res.ois != null && !res.ois.equals(XmlPullParser.NO_NAMESPACE) && res.ois.length() > 5 && !res.ois.equals(mInstance.oisStr)) {
                Log.d(TAG, "=======================  ois change !!! ===================res.ois = " + res.ois);
                String[] oisList2 = res.ois.split("\\|");
                if (oisList2 != null && oisList2.length > 0) {
                    mInstance.oisList = oisList2;
                    mInstance.curOisIdx = 0;
                    mInstance.oisStr = res.ois;
                    Parameter.set("ois", mInstance.oisStr);
                    Parameter.save();
                }
            }
        }
        Log.d(TAG, "Soapclient login end !");
        return res;
    }

    public static long getLoginSuccessTimeUtcMs() {
        return mInstance.mLoginSuccesTimeUtcMs;
    }

    public static int logout(String user, String terminal_id) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_LOGOUT, SoapBase.getLogoutEntity(user, terminal_id));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, new StringBuilder(String.valueOf(user)).append("logout failed!").append(" statusCode = ").append(res == null ? "null" : Integer.valueOf(res.statusCode)).toString());
        } else {
            Log.d(TAG, new StringBuilder(String.valueOf(user)).append("logout success!").toString());
        }
        return res == null ? -1 : res.statusCode;
    }

    public static int enable(String terminal_id, String mac, String user, String password, String soft_ver, String hard_ver, String channel) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_ENABLE, SoapBase.getEnableEntity(terminal_id, mac, user, password, soft_ver, hard_ver, channel));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "Enable terminal: " + terminal_id + " failed!" + " statusCode = " + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "Enable terminal: " + terminal_id + "success!");
        }
        return res == null ? -1 : res.statusCode;
    }

    public static AuthenResponse authen(String user, String terminal_id, String mediaId) {
        AuthenResponse authenResp = null;
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_AUTH, SoapBase.getAuthEntity(user, terminal_id, mediaId, mInstance.oisToken));
        if (res != null) {
            authenResp = parseAuthenBody(res.body);
            if (authenResp == null) {
                authenResp = new AuthenResponse();
            }
            authenResp.statusCode = res.statusCode;
        }
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "Authenticate media: " + mediaId + " failed!" + "statusCode = " + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "Authenticate media: " + mediaId + "success!");
        }
        return authenResp;
    }

    public static int register(String user, String password, String realname, String country, String postcode, String email, String addr, String phone, String mobile, String birthday, String channel, boolean enable) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_REGISTER, SoapBase.getRegisterEntity(user, password, realname, country, postcode, email, addr, phone, mobile, birthday, channel, enable));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "Register failed    user::" + user + " password::" + password + " realname::" + realname + " email::" + email + " address::" + addr + " phone::" + phone + " channel::" + channel + " statusCode = " + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "Register success    user::" + user + " password::" + password + " realname::" + realname + " email::" + email + " address::" + addr + " phone::" + phone);
        }
        if (res == null) {
            return -1;
        }
        return res.statusCode;
    }

    public static int subscribe(String user, String sid, boolean renew) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_SUBSCRIBE, SoapBase.getSubscribeEntity(user, sid, renew, mInstance.oisToken));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "subscribe failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "subscribe success mediaId::" + sid);
        }
        return res == null ? -1 : res.statusCode;
    }

    public static int recharge(String user, int amount, int timeout) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + mInstance.oisList[mInstance.curOisIdx] + SoapBase.METHOD_RECHARGE, SoapBase.getRechargeEntity(user, amount, mInstance.oisToken));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "recharge failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "recharge success!    amount::" + amount);
        }
        return res == null ? -1 : res.statusCode;
    }

    public static int unSubscribe(String user, String sid, long start_utc, long end_utc) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_UNSUBSCRIBE, SoapBase.getUnSubscribeEntity(user, sid, start_utc, end_utc, mInstance.oisToken));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "unSubscribe failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "unSubscribe success mediaId::" + sid);
        }
        if (res == null) {
            return -1;
        }
        return res.statusCode;
    }

    public static int subscribeByCreditCard(String user, String sid, boolean renew, String desc, String cardNumber, String creditcardType, String expdate, String safeCode, String firstName, String lastName, String street, String street2, String city, String province, String countryCode, String postCode, String birthday, String phone, String email) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + mInstance.oisList[mInstance.curOisIdx] + SoapBase.METHOD_SUBSCRIBE_BY_CREDITCARD, SoapBase.getSubscribeByCreditCardEntity(user, sid, renew, desc, mInstance.oisToken, cardNumber, creditcardType, expdate, safeCode, firstName, lastName, street, street2, city, province, countryCode, postCode, birthday, phone, email));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "subscribeByCreditCard failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "subscribeByCreditCard success sid::" + sid);
        }
        if (res == null) {
            return -1;
        }
        return res.statusCode;
    }

    public static int rechargeByCreditCard(String user, int amount, String desc, String cardNumber, String creditcardType, String expdate, String saveCode, String firstName, String lastName, String street, String street2, String city, String province, String countryCode, String postCode, String phone, String email) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + mInstance.oisList[mInstance.curOisIdx] + SoapBase.METHOD_RECHARGE_BY_CREDITCARD, SoapBase.getRechargeByCreditCardEntity(user, amount, desc, mInstance.oisToken, cardNumber, creditcardType, expdate, saveCode, firstName, lastName, street, street2, city, province, countryCode, postCode, phone, email));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "rechargeByCreditCard failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "rechargeByCreditCard success!    amount::" + amount);
        }
        if (res == null) {
            return -1;
        }
        return res.statusCode;
    }

    public static int renew(String user, String sid, long start_utcMs, long end_utcMs) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_RENEW, SoapBase.getRenewEntity(user, sid, start_utcMs / 1000, end_utcMs / 1000, mInstance.oisToken));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "renew failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "renew success sid::" + sid);
        }
        if (res == null) {
            return -1;
        }
        return res.statusCode;
    }

    public static int cancelRenew(String user, String sid, long start_utcMs, long end_utcMs) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_CANCELRENEW, SoapBase.getCancelRenewEntity(user, sid, start_utcMs / 1000, end_utcMs / 1000, mInstance.oisToken));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "cancelRenew failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "cancelRenew success sid::" + sid);
        }
        if (res == null) {
            return -1;
        }
        return res.statusCode;
    }

    public static PayUrlResponse getPayPalPayBuyUrl(String user, String sid, boolean renew, String desc) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        PayUrlResponse payUrlResponse = null;
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_PAYPAL_BUY, SoapBase.getPayPalPayBuyEntity(user, sid, renew, mInstance.oisToken, desc));
        if (res != null) {
            payUrlResponse = new PayUrlResponse();
            payUrlResponse.statusCode = res.statusCode;
            if (res.statusCode == 302 || res.statusCode == 200) {
                payUrlResponse.url = res.location;
                Log.d(TAG, "getPayPalPayBuyUrl success payUrl = " + payUrlResponse.url);
            }
        }
        if (res != null && (res.statusCode == 302 || res.statusCode == 200)) {
            return payUrlResponse;
        }
        Log.d(TAG, "getPayPalPayBuyUrl failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        return payUrlResponse;
    }

    public static PayUrlResponse getWAPTenPayBuyUrl(String user, String sid, boolean renew, String desc) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        PayUrlResponse payUrlResponse = null;
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_WAPTENPAY_BUY, SoapBase.getWAPTenPayBuyEntity(user, sid, renew, mInstance.oisToken, desc));
        if (res != null) {
            payUrlResponse = new PayUrlResponse();
            payUrlResponse.statusCode = res.statusCode;
            if (res.statusCode == 302 || res.statusCode == 200) {
                payUrlResponse.url = res.location;
                Log.d(TAG, "getWAPTenPayBuyUrl success payUrl = " + payUrlResponse.url);
            }
        }
        if (res != null && (res.statusCode == 302 || res.statusCode == 200)) {
            return payUrlResponse;
        }
        Log.d(TAG, "getWAPTenPayBuyUrl failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        return payUrlResponse;
    }

    public static PayUrlResponse getPCTenPayBuyUrl(String user, String sid, boolean renew, String desc) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        PayUrlResponse payUrlResponse = null;
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_PCTENPAY_BUY, SoapBase.getPCTenPayBuyEntity(user, sid, renew, mInstance.oisToken, desc));
        if (res != null) {
            payUrlResponse = new PayUrlResponse();
            payUrlResponse.statusCode = res.statusCode;
            if (res.statusCode == 302 || res.statusCode == 200) {
                payUrlResponse.url = res.location;
                Log.d(TAG, "getPCTenPayBuyUrl success payUrl = " + payUrlResponse.url);
            }
        }
        if (res != null && (res.statusCode == 302 || res.statusCode == 200)) {
            return payUrlResponse;
        }
        Log.d(TAG, "getPCTenPayBuyUrl failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        return payUrlResponse;
    }

    public static PayUrlResponse getPaypalRechargeUrl(String user, int amount, String desc) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        PayUrlResponse payUrlResponse = null;
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_PAYPAL_RECHARGE, SoapBase.getPaypalRechargeEntity(user, amount, mInstance.oisToken, desc));
        if (res != null) {
            payUrlResponse = new PayUrlResponse();
            payUrlResponse.statusCode = res.statusCode;
            if (res.statusCode == 302 || res.statusCode == 200) {
                payUrlResponse.url = res.location;
                Log.d(TAG, "getPaypalRechargeUrl success payUrl = " + payUrlResponse.url);
            }
        }
        if (res != null && (res.statusCode == 302 || res.statusCode == 200)) {
            return payUrlResponse;
        }
        Log.d(TAG, "getPaypalRechargeUrl failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        return payUrlResponse;
    }

    public static PayUrlResponse getWAPTenPayRechargeUrl(String user, int amount, String desc) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        PayUrlResponse payUrlResponse = null;
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_WAPTENPAY_RECHARGE, SoapBase.getWAPTenPayRechargeEntity(user, amount, mInstance.oisToken, desc));
        if (res != null) {
            payUrlResponse = new PayUrlResponse();
            payUrlResponse.statusCode = res.statusCode;
            if (res.statusCode == 302 || res.statusCode == 200) {
                payUrlResponse.url = res.location;
                Log.d(TAG, "getWAPTenPayRechargeUrl success payUrl = " + payUrlResponse.url);
            }
        }
        if (res != null && (res.statusCode == 302 || res.statusCode == 200)) {
            return payUrlResponse;
        }
        Log.d(TAG, "getWAPTenPayRechargeUrl failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        return payUrlResponse;
    }

    public static PayUrlResponse getPCTenPayRechargeUrl(String user, int amount, String desc) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        PayUrlResponse payUrlResponse = null;
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_PCTENPAY_RECHARGE, SoapBase.getPCTenPayRechargeEntity(user, amount, mInstance.oisToken, desc));
        if (res != null) {
            payUrlResponse = new PayUrlResponse();
            payUrlResponse.statusCode = res.statusCode;
            if (res.statusCode == 302 || res.statusCode == 200) {
                payUrlResponse.url = res.location;
                Log.d(TAG, "getPayPalPayBuyUrl success payUrl = " + payUrlResponse.url);
            }
        }
        if (res != null && (res.statusCode == 302 || res.statusCode == 200)) {
            return payUrlResponse;
        }
        Log.d(TAG, "getPayPalPayBuyUrl failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        return payUrlResponse;
    }

    public static int videoCardRecharge(String user, String cardId) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_VIDEOCARD_RECHARGE, SoapBase.getVideoCardRechargeEntity(user, cardId, mInstance.oisToken));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "videoCardRecharge video card recharge failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "videoCardRecharge video card recharge success! ");
        }
        return res == null ? -1 : res.statusCode;
    }

    public static VideoCardBeanResponse videoCardInfo(String user, String cardId) {
        VideoCardBeanResponse vbs = null;
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_VIDEOCARD_INFO, SoapBase.getVideoCardInfoEntity(user, cardId, mInstance.oisToken));
        if (res != null) {
            vbs = new VideoCardBeanResponse();
            vbs.statusCode = res.statusCode;
            if (res.statusCode == 200) {
                Log.d(TAG, "videoCardInfo video card info success! ");
                vbs.bean = parseVideoCardInfo(res.body, cardId);
            }
        }
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "videoCardInfo video card info failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        }
        return vbs;
    }

    public static UserBeanResponse getUserInfo(String user) {
        Log.d(TAG, "==getUserInfo==");
        Log.d(TAG, "==user==" + user);
        UserBeanResponse ubs = null;
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        String url = "https://" + getOis() + SoapBase.METHOD_GET_USERINFO;
        String content = SoapBase.getUserInfoEntity(user, mInstance.oisToken);
        Log.d(TAG, "==url==" + url + "==content==" + content);
        SoapResponse res = doPost(url, content);
        if (res != null) {
            ubs = new UserBeanResponse();
            ubs.statusCode = res.statusCode;
            if (res.statusCode == 200) {
                Log.d(TAG, "getUserInfo success!");
                ubs.bean = parseUserInfo(res.body, user);
            }
        }
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "getUserInfo failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        }
        return ubs;
    }

    public static int setUserInfo(String user, String realname, String country, String email, String addr, String phone, String mobile, String postcode, String birthday) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        } else if (user == null || user.isEmpty()) {
            return HttpStatus.SC_UNAUTHORIZED;
        } else {
            SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_SET_USERINFO, SoapBase.getSetUserInfoEntity(user, realname, country, email, addr, phone, mobile, postcode, birthday, mInstance.oisToken));
            if (res == null || res.statusCode != 200) {
                Log.d(TAG, "setUserInfo failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
            } else {
                Log.d(TAG, "setUserInfo success! ");
            }
            if (res == null) {
                return -1;
            }
            return res.statusCode;
        }
    }

    public static int bindUser(String user, String terminalId) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        } else if (user == null || user.isEmpty()) {
            return HttpStatus.SC_UNAUTHORIZED;
        } else {
            SoapResponse res = doPost("https://" + mInstance.oisList[mInstance.curOisIdx] + SoapBase.METHOD_USER_BIND, SoapBase.getUserBindEntity(user, terminalId, mInstance.oisToken));
            if (res == null || res.statusCode != 200) {
                Log.d(TAG, "bindUser failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
            } else {
                Log.d(TAG, "bindUser success! ");
            }
            return res == null ? -1 : res.statusCode;
        }
    }

    public static BankAccountBeanResponse getUserBankAccountInfo(String user) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        BankAccountBeanResponse babs = null;
        SoapResponse res = doPost("https://" + mInstance.oisList[mInstance.curOisIdx] + SoapBase.METHOD_GET_BANKACCOUNTINFO, SoapBase.getBankAccountInfoEntity(user, mInstance.oisToken));
        if (res != null) {
            babs = new BankAccountBeanResponse();
            babs.statusCode = res.statusCode;
            if (res.statusCode == 200) {
                Log.d(TAG, "getUserBankAccountInfo success!");
                babs.bean = parseBankAccountInfo(res.body, user);
            }
        }
        if (res != null && res.statusCode == 200) {
            return babs;
        }
        Log.d(TAG, "getUserBankAccountInfo failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        return babs;
    }

    public static int bindBankAccount(String user, BankAccountBean bab) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        } else if (user == null || user.isEmpty() || !user.equals(bab.getUser_id())) {
            return HttpStatus.SC_UNAUTHORIZED;
        } else {
            if (bab == null || bab.getCard_number().isEmpty() || bab.getCard_type().isEmpty() || bab.getExpiration_date().isEmpty() || bab.getSecurity_code().isEmpty() || bab.getFirst_name().isEmpty() || bab.getLast_name().isEmpty() || bab.getStreet().isEmpty() || bab.getCity().isEmpty() || bab.getState().isEmpty() || bab.getCountry().isEmpty() || bab.getPhone().isEmpty() || bab.getEmail().isEmpty() || (bab.getAuthorised() != 0 && bab.getAuthorised() != 1)) {
                return HttpStatus.SC_PAYMENT_REQUIRED;
            }
            SoapResponse res = doPost("https://" + mInstance.oisList[mInstance.curOisIdx] + SoapBase.METHOD_BIND_BANKACCOUNTINFO, SoapBase.getBankAccountBindEntity(user, bab, mInstance.oisToken));
            if (res == null || res.statusCode != 200) {
                Log.d(TAG, "bindBankAccount failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
            } else {
                Log.d(TAG, "bindBankAccount success! ");
            }
            return res == null ? -1 : res.statusCode;
        }
    }

    public static int unBindBankAccount(String user) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        } else if (user == null || user.isEmpty()) {
            return HttpStatus.SC_UNAUTHORIZED;
        } else {
            SoapResponse res = doPost("https://" + mInstance.oisList[mInstance.curOisIdx] + SoapBase.METHOD_UNBIND_BANKACCOUNTINFO, SoapBase.getBankAccountUnBindEntity(user, mInstance.oisToken));
            if (res == null || res.statusCode != 200) {
                Log.d(TAG, "unBindBankAccount failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
            } else {
                Log.d(TAG, "unBindBankAccount success! ");
            }
            return res == null ? -1 : res.statusCode;
        }
    }

    public static ArrayList<SubscribeBean> getSubscribeList(String user, int type) {
        ArrayList<SubscribeBean> sb = null;
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_SUBCRIBELIST, SoapBase.getSubscribeListEntity(user, mInstance.oisToken, type));
        if (res != null) {
            if (res.statusCode == 200) {
                Log.d(TAG, "getSubscribeList success!");
                sb = parseSubscribeList(res.body, user);
            } else if (res.statusCode != 403) {
                sb = new ArrayList<>();
            }
        }
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "getSubscribeList failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        }
        return sb;
    }

    public static ArrayList<AssetBean> getAssetList(String user, long start_utc, long end_utc, int page_no, int page_size) {
        ArrayList<AssetBean> cb = null;
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return null;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_CONSUMELIST, SoapBase.getAssetListEntity(user, mInstance.oisToken, start_utc, end_utc, page_no, page_size));
        if (res != null) {
            if (res.statusCode == 200) {
                Log.d(TAG, "getAssetList success!");
                cb = parseConsumeList(res.body, user);
            } else {
                cb = new ArrayList<>();
            }
        }
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "getAssetList failed! statusCode=" + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        }
        return cb;
    }

    public static int postParam(String terminalId, String paramName, String paramValue) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_PARAM, SoapBase.getNMPParamEntity(terminalId, paramName, paramValue));
        if (res == null || res.statusCode == 200) {
            Log.d(TAG, "postParam failed! statusCode = " + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "postParam success!");
        }
        return res == null ? -1 : res.statusCode;
    }

    public static int postAllParam(String terminalId, String allParams, int timeout) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_ALLPARAM, SoapBase.getNMPAllParamEntity(terminalId, allParams));
        if (res == null || res.statusCode == 200) {
            Log.d(TAG, "postAllParam failed! statusCode = " + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "postParam success!");
        }
        return res == null ? -1 : res.statusCode;
    }

    public static int changePassword(String user, String oldPassword, String newPassword, int timeout) {
        if (!isInit()) {
            Log.d(TAG, "SoapClient not init, return!");
            return -1;
        }
        SoapResponse res = doPost("https://" + getOis() + SoapBase.METHOD_CHANGE_PASSWORD, SoapBase.getChangePasswordEntity(user, oldPassword, newPassword, mInstance.oisToken));
        if (res == null || res.statusCode != 200) {
            Log.d(TAG, "changePassword failed! statusCode = " + (res == null ? "null" : Integer.valueOf(res.statusCode)));
        } else {
            Log.d(TAG, "changePassword success!");
        }
        return res == null ? -1 : res.statusCode;
    }

    public static void setSoapListener(SoapListener lsn) {
        if (mInstance != null) {
            mInstance.listener = lsn;
        }
    }

    public static String getOis() {
        if (mInstance == null || mInstance.oisList == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return mInstance.oisList[mInstance.curOisIdx];
    }

    public static String getEpgs() {
        if (mInstance != null) {
            return mInstance.epgsStr == null ? Parameter.get("epgs") : mInstance.epgsStr;
        }
        return null;
    }

    public static String getOisToken() {
        if (mInstance != null) {
            return mInstance.oisToken;
        }
        return null;
    }

    public static String getEpgsToken() {
        if (mInstance != null) {
            return mInstance.epgsToken;
        }
        return null;
    }

    public static long getOisUtcMs() {
        if (mInstance != null) {
            return mInstance.oisUtc;
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0050  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean parseNmpCommand(java.lang.String r31, java.lang.String r32, java.lang.String r33) {
        /*
            r22 = 0
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener
            if (r2 == 0) goto L_0x0014
            if (r31 == 0) goto L_0x0014
            java.lang.String r2 = ""
            r0 = r31
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L_0x0016
        L_0x0014:
            r2 = 0
        L_0x0015:
            return r2
        L_0x0016:
            r19 = 0
            java.io.StringReader r20 = new java.io.StringReader     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r20
            r1 = r31
            r0.<init>(r1)     // Catch:{ DocumentException -> 0x00c2 }
            org.xml.sax.InputSource r24 = new org.xml.sax.InputSource     // Catch:{ DocumentException -> 0x0552 }
            r0 = r24
            r1 = r20
            r0.<init>(r1)     // Catch:{ DocumentException -> 0x0552 }
            org.dom4j.io.SAXReader r21 = new org.dom4j.io.SAXReader     // Catch:{ DocumentException -> 0x0552 }
            r21.<init>()     // Catch:{ DocumentException -> 0x0552 }
            r0 = r21
            r1 = r24
            org.dom4j.Document r11 = r0.read(r1)     // Catch:{ DocumentException -> 0x0552 }
            r20.close()     // Catch:{ DocumentException -> 0x0552 }
            r19 = 0
            org.dom4j.Element r23 = r11.getRootElement()     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "Body"
            r0 = r23
            java.util.Iterator r14 = r0.elementIterator(r2)     // Catch:{ DocumentException -> 0x00c2 }
        L_0x0048:
            boolean r2 = r14.hasNext()     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0056
        L_0x004e:
            if (r19 == 0) goto L_0x0053
            r19.close()
        L_0x0053:
            r2 = r22
            goto L_0x0015
        L_0x0056:
            java.lang.Object r2 = r14.next()     // Catch:{ DocumentException -> 0x00c2 }
            org.dom4j.Element r2 = (org.dom4j.Element) r2     // Catch:{ DocumentException -> 0x00c2 }
            java.util.List r15 = r2.elements()     // Catch:{ DocumentException -> 0x00c2 }
            java.util.Iterator r28 = r15.iterator()     // Catch:{ DocumentException -> 0x00c2 }
        L_0x0064:
            boolean r2 = r28.hasNext()     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x0048
            java.lang.Object r13 = r28.next()     // Catch:{ DocumentException -> 0x00c2 }
            org.dom4j.Element r13 = (org.dom4j.Element) r13     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r10 = r13.getName()     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "user"
            boolean r2 = r10.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0064
            java.lang.String r2 = "SoapClient"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r30 = "===========================> receive nmp command: "
            r29.<init>(r30)     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r29
            java.lang.StringBuilder r29 = r0.append(r10)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r29 = r29.toString()     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "getparam"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x00f0
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x00d1
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x00d1
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x00d1
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x00c2:
            r12 = move-exception
        L_0x00c3:
            java.lang.String r2 = "SoapClient"
            java.lang.String r28 = "parse param error"
            r0 = r28
            android.util.Log.i(r2, r0)
            r12.printStackTrace()
            goto L_0x004e
        L_0x00d1:
            java.lang.String r2 = "name"
            java.lang.String r16 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r16 == 0) goto L_0x0064
            java.lang.String r2 = ""
            r0 = r16
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0064
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r16
            r2.onGetParam(r0)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x00f0:
            java.lang.String r2 = "getallparam"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x012a
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x011f
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x011f
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x011f
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x011f:
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onGetAllParam()     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x012a:
            java.lang.String r2 = "setparam"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x0184
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x0159
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0159
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0159
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x0159:
            java.lang.String r2 = "name"
            java.lang.String r16 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "value"
            java.lang.String r27 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r16 == 0) goto L_0x0064
            boolean r2 = r16.isEmpty()     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0064
            if (r27 == 0) goto L_0x0064
            boolean r2 = r27.isEmpty()     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0064
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r16
            r1 = r27
            r2.onSetParam(r0, r1)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x0184:
            java.lang.String r2 = "saveparam"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x01be
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x01b3
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x01b3
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x01b3
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x01b3:
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onSaveParam()     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x01be:
            java.lang.String r2 = "delparam"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x0216
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x01ed
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x01ed
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x01ed
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x01ed:
            java.lang.String r2 = "name"
            java.lang.String r16 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "value"
            java.lang.String r27 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r16 == 0) goto L_0x0064
            boolean r2 = r16.isEmpty()     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0064
            if (r27 == 0) goto L_0x0064
            boolean r2 = r27.isEmpty()     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0064
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r16
            r2.onDelParam(r0)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x0216:
            java.lang.String r2 = "openurl"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x0264
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x0245
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0245
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0245
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x0245:
            java.lang.String r2 = "url"
            java.lang.String r25 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r25 == 0) goto L_0x0064
            java.lang.String r2 = ""
            r0 = r25
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0064
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r25
            r2.onOpenUrl(r0)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x0264:
            java.lang.String r2 = "marquee"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x02d8
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x0293
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0293
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0293
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x0293:
            java.lang.String r2 = "title"
            java.lang.String r3 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "content"
            java.lang.String r4 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "fontSize"
            java.lang.String r2 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            int r5 = net.sunniwell.p008sz.mop4.sdk.util.StringUtil.string2int(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "fontColor"
            java.lang.String r6 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "speed"
            java.lang.String r2 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            int r7 = net.sunniwell.p008sz.mop4.sdk.util.StringUtil.string2int(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "loop"
            java.lang.String r2 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            int r8 = net.sunniwell.p008sz.mop4.sdk.util.StringUtil.string2int(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "duration"
            java.lang.String r2 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            int r9 = net.sunniwell.p008sz.mop4.sdk.util.StringUtil.string2int(r2)     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onMarquee(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x02d8:
            java.lang.String r2 = "alertmsg"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x031e
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x0307
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0307
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0307
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x0307:
            java.lang.String r2 = "title"
            java.lang.String r3 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "content"
            java.lang.String r4 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onAlertmsg(r3, r4)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x031e:
            java.lang.String r2 = "message"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x0364
            java.lang.String r2 = "user"
            java.lang.String r26 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r26 == 0) goto L_0x034d
            r0 = r26
            r1 = r33
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x034d
            java.lang.String r2 = "*"
            r0 = r26
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x034d
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "user not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x034d:
            java.lang.String r2 = "title"
            java.lang.String r3 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "content"
            java.lang.String r4 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onMessage(r3, r4)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x0364:
            java.lang.String r2 = "assignuser"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x03ae
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x0393
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0393
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0393
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x0393:
            java.lang.String r2 = "user"
            java.lang.String r26 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            java.lang.String r2 = "password"
            java.lang.String r17 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r26
            r1 = r17
            r2.onAssignUser(r0, r1)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x03ae:
            java.lang.String r2 = "mediaplay"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x03ee
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x03dd
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x03dd
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x03dd
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x03dd:
            java.lang.String r2 = "url"
            java.lang.String r25 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r25
            r2.onMediaPlay(r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x03ee:
            java.lang.String r2 = "mediastop"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x0428
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x041d
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x041d
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x041d
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x041d:
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onMediaStop()     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x0428:
            java.lang.String r2 = "upgrade"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x046a
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x0457
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0457
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0457
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x0457:
            java.lang.String r2 = "url"
            java.lang.String r25 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r0 = r25
            r2.onUpgrade(r0)     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x046a:
            java.lang.String r2 = "restart"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x04a4
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x0499
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0499
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0499
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x0499:
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onRestart()     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x04a4:
            java.lang.String r2 = "standby"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x04de
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x04d3
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x04d3
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x04d3
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x04d3:
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onStandby()     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x04de:
            java.lang.String r2 = "wakeup"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x0518
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x050d
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x050d
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x050d
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x050d:
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onWakeup()     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x0518:
            java.lang.String r2 = "shutdown"
            boolean r2 = r10.equalsIgnoreCase(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 == 0) goto L_0x0064
            java.lang.String r2 = "peer_id"
            java.lang.String r18 = r13.attributeValue(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r18 == 0) goto L_0x0547
            r0 = r18
            r1 = r32
            boolean r2 = r0.equals(r1)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0547
            java.lang.String r2 = "*"
            r0 = r18
            boolean r2 = r0.equals(r2)     // Catch:{ DocumentException -> 0x00c2 }
            if (r2 != 0) goto L_0x0547
            java.lang.String r2 = "SoapClient"
            java.lang.String r29 = "peerId not match, nmp command unvalid!"
            r0 = r29
            android.util.Log.d(r2, r0)     // Catch:{ DocumentException -> 0x00c2 }
            goto L_0x0064
        L_0x0547:
            net.sunniwell.sz.mop4.sdk.soap.SoapClient r2 = mInstance     // Catch:{ DocumentException -> 0x00c2 }
            net.sunniwell.sz.mop4.sdk.soap.SoapListener r2 = r2.listener     // Catch:{ DocumentException -> 0x00c2 }
            r2.onShutDown()     // Catch:{ DocumentException -> 0x00c2 }
            r22 = 1
            goto L_0x0064
        L_0x0552:
            r12 = move-exception
            r19 = r20
            goto L_0x00c3
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.parseNmpCommand(java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    public static boolean switchOIS() {
        if (mInstance.oisList.length <= 1) {
            return false;
        }
        if (mInstance.curOisIdx + 1 >= mInstance.oisList.length) {
            mInstance.curOisIdx = 0;
            return true;
        }
        SoapClient soapClient = mInstance;
        soapClient.curOisIdx++;
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static net.sunniwell.sz.mop4.sdk.soap.SoapClient.AuthenResponse parseAuthenBody(java.lang.String r15) {
        /*
            r13 = 0
            if (r15 == 0) goto L_0x000b
            java.lang.String r12 = ""
            boolean r12 = r15.equals(r12)
            if (r12 == 0) goto L_0x0014
        L_0x000b:
            java.lang.String r12 = "SoapClient"
            java.lang.String r13 = "parseAuthenBody, body = null"
            android.util.Log.w(r12, r13)
            r0 = 0
        L_0x0013:
            return r0
        L_0x0014:
            net.sunniwell.sz.mop4.sdk.soap.SoapClient$AuthenResponse r0 = new net.sunniwell.sz.mop4.sdk.soap.SoapClient$AuthenResponse
            r0.<init>()
            r0.result = r13
            r0.allow_watch_duration = r13
            r7 = 0
            boolean r12 = android.text.TextUtils.isEmpty(r15)     // Catch:{ Exception -> 0x0087 }
            if (r12 != 0) goto L_0x004b
            java.io.StringReader r8 = new java.io.StringReader     // Catch:{ Exception -> 0x0087 }
            r8.<init>(r15)     // Catch:{ Exception -> 0x0087 }
            org.xml.sax.InputSource r11 = new org.xml.sax.InputSource     // Catch:{ Exception -> 0x00aa }
            r11.<init>(r8)     // Catch:{ Exception -> 0x00aa }
            org.dom4j.io.SAXReader r9 = new org.dom4j.io.SAXReader     // Catch:{ Exception -> 0x00aa }
            r9.<init>()     // Catch:{ Exception -> 0x00aa }
            org.dom4j.Document r2 = r9.read(r11)     // Catch:{ Exception -> 0x00aa }
            r8.close()     // Catch:{ Exception -> 0x00aa }
            r7 = 0
            org.dom4j.Element r10 = r2.getRootElement()     // Catch:{ Exception -> 0x0087 }
            java.lang.String r12 = "Body"
            java.util.Iterator r5 = r10.elementIterator(r12)     // Catch:{ Exception -> 0x0087 }
        L_0x0045:
            boolean r12 = r5.hasNext()     // Catch:{ Exception -> 0x0087 }
            if (r12 != 0) goto L_0x0051
        L_0x004b:
            if (r7 == 0) goto L_0x0013
            r7.close()
            goto L_0x0013
        L_0x0051:
            java.lang.Object r12 = r5.next()     // Catch:{ Exception -> 0x0087 }
            org.dom4j.Element r12 = (org.dom4j.Element) r12     // Catch:{ Exception -> 0x0087 }
            java.util.List r6 = r12.elements()     // Catch:{ Exception -> 0x0087 }
            java.util.Iterator r12 = r6.iterator()     // Catch:{ Exception -> 0x0087 }
        L_0x005f:
            boolean r13 = r12.hasNext()     // Catch:{ Exception -> 0x0087 }
            if (r13 == 0) goto L_0x0045
            java.lang.Object r4 = r12.next()     // Catch:{ Exception -> 0x0087 }
            org.dom4j.Element r4 = (org.dom4j.Element) r4     // Catch:{ Exception -> 0x0087 }
            java.lang.String r1 = r4.getName()     // Catch:{ Exception -> 0x0087 }
            java.lang.String r13 = "result"
            boolean r13 = r1.equals(r13)     // Catch:{ Exception -> 0x0087 }
            if (r13 == 0) goto L_0x0097
            java.lang.String r13 = r4.getStringValue()     // Catch:{ Exception -> 0x0087 }
            java.lang.String r14 = "success"
            boolean r13 = r13.equalsIgnoreCase(r14)     // Catch:{ Exception -> 0x0087 }
            if (r13 == 0) goto L_0x0093
            r13 = 1
            r0.result = r13     // Catch:{ Exception -> 0x0087 }
            goto L_0x005f
        L_0x0087:
            r3 = move-exception
        L_0x0088:
            java.lang.String r12 = "SoapClient"
            java.lang.String r13 = "parse param error"
            android.util.Log.i(r12, r13)
            r3.printStackTrace()
            goto L_0x004b
        L_0x0093:
            r13 = 0
            r0.result = r13     // Catch:{ Exception -> 0x0087 }
            goto L_0x005f
        L_0x0097:
            java.lang.String r13 = "allow_watch_duration"
            boolean r13 = r1.equals(r13)     // Catch:{ Exception -> 0x0087 }
            if (r13 == 0) goto L_0x005f
            java.lang.String r13 = r4.getStringValue()     // Catch:{ Exception -> 0x0087 }
            int r13 = java.lang.Integer.parseInt(r13)     // Catch:{ Exception -> 0x0087 }
            r0.allow_watch_duration = r13     // Catch:{ Exception -> 0x0087 }
            goto L_0x005f
        L_0x00aa:
            r3 = move-exception
            r7 = r8
            goto L_0x0088
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.parseAuthenBody(java.lang.String):net.sunniwell.sz.mop4.sdk.soap.SoapClient$AuthenResponse");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static net.sunniwell.sz.mop4.sdk.soap.UserBean parseUserInfo(java.lang.String r20, java.lang.String r21) {
        /*
            if (r20 == 0) goto L_0x000e
            java.lang.String r16 = ""
            r0 = r20
            r1 = r16
            boolean r16 = r0.equals(r1)
            if (r16 == 0) goto L_0x0017
        L_0x000e:
            java.lang.String r16 = "SoapClient"
            java.lang.String r17 = "parseUserInfo, body = null"
            android.util.Log.w(r16, r17)
            r13 = 0
        L_0x0016:
            return r13
        L_0x0017:
            r13 = 0
            r8 = 0
            boolean r16 = android.text.TextUtils.isEmpty(r20)     // Catch:{ DocumentException -> 0x0181 }
            if (r16 != 0) goto L_0x004a
            java.io.StringReader r9 = new java.io.StringReader     // Catch:{ DocumentException -> 0x0181 }
            r0 = r20
            r9.<init>(r0)     // Catch:{ DocumentException -> 0x0181 }
            org.xml.sax.InputSource r12 = new org.xml.sax.InputSource     // Catch:{ DocumentException -> 0x0184 }
            r12.<init>(r9)     // Catch:{ DocumentException -> 0x0184 }
            org.dom4j.io.SAXReader r10 = new org.dom4j.io.SAXReader     // Catch:{ DocumentException -> 0x0184 }
            r10.<init>()     // Catch:{ DocumentException -> 0x0184 }
            org.dom4j.Document r3 = r10.read(r12)     // Catch:{ DocumentException -> 0x0184 }
            r9.close()     // Catch:{ DocumentException -> 0x0184 }
            r8 = 0
            org.dom4j.Element r11 = r3.getRootElement()     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r16 = "Body"
            r0 = r16
            java.util.Iterator r6 = r11.elementIterator(r0)     // Catch:{ DocumentException -> 0x0181 }
        L_0x0044:
            boolean r16 = r6.hasNext()     // Catch:{ DocumentException -> 0x0181 }
            if (r16 != 0) goto L_0x0050
        L_0x004a:
            if (r8 == 0) goto L_0x0016
            r8.close()
            goto L_0x0016
        L_0x0050:
            java.lang.Object r16 = r6.next()     // Catch:{ DocumentException -> 0x0181 }
            org.dom4j.Element r16 = (org.dom4j.Element) r16     // Catch:{ DocumentException -> 0x0181 }
            java.util.List r7 = r16.elements()     // Catch:{ DocumentException -> 0x0181 }
            java.util.Iterator r16 = r7.iterator()     // Catch:{ DocumentException -> 0x0181 }
            r14 = r13
        L_0x005f:
            boolean r17 = r16.hasNext()     // Catch:{ DocumentException -> 0x0095 }
            if (r17 != 0) goto L_0x0067
            r13 = r14
            goto L_0x0044
        L_0x0067:
            java.lang.Object r5 = r16.next()     // Catch:{ DocumentException -> 0x0095 }
            org.dom4j.Element r5 = (org.dom4j.Element) r5     // Catch:{ DocumentException -> 0x0095 }
            java.lang.String r2 = r5.getName()     // Catch:{ DocumentException -> 0x0095 }
            java.lang.String r17 = "info"
            r0 = r17
            boolean r17 = r2.equals(r0)     // Catch:{ DocumentException -> 0x0095 }
            if (r17 == 0) goto L_0x005f
            java.lang.String r17 = "user"
            r0 = r17
            java.lang.String r15 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0095 }
            if (r15 == 0) goto L_0x00a2
            r0 = r21
            boolean r17 = r15.equals(r0)     // Catch:{ DocumentException -> 0x0095 }
            if (r17 != 0) goto L_0x00a2
            java.lang.String r17 = "SoapClient"
            java.lang.String r18 = "user not match, parseUserInfo failed!"
            android.util.Log.d(r17, r18)     // Catch:{ DocumentException -> 0x0095 }
            goto L_0x005f
        L_0x0095:
            r4 = move-exception
            r13 = r14
        L_0x0097:
            java.lang.String r16 = "SoapClient"
            java.lang.String r17 = "parse param error"
            android.util.Log.i(r16, r17)
            r4.printStackTrace()
            goto L_0x004a
        L_0x00a2:
            net.sunniwell.sz.mop4.sdk.soap.UserBean r13 = new net.sunniwell.sz.mop4.sdk.soap.UserBean     // Catch:{ DocumentException -> 0x0095 }
            r13.<init>()     // Catch:{ DocumentException -> 0x0095 }
            java.lang.String r17 = "user"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setId(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "password"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setPassword(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "realname"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setRealname(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "country"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setCountry(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "email"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setEmail(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "addr"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setAddr(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "phone"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setPhone(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "mobile"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setMobile(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "postcode"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setPostcode(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "birthday"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setBirthday(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "termcnt"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setTermcnt(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "allowst"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setAllowst(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "amount"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r17
            r13.setBalances(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "enable_utc"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            long r18 = java.lang.Long.parseLong(r17)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r18
            r13.setEnableUtcSecond(r0)     // Catch:{ DocumentException -> 0x0181 }
            java.lang.String r17 = "validto_utc"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0181 }
            long r18 = java.lang.Long.parseLong(r17)     // Catch:{ DocumentException -> 0x0181 }
            r0 = r18
            r13.setValidtoUtcSecond(r0)     // Catch:{ DocumentException -> 0x0181 }
            r14 = r13
            goto L_0x005f
        L_0x0181:
            r4 = move-exception
            goto L_0x0097
        L_0x0184:
            r4 = move-exception
            r8 = r9
            goto L_0x0097
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.parseUserInfo(java.lang.String, java.lang.String):net.sunniwell.sz.mop4.sdk.soap.UserBean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static net.sunniwell.sz.mop4.sdk.soap.BankAccountBean parseBankAccountInfo(java.lang.String r18, java.lang.String r19) {
        /*
            if (r18 == 0) goto L_0x000c
            java.lang.String r15 = ""
            r0 = r18
            boolean r15 = r0.equals(r15)
            if (r15 == 0) goto L_0x0015
        L_0x000c:
            java.lang.String r15 = "SoapClient"
            java.lang.String r16 = "parseUserInfo, body = null"
            android.util.Log.w(r15, r16)
            r1 = 0
        L_0x0014:
            return r1
        L_0x0015:
            r1 = 0
            r9 = 0
            java.io.StringReader r10 = new java.io.StringReader     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r18
            r10.<init>(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            org.xml.sax.InputSource r13 = new org.xml.sax.InputSource     // Catch:{ DocumentException -> 0x01d2, Exception -> 0x01c9 }
            r13.<init>(r10)     // Catch:{ DocumentException -> 0x01d2, Exception -> 0x01c9 }
            org.dom4j.io.SAXReader r11 = new org.dom4j.io.SAXReader     // Catch:{ DocumentException -> 0x01d2, Exception -> 0x01c9 }
            r11.<init>()     // Catch:{ DocumentException -> 0x01d2, Exception -> 0x01c9 }
            org.dom4j.Document r4 = r11.read(r13)     // Catch:{ DocumentException -> 0x01d2, Exception -> 0x01c9 }
            r10.close()     // Catch:{ DocumentException -> 0x01d2, Exception -> 0x01c9 }
            r9 = 0
            org.dom4j.Element r12 = r4.getRootElement()     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r15 = "Body"
            java.util.Iterator r7 = r12.elementIterator(r15)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
        L_0x003a:
            boolean r15 = r7.hasNext()     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            if (r15 != 0) goto L_0x0046
        L_0x0040:
            if (r9 == 0) goto L_0x0014
            r9.close()
            goto L_0x0014
        L_0x0046:
            java.lang.Object r15 = r7.next()     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            org.dom4j.Element r15 = (org.dom4j.Element) r15     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.util.List r8 = r15.elements()     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.util.Iterator r15 = r8.iterator()     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r2 = r1
        L_0x0055:
            boolean r16 = r15.hasNext()     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            if (r16 != 0) goto L_0x005d
            r1 = r2
            goto L_0x003a
        L_0x005d:
            java.lang.Object r6 = r15.next()     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            org.dom4j.Element r6 = (org.dom4j.Element) r6     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            java.lang.String r3 = r6.getName()     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            java.lang.String r16 = "bankacount"
            r0 = r16
            boolean r16 = r3.equals(r0)     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            if (r16 == 0) goto L_0x0055
            java.lang.String r16 = "user"
            r0 = r16
            java.lang.String r14 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            if (r14 == 0) goto L_0x0083
            r0 = r19
            boolean r16 = r14.equals(r0)     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            if (r16 != 0) goto L_0x0098
        L_0x0083:
            java.lang.String r16 = "SoapClient"
            java.lang.String r17 = "user not match, parseBankAccountInfo failed!"
            android.util.Log.d(r16, r17)     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            goto L_0x0055
        L_0x008b:
            r5 = move-exception
            r1 = r2
        L_0x008d:
            java.lang.String r15 = "SoapClient"
            java.lang.String r16 = "parse param error"
            android.util.Log.i(r15, r16)
            r5.printStackTrace()
            goto L_0x0040
        L_0x0098:
            net.sunniwell.sz.mop4.sdk.soap.BankAccountBean r1 = new net.sunniwell.sz.mop4.sdk.soap.BankAccountBean     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            r1.<init>()     // Catch:{ DocumentException -> 0x008b, Exception -> 0x01cc }
            java.lang.String r16 = "user"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setUser_id(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "authorised"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            int r16 = java.lang.Integer.parseInt(r16)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setAuthorised(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "status"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            int r16 = java.lang.Integer.parseInt(r16)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setStatus(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "ACCT"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setCard_number(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "CREDITCARDTYPE"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setCard_type(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "EXPDATE"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setExpiration_date(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "CVV2"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setSecurity_code(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "FIRSTNAME"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setFirst_name(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "LASTNAME"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setLast_name(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "COMPANY"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r17 = "UTF-8"
            java.lang.String r16 = java.net.URLDecoder.decode(r16, r17)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setCompany_name(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "STREET"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r17 = "UTF-8"
            java.lang.String r16 = java.net.URLDecoder.decode(r16, r17)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setStreet(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "STREET2"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r17 = "UTF-8"
            java.lang.String r16 = java.net.URLDecoder.decode(r16, r17)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setStreet2(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "CITY"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r17 = "UTF-8"
            java.lang.String r16 = java.net.URLDecoder.decode(r16, r17)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setCity(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "STATE"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r17 = "UTF-8"
            java.lang.String r16 = java.net.URLDecoder.decode(r16, r17)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setState(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "COUNTRYCODE"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setCountry(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "COMPANY"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r17 = "UTF-8"
            java.lang.String r16 = java.net.URLDecoder.decode(r16, r17)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setCompany_name(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "BIRTHDAY"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setBirthday(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "PHONE"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setPhone(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            java.lang.String r16 = "EMAIL"
            r0 = r16
            java.lang.String r16 = r6.attributeValue(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r0 = r16
            r1.setEmail(r0)     // Catch:{ DocumentException -> 0x01cf, Exception -> 0x01c3 }
            r2 = r1
            goto L_0x0055
        L_0x01c3:
            r5 = move-exception
        L_0x01c4:
            r5.printStackTrace()
            goto L_0x0040
        L_0x01c9:
            r5 = move-exception
            r9 = r10
            goto L_0x01c4
        L_0x01cc:
            r5 = move-exception
            r1 = r2
            goto L_0x01c4
        L_0x01cf:
            r5 = move-exception
            goto L_0x008d
        L_0x01d2:
            r5 = move-exception
            r9 = r10
            goto L_0x008d
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.parseBankAccountInfo(java.lang.String, java.lang.String):net.sunniwell.sz.mop4.sdk.soap.BankAccountBean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.ArrayList<net.sunniwell.sz.mop4.sdk.soap.SubscribeBean> parseSubscribeList(java.lang.String r20, java.lang.String r21) {
        /*
            if (r20 == 0) goto L_0x000e
            java.lang.String r16 = ""
            r0 = r20
            r1 = r16
            boolean r16 = r0.equals(r1)
            if (r16 == 0) goto L_0x0017
        L_0x000e:
            java.lang.String r16 = "SoapClient"
            java.lang.String r17 = "parseSubscribeList, body = null"
            android.util.Log.w(r16, r17)
            r13 = 0
        L_0x0016:
            return r13
        L_0x0017:
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            r8 = 0
            java.io.StringReader r9 = new java.io.StringReader     // Catch:{ DocumentException -> 0x0090 }
            r0 = r20
            r9.<init>(r0)     // Catch:{ DocumentException -> 0x0090 }
            org.xml.sax.InputSource r14 = new org.xml.sax.InputSource     // Catch:{ DocumentException -> 0x0186 }
            r14.<init>(r9)     // Catch:{ DocumentException -> 0x0186 }
            org.dom4j.io.SAXReader r10 = new org.dom4j.io.SAXReader     // Catch:{ DocumentException -> 0x0186 }
            r10.<init>()     // Catch:{ DocumentException -> 0x0186 }
            org.dom4j.Document r3 = r10.read(r14)     // Catch:{ DocumentException -> 0x0186 }
            r9.close()     // Catch:{ DocumentException -> 0x0186 }
            r8 = 0
            org.dom4j.Element r11 = r3.getRootElement()     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r16 = "Body"
            r0 = r16
            java.util.Iterator r6 = r11.elementIterator(r0)     // Catch:{ DocumentException -> 0x0090 }
        L_0x0042:
            boolean r16 = r6.hasNext()     // Catch:{ DocumentException -> 0x0090 }
            if (r16 != 0) goto L_0x004e
        L_0x0048:
            if (r8 == 0) goto L_0x0016
            r8.close()
            goto L_0x0016
        L_0x004e:
            java.lang.Object r16 = r6.next()     // Catch:{ DocumentException -> 0x0090 }
            org.dom4j.Element r16 = (org.dom4j.Element) r16     // Catch:{ DocumentException -> 0x0090 }
            java.util.List r7 = r16.elements()     // Catch:{ DocumentException -> 0x0090 }
            java.util.Iterator r16 = r7.iterator()     // Catch:{ DocumentException -> 0x0090 }
        L_0x005c:
            boolean r17 = r16.hasNext()     // Catch:{ DocumentException -> 0x0090 }
            if (r17 == 0) goto L_0x0042
            java.lang.Object r5 = r16.next()     // Catch:{ DocumentException -> 0x0090 }
            org.dom4j.Element r5 = (org.dom4j.Element) r5     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r2 = r5.getName()     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r17 = "subscribelist"
            r0 = r17
            boolean r17 = r2.equals(r0)     // Catch:{ DocumentException -> 0x0090 }
            if (r17 == 0) goto L_0x005c
            java.lang.String r17 = "user"
            r0 = r17
            java.lang.String r15 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            if (r15 == 0) goto L_0x009c
            r0 = r21
            boolean r17 = r15.equals(r0)     // Catch:{ DocumentException -> 0x0090 }
            if (r17 != 0) goto L_0x009c
            java.lang.String r17 = "SoapClient"
            java.lang.String r18 = "user not match, parseSubscribeList failed!"
            android.util.Log.d(r17, r18)     // Catch:{ DocumentException -> 0x0090 }
            goto L_0x005c
        L_0x0090:
            r4 = move-exception
        L_0x0091:
            java.lang.String r16 = "SoapClient"
            java.lang.String r17 = "parse param error"
            android.util.Log.i(r16, r17)
            r4.printStackTrace()
            goto L_0x0048
        L_0x009c:
            net.sunniwell.sz.mop4.sdk.soap.SubscribeBean r12 = new net.sunniwell.sz.mop4.sdk.soap.SubscribeBean     // Catch:{ DocumentException -> 0x0090 }
            r12.<init>()     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r17 = "user"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            r0 = r17
            r12.setUser_id(r0)     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r17 = "sid"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            r0 = r17
            r12.setService_id(r0)     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r17 = "start_utc"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            long r18 = java.lang.Long.parseLong(r17)     // Catch:{ DocumentException -> 0x0090 }
            r0 = r18
            r12.setStart_utcSecond(r0)     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r17 = "end_utc"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            long r18 = java.lang.Long.parseLong(r17)     // Catch:{ DocumentException -> 0x0090 }
            r0 = r18
            r12.setEnd_utcSecond(r0)     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r17 = "price"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0090 }
            r0 = r17
            r12.setPrice(r0)     // Catch:{ DocumentException -> 0x0090 }
            java.lang.String r17 = "renew"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            if (r17 == 0) goto L_0x016e
            java.lang.String r17 = "renew"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            boolean r17 = r17.isEmpty()     // Catch:{ DocumentException -> 0x0090 }
            if (r17 != 0) goto L_0x016e
            java.lang.String r17 = "renew"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0090 }
            r0 = r17
            r12.setRenew(r0)     // Catch:{ DocumentException -> 0x0090 }
        L_0x0117:
            java.lang.String r17 = "renew_state"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            if (r17 == 0) goto L_0x0176
            java.lang.String r17 = "renew_state"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            boolean r17 = r17.isEmpty()     // Catch:{ DocumentException -> 0x0090 }
            if (r17 != 0) goto L_0x0176
            java.lang.String r17 = "renew_state"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0090 }
            r0 = r17
            r12.setRenew_state(r0)     // Catch:{ DocumentException -> 0x0090 }
        L_0x0140:
            java.lang.String r17 = "errno"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            if (r17 == 0) goto L_0x017e
            java.lang.String r17 = "errno"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            boolean r17 = r17.isEmpty()     // Catch:{ DocumentException -> 0x0090 }
            if (r17 != 0) goto L_0x017e
            java.lang.String r17 = "errno"
            r0 = r17
            java.lang.String r17 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x0090 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0090 }
            r0 = r17
            r12.setErrno(r0)     // Catch:{ DocumentException -> 0x0090 }
        L_0x0169:
            r13.add(r12)     // Catch:{ DocumentException -> 0x0090 }
            goto L_0x005c
        L_0x016e:
            r17 = 0
            r0 = r17
            r12.setRenew(r0)     // Catch:{ DocumentException -> 0x0090 }
            goto L_0x0117
        L_0x0176:
            r17 = 0
            r0 = r17
            r12.setRenew_state(r0)     // Catch:{ DocumentException -> 0x0090 }
            goto L_0x0140
        L_0x017e:
            r17 = 0
            r0 = r17
            r12.setErrno(r0)     // Catch:{ DocumentException -> 0x0090 }
            goto L_0x0169
        L_0x0186:
            r4 = move-exception
            r8 = r9
            goto L_0x0091
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.parseSubscribeList(java.lang.String, java.lang.String):java.util.ArrayList");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.ArrayList<net.sunniwell.sz.mop4.sdk.soap.AssetBean> parseConsumeList(java.lang.String r20, java.lang.String r21) {
        /*
            if (r20 == 0) goto L_0x000e
            java.lang.String r16 = ""
            r0 = r20
            r1 = r16
            boolean r16 = r0.equals(r1)
            if (r16 == 0) goto L_0x0017
        L_0x000e:
            java.lang.String r16 = "SoapClient"
            java.lang.String r17 = "parseConsumeList, body = null"
            android.util.Log.w(r16, r17)
            r3 = 0
        L_0x0016:
            return r3
        L_0x0017:
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r10 = 0
            boolean r16 = android.text.TextUtils.isEmpty(r20)     // Catch:{ DocumentException -> 0x0096 }
            if (r16 != 0) goto L_0x004e
            java.io.StringReader r11 = new java.io.StringReader     // Catch:{ DocumentException -> 0x0096 }
            r0 = r20
            r11.<init>(r0)     // Catch:{ DocumentException -> 0x0096 }
            org.xml.sax.InputSource r14 = new org.xml.sax.InputSource     // Catch:{ DocumentException -> 0x010a }
            r14.<init>(r11)     // Catch:{ DocumentException -> 0x010a }
            org.dom4j.io.SAXReader r12 = new org.dom4j.io.SAXReader     // Catch:{ DocumentException -> 0x010a }
            r12.<init>()     // Catch:{ DocumentException -> 0x010a }
            org.dom4j.Document r5 = r12.read(r14)     // Catch:{ DocumentException -> 0x010a }
            r11.close()     // Catch:{ DocumentException -> 0x010a }
            r10 = 0
            org.dom4j.Element r13 = r5.getRootElement()     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r16 = "Body"
            r0 = r16
            java.util.Iterator r8 = r13.elementIterator(r0)     // Catch:{ DocumentException -> 0x0096 }
        L_0x0048:
            boolean r16 = r8.hasNext()     // Catch:{ DocumentException -> 0x0096 }
            if (r16 != 0) goto L_0x0054
        L_0x004e:
            if (r10 == 0) goto L_0x0016
            r10.close()
            goto L_0x0016
        L_0x0054:
            java.lang.Object r16 = r8.next()     // Catch:{ DocumentException -> 0x0096 }
            org.dom4j.Element r16 = (org.dom4j.Element) r16     // Catch:{ DocumentException -> 0x0096 }
            java.util.List r9 = r16.elements()     // Catch:{ DocumentException -> 0x0096 }
            java.util.Iterator r16 = r9.iterator()     // Catch:{ DocumentException -> 0x0096 }
        L_0x0062:
            boolean r17 = r16.hasNext()     // Catch:{ DocumentException -> 0x0096 }
            if (r17 == 0) goto L_0x0048
            java.lang.Object r7 = r16.next()     // Catch:{ DocumentException -> 0x0096 }
            org.dom4j.Element r7 = (org.dom4j.Element) r7     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r4 = r7.getName()     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r17 = "assetlist"
            r0 = r17
            boolean r17 = r4.equals(r0)     // Catch:{ DocumentException -> 0x0096 }
            if (r17 == 0) goto L_0x0062
            java.lang.String r17 = "user"
            r0 = r17
            java.lang.String r15 = r7.attributeValue(r0)     // Catch:{ DocumentException -> 0x0096 }
            if (r15 == 0) goto L_0x00a2
            r0 = r21
            boolean r17 = r15.equals(r0)     // Catch:{ DocumentException -> 0x0096 }
            if (r17 != 0) goto L_0x00a2
            java.lang.String r17 = "SoapClient"
            java.lang.String r18 = "user not match, parseConsumeList failed!"
            android.util.Log.d(r17, r18)     // Catch:{ DocumentException -> 0x0096 }
            goto L_0x0062
        L_0x0096:
            r6 = move-exception
        L_0x0097:
            java.lang.String r16 = "SoapClient"
            java.lang.String r17 = "parse param error"
            android.util.Log.i(r16, r17)
            r6.printStackTrace()
            goto L_0x004e
        L_0x00a2:
            net.sunniwell.sz.mop4.sdk.soap.AssetBean r2 = new net.sunniwell.sz.mop4.sdk.soap.AssetBean     // Catch:{ DocumentException -> 0x0096 }
            r2.<init>()     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r17 = "user"
            r0 = r17
            java.lang.String r17 = r7.attributeValue(r0)     // Catch:{ DocumentException -> 0x0096 }
            r0 = r17
            r2.setUser_id(r0)     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r17 = "sid"
            r0 = r17
            java.lang.String r17 = r7.attributeValue(r0)     // Catch:{ DocumentException -> 0x0096 }
            r0 = r17
            r2.setService_id(r0)     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r17 = "op_type"
            r0 = r17
            java.lang.String r17 = r7.attributeValue(r0)     // Catch:{ DocumentException -> 0x0096 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0096 }
            r0 = r17
            r2.setOp_type(r0)     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r17 = "op_amount"
            r0 = r17
            java.lang.String r17 = r7.attributeValue(r0)     // Catch:{ DocumentException -> 0x0096 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0096 }
            r0 = r17
            r2.setOp_amount(r0)     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r17 = "op_utc"
            r0 = r17
            java.lang.String r17 = r7.attributeValue(r0)     // Catch:{ DocumentException -> 0x0096 }
            long r18 = java.lang.Long.parseLong(r17)     // Catch:{ DocumentException -> 0x0096 }
            r0 = r18
            r2.setOp_utcSecond(r0)     // Catch:{ DocumentException -> 0x0096 }
            java.lang.String r17 = "balances"
            r0 = r17
            java.lang.String r17 = r7.attributeValue(r0)     // Catch:{ DocumentException -> 0x0096 }
            int r17 = java.lang.Integer.parseInt(r17)     // Catch:{ DocumentException -> 0x0096 }
            r0 = r17
            r2.setBalances(r0)     // Catch:{ DocumentException -> 0x0096 }
            r3.add(r2)     // Catch:{ DocumentException -> 0x0096 }
            goto L_0x0062
        L_0x010a:
            r6 = move-exception
            r10 = r11
            goto L_0x0097
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.parseConsumeList(java.lang.String, java.lang.String):java.util.ArrayList");
    }

    /* JADX INFO: used method not loaded: org.dom4j.Element.attributeValue(java.lang.String):null, types can be incorrect */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0088, code lost:
        r13 = new net.sunniwell.p008sz.mop4.sdk.soap.VideoCardBean();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        r13.setCard_id(r5.attributeValue("card_id"));
        r13.setAmount(java.lang.Integer.parseInt(r5.attributeValue("amount")));
        r13.setUsed(java.lang.Integer.parseInt(r5.attributeValue("used")));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00b0, code lost:
        r14 = r13;
     */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static net.sunniwell.sz.mop4.sdk.soap.VideoCardBean parseVideoCardInfo(java.lang.String r17, java.lang.String r18) {
        /*
            if (r17 == 0) goto L_0x000c
            java.lang.String r15 = ""
            r0 = r17
            boolean r15 = r0.equals(r15)
            if (r15 == 0) goto L_0x0015
        L_0x000c:
            java.lang.String r15 = "SoapClient"
            java.lang.String r16 = "parseVideoCardInfo, body = null"
            android.util.Log.w(r15, r16)
            r13 = 0
        L_0x0014:
            return r13
        L_0x0015:
            r13 = 0
            r8 = 0
            boolean r15 = android.text.TextUtils.isEmpty(r17)     // Catch:{ DocumentException -> 0x00b2 }
            if (r15 != 0) goto L_0x0048
            java.io.StringReader r9 = new java.io.StringReader     // Catch:{ DocumentException -> 0x00b2 }
            r0 = r17
            r9.<init>(r0)     // Catch:{ DocumentException -> 0x00b2 }
            org.xml.sax.InputSource r12 = new org.xml.sax.InputSource     // Catch:{ DocumentException -> 0x00be }
            r12.<init>(r9)     // Catch:{ DocumentException -> 0x00be }
            org.dom4j.io.SAXReader r10 = new org.dom4j.io.SAXReader     // Catch:{ DocumentException -> 0x00be }
            r10.<init>()     // Catch:{ DocumentException -> 0x00be }
            org.dom4j.Document r3 = r10.read(r12)     // Catch:{ DocumentException -> 0x00be }
            r9.close()     // Catch:{ DocumentException -> 0x00be }
            r8 = 0
            org.dom4j.Element r11 = r3.getRootElement()     // Catch:{ DocumentException -> 0x00b2 }
            java.lang.String r15 = "Body"
            java.util.Iterator r6 = r11.elementIterator(r15)     // Catch:{ DocumentException -> 0x00b2 }
            r14 = r13
        L_0x0041:
            boolean r15 = r6.hasNext()     // Catch:{ DocumentException -> 0x00c1 }
            if (r15 != 0) goto L_0x004e
            r13 = r14
        L_0x0048:
            if (r8 == 0) goto L_0x0014
            r8.close()
            goto L_0x0014
        L_0x004e:
            java.lang.Object r15 = r6.next()     // Catch:{ DocumentException -> 0x00c1 }
            org.dom4j.Element r15 = (org.dom4j.Element) r15     // Catch:{ DocumentException -> 0x00c1 }
            java.util.List r7 = r15.elements()     // Catch:{ DocumentException -> 0x00c1 }
            java.util.Iterator r15 = r7.iterator()     // Catch:{ DocumentException -> 0x00c1 }
        L_0x005c:
            boolean r16 = r15.hasNext()     // Catch:{ DocumentException -> 0x00c1 }
            if (r16 == 0) goto L_0x0041
            java.lang.Object r5 = r15.next()     // Catch:{ DocumentException -> 0x00c1 }
            org.dom4j.Element r5 = (org.dom4j.Element) r5     // Catch:{ DocumentException -> 0x00c1 }
            java.lang.String r2 = r5.getName()     // Catch:{ DocumentException -> 0x00c1 }
            java.lang.String r16 = "info"
            r0 = r16
            boolean r16 = r2.equals(r0)     // Catch:{ DocumentException -> 0x00c1 }
            if (r16 == 0) goto L_0x005c
            java.lang.String r16 = "card_id"
            r0 = r16
            java.lang.String r1 = r5.attributeValue(r0)     // Catch:{ DocumentException -> 0x00c1 }
            if (r1 == 0) goto L_0x005c
            r0 = r18
            boolean r16 = r1.equals(r0)     // Catch:{ DocumentException -> 0x00c1 }
            if (r16 == 0) goto L_0x005c
            net.sunniwell.sz.mop4.sdk.soap.VideoCardBean r13 = new net.sunniwell.sz.mop4.sdk.soap.VideoCardBean     // Catch:{ DocumentException -> 0x00c1 }
            r13.<init>()     // Catch:{ DocumentException -> 0x00c1 }
            java.lang.String r15 = "card_id"
            java.lang.String r15 = r5.attributeValue(r15)     // Catch:{ DocumentException -> 0x00b2 }
            r13.setCard_id(r15)     // Catch:{ DocumentException -> 0x00b2 }
            java.lang.String r15 = "amount"
            java.lang.String r15 = r5.attributeValue(r15)     // Catch:{ DocumentException -> 0x00b2 }
            int r15 = java.lang.Integer.parseInt(r15)     // Catch:{ DocumentException -> 0x00b2 }
            r13.setAmount(r15)     // Catch:{ DocumentException -> 0x00b2 }
            java.lang.String r15 = "used"
            java.lang.String r15 = r5.attributeValue(r15)     // Catch:{ DocumentException -> 0x00b2 }
            int r15 = java.lang.Integer.parseInt(r15)     // Catch:{ DocumentException -> 0x00b2 }
            r13.setUsed(r15)     // Catch:{ DocumentException -> 0x00b2 }
            r14 = r13
            goto L_0x0041
        L_0x00b2:
            r4 = move-exception
        L_0x00b3:
            java.lang.String r15 = "SoapClient"
            java.lang.String r16 = "parse param error"
            android.util.Log.i(r15, r16)
            r4.printStackTrace()
            goto L_0x0048
        L_0x00be:
            r4 = move-exception
            r8 = r9
            goto L_0x00b3
        L_0x00c1:
            r4 = move-exception
            r13 = r14
            goto L_0x00b3
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.parseVideoCardInfo(java.lang.String, java.lang.String):net.sunniwell.sz.mop4.sdk.soap.VideoCardBean");
    }

    /* access modifiers changed from: private */
    public static void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SoapResponse doPost(String url, String content) {
        String value;
        String value2;
        String tmp;
        String tmp2;
        String tmp3;
        String tmp4;
        String tmp5;
        synchronized (mInstance.mSyncObj) {
            SoapResponse soapResponse = null;
            try {
                HttpResponse httpResponse = mInstance.mHelper.doPost(url, content);
                if (httpResponse == null) {
                    mInstance.mHelper.disconnect();
                    mInstance.mHelper.connect();
                } else {
                    SoapResponse soapResponse2 = new SoapResponse();
                    try {
                        int statusCode = httpResponse.getStatusLine().getStatusCode();
                        soapResponse2.statusCode = statusCode;
                        if (statusCode == 200) {
                            String method = url.substring(url.length() - 5);
                            if (method != null && method.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_LOGIN)) {
                                if (httpResponse.getFirstHeader("Token") == null) {
                                    tmp = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp = httpResponse.getFirstHeader("Token").getValue();
                                }
                                if (tmp != null && !tmp.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.oisToken = tmp;
                                }
                                if (httpResponse.getFirstHeader("EPGS-Token") == null) {
                                    tmp2 = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp2 = httpResponse.getFirstHeader("EPGS-Token").getValue();
                                }
                                if (tmp2 != null && !tmp2.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.epgsToken = tmp2;
                                }
                                if (httpResponse.getFirstHeader("OIS") == null) {
                                    tmp3 = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp3 = httpResponse.getFirstHeader("OIS").getValue();
                                }
                                if (tmp3 != null && !tmp3.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.ois = tmp3;
                                }
                                if (httpResponse.getFirstHeader("EPGS") == null) {
                                    tmp4 = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp4 = httpResponse.getFirstHeader("EPGS").getValue();
                                }
                                if (tmp4 != null && !tmp4.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.epgs = tmp4;
                                }
                                if (httpResponse.getFirstHeader("UTC") == null) {
                                    tmp5 = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp5 = httpResponse.getFirstHeader("UTC").getValue();
                                }
                                if (tmp5 != null && !tmp5.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.utc = Long.parseLong(tmp5);
                                }
                            }
                            BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                            int length = (int) httpResponse.getEntity().getContentLength();
                            if (length > 0) {
                                char[] buffer = new char[length];
                                soapResponse2.body = XmlPullParser.NO_NAMESPACE;
                                while (true) {
                                    int read = br.read(buffer, 0, length);
                                    if (read <= 0) {
                                        break;
                                    }
                                    soapResponse2.body += String.copyValueOf(buffer, 0, read).trim();
                                }
                            }
                            if (httpResponse.getFirstHeader("Location") == null) {
                                value2 = XmlPullParser.NO_NAMESPACE;
                            } else {
                                value2 = httpResponse.getFirstHeader("Location").getValue();
                            }
                            soapResponse2.location = value2;
                            br.close();
                            Log.i(TAG, "httpsPost success ");
                            soapResponse = soapResponse2;
                        } else if (statusCode == 301 || statusCode == 302) {
                            if (httpResponse.getFirstHeader("Location") == null) {
                                value = XmlPullParser.NO_NAMESPACE;
                            } else {
                                value = httpResponse.getFirstHeader("Location").getValue();
                            }
                            soapResponse2.location = value;
                            Log.i(TAG, "httpsPost 30X " + soapResponse2.location);
                            soapResponse = soapResponse2;
                        } else {
                            soapResponse = soapResponse2;
                        }
                    } catch (Exception e) {
                        e = e;
                        SoapResponse soapResponse3 = soapResponse2;
                        try {
                            e.printStackTrace();
                            soapResponse = null;
                            return soapResponse;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        SoapResponse soapResponse4 = soapResponse2;
                        throw th;
                    }
                }
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                soapResponse = null;
                return soapResponse;
            }
            return soapResponse;
        }
    }

    private static SoapResponse doGet(String url, List<NameValuePair> dataList) {
        String value;
        String tmp;
        String tmp2;
        String tmp3;
        String tmp4;
        String tmp5;
        synchronized (mInstance.mSyncObj) {
            SoapResponse soapResponse = null;
            try {
                HttpResponse httpResponse = mInstance.mHelper.doGet(url, dataList);
                if (httpResponse == null) {
                    mInstance.mHelper.disconnect();
                    mInstance.mHelper.connect();
                } else {
                    SoapResponse soapResponse2 = new SoapResponse();
                    try {
                        int statusCode = httpResponse.getStatusLine().getStatusCode();
                        soapResponse2.statusCode = statusCode;
                        if (statusCode == 200) {
                            String method = url.substring(url.length() - 5);
                            if (method != null && method.equalsIgnoreCase(LogBean1.TERMINAL_ACTION_LOGIN)) {
                                if (httpResponse.getFirstHeader("Token") == null) {
                                    tmp = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp = httpResponse.getFirstHeader("Token").getValue();
                                }
                                if (tmp != null && !tmp.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.oisToken = tmp;
                                }
                                if (httpResponse.getFirstHeader("EPGS-Token") == null) {
                                    tmp2 = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp2 = httpResponse.getFirstHeader("EPGS-Token").getValue();
                                }
                                if (tmp2 != null && !tmp2.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.epgsToken = tmp2;
                                }
                                if (httpResponse.getFirstHeader("OIS") == null) {
                                    tmp3 = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp3 = httpResponse.getFirstHeader("OIS").getValue();
                                }
                                if (tmp3 != null && !tmp3.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.ois = tmp3;
                                }
                                if (httpResponse.getFirstHeader("EPGS") == null) {
                                    tmp4 = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp4 = httpResponse.getFirstHeader("EPGS").getValue();
                                }
                                if (tmp4 != null && !tmp4.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.epgs = tmp4;
                                }
                                if (httpResponse.getFirstHeader("UTC") == null) {
                                    tmp5 = XmlPullParser.NO_NAMESPACE;
                                } else {
                                    tmp5 = httpResponse.getFirstHeader("UTC").getValue();
                                }
                                if (tmp5 != null && !tmp5.equals(XmlPullParser.NO_NAMESPACE)) {
                                    soapResponse2.utc = Long.parseLong(tmp5);
                                }
                            }
                            BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                            int length = (int) httpResponse.getEntity().getContentLength();
                            if (length > 0) {
                                char[] buffer = new char[length];
                                soapResponse2.body = XmlPullParser.NO_NAMESPACE;
                                while (true) {
                                    int read = br.read(buffer, 0, length);
                                    if (read <= 0) {
                                        break;
                                    }
                                    soapResponse2.body += String.copyValueOf(buffer, 0, read).trim();
                                }
                            }
                            br.close();
                            Log.e(TAG, "httpsPost success " + soapResponse2.body);
                            soapResponse = soapResponse2;
                        } else if (statusCode == 301 || statusCode == 302) {
                            if (httpResponse.getFirstHeader("Location") == null) {
                                value = XmlPullParser.NO_NAMESPACE;
                            } else {
                                value = httpResponse.getFirstHeader("Location").getValue();
                            }
                            soapResponse2.location = value;
                            Log.i(TAG, "httpsPost 30X " + soapResponse2.location);
                            soapResponse = soapResponse2;
                        } else {
                            soapResponse = soapResponse2;
                        }
                    } catch (Exception e) {
                        e = e;
                        SoapResponse soapResponse3 = soapResponse2;
                        try {
                            e.printStackTrace();
                            soapResponse = null;
                            return soapResponse;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        SoapResponse soapResponse4 = soapResponse2;
                        throw th;
                    }
                }
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                soapResponse = null;
                return soapResponse;
            }
            return soapResponse;
        }
    }
}
