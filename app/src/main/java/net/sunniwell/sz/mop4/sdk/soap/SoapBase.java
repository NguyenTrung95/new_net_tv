package net.sunniwell.sz.mop4.sdk.soap;

import java.net.URLEncoder;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapBase */
public class SoapBase {
    public static final String METHOD_ALLPARAM = "/ois/terminal/allparam_ret";
    public static final String METHOD_AUTH = "/ois/play/authen";
    public static final String METHOD_BIND_BANKACCOUNTINFO = "/ois/user/bankaccount/bind";
    public static final String METHOD_CANCELRENEW = "/ois/user/subscribe/cancelrenew";
    public static final String METHOD_CHANGE_PASSWORD = "/ois/user/setpassword";
    public static final String METHOD_CONSUMELIST = "/ois/asset/list";
    public static final String METHOD_ENABLE = "/ois/stb/enable";
    public static final String METHOD_GET_BANKACCOUNTINFO = "/ois/user/bankaccount/info";
    public static final String METHOD_GET_USERINFO = "/ois/user/info";
    public static final String METHOD_LOGIN = "/ois/user/login";
    public static final String METHOD_LOGOUT = "/ois/user/logout";
    public static final String METHOD_PARAM = "/ois/terminal/param_ret";
    public static final String METHOD_PAYPAL_BUY = "/ois/paypal/buy";
    public static final String METHOD_PAYPAL_RECHARGE = "/ois/paypal/recharge";
    public static final String METHOD_PCTENPAY_BUY = "/ois/pctenpay/buy";
    public static final String METHOD_PCTENPAY_RECHARGE = "/ois/pctenpay/recharge";
    public static final String METHOD_RECHARGE = "/ois/user/recharge";
    public static final String METHOD_RECHARGE_BY_CREDITCARD = "/ois/paypal/paymentpro/recharge";
    public static final String METHOD_REGISTER = "/ois/user/register";
    public static final String METHOD_RENEW = "/ois/user/subscribe/renew";
    public static final String METHOD_SENDLOG = "/ois/log";
    public static final String METHOD_SET_USERINFO = "/ois/user/modify";
    public static final String METHOD_SUBCRIBELIST = "/ois/subscribe/list";
    public static final String METHOD_SUBSCRIBE = "/ois/user/subscribe";
    public static final String METHOD_SUBSCRIBE_BY_CREDITCARD = "/ois/paypal/paymentpro/buy";
    public static final String METHOD_UNBIND_BANKACCOUNTINFO = "/ois/user/bankaccount/unbind";
    public static final String METHOD_UNSUBSCRIBE = "/ois/user/unsubscribe";
    public static final String METHOD_USER_BIND = "/ois/user/bind";
    public static final String METHOD_VIDEOCARD_INFO = "/ois/videocard/info";
    public static final String METHOD_VIDEOCARD_RECHARGE = "/ois/videocard/recharge";
    public static final String METHOD_WAPTENPAY_BUY = "/ois/waptenpay/buy";
    public static final String METHOD_WAPTENPAY_RECHARGE = "/ois/waptenpay/recharge";
    private static final String SOAP_HEAD = "<?xml version='1.0' encoding='UTF-8'?>\r\n<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"default\" SOAP-ENV:encodingStyle=\"default\">\r\n\t<SOAP-ENV:Body>\r\n";
    private static final String SOAP_TAIL = "\r\n\t</SOAP-ENV:Body>\r\n</SOAP-ENV:Envelope>\r\n";

    public static String getLoginEntity(String user, String password, String terminalId, String terminalType, String mac, String netmode, String soft_ver, String hard_ver, String epg, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (password == null) {
            password = XmlPullParser.NO_NAMESPACE;
        }
        if (terminalId == null) {
            terminalId = XmlPullParser.NO_NAMESPACE;
        }
        if (terminalType == null) {
            terminalType = XmlPullParser.NO_NAMESPACE;
        }
        if (mac == null) {
            mac = XmlPullParser.NO_NAMESPACE;
        }
        if (netmode == null) {
            netmode = XmlPullParser.NO_NAMESPACE;
        }
        if (soft_ver == null) {
            soft_ver = XmlPullParser.NO_NAMESPACE;
        }
        if (hard_ver == null) {
            hard_ver = XmlPullParser.NO_NAMESPACE;
        }
        if (epg == null) {
            epg = XmlPullParser.NO_NAMESPACE;
        }
        String body = "<login user=\"" + user + "\" " + "password=" + "\"" + password + "\" " + "terminal_id=" + "\"" + terminalId + "\" " + "terminal_type=" + "\"" + terminalType + "\" " + "mac=" + "\"" + mac + "\" " + "netmode=" + "\"" + netmode + "\" " + "soft_ver=" + "\"" + soft_ver + "\" " + "hard_ver=" + "\"" + hard_ver + "\" " + "epg=" + "\"" + epg + "\" " + "token=" + "\"" + token + "\" " + "/>";
        LogcatUtils.m321d("SoapClient content " + body);
        return new StringBuilder(SOAP_HEAD).append(body).append(SOAP_TAIL).toString();
    }

    public static String getLogoutEntity(String user, String terminalId) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (terminalId == null) {
            terminalId = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<logout user=\"" + user + "\" " + "terminal_id=" + "\"" + terminalId + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getEnableEntity(String terminalId, String mac, String user, String password, String soft_ver, String hard_ver, String channel) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (password == null) {
            password = XmlPullParser.NO_NAMESPACE;
        }
        if (terminalId == null) {
            terminalId = XmlPullParser.NO_NAMESPACE;
        }
        if (mac == null) {
            mac = XmlPullParser.NO_NAMESPACE;
        }
        if (soft_ver == null) {
            soft_ver = XmlPullParser.NO_NAMESPACE;
        }
        if (hard_ver == null) {
            hard_ver = XmlPullParser.NO_NAMESPACE;
        }
        if (channel == null) {
            channel = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<enable stb_id=\"" + terminalId + "\" " + "mac=" + "\"" + mac + "\" " + "user=" + "\"" + user + "\" " + "password=" + "\"" + password + "\" " + "soft_ver=" + "\"" + soft_ver + "\" " + "hard_ver=" + "\"" + hard_ver + "\" " + "channel=" + "\"" + channel + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getRegisterEntity(String user, String password, String realname, String country, String postcode, String email, String addr, String phone, String mobile, String birthday, String channel, boolean enable) {
        String strenable = "false";
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (password == null) {
            password = XmlPullParser.NO_NAMESPACE;
        }
        if (realname == null) {
            realname = XmlPullParser.NO_NAMESPACE;
        }
        if (country == null) {
            country = XmlPullParser.NO_NAMESPACE;
        }
        if (postcode == null) {
            postcode = XmlPullParser.NO_NAMESPACE;
        }
        if (email == null) {
            email = XmlPullParser.NO_NAMESPACE;
        }
        if (addr == null) {
            addr = XmlPullParser.NO_NAMESPACE;
        }
        if (phone == null) {
            phone = XmlPullParser.NO_NAMESPACE;
        }
        if (mobile == null) {
            mobile = XmlPullParser.NO_NAMESPACE;
        }
        if (birthday == null) {
            birthday = XmlPullParser.NO_NAMESPACE;
        }
        if (channel == null) {
            channel = XmlPullParser.NO_NAMESPACE;
        }
        if (enable) {
            strenable = "true";
        }
        return new StringBuilder(SOAP_HEAD).append("<register user=\"" + user + "\" " + "password=" + "\"" + password + "\" " + "realname=" + "\"" + realname + "\" " + "country=" + "\"" + country + "\" " + "postcode=" + "\"" + postcode + "\" " + "email=" + "\"" + email + "\" " + "addr=" + "\"" + addr + "\" " + "phone=" + "\"" + phone + "\" " + "mobile=" + "\"" + mobile + "\" " + "birthday=" + "\"" + birthday + "\" " + "channel=" + "\"" + channel + "\" " + "enable=" + "\"" + strenable + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getAuthEntity(String user, String terminalId, String mediaId, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (terminalId == null) {
            terminalId = XmlPullParser.NO_NAMESPACE;
        }
        if (mediaId == null) {
            mediaId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<authen user=\"" + user + "\" " + "terminal_id=" + "\"" + terminalId + "\"" + "media_id=" + "\"" + mediaId + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getSubscribeEntity(String user, String serviceId, boolean renew, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (serviceId == null) {
            serviceId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<subscribe user=\"" + user + "\" " + "sid=" + "\"" + serviceId + "\" " + "renew=" + "\"" + (renew ? "1" : "0") + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getSubscribeByCreditCardEntity(String user, String serviceId, boolean renew, String desc, String token, String cardNumber, String creditcardType, String expdate, String safeCode, String firstName, String lastName, String street, String street2, String city, String province, String countryCode, String postCode, String birthday, String phone, String email) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (serviceId == null) {
            serviceId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<paypal user=\"" + user + "\" " + "sid=" + "\"" + serviceId + "\" " + "renew=" + "\"" + (renew ? "1" : "0") + "\" " + "token=" + "\"" + token + "\" " + "desc=" + "\"" + desc + "\" " + "ACCT=" + "\"" + cardNumber + "\" " + "CREDITCARDTYPE=" + "\"" + creditcardType + "\" " + "EXPDATE=" + "\"" + expdate + "\" " + "CVV2=" + "\"" + safeCode + "\" " + "FIRSTNAME=" + "\"" + firstName + "\" " + "LASTNAME=" + "\"" + lastName + "\" " + "STREET=" + "\"" + street + "\" " + "STREET2=" + "\"" + street2 + "\" " + "CITY=" + "\"" + city + "\" " + "STATE=" + "\"" + province + "\" " + "COUNTRYCODE=" + "\"" + countryCode + "\" " + "ZIP=" + "\"" + postCode + "\" " + "BIRTHDAY=" + "\"" + birthday + "\" " + "PHONE=" + "\"" + phone + "\" " + "EMAIL=" + "\"" + email + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getUnSubscribeEntity(String user, String serviceId, long start_utc, long end_utc, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (serviceId == null) {
            serviceId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<unsubscribe user=\"" + user + "\" " + "sid=" + "\"" + serviceId + "\" " + "start_utc" + "\"" + start_utc + "\" " + "end_utc" + "\"" + end_utc + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getRenewEntity(String user, String serviceId, long start_utc, long end_utc, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (serviceId == null) {
            serviceId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<renew user=\"" + user + "\" " + "sid=" + "\"" + serviceId + "\" " + "start_utc=" + "\"" + start_utc + "\" " + "end_utc=" + "\"" + end_utc + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getCancelRenewEntity(String user, String serviceId, long start_utc, long end_utc, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (serviceId == null) {
            serviceId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<cancelrenew user=\"" + user + "\" " + "sid=" + "\"" + serviceId + "\" " + "start_utc=" + "\"" + start_utc + "\" " + "end_utc=" + "\"" + end_utc + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getPayPalPayBuyEntity(String user, String serviceId, boolean renew, String token, String desc) {
        String desc2;
        String desc3;
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (serviceId == null) {
            serviceId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        if (desc == null) {
            desc2 = XmlPullParser.NO_NAMESPACE;
        } else {
            desc2 = desc;
        }
        try {
            desc3 = new String(new String(desc2.getBytes("gbk"), "gbk").getBytes("utf-8"), "utf-8");
        } catch (Exception e) {
            desc3 = desc2;
        }
        return new StringBuilder(SOAP_HEAD).append("<paypal user=\"" + user + "\" " + "sid=" + "\"" + serviceId + "\" " + "renew=" + "\"" + (renew ? "1" : "0") + "\" " + "desc=" + "\"" + desc3 + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getWAPTenPayBuyEntity(String user, String serviceId, boolean renew, String token, String desc) {
        String desc2;
        String desc3;
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (serviceId == null) {
            serviceId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        if (desc == null) {
            desc2 = XmlPullParser.NO_NAMESPACE;
        } else {
            desc2 = desc;
        }
        try {
            desc3 = new String(new String(desc2.getBytes("gbk"), "gbk").getBytes("utf-8"), "utf-8");
        } catch (Exception e) {
            desc3 = desc2;
        }
        return new StringBuilder(SOAP_HEAD).append("<waptenpay user=\"" + user + "\" " + "sid=" + "\"" + serviceId + "\" " + "renew=" + "\"" + (renew ? "1" : "0") + "\" " + "desc=" + "\"" + desc3 + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getPCTenPayBuyEntity(String user, String serviceId, boolean renew, String token, String desc) {
        String desc2;
        String desc3;
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (serviceId == null) {
            serviceId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        if (desc == null) {
            desc2 = XmlPullParser.NO_NAMESPACE;
        } else {
            desc2 = desc;
        }
        try {
            desc3 = new String(new String(desc2.getBytes("gbk"), "gbk").getBytes("utf-8"), "utf-8");
        } catch (Exception e) {
            desc3 = desc2;
        }
        return new StringBuilder(SOAP_HEAD).append("<pctenpay user=\"" + user + "\" " + "sid=" + "\"" + serviceId + "\" " + "renew=" + "\"" + (renew ? "1" : "0") + "\" " + "desc=" + "\"" + desc3 + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getPaypalRechargeEntity(String user, int amount, String token, String desc) {
        String desc2;
        String desc3;
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        if (amount < 0) {
            amount = 0;
        }
        if (desc == null) {
            desc2 = XmlPullParser.NO_NAMESPACE;
        } else {
            desc2 = desc;
        }
        try {
            desc3 = new String(new String(desc2.getBytes("gbk"), "gbk").getBytes("utf-8"), "utf-8");
        } catch (Exception e) {
            desc3 = desc2;
        }
        return new StringBuilder(SOAP_HEAD).append("<paypal user=\"" + user + "\" " + "amount=" + "\"" + amount + "\" " + "desc=" + "\"" + desc3 + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getWAPTenPayRechargeEntity(String user, int amount, String token, String desc) {
        String desc2;
        String desc3;
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        if (amount < 0) {
            amount = 0;
        }
        if (desc == null) {
            desc2 = XmlPullParser.NO_NAMESPACE;
        } else {
            desc2 = desc;
        }
        try {
            desc3 = new String(new String(desc2.getBytes("gbk"), "gbk").getBytes("utf-8"), "utf-8");
        } catch (Exception e) {
            desc3 = desc2;
        }
        return new StringBuilder(SOAP_HEAD).append("<waptenpay user=\"" + user + "\" " + "amount=" + "\"" + amount + "\" " + "desc=" + "\"" + desc3 + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getPCTenPayRechargeEntity(String user, int amount, String token, String desc) {
        String desc2;
        String desc3;
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        if (amount < 0) {
            amount = 0;
        }
        if (desc == null) {
            desc2 = XmlPullParser.NO_NAMESPACE;
        } else {
            desc2 = desc;
        }
        try {
            desc3 = new String(new String(desc2.getBytes("gbk"), "gbk").getBytes("utf-8"), "utf-8");
        } catch (Exception e) {
            desc3 = desc2;
        }
        return new StringBuilder(SOAP_HEAD).append("<pctenpay user=\"" + user + "\" " + "amount=" + "\"" + amount + "\" " + "desc=" + "\"" + desc3 + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getRechargeEntity(String user, int amount, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<recharge user=\"" + user + "\" " + "amount=" + "\"" + amount + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getRechargeByCreditCardEntity(String user, int amount, String desc, String token, String cardNumber, String creditcardType, String expdate, String saveCode, String firstName, String lastName, String street, String street2, String city, String province, String countryCode, String postCode, String phone, String email) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<recharge user=\"" + user + "\" " + "amount=" + "\"" + amount + "\" " + "token=" + "\"" + token + "\" " + "desc=" + "\"" + desc + "\" " + "ACCT=" + "\"" + cardNumber + "\" " + "CREDITCARDTYPE=" + "\"" + creditcardType + "\" " + "EXPDATE=" + "\"" + expdate + "\" " + "CVV2=" + "\"" + saveCode + "\" " + "FIRSTNAME=" + "\"" + firstName + "\" " + "LASTNAME=" + "\"" + lastName + "\" " + "STREET=" + "\"" + street + "\" " + "STREET2=" + "\"" + street2 + "\" " + "CITY=" + "\"" + city + "\" " + "STATE=" + "\"" + province + "\" " + "COUNTRYCODE=" + "\"" + countryCode + "\" " + "ZIP=" + "\"" + postCode + "\" " + "PHONE=" + "\"" + phone + "\" " + "EMAIL=" + "\"" + email + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getVideoCardRechargeEntity(String user, String cardId, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (cardId == null) {
            cardId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<recharge user=\"" + user + "\" " + "card_id=" + "\"" + cardId + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getVideoCardInfoEntity(String user, String cardId, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (cardId == null) {
            cardId = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<info user=\"" + user + "\" " + "card_id=" + "\"" + cardId + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getUserInfoEntity(String user, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<info user=\"" + user + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getSetUserInfoEntity(String user, String realname, String country, String email, String addr, String phone, String mobile, String postcode, String birthday, String token) {
        String body = "<modify ";
        if (user != null) {
            body = new StringBuilder(String.valueOf(body)).append("user=\"").append(user).append("\" ").toString();
        }
        if (realname != null) {
            body = new StringBuilder(String.valueOf(body)).append("realname=\"").append(realname).append("\" ").toString();
        }
        if (country != null) {
            body = new StringBuilder(String.valueOf(body)).append("country=\"").append(country).append("\" ").toString();
        }
        if (email != null) {
            body = new StringBuilder(String.valueOf(body)).append("email=\"").append(email).append("\" ").toString();
        }
        if (addr != null) {
            body = new StringBuilder(String.valueOf(body)).append("addr=\"").append(addr).append("\" ").toString();
        }
        if (phone != null) {
            body = new StringBuilder(String.valueOf(body)).append("phone=\"").append(phone).append("\" ").toString();
        }
        if (mobile != null) {
            body = new StringBuilder(String.valueOf(body)).append("mobile=\"").append(mobile).append("\" ").toString();
        }
        if (postcode != null) {
            body = new StringBuilder(String.valueOf(body)).append("postcode=\"").append(postcode).append("\" ").toString();
        }
        if (birthday != null) {
            body = new StringBuilder(String.valueOf(body)).append("birthday=\"").append(birthday).append("\" ").toString();
        }
        if (token != null) {
            body = new StringBuilder(String.valueOf(body)).append("token=\"").append(token).append("\" ").toString();
        }
        return new StringBuilder(SOAP_HEAD).append(new StringBuilder(String.valueOf(body)).append("/>").toString()).append(SOAP_TAIL).toString();
    }

    public static String getUserBindEntity(String user, String terminalId, String token) {
        String body = "<bind ";
        if (user != null) {
            body = new StringBuilder(String.valueOf(body)).append("user=\"").append(user).append("\"").toString();
        }
        if (terminalId != null) {
            body = new StringBuilder(String.valueOf(body)).append("terminal_id=\"").append(terminalId).append("\"").toString();
        }
        if (token != null) {
            body = new StringBuilder(String.valueOf(body)).append("token=\"").append(token).append("\"").toString();
        }
        return new StringBuilder(SOAP_HEAD).append(new StringBuilder(String.valueOf(body)).append(" />").toString()).append(SOAP_TAIL).toString();
    }

    public static String getBankAccountInfoEntity(String user, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<bankacount user=\"" + user + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getBankAccountBindEntity(String user, BankAccountBean bab, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        if (bab == null) {
            try {
                bab = new BankAccountBean();
            } catch (Exception e) {
                e.printStackTrace();
                return XmlPullParser.NO_NAMESPACE;
            }
        }
        return new StringBuilder(SOAP_HEAD).append("<bankacount user=\"" + user + "\" " + "token=" + "\"" + token + "\" " + "authorised=" + "\"" + bab.getAuthorised() + "\" " + "ACCT=" + "\"" + bab.getCard_number() + "\" " + "CREDITCARDTYPE=" + "\"" + bab.getCard_type() + "\" " + "EXPDATE=" + "\"" + bab.getExpiration_date() + "\" " + "CVV2=" + "\"" + bab.getSecurity_code() + "\" " + "FIRSTNAME=" + "\"" + bab.getFirst_name() + "\" " + "LASTNAME=" + "\"" + bab.getLast_name() + "\" " + "STREET=" + "\"" + URLEncoder.encode(bab.getStreet(), "UTF-8") + "\" " + "STREET2=" + "\"" + URLEncoder.encode(bab.getStreet2(), "UTF-8") + "\" " + "CITY=" + "\"" + URLEncoder.encode(bab.getCity(), "UTF-8") + "\" " + "STATE=" + "\"" + URLEncoder.encode(bab.getState(), "UTF-8") + "\" " + "COUNTRYCODE=" + "\"" + bab.getCountry() + "\" " + "ZIP=" + "\"" + bab.getPostcode() + "\" " + "COMPANY=" + "\"" + URLEncoder.encode(bab.getCompany_name(), "UTF-8") + "\" " + "BIRTHDAY=" + "\"" + bab.getBirthday() + "\" " + "PHONE=" + "\"" + bab.getPhone() + "\" " + "EMAIL=" + "\"" + bab.getEmail() + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getBankAccountUnBindEntity(String user, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        try {
            return new StringBuilder(SOAP_HEAD).append("<bankacount user=\"" + user + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    public static String getNMPParamEntity(String terminalId, String paramName, String paramValue) {
        if (terminalId == null) {
            terminalId = XmlPullParser.NO_NAMESPACE;
        }
        if (paramName == null) {
            paramName = XmlPullParser.NO_NAMESPACE;
        }
        if (paramValue == null) {
            paramValue = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<param terminal_id=\"" + terminalId + "\" " + "name=" + "\"" + paramName + "\" " + "value=" + "\"" + paramValue + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getNMPAllParamEntity(String terminalId, String allParams) {
        if (terminalId == null) {
            terminalId = XmlPullParser.NO_NAMESPACE;
        }
        if (allParams == null) {
            allParams = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<allparam terminal_id=\"" + terminalId + "\" " + "value=" + "\"" + allParams + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getChangePasswordEntity(String user, String password_old, String password_new, String token) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (password_old == null) {
            password_old = XmlPullParser.NO_NAMESPACE;
        }
        if (password_new == null) {
            password_new = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<setpassword user=\"" + user + "\" " + "password_old=" + "\"" + password_old + "\" " + "password_new=" + "\"" + password_new + "\" " + "password_confirm=" + "\"" + password_new + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getLogEntity(String body) {
        return new StringBuilder(SOAP_HEAD).append(body).append(SOAP_TAIL).toString();
    }

    public static String getSubscribeListEntity(String user, String token, int type) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuilder(SOAP_HEAD).append("<subscribelist user=\"" + user + "\" " + "token=" + "\"" + token + "\" " + "type=" + "\"" + type + "\" " + "/>").append(SOAP_TAIL).toString();
    }

    public static String getAssetListEntity(String user, String token, long start_utc, long end_utc, int pageno, int pagesize) {
        if (user == null) {
            user = XmlPullParser.NO_NAMESPACE;
        }
        if (token == null) {
            token = XmlPullParser.NO_NAMESPACE;
        }
        if (start_utc < 0) {
            start_utc = 0;
        }
        if (end_utc < 0) {
            end_utc = 0;
        }
        if (pageno < 1) {
            pageno = 1;
        }
        if (pagesize < 1) {
            pagesize = 1;
        }
        return new StringBuilder(SOAP_HEAD).append("<assetlist user=\"" + user + "\" " + "start_utc=" + "\"" + start_utc + "\" " + "end_utc=" + "\"" + end_utc + "\" " + "page_no=" + "\"" + pageno + "\" " + "page_size=" + "\"" + pagesize + "\" " + "token=" + "\"" + token + "\" " + "/>").append(SOAP_TAIL).toString();
    }
}
