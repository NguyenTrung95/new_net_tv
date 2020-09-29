package net.sunniwell.sz.mop4.sdk.category;

import android.util.Log;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import net.sunniwell.sz.mop4.sdk.param.DefaultParam;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import net.sunniwell.sz.mop4.sdk.sync.OnColumnSyncListener;
import net.sunniwell.sz.mop4.sdk.sync.SyncManager;
import net.sunniwell.sz.mop4.sdk.util.Tools;

/* renamed from: net.sunniwell.sz.mop4.sdk.category.CategoryManager */
public class CategoryManager {
    private static final String TAG = "CategoryManager";
    /* access modifiers changed from: private */
    public static HashMap<String, ArrayList<CategoryBean>> mCategoryMap = null;
    /* access modifiers changed from: private */
    public static String mCurLang = DefaultParam.language;
    /* access modifiers changed from: private */
    public static String mDirPath = null;
    private static OnColumnSyncListener onSyncChangeListener = new OnColumnSyncListener() {
        public void onColumnSyncChange(int columnId, int oldSync, int newSync) {
            Log.d(CategoryManager.TAG, "onSyncChange columnId=" + columnId + " oldSync=" + oldSync + " newSync=" + newSync);
            ArrayList<CategoryBean> cblist = CategoryDataUtil.get(columnId, CategoryManager.mCurLang);
            if (CategoryManager.mCategoryMap != null && cblist != null) {
                CategoryManager.mCategoryMap.remove(new StringBuilder(String.valueOf(columnId)).append("_").append(oldSync).append("_").append(CategoryManager.mCurLang).toString());
                CategoryManager.mCategoryMap.put(new StringBuilder(String.valueOf(columnId)).append("_").append(newSync).append("_").append(CategoryManager.mCurLang).toString(), cblist);
                String key = new StringBuilder(String.valueOf(SoapClient.getEpgs().split(":")[0])).append("_").append(columnId).append("_").append(newSync).append("_").append(CategoryManager.mCurLang).toString();
                if (CategoryManager.mDirPath != null) {
                    final String prefix = new StringBuilder(String.valueOf(SoapClient.getEpgs().split(":")[0])).append("_").append(columnId).append("_").toString();
                    try {
                        for (File f : new File(CategoryManager.mDirPath).listFiles(new FileFilter() {
                            public boolean accept(File pathname) {
                                if (pathname.getName().startsWith(prefix)) {
                                    return true;
                                }
                                return false;
                            }
                        })) {
                            f.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Tools.save(cblist, CategoryManager.mDirPath + key);
                }
            }
        }
    };

    public static boolean init(String path, String lang) {
        if (path != null && !path.equalsIgnoreCase(mDirPath)) {
            mDirPath = new StringBuilder(String.valueOf(path)).append("/sunniwell/category/").toString();
            mDirPath = mDirPath.replaceAll("//", "/");
            File dir = new File(mDirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        Log.d(TAG, "init() mDirPath=" + mDirPath);
        if (lang != null && !lang.equals(mCurLang)) {
            mCurLang = lang;
        }
        if (mCategoryMap == null) {
            mCategoryMap = new HashMap<>();
            SyncManager.addOnColumnSyncListener(CategoryManager.class.getName(), onSyncChangeListener);
        }
        return true;
    }

    public static void setLanguage(String lang) {
        mCurLang = lang;
    }

    public static ArrayList<CategoryBean> get(int columnId) {
        int curSync = SyncManager.get(columnId);
        String key = new StringBuilder(String.valueOf(SoapClient.getEpgs().split(":")[0])).append("_").append(columnId).append("_").append(curSync).append("_").append(mCurLang).toString();
        Log.d(TAG, "get() columnId=" + columnId + " curSync=" + curSync + " mCurLang=" + mCurLang);
        if (mCategoryMap != null && mCategoryMap.size() > 0) {
            ArrayList<CategoryBean> retList = (ArrayList) mCategoryMap.get(new StringBuilder(String.valueOf(columnId)).append("_").append(curSync).append("_").append(mCurLang).toString());
            if (retList != null && retList.size() > 0) {
                Log.d(TAG, "Get Category, hit on memory !!! retList =" + retList);
                return (ArrayList) retList.clone();
            }
        }
        if (mDirPath != null) {
            File file = new File(mDirPath + key);
            if (file.exists() && file.isFile()) {
                ArrayList<CategoryBean> retList2 = (ArrayList) Tools.read(mDirPath + key);
                if (retList2 != null && retList2.size() > 0) {
                    if (mCategoryMap != null) {
                        mCategoryMap.put(new StringBuilder(String.valueOf(columnId)).append("_").append(curSync).append("_").append(mCurLang).toString(), retList2);
                    }
                    Log.d(TAG, "Get Category, hit on flash !!! retList =" + retList2);
                    return (ArrayList) retList2.clone();
                }
            }
        }
        ArrayList<CategoryBean> retList3 = CategoryDataUtil.get(columnId, mCurLang);
        if (retList3 == null) {
            return null;
        }
        if (mCategoryMap != null) {
            mCategoryMap.put(new StringBuilder(String.valueOf(columnId)).append("_").append(curSync).append("_").append(mCurLang).toString(), retList3);
        }
        if (mDirPath != null) {
            Tools.save(retList3, mDirPath + key);
        }
        Log.d(TAG, "CategoryManager Get Category, from server!!! retList =" + retList3);
        return (ArrayList) retList3.clone();
    }
}
