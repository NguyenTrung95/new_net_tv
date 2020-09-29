package net.sunniwell.sz.mop4.sdk.sync;

import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.sunniwell.sz.mop4.sdk.epg.EPGManager;
import net.sunniwell.sz.mop4.sdk.util.Tools;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.sync.SyncManager */
public class SyncManager {
    private static final String TAG = "SyncManager";
    private static HashMap<String, OnColumnSyncListener> mColumnSyncListenerMap = null;
    /* access modifiers changed from: private */
    public static String mDirPath = null;
    /* access modifiers changed from: private */
    public static String mEPGSync = XmlPullParser.NO_NAMESPACE;
    /* access modifiers changed from: private */
    public static OnEPGSyncListener mEPGSyncListener = null;
    /* access modifiers changed from: private */
    public static boolean mRunFlag = false;
    /* access modifiers changed from: private */
    public static ArrayList<SyncBean> mSyncList = null;
    /* access modifiers changed from: private */
    public static Object mSyncObj = new Object();
    private static Thread mThread = new Thread(new Runnable() {
        public void run() {
            if (SyncManager.mDirPath != null) {
                SyncManager.mSyncList = (ArrayList) Tools.read(SyncManager.mDirPath + "sync.list");
                SyncManager.mEPGSync = (String) Tools.read(SyncManager.mDirPath + "epgsync");
            }
            while (SyncManager.mRunFlag) {
                String newEPGSync = EPGManager.sync();
                ArrayList<SyncBean> newSyncList = SyncDataUtil.get();
                if (newSyncList == null || newEPGSync == null) {
                    SyncManager.delay(30000);
                } else {
                    if (SyncManager.parseSyncChange(SyncManager.mSyncList, newSyncList)) {
                        ArrayList<SyncBean> oldSyncList = SyncManager.mSyncList;
                        synchronized (SyncManager.mSyncObj) {
                            SyncManager.mSyncList = newSyncList;
                        }
                        if (SyncManager.mDirPath != null) {
                            Tools.save(SyncManager.mSyncList, SyncManager.mDirPath + "sync.list");
                        }
                        SyncManager.callListener(oldSyncList, SyncManager.mSyncList);
                    }
                    if (!newEPGSync.equals(SyncManager.mEPGSync)) {
                        if (SyncManager.mEPGSyncListener != null) {
                            SyncManager.mEPGSyncListener.onEPGSyncChange(SyncManager.mEPGSync, newEPGSync);
                        }
                        SyncManager.mEPGSync = newEPGSync;
                        if (SyncManager.mDirPath != null) {
                            Tools.save(SyncManager.mEPGSync, SyncManager.mDirPath + "epgsync");
                        }
                    }
                    SyncManager.delay(180000);
                }
            }
        }
    });

    public static boolean init(String path) {
        if (path != null && !path.equalsIgnoreCase(mDirPath)) {
            mDirPath = new StringBuilder(String.valueOf(path)).append("/sunniwell/sync/").toString();
            mDirPath = mDirPath.replaceAll("//", "/");
            File dir = new File(mDirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        Log.d(TAG, "SyncManager init() mDirPath=" + mDirPath);
        if (!mRunFlag) {
            mRunFlag = true;
            mThread.start();
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int get(int r5) {
        /*
            java.lang.Object r2 = mSyncObj
            monitor-enter(r2)
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.sync.SyncBean> r1 = mSyncList     // Catch:{ all -> 0x004a }
            if (r1 == 0) goto L_0x0013
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.sync.SyncBean> r1 = mSyncList     // Catch:{ all -> 0x004a }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x004a }
        L_0x000d:
            boolean r3 = r1.hasNext()     // Catch:{ all -> 0x004a }
            if (r3 != 0) goto L_0x0016
        L_0x0013:
            monitor-exit(r2)     // Catch:{ all -> 0x004a }
            r1 = 0
        L_0x0015:
            return r1
        L_0x0016:
            java.lang.Object r0 = r1.next()     // Catch:{ all -> 0x004a }
            net.sunniwell.sz.mop4.sdk.sync.SyncBean r0 = (net.sunniwell.p008sz.mop4.sdk.sync.SyncBean) r0     // Catch:{ all -> 0x004a }
            int r3 = r0.getId()     // Catch:{ all -> 0x004a }
            if (r3 != r5) goto L_0x000d
            java.lang.String r1 = "SyncManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x004a }
            java.lang.String r4 = "SyncManager get() columnId="
            r3.<init>(r4)     // Catch:{ all -> 0x004a }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x004a }
            java.lang.String r4 = " sb.getSync()="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x004a }
            int r4 = r0.getSync()     // Catch:{ all -> 0x004a }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x004a }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x004a }
            android.util.Log.d(r1, r3)     // Catch:{ all -> 0x004a }
            int r1 = r0.getSync()     // Catch:{ all -> 0x004a }
            monitor-exit(r2)     // Catch:{ all -> 0x004a }
            goto L_0x0015
        L_0x004a:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x004a }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.sync.SyncManager.get(int):int");
    }

    public static ArrayList<SyncBean> getList() {
        synchronized (mSyncObj) {
            if (mSyncList == null) {
                return null;
            }
            ArrayList<SyncBean> arrayList = (ArrayList) mSyncList.clone();
            return arrayList;
        }
    }

    public static boolean addOnColumnSyncListener(String handle, OnColumnSyncListener listener) {
        if (handle == null || handle.equals(XmlPullParser.NO_NAMESPACE) || listener == null) {
            return false;
        }
        synchronized (mSyncObj) {
            if (mColumnSyncListenerMap == null) {
                mColumnSyncListenerMap = new HashMap<>();
            }
            if (mColumnSyncListenerMap != null) {
                mColumnSyncListenerMap.put(handle, listener);
            }
        }
        return true;
    }

    public static void removeOnColumnSyncListener(String handle) {
        synchronized (mSyncObj) {
            if (mColumnSyncListenerMap != null) {
                mColumnSyncListenerMap.remove(handle);
            }
        }
    }

    public static void setOnEPGSyncListener(OnEPGSyncListener listener) {
        mEPGSyncListener = listener;
    }

    /* access modifiers changed from: private */
    public static boolean parseSyncChange(ArrayList<SyncBean> oldSyncList, ArrayList<SyncBean> newSyncList) {
        if (newSyncList == null) {
            return false;
        }
        if (oldSyncList != null && oldSyncList.equals(newSyncList)) {
            return false;
        }
        Iterator it = newSyncList.iterator();
        while (it.hasNext()) {
            SyncBean newsb = (SyncBean) it.next();
            int newSync = newsb.getSync();
            int oldSync = getSync(oldSyncList, newsb.getId());
            if (newSync != 0 && newSync != oldSync) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static void callListener(ArrayList<SyncBean> oldSyncList, ArrayList<SyncBean> newSyncList) {
        if (newSyncList != null) {
            if (oldSyncList == null || !oldSyncList.equals(newSyncList)) {
                Iterator it = newSyncList.iterator();
                while (it.hasNext()) {
                    SyncBean newsb = (SyncBean) it.next();
                    int newSync = newsb.getSync();
                    int oldSync = getSync(oldSyncList, newsb.getId());
                    if (!(newSync == 0 || newSync == oldSync)) {
                        Log.i(TAG, "onColumnSyncChange, columnid = " + newsb.getId() + ", oldSync = " + oldSync + ", newSync = " + newSync);
                        ArrayList<String> keySet = new ArrayList<>();
                        synchronized (mSyncObj) {
                            if (mColumnSyncListenerMap != null && mColumnSyncListenerMap.size() > 0) {
                                for (String key : mColumnSyncListenerMap.keySet()) {
                                    keySet.add(key);
                                }
                            }
                        }
                        if (keySet.size() > 0) {
                            Iterator it2 = keySet.iterator();
                            while (it2.hasNext()) {
                                OnColumnSyncListener listener = (OnColumnSyncListener) mColumnSyncListenerMap.get((String) it2.next());
                                if (listener != null) {
                                    listener.onColumnSyncChange(newsb.getId(), oldSync, newSync);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static int getSync(ArrayList<SyncBean> syncList, int columnId) {
        if (syncList != null && syncList.size() > 0) {
            Iterator it = syncList.iterator();
            while (it.hasNext()) {
                SyncBean sb = (SyncBean) it.next();
                if (sb.getId() == columnId) {
                    return sb.getSync();
                }
            }
        }
        return 0;
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
