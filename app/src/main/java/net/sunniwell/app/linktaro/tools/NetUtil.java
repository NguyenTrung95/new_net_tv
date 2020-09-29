package net.sunniwell.app.linktaro.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.xmlpull.v1.XmlPullParser;

public class NetUtil {
    private static final String TAG = "NetWorkManager";

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w(TAG, "无法获取ConnectivityManager");
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                int i = 0;
                while (i < info.length) {
                    if (!info[i].isAvailable() || !info[i].isConnected()) {
                        i++;
                    } else {
                        Log.d(TAG, "network is available");
                        return true;
                    }
                }
            }
        }
        Log.d(TAG, "network is not available");
        return false;
    }

    public static boolean isEthernetAvailable(Context context) throws Exception {
        if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(9).isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean isWifiAvailable(Context context) throws Exception {
        if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(1).isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static String getIpAddress() throws SocketException {
        String str = XmlPullParser.NO_NAMESPACE;
        String ipAddress = XmlPullParser.NO_NAMESPACE;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) netInterfaces.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String tempAddress = inetAddress.getHostAddress().toString();
                            if (!tempAddress.contains("::")) {
                                ipAddress = tempAddress;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }
}
