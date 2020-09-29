package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ActionPauseBean */
public class ActionPauseBean extends ActionPlayBean {
    public ActionPauseBean() {
        this.mExtend = LogBean1.TERMINAL_ACTION_PAUSE;
    }

    public ActionPauseBean(String description, String mediaId, String title, String url) {
        super(description, mediaId, title, url);
        this.mExtend = LogBean1.TERMINAL_ACTION_PAUSE;
    }
}
