package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ActionLoginBean */
public class ActionLoginBean extends ActionBean {
    private long mUtcMs;

    ActionLoginBean() {
        this.mUtcMs = 0;
        this.mExtend = LogBean1.TERMINAL_ACTION_LOGIN;
        this.mUtcMs = System.currentTimeMillis();
    }

    ActionLoginBean(String description) {
        super(description);
        this.mUtcMs = 0;
        this.mExtend = LogBean1.TERMINAL_ACTION_LOGIN;
        this.mUtcMs = System.currentTimeMillis();
    }

    public String toString() {
        this.mContent = String.format("{\"terminal_id\"=\"%s\",\"user_id\"=\"%s\",\"description\"=\"%s\",\"time\"=\"%s\"}", new Object[]{this.mTerminalId, this.mUserId, this.mDescription, Long.valueOf(this.mUtcMs)});
        return super.toString();
    }
}
