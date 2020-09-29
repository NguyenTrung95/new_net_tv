package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ActionStopBean */
public class ActionStopBean extends ActionPlayBean {
    public ActionStopBean() {
        this.mExtend = LogBean1.TERMINAL_ACTION_STOP;
    }

    public ActionStopBean(String description, String mediaId, String title, String url) {
        super(description, mediaId, title, url);
        this.mExtend = LogBean1.TERMINAL_ACTION_STOP;
    }
}
