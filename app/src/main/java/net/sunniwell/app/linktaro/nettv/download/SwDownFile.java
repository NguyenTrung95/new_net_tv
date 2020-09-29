package net.sunniwell.app.linktaro.nettv.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import java.lang.ref.WeakReference;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.download.manager.DownLoadCallback;
import net.sunniwell.download.manager.DownloadManager;
import org.apache.http.HttpHost;
import org.xmlpull.p019v1.XmlPullParser;

public class SwDownFile {
    public static final int CREATE_NEWFILE_ERROR = 0;
    private static final int DOWNLOAD_COMPLETED = 0;
    private static final int DOWNLOAD_ERROR = 3;
    private static final int DOWNLOAD_FAILURE = 4;
    private static final String DOWNLOAD_PATH = "/P2P_DOWNLOAD/";
    private static final int DOWNLOAD_PROGRESS_UPDATE = 1;
    private static final int DOWNLOAD_SPEED_UPDATE = 2;
    public static final int INPUT_STREAM_ERROR = 3;
    public static final int IS_DOWNLOADING = 1;
    public static final int IS_PAUSED = 2;
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(SwDownFile.class);
    public static final int NOT_DOWNLOADING = 0;
    public static final int OUTPUT_STREAM_ERROR = 4;
    private static String ROOT_PATH = null;
    public static final int URL_ERROR = 1;
    public static final int URL_OPEN_CONNECT_ERROR = 2;
    public static final int WRITE_ERROR = 5;
    private static SwDownFile swDownFile;
    /* access modifiers changed from: private */
    public DownFileCallBackListener backListener;
    private Context mContext;
    private DownloadManager mDownloadManager;
    /* access modifiers changed from: private */
    public long mDownloadPercent;
    /* access modifiers changed from: private */
    public int mDownloadSpeed;
    private String mEndTime;
    /* access modifiers changed from: private */
    public EventHandler mEventHandler;
    /* access modifiers changed from: private */
    public boolean mIsDownLoading = false;
    /* access modifiers changed from: private */
    public boolean mIsPause = false;
    private long mSpeed = 0;
    private String mStartTime;
    private String mTaskName = XmlPullParser.NO_NAMESPACE;
    private String mTerminalid;
    /* access modifiers changed from: private */
    public String mdownurl = XmlPullParser.NO_NAMESPACE;
    /* access modifiers changed from: private */
    public String mpath = XmlPullParser.NO_NAMESPACE;

    public interface DownFileCallBackListener {
        void onDownFileComplete();

        void onDownFileErrorCallBack(int i);

        void onDownFileProgressUpdate(int i);

        void onDownFileSpeedUpdate(int i);
    }

    private class EventHandler extends Handler {
        private SwDownFile mSwDownFile;

        public EventHandler(SwDownFile df, Looper looper) {
            super(looper);
            this.mSwDownFile = df;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (SwDownFile.this.backListener != null) {
                        SwDownFile.this.backListener.onDownFileComplete();
                    }
                    SwDownFile.this.mIsDownLoading = false;
                    SwDownFile.this.mIsPause = false;
                    return;
                case 3:
                    if (SwDownFile.this.backListener != null) {
                        SwDownFile.this.backListener.onDownFileErrorCallBack(msg.arg1);
                    }
                    SwDownFile.this.mIsDownLoading = false;
                    SwDownFile.this.mIsPause = false;
                    return;
                case 4:
                    SwDownFile.this.downFile(SwDownFile.this.mdownurl, SwDownFile.this.mpath);
                    return;
                default:
                    return;
            }
        }
    }

    public SwDownFile(Context context, DownFileCallBackListener backListener2) {
        LOG.mo8825d("SwDownFile---->constructor");
        this.backListener = backListener2;
        this.mContext = context;
        ROOT_PATH = new StringBuilder(String.valueOf(SWSysProp.getStringParam("mount_path", "/storage/sda1"))).append("/P2P_DOWNLOAD/").toString();
        LOG.mo8825d("ROOT_PATH---->" + ROOT_PATH);
        this.mDownloadManager = DownloadManager.getDownloadManager(ROOT_PATH);
        Looper looper = Looper.myLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            Looper looper2 = Looper.getMainLooper();
            if (looper2 != null) {
                this.mEventHandler = new EventHandler(this, looper2);
            } else {
                this.mEventHandler = null;
            }
        }
        this.mTerminalid = SystemProperties.get("ro.serialno");
    }

    public void startDownLoad(String taskName, String vodUrl, String startTime, String endTime, String path) {
        LOG.mo8825d("******startDownLoad********");
        this.mTaskName = taskName;
        this.mdownurl = vodUrl;
        this.mpath = path;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        LOG.mo8825d("vodurl:" + vodUrl);
        LOG.mo8825d("start:" + startTime);
        LOG.mo8825d("endTime:" + endTime);
        if (!StringUtils.isEmpty(this.mStartTime) && !StringUtils.isEmpty(this.mEndTime)) {
            this.mdownurl += "&playseek=" + this.mStartTime + "-" + this.mEndTime;
        }
        this.mdownurl = this.mdownurl.replace("m3u8", "ts");
        this.mdownurl = this.mdownurl.replace("hls", HttpHost.DEFAULT_SCHEME_NAME);
        LOG.mo8825d("down url:" + this.mdownurl);
        if (StringUtils.isNotEmpty(this.mdownurl)) {
            LOG.mo8825d("isDownSuccess------>" + downFile(this.mdownurl, this.mpath));
            this.mIsDownLoading = true;
            this.mIsPause = false;
        }
    }

    public String getTaskName() {
        return this.mTaskName;
    }

    public void finish() {
        try {
            LOG.mo8825d("finish-------->");
            this.mIsDownLoading = false;
            this.mIsPause = false;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setDownFileCallBackListener(DownFileCallBackListener backListener2) {
        this.backListener = backListener2;
    }

    public static void postEventFromNative(Object weak_ref, int what, int arg1, int arg2, Object obj) {
        SwDownFile swDF = (SwDownFile) ((WeakReference) weak_ref).get();
        if (swDF != null && swDF.mEventHandler != null) {
            swDF.mEventHandler.sendMessage(swDF.mEventHandler.obtainMessage(what, arg1, arg2, obj));
        }
    }

    public void pause() {
        this.mIsDownLoading = false;
        this.mIsPause = true;
        pauseDownLoad();
    }

    public void stop() {
        this.mIsDownLoading = false;
        this.mIsPause = false;
        stopDownLoad();
    }

    public void resume() {
        this.mIsDownLoading = true;
        this.mIsPause = false;
        resumeDownLoad();
    }

    public int getDownLoadStatus() {
        if (this.mIsDownLoading) {
            return 1;
        }
        if (this.mIsPause) {
            return 2;
        }
        return 0;
    }

    public long getDownloadPercent() {
        return this.mDownloadPercent;
    }

    public int getDownloadSpeed() {
        return this.mDownloadSpeed;
    }

    /* access modifiers changed from: private */
    public int downFile(String vodUrl, String mPath) {
        LOG.mo8825d("==vodUrl==" + vodUrl + "==mPath==" + mPath);
        String[] strings = mPath.split("\\/");
        String name = strings[strings.length - 1];
        LOG.mo8825d("==name==" + name);
        this.mDownloadManager.setmFileName(name);
        this.mDownloadManager.addHandler(vodUrl);
        this.mDownloadManager.setDownLoadCallback(new DownLoadCallback() {
            public void onLoading(String url, long totalSize, long currentSize, long speed) {
                super.onLoading(url, totalSize, currentSize, speed);
                if (totalSize > 0) {
                    SwDownFile.this.mDownloadPercent = (100 * currentSize) / totalSize;
                    SwDownFile.this.mDownloadSpeed = (int) (1000 * speed);
                }
            }

            public void onSuccess(String url) {
                super.onSuccess(url);
                SwDownFile.LOG.mo8825d("==download onSuccess==");
                SwDownFile.this.mEventHandler.sendEmptyMessage(0);
            }

            public void onFailure(String url, String strMsg) {
                super.onFailure(url, strMsg);
                SwDownFile.LOG.mo8825d("==download onFailure==");
                SwDownFile.LOG.mo8825d("url:" + url + ";strMsg:" + strMsg);
                if (strMsg == null || strMsg.length() == 0) {
                    SwDownFile.this.mEventHandler.sendEmptyMessageDelayed(4, 10000);
                } else {
                    SwDownFile.this.mEventHandler.sendEmptyMessage(3);
                }
            }
        });
        return 0;
    }

    private void stopDownLoad() {
        this.mDownloadManager.deleteHandler(this.mdownurl);
    }

    private void pauseDownLoad() {
        this.mDownloadManager.pauseHandler(this.mdownurl);
    }

    private void resumeDownLoad() {
        this.mDownloadManager.continueHandler(this.mdownurl);
    }
}
