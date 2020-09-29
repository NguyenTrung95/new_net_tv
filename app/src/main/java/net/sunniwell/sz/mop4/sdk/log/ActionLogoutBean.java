package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ActionLogoutBean */
public class ActionLogoutBean extends ActionLoginBean {
    public ActionLogoutBean() {
        this.mExtend = LogBean1.TERMINAL_ACTION_LOGOUT;
    }

    public ActionLogoutBean(String description) {
        super(description);
        this.mExtend = LogBean1.TERMINAL_ACTION_LOGOUT;
    }
}
