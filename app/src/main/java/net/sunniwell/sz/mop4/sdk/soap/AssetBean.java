package net.sunniwell.sz.mop4.sdk.soap;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.AssetBean */
public class AssetBean implements Serializable {
    public static final int TYPE_RECHARGE = 1;
    public static final int TYPE_SUBSCRIBE = 2;
    public static final int TYPE_UNSUBSCRIBE = 3;
    private static final long serialVersionUID = 1;
    private int balances = 0;
    private int op_amount = 0;
    private int op_type = 0;
    private long op_utcMs = 0;
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

    public long getOp_utcUtcMs() {
        return this.op_utcMs;
    }

    public void setOp_utcSecond(long op_utcSecond) {
        this.op_utcMs = 1000 * op_utcSecond;
    }

    public int getBalances() {
        return this.balances;
    }

    public void setBalances(int balances2) {
        this.balances = balances2;
    }
}
