package net.sunniwell.app.linktaro.nettv.bean;

public class UserDetail {
    private String account;
    private String address;
    private String email;
    private String ifDownload;
    private String ifRecord;
    private String password;
    private String phone;
    private String showname;
    private String status;
    private String userGrant;
    private String userPassExpressly;
    private String userSex;

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public String getUserPassExpressly() {
        return this.userPassExpressly;
    }

    public void setUserPassExpressly(String userPassExpressly2) {
        this.userPassExpressly = userPassExpressly2;
    }

    public String getIfDownload() {
        return this.ifDownload;
    }

    public void setIfDownload(String ifDownload2) {
        this.ifDownload = ifDownload2;
    }

    public String getShowname() {
        return this.showname;
    }

    public void setShowname(String showname2) {
        this.showname = showname2;
    }

    public String getUserSex() {
        return this.userSex;
    }

    public void setUserSex(String userSex2) {
        this.userSex = userSex2;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account2) {
        this.account = account2;
    }

    public String getUserGrant() {
        return this.userGrant;
    }

    public void setUserGrant(String userGrant2) {
        this.userGrant = userGrant2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getIfRecord() {
        return this.ifRecord;
    }

    public void setIfRecord(String ifRecord2) {
        this.ifRecord = ifRecord2;
    }

    public String toString() {
        return "UserDetail [phone=" + this.phone + ", status=" + this.status + ", email=" + this.email + ", address=" + this.address + ", userPassExpressly=" + this.userPassExpressly + ", ifDownload=" + this.ifDownload + ", showname=" + this.showname + ", userSex=" + this.userSex + ", account=" + this.account + ", userGrant=" + this.userGrant + ", password=" + this.password + ", ifRecord=" + this.ifRecord + "]";
    }
}
