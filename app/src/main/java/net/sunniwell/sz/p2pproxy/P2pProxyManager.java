package net.sunniwell.sz.p2pproxy;

import android.content.Context;
import android.os.SystemProperties;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.common.log.SWLogger;

public class P2pProxyManager {
    private static final SWLogger LOG = SWLogger.getLogger(P2pProxyManager.class);
    private P2pProxyHls mProxy;

    public P2pSystemPrProxyManager(Context context) {
        this.mProxy = null;
        this.mProxy = new P2pProxyHls();
    }

    public void initP2pProxy() {
        String ois;
        LOG.mo8825d("[initP2pProxy]");
        this.mProxy.mopProxySystemInit();
        String user = SWSysProp.getStringParam("user_name", "sunniwell");
        String ois2 = SWSysProp.getStringParam("ois", "ois01.jhometv.net:5001");
        String tid = SystemProperties.get("ro.serialno");
        if (ois2 == null || ois2.length() <= 0) {
            ois = "ois01.jhometv.net";
        } else {
            ois = ois2.split("\\:")[0];
        }
        LOG.mo8825d("[user]" + user);
        LOG.mo8825d("[ois]" + ois);
        this.mProxy.mopProxyInit(tid, user, "1", "127.0.0.1", 5000);
        this.mProxy.mopProxyAddOisServer(ois, 5000);
        this.mProxy.mopProxyStart();
    }

    public void exitP2pProxy() {
        LOG.mo8825d("[exitP2pProxy]");
        this.mProxy.mopProxyP2pStop();
        this.mProxy.mopProxyDestroy();
        this.mProxy.mopProxySystemExit();
    }
}
