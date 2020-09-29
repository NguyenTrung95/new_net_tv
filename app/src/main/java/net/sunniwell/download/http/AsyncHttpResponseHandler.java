package net.sunniwell.download.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import org.apache.http.Header;

public class AsyncHttpResponseHandler {
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int FINISH_MESSAGE = 3;
    protected static final int PROGRESS_MESSAGE = 0;
    protected static final int START_MESSAGE = 2;
    protected static final int SUCCESS_MESSAGE = 4;
    private Handler handler;

    public AsyncHttpResponseHandler() {
        if (Looper.myLooper() != null) {
            this.handler = new Handler() {
                public void handleMessage(Message msg) {
                    AsyncHttpResponseHandler.this.handleMessage(msg);
                }
            };
        }
    }

    public void onStart() {
    }

    public void onFinish() {
    }

    public void onSuccess(String content) {
    }

    public void onProgress(long totalSize, long currentSize, long speed) {
    }

    public void onSuccess(int statusCode, Header[] headers, String content) {
        onSuccess(statusCode, content);
    }

    public void onSuccess(int statusCode, String content) {
        onSuccess(content);
    }

    public void onFailure(Throwable error) {
    }

    public void onFailure(Throwable error, String content) {
        onFailure(error);
    }

    /* access modifiers changed from: protected */
    public void sendSuccessMessage(int statusCode, Header[] headers, String responseBody) {
        sendMessage(obtainMessage(4, new Object[]{new Integer(statusCode), headers, responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(Throwable e, String responseBody) {
        sendMessage(obtainMessage(1, new Object[]{e, responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(Throwable e, byte[] responseBody) {
        sendMessage(obtainMessage(1, new Object[]{e, responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendStartMessage() {
        sendMessage(obtainMessage(2, null));
    }

    /* access modifiers changed from: protected */
    public void sendFinishMessage() {
        sendMessage(obtainMessage(3, null));
    }

    /* access modifiers changed from: protected */
    public void handleSuccessMessage(int statusCode, Header[] headers, String responseBody) {
        onSuccess(statusCode, headers, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleFailureMessage(Throwable e, String responseBody) {
        onFailure(e, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleProgressMessage(long totalSize, long currentSize, long speed) {
        onProgress(totalSize, currentSize, speed);
    }

    /* access modifiers changed from: protected */
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Object[] response = (Object[]) msg.obj;
                handleProgressMessage(((Long) response[0]).longValue(), ((Long) response[1]).longValue(), ((Long) response[2]).longValue());
                return;
            case 1:
                Object[] response2 = (Object[]) msg.obj;
                handleFailureMessage((Throwable) response2[0], (String) response2[1]);
                return;
            case 2:
                onStart();
                return;
            case 3:
                onFinish();
                return;
            case 4:
                Object[] response3 = (Object[]) msg.obj;
                handleSuccessMessage(((Integer) response3[0]).intValue(), (Header[]) response3[1], (String) response3[2]);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void sendMessage(Message msg) {
        if (this.handler != null) {
            this.handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    /* access modifiers changed from: protected */
    public Message obtainMessage(int responseMessage, Object response) {
        if (this.handler != null) {
            return this.handler.obtainMessage(responseMessage, response);
        }
        Message msg = Message.obtain();
        msg.what = responseMessage;
        msg.obj = response;
        return msg;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x001f  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0036  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendResponseMessage(org.apache.http.HttpResponse r10) {
        /*
            r9 = this;
            org.apache.http.StatusLine r4 = r10.getStatusLine()
            r3 = 0
            r1 = 0
            org.apache.http.HttpEntity r5 = r10.getEntity()     // Catch:{ IOException -> 0x0030 }
            if (r5 == 0) goto L_0x0017
            org.apache.http.entity.BufferedHttpEntity r2 = new org.apache.http.entity.BufferedHttpEntity     // Catch:{ IOException -> 0x0030 }
            r2.<init>(r5)     // Catch:{ IOException -> 0x0030 }
            java.lang.String r6 = "UTF-8"
            java.lang.String r3 = org.apache.http.util.EntityUtils.toString(r2, r6)     // Catch:{ IOException -> 0x0042 }
        L_0x0017:
            int r6 = r4.getStatusCode()
            r7 = 300(0x12c, float:4.2E-43)
            if (r6 < r7) goto L_0x0036
            org.apache.http.client.HttpResponseException r6 = new org.apache.http.client.HttpResponseException
            int r7 = r4.getStatusCode()
            java.lang.String r8 = r4.getReasonPhrase()
            r6.<init>(r7, r8)
            r9.sendFailureMessage(r6, r3)
        L_0x002f:
            return
        L_0x0030:
            r0 = move-exception
        L_0x0031:
            r6 = 0
            r9.sendFailureMessage(r0, r6)
            goto L_0x0017
        L_0x0036:
            int r6 = r4.getStatusCode()
            org.apache.http.Header[] r7 = r10.getAllHeaders()
            r9.sendSuccessMessage(r6, r7, r3)
            goto L_0x002f
        L_0x0042:
            r0 = move-exception
            r1 = r2
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.download.http.AsyncHttpResponseHandler.sendResponseMessage(org.apache.http.HttpResponse):void");
    }
}
