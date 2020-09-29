package net.sunniwell.download.manager;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.sunniwell.download.common.TAStringUtils;
import net.sunniwell.download.http.AsyncHttpClient;
import net.sunniwell.download.http.AsyncHttpResponseHandler;
import net.sunniwell.download.http.FileHttpResponseHandler;
import org.xmlpull.v1.XmlPullParser;

public class DownloadManager extends Thread {
    private static final String SDCARD_ROOT = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").toString();
    public static final String FILE_ROOT = (SDCARD_ROOT + "P2P_DOWNLOAD/");
    private static final int MAX_DOWNLOAD_THREAD_COUNT = 3;
    private static final int MAX_handler_COUNT = 100;
    private static DownloadManager downloadManager;
    private Boolean isRunning;
    /* access modifiers changed from: private */
    public DownLoadCallback mDownLoadCallback;
    /* access modifiers changed from: private */
    public List<AsyncHttpResponseHandler> mDownloadinghandlers;
    private String mFileName;
    private List<AsyncHttpResponseHandler> mPausinghandlers;
    private handlerQueue mhandlerQueue;
    private String rootPath;
    private AsyncHttpClient syncHttpClient;

    private class handlerQueue {
        private Queue<AsyncHttpResponseHandler> handlerQueue = new LinkedList();

        public handlerQueue() {
        }

        public void offer(AsyncHttpResponseHandler handler) {
            this.handlerQueue.offer(handler);
        }

        public AsyncHttpResponseHandler poll() {
            while (true) {
                if (DownloadManager.this.mDownloadinghandlers.size() < 3) {
                    AsyncHttpResponseHandler handler = (AsyncHttpResponseHandler) this.handlerQueue.poll();
                    if (handler != null) {
                        return handler;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public AsyncHttpResponseHandler get(int position) {
            if (position >= size()) {
                return null;
            }
            return (AsyncHttpResponseHandler) ((LinkedList) this.handlerQueue).get(position);
        }

        public int size() {
            return this.handlerQueue.size();
        }

        public boolean remove(int position) {
            return this.handlerQueue.remove(get(position));
        }

        public boolean remove(AsyncHttpResponseHandler handler) {
            return this.handlerQueue.remove(handler);
        }
    }

    public static DownloadManager getDownloadManager() {
        if (downloadManager == null) {
            downloadManager = new DownloadManager(FILE_ROOT);
        }
        return downloadManager;
    }

    public static DownloadManager getDownloadManager(String rootPath2) {
        if (downloadManager == null) {
            downloadManager = new DownloadManager(rootPath2);
        }
        return downloadManager;
    }

    private DownloadManager() {
        this(FILE_ROOT);
    }

    private DownloadManager(String rootPath2) {
        this.isRunning = Boolean.valueOf(false);
        this.rootPath = XmlPullParser.NO_NAMESPACE;
        this.rootPath = rootPath2;
        this.mhandlerQueue = new handlerQueue();
        this.mDownloadinghandlers = new ArrayList();
        this.mPausinghandlers = new ArrayList();
        this.syncHttpClient = new AsyncHttpClient();
        if (!TAStringUtils.isEmpty(rootPath2)) {
            File rootFile = new File(rootPath2);
            if (!rootFile.exists()) {
                rootFile.mkdir();
            }
        }
    }

    public String getRootPath() {
        if (TAStringUtils.isEmpty(this.rootPath)) {
            this.rootPath = FILE_ROOT;
        }
        return this.rootPath;
    }

    public void setDownLoadCallback(DownLoadCallback downLoadCallback) {
        this.mDownLoadCallback = downLoadCallback;
    }

    public void startManage() {
        this.isRunning = Boolean.valueOf(true);
        start();
        if (this.mDownLoadCallback != null) {
            this.mDownLoadCallback.sendStartMessage();
        }
    }

    public void close() {
        this.isRunning = Boolean.valueOf(false);
        pauseAllHandler();
        if (this.mDownLoadCallback != null) {
            this.mDownLoadCallback.sendStopMessage();
        }
        stop();
    }

    public boolean isRunning() {
        return this.isRunning.booleanValue();
    }

    public void run() {
        super.run();
        while (this.isRunning.booleanValue()) {
            FileHttpResponseHandler handler = (FileHttpResponseHandler) this.mhandlerQueue.poll();
            if (handler != null) {
                this.mDownloadinghandlers.add(handler);
                handler.setInterrupt(false);
                this.syncHttpClient.download(handler.getUrl(), handler);
            }
        }
    }

    public void addHandler(String url) {
        if (getTotalhandlerCount() >= 100) {
            if (this.mDownLoadCallback != null) {
                this.mDownLoadCallback.sendFailureMessage(url, "任务列表已满");
            }
        } else if (!TextUtils.isEmpty(url) && !hasHandler(url)) {
            try {
                addHandler(newAsyncHttpResponseHandler(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addHandler(AsyncHttpResponseHandler handler) {
        broadcastAddHandler(((FileHttpResponseHandler) handler).getUrl());
        this.mhandlerQueue.offer(handler);
        if (!isAlive()) {
            startManage();
        }
    }

    private void broadcastAddHandler(String url) {
        broadcastAddHandler(url, false);
    }

    private void broadcastAddHandler(String url, boolean isInterrupt) {
        if (this.mDownLoadCallback != null) {
            this.mDownLoadCallback.sendAddMessage(url, Boolean.valueOf(false));
        }
    }

    public void reBroadcastAddAllhandler() {
        for (int i = 0; i < this.mDownloadinghandlers.size(); i++) {
            FileHttpResponseHandler handler = (FileHttpResponseHandler) this.mDownloadinghandlers.get(i);
            broadcastAddHandler(handler.getUrl(), handler.isInterrupt());
        }
        for (int i2 = 0; i2 < this.mhandlerQueue.size(); i2++) {
            broadcastAddHandler(((FileHttpResponseHandler) this.mhandlerQueue.get(i2)).getUrl());
        }
        for (int i3 = 0; i3 < this.mPausinghandlers.size(); i3++) {
            broadcastAddHandler(((FileHttpResponseHandler) this.mPausinghandlers.get(i3)).getUrl());
        }
    }

    public boolean hasHandler(String url) {
        for (int i = 0; i < this.mDownloadinghandlers.size(); i++) {
            if (((FileHttpResponseHandler) this.mDownloadinghandlers.get(i)).getUrl().equals(url)) {
                return true;
            }
        }
        for (int i2 = 0; i2 < this.mhandlerQueue.size(); i2++) {
            if (((FileHttpResponseHandler) this.mhandlerQueue.get(i2)).getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    public FileHttpResponseHandler getHandler(String url) {
        FileHttpResponseHandler handler = null;
        for (int i = 0; i < this.mDownloadinghandlers.size(); i++) {
            handler = (FileHttpResponseHandler) this.mDownloadinghandlers.get(i);
        }
        for (int i2 = 0; i2 < this.mhandlerQueue.size(); i2++) {
            handler = (FileHttpResponseHandler) this.mhandlerQueue.get(i2);
        }
        return handler;
    }

    public AsyncHttpResponseHandler gethandler(int position) {
        if (position >= this.mDownloadinghandlers.size()) {
            return this.mhandlerQueue.get(position - this.mDownloadinghandlers.size());
        }
        return (AsyncHttpResponseHandler) this.mDownloadinghandlers.get(position);
    }

    public int getQueuehandlerCount() {
        return this.mhandlerQueue.size();
    }

    public int getDownloadinghandlerCount() {
        return this.mDownloadinghandlers.size();
    }

    public int getPausinghandlerCount() {
        return this.mPausinghandlers.size();
    }

    public int getTotalhandlerCount() {
        return getQueuehandlerCount() + getDownloadinghandlerCount() + getPausinghandlerCount();
    }

    public void checkUncompletehandlers() {
        List<String> urlList = DownLoadConfigUtil.getURLArray();
        if (urlList.size() >= 0) {
            for (int i = 0; i < urlList.size(); i++) {
                addHandler((String) urlList.get(i));
            }
        }
    }

    public synchronized void pauseHandler(String url) {
        for (int i = 0; i < this.mDownloadinghandlers.size(); i++) {
            FileHttpResponseHandler handler = (FileHttpResponseHandler) this.mDownloadinghandlers.get(i);
            if (handler != null && handler.getUrl().equals(url)) {
                pausehandler(handler);
            }
        }
    }

    public synchronized void pauseAllHandler() {
        for (int i = 0; i < this.mhandlerQueue.size(); i++) {
            AsyncHttpResponseHandler handler = this.mhandlerQueue.get(i);
            this.mhandlerQueue.remove(handler);
            this.mPausinghandlers.add(handler);
        }
        for (int i2 = 0; i2 < this.mDownloadinghandlers.size(); i2++) {
            AsyncHttpResponseHandler handler2 = (AsyncHttpResponseHandler) this.mDownloadinghandlers.get(i2);
            if (handler2 != null) {
                pausehandler(handler2);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001a, code lost:
        if (r2 < r5.mPausinghandlers.size()) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        r1 = (net.sunniwell.download.http.FileHttpResponseHandler) r5.mhandlerQueue.get(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0062, code lost:
        if (r1 == null) goto L_0x0073;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x006c, code lost:
        if (r1.getUrl().equals(r6) == false) goto L_0x0073;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x006e, code lost:
        r5.mhandlerQueue.remove((net.sunniwell.download.http.AsyncHttpResponseHandler) r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0073, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0076, code lost:
        r1 = (net.sunniwell.download.http.FileHttpResponseHandler) r5.mPausinghandlers.get(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x007e, code lost:
        if (r1 == null) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0088, code lost:
        if (r1.getUrl().equals(r6) == false) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x008a, code lost:
        r5.mPausinghandlers.remove(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x008f, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000a, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0011, code lost:
        if (r2 < r5.mhandlerQueue.size()) goto L_0x005a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0013, code lost:
        r2 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void deleteHandler(java.lang.String r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            r2 = 0
        L_0x0002:
            java.util.List<net.sunniwell.download.http.AsyncHttpResponseHandler> r4 = r5.mDownloadinghandlers     // Catch:{ all -> 0x0054 }
            int r4 = r4.size()     // Catch:{ all -> 0x0054 }
            if (r2 < r4) goto L_0x001e
            r2 = 0
        L_0x000b:
            net.sunniwell.download.manager.DownloadManager$handlerQueue r4 = r5.mhandlerQueue     // Catch:{ all -> 0x0054 }
            int r4 = r4.size()     // Catch:{ all -> 0x0054 }
            if (r2 < r4) goto L_0x005a
            r2 = 0
        L_0x0014:
            java.util.List<net.sunniwell.download.http.AsyncHttpResponseHandler> r4 = r5.mPausinghandlers     // Catch:{ all -> 0x0054 }
            int r4 = r4.size()     // Catch:{ all -> 0x0054 }
            if (r2 < r4) goto L_0x0076
        L_0x001c:
            monitor-exit(r5)
            return
        L_0x001e:
            java.util.List<net.sunniwell.download.http.AsyncHttpResponseHandler> r4 = r5.mDownloadinghandlers     // Catch:{ all -> 0x0054 }
            java.lang.Object r1 = r4.get(r2)     // Catch:{ all -> 0x0054 }
            net.sunniwell.download.http.FileHttpResponseHandler r1 = (net.sunniwell.download.http.FileHttpResponseHandler) r1     // Catch:{ all -> 0x0054 }
            if (r1 == 0) goto L_0x0057
            java.lang.String r4 = r1.getUrl()     // Catch:{ all -> 0x0054 }
            boolean r4 = r4.equals(r6)     // Catch:{ all -> 0x0054 }
            if (r4 == 0) goto L_0x0057
            java.io.File r0 = r1.getFile()     // Catch:{ all -> 0x0054 }
            boolean r4 = r0.exists()     // Catch:{ all -> 0x0054 }
            if (r4 == 0) goto L_0x003f
            r0.delete()     // Catch:{ all -> 0x0054 }
        L_0x003f:
            java.io.File r3 = r1.getTempFile()     // Catch:{ all -> 0x0054 }
            boolean r4 = r3.exists()     // Catch:{ all -> 0x0054 }
            if (r4 == 0) goto L_0x004c
            r3.delete()     // Catch:{ all -> 0x0054 }
        L_0x004c:
            r4 = 1
            r1.setInterrupt(r4)     // Catch:{ all -> 0x0054 }
            r5.completehandler(r1)     // Catch:{ all -> 0x0054 }
            goto L_0x001c
        L_0x0054:
            r4 = move-exception
            monitor-exit(r5)
            throw r4
        L_0x0057:
            int r2 = r2 + 1
            goto L_0x0002
        L_0x005a:
            net.sunniwell.download.manager.DownloadManager$handlerQueue r4 = r5.mhandlerQueue     // Catch:{ all -> 0x0054 }
            net.sunniwell.download.http.AsyncHttpResponseHandler r1 = r4.get(r2)     // Catch:{ all -> 0x0054 }
            net.sunniwell.download.http.FileHttpResponseHandler r1 = (net.sunniwell.download.http.FileHttpResponseHandler) r1     // Catch:{ all -> 0x0054 }
            if (r1 == 0) goto L_0x0073
            java.lang.String r4 = r1.getUrl()     // Catch:{ all -> 0x0054 }
            boolean r4 = r4.equals(r6)     // Catch:{ all -> 0x0054 }
            if (r4 == 0) goto L_0x0073
            net.sunniwell.download.manager.DownloadManager$handlerQueue r4 = r5.mhandlerQueue     // Catch:{ all -> 0x0054 }
            r4.remove(r1)     // Catch:{ all -> 0x0054 }
        L_0x0073:
            int r2 = r2 + 1
            goto L_0x000b
        L_0x0076:
            java.util.List<net.sunniwell.download.http.AsyncHttpResponseHandler> r4 = r5.mPausinghandlers     // Catch:{ all -> 0x0054 }
            java.lang.Object r1 = r4.get(r2)     // Catch:{ all -> 0x0054 }
            net.sunniwell.download.http.FileHttpResponseHandler r1 = (net.sunniwell.download.http.FileHttpResponseHandler) r1     // Catch:{ all -> 0x0054 }
            if (r1 == 0) goto L_0x008f
            java.lang.String r4 = r1.getUrl()     // Catch:{ all -> 0x0054 }
            boolean r4 = r4.equals(r6)     // Catch:{ all -> 0x0054 }
            if (r4 == 0) goto L_0x008f
            java.util.List<net.sunniwell.download.http.AsyncHttpResponseHandler> r4 = r5.mPausinghandlers     // Catch:{ all -> 0x0054 }
            r4.remove(r1)     // Catch:{ all -> 0x0054 }
        L_0x008f:
            int r2 = r2 + 1
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.download.manager.DownloadManager.deleteHandler(java.lang.String):void");
    }

    public synchronized void continueHandler(String url) {
        for (int i = 0; i < this.mPausinghandlers.size(); i++) {
            FileHttpResponseHandler handler = (FileHttpResponseHandler) this.mPausinghandlers.get(i);
            if (handler != null && handler.getUrl().equals(url)) {
                continuehandler(handler);
            }
        }
    }

    public synchronized void pausehandler(AsyncHttpResponseHandler handler) {
        FileHttpResponseHandler fileHttpResponseHandler = (FileHttpResponseHandler) handler;
        if (handler != null) {
            fileHttpResponseHandler.setInterrupt(true);
            String url = fileHttpResponseHandler.getUrl();
            try {
                this.mDownloadinghandlers.remove(handler);
                this.mPausinghandlers.add(newAsyncHttpResponseHandler(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public synchronized void continuehandler(AsyncHttpResponseHandler handler) {
        if (handler != null) {
            this.mPausinghandlers.remove(handler);
            this.mhandlerQueue.offer(handler);
        }
    }

    public synchronized void completehandler(AsyncHttpResponseHandler handler) {
        if (this.mDownloadinghandlers.contains(handler)) {
            DownLoadConfigUtil.clearURL(this.mDownloadinghandlers.indexOf(handler));
            this.mDownloadinghandlers.remove(handler);
            if (this.mDownLoadCallback != null) {
                this.mDownLoadCallback.sendFinishMessage(((FileHttpResponseHandler) handler).getUrl());
            }
        }
    }

    public void setmFileName(String mFileName2) {
        this.mFileName = mFileName2;
    }

    private AsyncHttpResponseHandler newAsyncHttpResponseHandler(String url) throws MalformedURLException {
        if (this.mFileName == null || this.mFileName.length() == 0) {
            this.mFileName = TAStringUtils.getFileNameFromUrl(url);
        }
        return new FileHttpResponseHandler(url, this.rootPath, this.mFileName) {
            public void onProgress(long totalSize, long currentSize, long speed) {
                super.onProgress(totalSize, currentSize, speed);
                if (DownloadManager.this.mDownLoadCallback != null) {
                    DownloadManager.this.mDownLoadCallback.sendLoadMessage(getUrl(), totalSize, currentSize, speed);
                }
            }

            public void onSuccess(String content) {
                if (DownloadManager.this.mDownLoadCallback != null) {
                    DownloadManager.this.mDownLoadCallback.sendSuccessMessage(getUrl());
                }
            }

            public void onFinish() {
                DownloadManager.this.completehandler(this);
            }

            public void onStart() {
                DownLoadConfigUtil.storeURL(DownloadManager.this.mDownloadinghandlers.indexOf(this), getUrl());
            }

            public void onFailure(Throwable error) {
                Log.d("DownloadManager", "Throwable");
                if (error != null && DownloadManager.this.mDownLoadCallback != null) {
                    DownloadManager.this.mDownLoadCallback.sendFailureMessage(getUrl(), error.getMessage());
                }
            }
        };
    }
}
