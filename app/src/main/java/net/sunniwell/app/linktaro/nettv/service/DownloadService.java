package net.sunniwell.app.linktaro.nettv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import net.sunniwell.app.linktaro.nettv.download.DownUtils;
import net.sunniwell.common.log.SWLogger;

public class DownloadService extends Service {
    /* access modifiers changed from: private */
    public final SWLogger log = SWLogger.getLogger(getClass());
    /* access modifiers changed from: private */
    public DownUtils mDownUtils;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DownloadService.this.log.mo8825d("----start redown----");
                    new Thread(new Runnable() {
                        public void run() {
                            DownloadService.this.mDownUtils = DownUtils.getDownUtilsInstance(DownloadService.this);
                            DownloadService.this.log.mo8825d("-------mDownUtils=" + DownloadService.this.mDownUtils);
                            DownloadService.this.log.mo8825d("-----state=" + DownloadService.this.mDownUtils.getDownLoadStatus());
                            DownloadService.this.mDownUtils.resumeDownTask();
                        }
                    }).start();
                    return;
                default:
                    return;
            }
        }
    };

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        this.log.mo8825d("----abc-onStart--abc-----");
        this.mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
