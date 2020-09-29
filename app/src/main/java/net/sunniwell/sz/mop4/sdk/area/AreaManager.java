package net.sunniwell.sz.mop4.sdk.area;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;

/* renamed from: net.sunniwell.sz.mop4.sdk.area.AreaManager */
public class AreaManager {
    private static final String TAG = "AreaManager";
    private static HashMap<String, ArrayList<AreaBean>> map = new HashMap<>();

    public static ArrayList<AreaBean> get(String lang) {
        Log.d(TAG, "Get area, key =" + ("area_" + SoapClient.getEpgs().split(":")[0] + "_" + lang));
        if (map.get(lang) == null || ((ArrayList) map.get(lang)).size() <= 0) {
            ArrayList<AreaBean> list = AreaDataUtil.get(lang);
            if (list == null || list.size() <= 0) {
                return null;
            }
            map.put(lang, list);
            Log.d(TAG, "Get area, get from server success!!!");
            return (ArrayList) list.clone();
        }
        ArrayList<AreaBean> list2 = (ArrayList) map.get(lang);
        Log.d(TAG, "Get area, hit on memory cache!!!");
        return (ArrayList) list2.clone();
    }
}
