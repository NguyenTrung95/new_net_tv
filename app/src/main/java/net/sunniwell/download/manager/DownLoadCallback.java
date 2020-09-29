package net.sunniwell.download.manager;

import android.os.Handler;
import android.os.Message;

public class DownLoadCallback extends Handler {
    protected static final int ADD_MESSAGE = 1;
    protected static final int FAILURE_MESSAGE = 4;
    protected static final int FINISH_MESSAGE = 5;
    protected static final int PROGRESS_MESSAGE = 2;
    protected static final int START_MESSAGE = 0;
    protected static final int STOP_MESSAGE = 6;
    protected static final int SUCCESS_MESSAGE = 3;

    public void onStart() {
    }

    public void onAdd(String url, Boolean isInterrupt) {
    }

    public void onLoading(String url, long totalSize, long currentSize, long speed) {
    }

    public void onSuccess(String url) {
    }

    public void onFailure(String url, String strMsg) {
    }

    public void onFinish(String url) {
    }

    public void onStop() {
    }

    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:
                onStart();
                return;
            case 1:
                Object[] response = (Object[]) msg.obj;
                onAdd((String) response[0], (Boolean) response[1]);
                return;
            case 2:
                Object[] response2 = (Object[]) msg.obj;
                onLoading((String) response2[0], ((Long) response2[1]).longValue(), ((Long) response2[2]).longValue(), ((Long) response2[3]).longValue());
                return;
            case 3:
                onSuccess((String) ((Object[]) msg.obj)[0]);
                return;
            case 4:
                Object[] response3 = (Object[]) msg.obj;
                onFailure((String) response3[0], (String) response3[1]);
                return;
            case 5:
                onFinish((String) ((Object[]) msg.obj)[0]);
                return;
            case 6:
                onStop();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void sendSuccessMessage(String url) {
        sendMessage(obtainMessage(3, new Object[]{url}));
    }

    /* access modifiers changed from: protected */
    public void sendLoadMessage(String url, long totalSize, long currentSize, long speed) {
        sendMessage(obtainMessage(2, new Object[]{url, Long.valueOf(totalSize), Long.valueOf(currentSize), Long.valueOf(speed)}));
    }

    /* access modifiers changed from: protected */
    public void sendAddMessage(String url, Boolean isInterrupt) {
        sendMessage(obtainMessage(1, new Object[]{url, isInterrupt}));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(String url, String strMsg) {
        sendMessage(obtainMessage(4, new Object[]{url, strMsg}));
    }

    /* access modifiers changed from: protected */
    public void sendStartMessage() {
        sendMessage(obtainMessage(0, null));
    }

    /* access modifiers changed from: protected */
    public void sendStopMessage() {
        sendMessage(obtainMessage(6, null));
    }

    /* access modifiers changed from: protected */
    public void sendFinishMessage(String url) {
        sendMessage(obtainMessage(5, new Object[]{url}));
    }
}
