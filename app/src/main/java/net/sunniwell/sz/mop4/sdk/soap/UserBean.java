package net.sunniwell.sz.mop4.sdk.soap;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.UserBean */
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1;
    private String addr = XmlPullParser.NO_NAMESPACE;
    private int allowst = -1;
    private int balances = 0;
    private String birthday = XmlPullParser.NO_NAMESPACE;
    private String country = XmlPullParser.NO_NAMESPACE;
    private String email = XmlPullParser.NO_NAMESPACE;
    private long enableUtcMs = 0;

    /* renamed from: id */
    private String f342id = XmlPullParser.NO_NAMESPACE;
    private String mobile = XmlPullParser.NO_NAMESPACE;
    private String password = XmlPullParser.NO_NAMESPACE;
    private String phone = XmlPullParser.NO_NAMESPACE;
    private String postcode = XmlPullParser.NO_NAMESPACE;
    private String realname = XmlPullParser.NO_NAMESPACE;
    private int termcnt = 0;
    private long validtoUtcMs = 0;

    public String getId() {
        return this.f342id;
    }

    public void setId(String id) {
        this.f342id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getRealname() {
        return this.realname;
    }

    public void setRealname(String realname2) {
        this.realname = realname2;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday2) {
        this.birthday = birthday2;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country2) {
        this.country = country2;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode2) {
        this.postcode = postcode2;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String addr2) {
        this.addr = addr2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public int getTermcnt() {
        return this.termcnt;
    }

    public void setTermcnt(int termcnt2) {
        this.termcnt = termcnt2;
    }

    public int getBalances() {
        return this.balances;
    }

    public void setBalances(int balances2) {
        this.balances = balances2;
    }

    public int getAllowst() {
        return this.allowst;
    }

    public void setAllowst(int allowst2) {
        this.allowst = allowst2;
    }

    public long getEnableUtcMs() {
        return this.enableUtcMs;
    }

    public void setEnableUtcSecond(long enableUtcSecond) {
        this.enableUtcMs = 1000 * enableUtcSecond;
    }

    public long getValidtoUtcMs() {
        return this.validtoUtcMs;
    }

    public void setValidtoUtcSecond(long validtoUtcSecond) {
        this.validtoUtcMs = 1000 * validtoUtcSecond;
    }
}
