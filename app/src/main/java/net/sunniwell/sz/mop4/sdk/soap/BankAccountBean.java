package net.sunniwell.sz.mop4.sdk.soap;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.BankAccountBean */
public class BankAccountBean implements Serializable {
    private static final long serialVersionUID = 4572730199607703039L;
    private int authorised = 0;
    private String birthday = XmlPullParser.NO_NAMESPACE;
    private String card_number = XmlPullParser.NO_NAMESPACE;
    private String card_type = XmlPullParser.NO_NAMESPACE;
    private String city = XmlPullParser.NO_NAMESPACE;
    private String company_name = XmlPullParser.NO_NAMESPACE;
    private String country = XmlPullParser.NO_NAMESPACE;
    private String email = XmlPullParser.NO_NAMESPACE;
    private String expiration_date = XmlPullParser.NO_NAMESPACE;
    private String first_name = XmlPullParser.NO_NAMESPACE;
    private String last_name = XmlPullParser.NO_NAMESPACE;
    private String phone = XmlPullParser.NO_NAMESPACE;
    private String postcode = XmlPullParser.NO_NAMESPACE;
    private String security_code = XmlPullParser.NO_NAMESPACE;
    private String state = XmlPullParser.NO_NAMESPACE;
    private int status = 0;
    private String street = XmlPullParser.NO_NAMESPACE;
    private String street2 = XmlPullParser.NO_NAMESPACE;
    private String user_id = XmlPullParser.NO_NAMESPACE;

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id2) {
        this.user_id = user_id2;
    }

    public int getAuthorised() {
        return this.authorised;
    }

    public void setAuthorised(int authorised2) {
        this.authorised = authorised2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public String getCard_number() {
        return this.card_number;
    }

    public void setCard_number(String card_number2) {
        this.card_number = card_number2;
    }

    public String getCard_type() {
        return this.card_type;
    }

    public void setCard_type(String card_type2) {
        this.card_type = card_type2;
    }

    public String getExpiration_date() {
        return this.expiration_date;
    }

    public void setExpiration_date(String expiration_date2) {
        this.expiration_date = expiration_date2;
    }

    public String getSecurity_code() {
        return this.security_code;
    }

    public void setSecurity_code(String security_code2) {
        this.security_code = security_code2;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name2) {
        this.first_name = first_name2;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name2) {
        this.last_name = last_name2;
    }

    public String getCompany_name() {
        return this.company_name;
    }

    public void setCompany_name(String company_name2) {
        this.company_name = company_name2;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street3) {
        this.street = street3;
    }

    public String getStreet2() {
        return this.street2;
    }

    public void setStreet2(String street22) {
        this.street2 = street22;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state2) {
        this.state = state2;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode2) {
        this.postcode = postcode2;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country2) {
        this.country = country2;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday2) {
        this.birthday = birthday2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public BankAccountBean clone() {
        BankAccountBean bean = new BankAccountBean();
        bean.setAuthorised(this.authorised);
        bean.setBirthday(this.birthday);
        bean.setCard_number(this.card_number);
        bean.setCard_type(this.card_type);
        bean.setCity(this.city);
        bean.setCompany_name(this.company_name);
        bean.setCountry(this.country);
        bean.setEmail(this.email);
        bean.setExpiration_date(this.expiration_date);
        bean.setFirst_name(this.first_name);
        bean.setLast_name(this.last_name);
        bean.setPhone(this.phone);
        bean.setPostcode(this.postcode);
        bean.setSecurity_code(this.security_code);
        bean.setState(this.state);
        bean.setStreet(this.street);
        bean.setStreet2(this.street2);
        bean.setUser_id(this.user_id);
        return bean;
    }
}
