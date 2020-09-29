package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ActionRebootBean */
public class ActionRebootBean extends ActionLoginBean {
    public ActionRebootBean() {
        this.mExtend = LogBean1.TERMINAL_ACTION_REBOOT;
    }

    public ActionRebootBean(String description) {
        super(description);
        this.mExtend = LogBean1.TERMINAL_ACTION_REBOOT;
    }
}
