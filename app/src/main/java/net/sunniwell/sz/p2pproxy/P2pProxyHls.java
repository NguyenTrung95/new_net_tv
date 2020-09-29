package net.sunniwell.sz.p2pproxy;

public class P2pProxyHls {
    public native boolean mopProxyAddOisServer(String str, int i);

    public native boolean mopProxyDestroy();

    public native int mopProxyGetListenPort();

    public native boolean mopProxyInit(String str, String str2, String str3, String str4, int i);

    public native boolean mopProxyP2pStop();

    public native boolean mopProxySetLessDelay(boolean z);

    public native boolean mopProxySetOisServer(String str, int i);

    public native boolean mopProxySetPrivateProtocol(boolean z);

    public native boolean mopProxySetUser(String str, String str2, String str3);

    public native boolean mopProxyStart();

    public native boolean mopProxySystemExit();

    public native boolean mopProxySystemInit();

    static {
        System.loadLibrary("p2pproxyhls-jni");
    }
}
