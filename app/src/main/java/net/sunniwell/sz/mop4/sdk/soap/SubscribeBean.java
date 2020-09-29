package net.sunniwell.sz.mop4.sdk.soap;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.SubscribeBean */
public class SubscribeBean implements Serializable {
    private static final long serialVersionUID = 1;
    private long end_utcMs = 0;
    private int errno = 0;
    private int price = 0;
    private int renew = 0;
    private int renew_state = 0;
    private String service_id = XmlPullParser.NO_NAMESPACE;
    private long start_utcMs = 0;
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

    public long getStart_utcMs() {
        return this.start_utcMs;
    }

    public void setStart_utcSecond(long start_utcSecond) {
        this.start_utcMs = 1000 * start_utcSecond;
    }

    public long getEnd_utcMs() {
        return this.end_utcMs;
    }

    public void setEnd_utcSecond(long end_utcSecond) {
        this.end_utcMs = 1000 * end_utcSecond;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price2) {
        this.price = price2;
    }

    public int getRenew() {
        return this.renew;
    }

    public void setRenew(int renew2) {
        this.renew = renew2;
    }

    public int getRenew_state() {
        return this.renew_state;
    }

    public void setRenew_state(int renew_state2) {
        this.renew_state = renew_state2;
    }

    public int getErrno() {
        return this.errno;
    }

    public void setErrno(int errno2) {
        this.errno = errno2;
    }
}
