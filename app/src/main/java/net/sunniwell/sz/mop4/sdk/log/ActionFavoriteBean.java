package net.sunniwell.sz.mop4.sdk.log;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.ActionFavoriteBean */
public class ActionFavoriteBean extends ActionPlayBean {
    public ActionFavoriteBean() {
        this.mExtend = LogBean1.TERMINAL_ACTION_FAVORITE;
    }

    public ActionFavoriteBean(String description, String mediaId, String title, String url) {
        super(description, mediaId, title, url);
        this.mExtend = LogBean1.TERMINAL_ACTION_FAVORITE;
    }
}
