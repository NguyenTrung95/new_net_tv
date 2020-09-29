package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ActionInstallBean */
public class ActionInstallBean extends ActionPlayBean {
    public ActionInstallBean() {
        this.mExtend = "install";
    }

    public ActionInstallBean(String description, String mediaId, String title, String url) {
        super(description, mediaId, title, url);
        this.mExtend = "install";
    }
}
