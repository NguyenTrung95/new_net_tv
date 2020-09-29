package net.sunniwell.sz.mop4.sdk.soap;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapListener */
public interface SoapListener {
    void onAlertmsg(String str, String str2);

    void onAssignUser(String str, String str2);

    void onDelParam(String str);

    void onEpgsChange();

    void onGetAllParam();

    void onGetParam(String str);

    void onLoginFailed(int i);

    void onLoginSuccess();

    void onMarquee(String str, String str2, int i, String str3, int i2, int i3, int i4);

    void onMediaPlay(String str);

    void onMediaStop();

    void onMessage(String str, String str2);

    void onOpenUrl(String str);

    void onRestart();

    void onSaveParam();

    void onSetParam(String str, String str2);

    void onShutDown();

    void onStandby();

    void onUpgrade(String str);

    void onWakeup();
}
