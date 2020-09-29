package net.sunniwell.download.http;

import android.os.Message;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class BinaryHttpResponseHandler extends AsyncHttpResponseHandler {
    private static String[] mAllowedContentTypes = {"image/jpeg", "image/png"};

    public BinaryHttpResponseHandler() {
    }

    public BinaryHttpResponseHandler(String[] allowedContentTypes) {
        this();
        mAllowedContentTypes = allowedContentTypes;
    }

    public void onSuccess(byte[] binaryData) {
    }

    public void onSuccess(int statusCode, byte[] binaryData) {
        onSuccess(binaryData);
    }

    @Deprecated
    public void onFailure(Throwable error, byte[] binaryData) {
        onFailure(error);
    }

    /* access modifiers changed from: protected */
    public void sendSuccessMessage(int statusCode, byte[] responseBody) {
        sendMessage(obtainMessage(4, new Object[]{Integer.valueOf(statusCode), responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(Throwable e, byte[] responseBody) {
        sendMessage(obtainMessage(1, new Object[]{e, responseBody}));
    }

    /* access modifiers changed from: protected */
    public void handleSuccessMessage(int statusCode, byte[] responseBody) {
        onSuccess(statusCode, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleFailureMessage(Throwable e, byte[] responseBody) {
        onFailure(e, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                Object[] response = (Object[]) msg.obj;
                handleFailureMessage((Throwable) response[0], response[1].toString());
                return;
            case 4:
                Object[] response2 = (Object[]) msg.obj;
                handleSuccessMessage(((Integer) response2[0]).intValue(), (byte[]) response2[1]);
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void sendResponseMessage(HttpResponse response) {
        StatusLine status = response.getStatusLine();
        Header[] contentTypeHeaders = response.getHeaders(HTTP.CONTENT_TYPE);
        byte[] responseBody = null;
        if (contentTypeHeaders.length != 1) {
            sendFailureMessage(new HttpResponseException(status.getStatusCode(), "None, or more than one, Content-Type Header found!"), null);
            return;
        }
        Header contentTypeHeader = contentTypeHeaders[0];
        boolean foundAllowedContentType = false;
        for (String anAllowedContentType : mAllowedContentTypes) {
            if (Pattern.matches(anAllowedContentType, contentTypeHeader.getValue())) {
                foundAllowedContentType = true;
            }
        }
        if (!foundAllowedContentType) {
            sendFailureMessage(new HttpResponseException(status.getStatusCode(), "Content-Type not allowed!"), null);
            return;
        }
        HttpEntity entity = null;
        try {
            HttpEntity temp = response.getEntity();
            if (temp != null) {
                entity = new BufferedHttpEntity(temp);
            }
            responseBody = EntityUtils.toByteArray(entity);
        } catch (IOException e) {
            sendFailureMessage(e, null);
        }
        if (status.getStatusCode() >= 300) {
            sendFailureMessage(new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), responseBody);
        } else {
            sendSuccessMessage(status.getStatusCode(), responseBody);
        }
    }
}
