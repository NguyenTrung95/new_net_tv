package net.sunniwell.sz.mop4.sdk.media;

import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sunniwell.common.tools.DateTime;
import net.sunniwell.sz.mop4.sdk.soap.SoapClient;
import net.sunniwell.sz.mop4.sdk.sync.SyncManager;
import net.sunniwell.sz.mop4.sdk.util.Tools;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.media.MediaManager */
public class MediaManager {
    public static final String SORT_BY_CHNLNUM = "chnlnum";
    public static final String SORT_BY_POPULAR = "popular";
    public static final String SORT_BY_SCORE = "score";
    public static final String SORT_BY_TAG = "tag";
    public static final String SORT_BY_TAGS = "tags";
    public static final String SORT_BY_TIME = "time";
    public static final String SORT_DEFAULT = "sort";
    private static final String TAG = "MediaManager";
    private static ArrayList<MediaCacheBean> mCacheList = null;
    /* access modifiers changed from: private */
    public static String mDirPath = null;
    /* access modifiers changed from: private */
    public static boolean mRunFlag = false;
    private static Thread mThread = new Thread(new Runnable() {
        public void run() {
            while (MediaManager.mRunFlag) {
                if (MediaManager.mDirPath != null) {
                    File dir = new File(MediaManager.mDirPath);
                    if (dir.exists() && dir.isDirectory()) {
                        File[] files = dir.listFiles();
                        if (files == null) {
                            MediaManager.delay(10000);
                        } else {
                            for (File file : files) {
                                String[] items = file.getName().split("_");
                                if (items != null && items.length > 2 && items[0] != null && !items[0].equals(XmlPullParser.NO_NAMESPACE) && items[1] != null && !items[1].equals(XmlPullParser.NO_NAMESPACE) && items[2] != null && !items[2].equals(XmlPullParser.NO_NAMESPACE)) {
                                    String epgs = items[0];
                                    String epg = SoapClient.getEpgs();
                                    if (epg != null) {
                                        if (epgs.equals(epg.split(":")[0])) {
                                            try {
                                                int columnId = Integer.parseInt(items[1]);
                                                int sync = Integer.parseInt(items[2]);
                                                int curSync = SyncManager.get(columnId);
                                                if (!(curSync == 0 || sync == curSync)) {
                                                    file.delete();
                                                    Log.i(MediaManager.TAG, "Remove cache file = " + file.getName());
                                                }
                                            } catch (Exception ee) {
                                                file.delete();
                                                ee.printStackTrace();
                                            }
                                        } else {
                                            file.delete();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                MediaManager.delay(DateTime.MILLIS_PER_HOUR);
            }
        }
    });

    /* renamed from: net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean */
    private static class MediaCacheBean {
        public String key;
        public MediaListBean mlb;

        private MediaCacheBean() {
        }

        /* synthetic */ MediaCacheBean(MediaCacheBean mediaCacheBean) {
            this();
        }
    }

    public static boolean init(String path) {
        if (path != null && !path.equalsIgnoreCase(mDirPath)) {
            mDirPath = new StringBuilder(String.valueOf(path)).append("/sunniwell/media/").toString();
            mDirPath = mDirPath.replaceAll("//", "/");
            File dir = new File(mDirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        Log.d(TAG, "MediaManager init() mDirPath=" + mDirPath);
        if (!mRunFlag) {
            mRunFlag = true;
            mCacheList = new ArrayList<>();
            mThread.start();
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x013f  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0164  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0189  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x01ae  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x01d5  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0325  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0366  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x03bd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static net.sunniwell.sz.mop4.sdk.media.MediaListBean get(int r10, java.lang.String r11, java.lang.String r12, java.lang.String r13, java.lang.String r14, java.lang.String r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, java.lang.String r19, java.lang.String r20, int r21, int r22, java.lang.String r23) {
        /*
            int r1 = net.sunniwell.p008sz.mop4.sdk.sync.SyncManager.get(r10)
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.getEpgs()
            java.lang.String r9 = ":"
            java.lang.String[] r8 = r8.split(r9)
            r9 = 0
            r8 = r8[r9]
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r10)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r1)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            r0 = r21
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            r0 = r22
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r4 = r7.toString()
            boolean r7 = android.text.TextUtils.isEmpty(r11)
            if (r7 != 0) goto L_0x023d
            java.lang.String r7 = "-1"
            boolean r7 = r11.contains(r7)
            if (r7 != 0) goto L_0x023d
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r11)
            java.lang.String r4 = r7.toString()
        L_0x006f:
            if (r12 == 0) goto L_0x0253
            java.lang.String r7 = ""
            boolean r7 = r12.equals(r7)
            if (r7 != 0) goto L_0x0253
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r12)
            java.lang.String r4 = r7.toString()
        L_0x0090:
            if (r13 == 0) goto L_0x0268
            java.lang.String r7 = ""
            boolean r7 = r13.equals(r7)
            if (r7 != 0) goto L_0x0268
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r13)
            java.lang.String r4 = r7.toString()
        L_0x00b1:
            if (r14 == 0) goto L_0x027d
            java.lang.String r7 = ""
            boolean r7 = r14.equals(r7)
            if (r7 != 0) goto L_0x027d
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r14)
            java.lang.String r4 = r7.toString()
        L_0x00d2:
            if (r15 == 0) goto L_0x0292
            java.lang.String r7 = ""
            boolean r7 = r15.equals(r7)
            if (r7 != 0) goto L_0x0292
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r15)
            java.lang.String r4 = r7.toString()
        L_0x00f3:
            if (r16 == 0) goto L_0x02a7
            java.lang.String r7 = ""
            r0 = r16
            boolean r7 = r0.equals(r7)
            if (r7 != 0) goto L_0x02a7
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r4 = r7.toString()
        L_0x0118:
            if (r17 == 0) goto L_0x02bc
            java.lang.String r7 = ""
            r0 = r17
            boolean r7 = r0.equals(r7)
            if (r7 != 0) goto L_0x02bc
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            r0 = r17
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r4 = r7.toString()
        L_0x013d:
            if (r18 == 0) goto L_0x02d1
            java.lang.String r7 = ""
            r0 = r18
            boolean r7 = r0.equals(r7)
            if (r7 != 0) goto L_0x02d1
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            r0 = r18
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r4 = r7.toString()
        L_0x0162:
            if (r19 == 0) goto L_0x02e6
            java.lang.String r7 = ""
            r0 = r19
            boolean r7 = r0.equals(r7)
            if (r7 != 0) goto L_0x02e6
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            r0 = r19
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r4 = r7.toString()
        L_0x0187:
            if (r20 == 0) goto L_0x02fb
            java.lang.String r7 = ""
            r0 = r20
            boolean r7 = r0.equals(r7)
            if (r7 != 0) goto L_0x02fb
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            r0 = r20
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r4 = r7.toString()
        L_0x01ac:
            if (r23 == 0) goto L_0x0310
            java.lang.String r7 = ""
            r0 = r23
            boolean r7 = r0.equals(r7)
            if (r7 != 0) goto L_0x0310
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            r0 = r23
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r4 = r7.toString()
        L_0x01d1:
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean> r7 = mCacheList
            if (r7 != 0) goto L_0x01dc
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            mCacheList = r7
        L_0x01dc:
            r3 = 0
        L_0x01dd:
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean> r7 = mCacheList
            int r7 = r7.size()
            if (r3 < r7) goto L_0x0325
            java.lang.String r7 = mDirPath
            if (r7 == 0) goto L_0x0359
            java.io.File r2 = new java.io.File
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = mDirPath
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r7.<init>(r8)
            java.lang.StringBuilder r7 = r7.append(r4)
            java.lang.String r7 = r7.toString()
            r2.<init>(r7)
            boolean r7 = r2.exists()
            if (r7 == 0) goto L_0x0359
            boolean r7 = r2.isFile()
            if (r7 == 0) goto L_0x0359
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = mDirPath
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r7.<init>(r8)
            java.lang.StringBuilder r7 = r7.append(r4)
            java.lang.String r7 = r7.toString()
            java.lang.Object r6 = net.sunniwell.p008sz.mop4.sdk.util.Tools.read(r7)
            net.sunniwell.sz.mop4.sdk.media.MediaListBean r6 = (net.sunniwell.p008sz.mop4.sdk.media.MediaListBean) r6
            java.lang.String r7 = "MediaManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "Hit on flash cache!!! key = "
            r8.<init>(r9)
            java.lang.StringBuilder r8 = r8.append(r4)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r7, r8)
            if (r6 == 0) goto L_0x0359
        L_0x023c:
            return r6
        L_0x023d:
            r11 = 0
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x006f
        L_0x0253:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x0090
        L_0x0268:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x00b1
        L_0x027d:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x00d2
        L_0x0292:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x00f3
        L_0x02a7:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x0118
        L_0x02bc:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x013d
        L_0x02d1:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x0162
        L_0x02e6:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x0187
        L_0x02fb:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x01ac
        L_0x0310:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r4)
            r7.<init>(r8)
            java.lang.String r8 = "_"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r4 = r7.toString()
            goto L_0x01d1
        L_0x0325:
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean> r7 = mCacheList
            java.lang.Object r7 = r7.get(r3)
            net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean r7 = (net.sunniwell.p008sz.mop4.sdk.media.MediaManager.MediaCacheBean) r7
            java.lang.String r7 = r7.key
            boolean r7 = r7.equals(r4)
            if (r7 == 0) goto L_0x0355
            java.lang.String r7 = "MediaManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "Hit on memory cache!!! key = "
            r8.<init>(r9)
            java.lang.StringBuilder r8 = r8.append(r4)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r7, r8)
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean> r7 = mCacheList
            java.lang.Object r7 = r7.get(r3)
            net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean r7 = (net.sunniwell.p008sz.mop4.sdk.media.MediaManager.MediaCacheBean) r7
            net.sunniwell.sz.mop4.sdk.media.MediaListBean r6 = r7.mlb
            goto L_0x023c
        L_0x0355:
            int r3 = r3 + 1
            goto L_0x01dd
        L_0x0359:
            java.lang.String r7 = "MediaManager"
            java.lang.String r8 = "Try to get MediaList from server."
            android.util.Log.d(r7, r8)
            net.sunniwell.sz.mop4.sdk.media.MediaListBean r6 = net.sunniwell.p008sz.mop4.sdk.media.MediaDataUtil.get(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23)
            if (r6 == 0) goto L_0x03bd
            net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean r5 = new net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean
            r7 = 0
            r5.<init>(r7)
            r5.key = r4
            r5.mlb = r6
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean> r7 = mCacheList
            int r7 = r7.size()
            r8 = 100
            if (r7 < r8) goto L_0x0387
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean> r7 = mCacheList
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean> r8 = mCacheList
            int r8 = r8.size()
            int r8 = r8 + -1
            r7.remove(r8)
        L_0x0387:
            java.util.ArrayList<net.sunniwell.sz.mop4.sdk.media.MediaManager$MediaCacheBean> r7 = mCacheList
            r8 = 0
            r7.add(r8, r5)
            java.lang.String r7 = mDirPath
            if (r7 == 0) goto L_0x03a7
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = mDirPath
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r7.<init>(r8)
            java.lang.StringBuilder r7 = r7.append(r4)
            java.lang.String r7 = r7.toString()
            net.sunniwell.p008sz.mop4.sdk.util.Tools.save(r6, r7)
        L_0x03a7:
            java.lang.String r7 = "MediaManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "get MediaList from server key = "
            r8.<init>(r9)
            java.lang.StringBuilder r8 = r8.append(r4)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r7, r8)
            goto L_0x023c
        L_0x03bd:
            r6 = 0
            goto L_0x023c
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.media.MediaManager.get(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, java.lang.String):net.sunniwell.sz.mop4.sdk.media.MediaListBean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x00ca, code lost:
        if (r3 != null) goto L_0x00cc;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static net.sunniwell.sz.mop4.sdk.media.MediaBean detail(int r7, java.lang.String r8, int r9, int r10, java.lang.String r11, java.lang.String r12) {
        /*
            int r0 = net.sunniwell.p008sz.mop4.sdk.sync.SyncManager.get(r7)
            if (r11 != 0) goto L_0x0008
            java.lang.String r11 = ""
        L_0x0008:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = net.sunniwell.p008sz.mop4.sdk.soap.SoapClient.getEpgs()
            java.lang.String r6 = ":"
            java.lang.String[] r5 = r5.split(r6)
            r6 = 0
            r5 = r5[r6]
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r4.<init>(r5)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r7)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r8)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r9)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r10)
            java.lang.String r2 = r4.toString()
            if (r11 == 0) goto L_0x005e
            java.lang.String r4 = ""
            boolean r4 = r11.equals(r4)
            if (r4 == 0) goto L_0x00cd
        L_0x005e:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = java.lang.String.valueOf(r2)
            r4.<init>(r5)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r12)
            java.lang.String r2 = r4.toString()
        L_0x0075:
            java.lang.String r4 = mDirPath
            if (r4 == 0) goto L_0x00ef
            java.io.File r1 = new java.io.File
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = mDirPath
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r4.<init>(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r4 = r4.toString()
            r1.<init>(r4)
            boolean r4 = r1.exists()
            if (r4 == 0) goto L_0x00ef
            boolean r4 = r1.isFile()
            if (r4 == 0) goto L_0x00ef
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = mDirPath
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r4.<init>(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r4 = r4.toString()
            java.lang.Object r3 = net.sunniwell.p008sz.mop4.sdk.util.Tools.read(r4)
            net.sunniwell.sz.mop4.sdk.media.MediaBean r3 = (net.sunniwell.p008sz.mop4.sdk.media.MediaBean) r3
            java.lang.String r4 = "MediaManager"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "Get detail, hit on flash cache!!! key = "
            r5.<init>(r6)
            java.lang.StringBuilder r5 = r5.append(r2)
            java.lang.String r5 = r5.toString()
            android.util.Log.d(r4, r5)
            if (r3 == 0) goto L_0x00ef
        L_0x00cc:
            return r3
        L_0x00cd:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = java.lang.String.valueOf(r2)
            r4.<init>(r5)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r11)
            java.lang.String r5 = "_"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r12)
            java.lang.String r2 = r4.toString()
            goto L_0x0075
        L_0x00ef:
            net.sunniwell.sz.mop4.sdk.media.MediaBean r3 = net.sunniwell.p008sz.mop4.sdk.media.MediaDataUtil.detail(r7, r8, r9, r10, r11, r12)
            if (r3 == 0) goto L_0x00cc
            java.lang.String r4 = mDirPath
            if (r4 == 0) goto L_0x00cc
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = mDirPath
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r4.<init>(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r4 = r4.toString()
            net.sunniwell.p008sz.mop4.sdk.util.Tools.save(r3, r4)
            goto L_0x00cc
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.media.MediaManager.detail(int, java.lang.String, int, int, java.lang.String, java.lang.String):net.sunniwell.sz.mop4.sdk.media.MediaBean");
    }

    public static List<MediaBean> getRelates(int columnId, String mediaId, int size, String lang) {
        String key = new StringBuilder(String.valueOf(SoapClient.getEpgs().split(":")[0])).append("_").append(columnId).append("_").append(SyncManager.get(columnId)).append("_").append(mediaId).append("_").append(size).append("_").append(lang).toString();
        if (mDirPath != null) {
            File file = new File(mDirPath + key);
            if (file.exists() && file.isFile()) {
                ArrayList<MediaBean> ml = (ArrayList) Tools.read(mDirPath + key);
                Log.d(TAG, "Get relates, hit on flash cache!!! key = " + key);
                return ml;
            }
        }
        ArrayList<MediaBean> ml2 = MediaDataUtil.getRelates(columnId, mediaId, size, lang);
        if (ml2 == null) {
            return null;
        }
        if (mDirPath == null) {
            return ml2;
        }
        Tools.save(ml2, mDirPath + key);
        return ml2;
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
