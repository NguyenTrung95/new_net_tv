package net.sunniwell.sz.mop4.sdk.tag;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;

/* renamed from: net.sunniwell.sz.mop4.sdk.tag.TagManager */
public class TagManager {
    private static final String TAG = "TagManager";
    private static HashMap<String, ArrayList<TagBean>> map = new HashMap<>();

    public static ArrayList<TagBean> get(String lang) {
        Log.d(TAG, "TagManager Get tag, key =" + ("tag_" + SoapClient.getEpgs().split(":")[0] + "_" + lang));
        if (map.get(lang) == null || ((ArrayList) map.get(lang)).size() <= 0) {
            ArrayList<TagBean> list = TagDataUtil.get(lang);
            if (list == null || list.size() <= 0) {
                return null;
            }
            map.put(lang, list);
            Log.d(TAG, "Get tag, get from server success!!!");
            return (ArrayList) list.clone();
        }
        ArrayList<TagBean> list2 = (ArrayList) map.get(lang);
        Log.d(TAG, "TagManager Get tag, hit on memory cache!!!");
        return (ArrayList) list2.clone();
    }
}
