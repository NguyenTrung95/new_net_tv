package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ExceptionInstallBean */
public class ExceptionInstallBean extends ExceptionPlayBean {
    public ExceptionInstallBean() {
        this.mExtend = "install";
    }

    public ExceptionInstallBean(String description, String mediaId, String title, String url) {
        super(description, mediaId, title, url);
        this.mExtend = "install";
    }
}
