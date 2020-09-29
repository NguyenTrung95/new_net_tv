package net.sunniwell.aidl.bean;

import org.xmlpull.v1.XmlPullParser;

import java.io.Serializable;

public class AssetBean implements Serializable {
    public static final int TYPE_RECHARGE = 1;
    public static final int TYPE_SUBSCRIBE = 2;
    public static final int TYPE_UNSUBSCRIBE = 3;
    private static final long serialVersionUID = 1;
    private int balances = 0;
    private int op_amount = 0;
    private int op_type = 0;
    private long op_utc = 0;
    private String service_id = XmlPullParser.NO_NAMESPACE;
    private String user_id = XmlPullParser.NO_NAMESPACE;

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id2) {
        this.user_id = user_id2;
    }

    public String getService_id() {
        return this.service_id;
    }

    public void setService_id(String service_id2) {
        this.service_id = service_id2;
    }

    public int getOp_type() {
        return this.op_type;
    }

    public void setOp_type(int op_type2) {
        this.op_type = op_type2;
    }

    public int getOp_amount() {
        return this.op_amount;
    }

    public void setOp_amount(int op_amount2) {
        this.op_amount = op_amount2;
    }

    public long getOp_utc() {
        return this.op_utc;
    }

    public void setOp_utc(long op_utc2) {
        this.op_utc = op_utc2;
    }

    public int getBalances() {
        return this.balances;
    }

    public void setBalances(int balances2) {
        this.balances = balances2;
    }
}
