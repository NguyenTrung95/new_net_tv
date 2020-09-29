package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ActionStandbyBean */
public class ActionStandbyBean extends ActionLoginBean {
    public ActionStandbyBean() {
        this.mExtend = LogBean1.TERMINAL_ACTION_STANDBY;
    }

    public ActionStandbyBean(String description) {
        super(description);
        this.mExtend = LogBean1.TERMINAL_ACTION_STANDBY;
    }
}
