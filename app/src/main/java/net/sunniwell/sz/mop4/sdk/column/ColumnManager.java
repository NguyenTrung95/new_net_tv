package net.sunniwell.sz.mop4.sdk.column;

import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import net.sunniwell.sz.mop4.sdk.param.DefaultParam;
import net.sunniwell.sz.mop4.sdk.util.Tools;

/* renamed from: net.sunniwell.sz.mop4.sdk.column.ColumnManager */
public class ColumnManager {
    private static final String TAG = "ColumnManager";
    /* access modifiers changed from: private */
    public static ArrayList<ColumnBean> mColumnList = null;
    /* access modifiers changed from: private */
    public static HashMap<Integer, ArrayList<ColumnBean>> mColumnMap = null;
    /* access modifiers changed from: private */
    public static String mCurLang = DefaultParam.language;
    /* access modifiers changed from: private */
    public static String mDirPath = null;
    /* access modifiers changed from: private */
    public static boolean mRunFlag = false;
    /* access modifiers changed from: private */
    public static boolean mSync = true;
    private static Thread mThread = new Thread(new Runnable() {
        public void run() {
            if (ColumnManager.mDirPath != null) {
                ColumnManager.mColumnMap = (HashMap) Tools.read(ColumnManager.mDirPath + ColumnManager.mCurLang + "_column.map");
            }
            if (ColumnManager.mColumnMap == null) {
                Log.i(ColumnManager.TAG, "mColumnMap == null, new one for it");
                ColumnManager.mColumnMap = new HashMap();
            }
            if (ColumnManager.mDirPath != null) {
                ColumnManager.mColumnList = (ArrayList) Tools.read(ColumnManager.mDirPath + ColumnManager.mCurLang + "_column.list");
            }
            if (ColumnManager.mColumnList == null) {
                Log.i(ColumnManager.TAG, "mColumnList == null, new one for it");
                ColumnManager.mColumnList = new ArrayList();
            }
            while (ColumnManager.mRunFlag) {
                if (ColumnManager.mSync) {
                    int flag = 0;
                    ArrayList<ColumnBean> list = ColumnDataUtil.getList(ColumnManager.mCurLang);
                    if (list != null && list.size() > 0) {
                        ColumnManager.mColumnList = list;
                        if (ColumnManager.mDirPath != null) {
                            Tools.save(list, ColumnManager.mDirPath + ColumnManager.mCurLang + "_column.list");
                        }
                        flag = 0 + 1;
                    }
                    HashMap<Integer, ArrayList<ColumnBean>> map = ColumnDataUtil.getMap(ColumnManager.mCurLang);
                    if (map != null && map.size() > 0) {
                        ColumnManager.mColumnMap = map;
                        if (ColumnManager.mDirPath != null) {
                            Tools.save(map, ColumnManager.mDirPath + ColumnManager.mCurLang + "_column.map");
                        }
                        flag++;
                    }
                    if (flag == 2) {
                        ColumnManager.mSync = false;
                        Log.i(ColumnManager.TAG, "getMap_List from server OK !!!");
                    }
                }
                ColumnManager.delay(3000);
            }
        }
    });

    public static boolean init(String path, String lang) {
        if (path != null && !path.equalsIgnoreCase(mDirPath)) {
            mDirPath = new StringBuilder(String.valueOf(path)).append("/sunniwell/column/").toString();
            mDirPath = mDirPath.replaceAll("//", "/");
            File dir = new File(mDirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        Log.d(TAG, "init() mDirPath=" + mDirPath);
        if (lang != null && !lang.equals(mCurLang)) {
            mColumnList = null;
            mColumnMap = null;
            mCurLang = lang;
        }
        mSync = true;
        if (!mRunFlag) {
            mRunFlag = true;
            mThread.start();
        }
        return true;
    }

    public static ArrayList<ColumnBean> get(int pid) {
        if (mColumnMap != null && mColumnMap.get(Integer.valueOf(pid)) != null && ((ArrayList) mColumnMap.get(Integer.valueOf(pid))).size() > 0) {
            return (ArrayList) ((ArrayList) mColumnMap.get(Integer.valueOf(pid))).clone();
        }
        Log.i(TAG, "mColumnMap == null, pid = " + pid);
        return null;
    }

    public static int getPid(int id) {
        if (mColumnList != null) {
            for (int i = 0; i < mColumnList.size(); i++) {
                if (((ColumnBean) mColumnList.get(i)).getId() == id) {
                    return ((ColumnBean) mColumnList.get(i)).getPid();
                }
            }
        }
        return -1;
    }

    public static ArrayList<ColumnBean> getAll() {
        if (mColumnList != null) {
            return (ArrayList) mColumnList.clone();
        }
        return null;
    }

    public static ColumnBean query(int pid, String title) {
        if (mColumnList != null && mColumnList.size() > 0) {
            for (int i = 0; i < mColumnList.size(); i++) {
                ColumnBean cb = (ColumnBean) mColumnList.get(i);
                if (cb.getPid() == pid && cb.getTitle() != null && cb.getTitle().equals(title)) {
                    return (ColumnBean) mColumnList.get(i);
                }
            }
        }
        return null;
    }

    public static boolean setLanguage(String lang) {
        mCurLang = lang;
        return true;
    }

    public static boolean refresh() {
        mSync = true;
        return true;
    }

    public static void cls() {
        mColumnList = null;
        mColumnMap = null;
    }

    /* access modifiers changed from: private */
    public static void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
