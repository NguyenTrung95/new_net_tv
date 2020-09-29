package net.sunniwell.sz.mop4.sdk.soap;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.VideoCardBean */
public class VideoCardBean implements Serializable {
    private static final long serialVersionUID = 1;
    private int amount = 0;
    private String card_id = XmlPullParser.NO_NAMESPACE;
    private int used = 0;

    public String getCard_id() {
        return this.card_id;
    }

    public void setCard_id(String card_id2) {
        this.card_id = card_id2;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount2) {
        this.amount = amount2;
    }

    public int getUsed() {
        return this.used;
    }

    public void setUsed(int used2) {
        this.used = used2;
    }
}
