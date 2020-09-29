package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ExceptionFileBean */
public class ExceptionFileBean extends ExceptionBean {
    public ExceptionFileBean() {
        this.mExtend = LogBean1.TERMINAL_EXCEPTION_FILE;
    }

    public ExceptionFileBean(String description) {
        super(description);
        this.mExtend = LogBean1.TERMINAL_EXCEPTION_FILE;
    }

    public String toString() {
        this.mContent = String.format("{\"terminal_id\"=\"%s\",\"user_id\"=\"%s\",\"description\"=\"%s\"}", new Object[]{this.mTerminalId, this.mUserId, this.mDescription});
        return super.toString();
    }
}
