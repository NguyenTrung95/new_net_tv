package net.sunniwell.download.http;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

public class AsyncHttpRequest implements Runnable {
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount;
    private boolean isBinaryRequest;
    private final HttpUriRequest request;
    private final AsyncHttpResponseHandler responseHandler;

    public AsyncHttpRequest(AbstractHttpClient client2, HttpContext context2, HttpUriRequest request2, AsyncHttpResponseHandler responseHandler2) {
        this.client = client2;
        this.context = context2;
        this.request = request2;
        this.responseHandler = responseHandler2;
        if (responseHandler2 instanceof BinaryHttpResponseHandler) {
            this.isBinaryRequest = true;
        } else if (responseHandler2 instanceof FileHttpResponseHandler) {
            FileHttpResponseHandler fileHttpResponseHandler = (FileHttpResponseHandler) responseHandler2;
            File tempFile = fileHttpResponseHandler.getTempFile();
            if (tempFile.exists()) {
                long previousFileSize = tempFile.length();
                fileHttpResponseHandler.setPreviousFileSize(previousFileSize);
                this.request.setHeader("RANGE", "bytes=" + previousFileSize + "-");
            }
        }
    }

    public void run() {
        try {
            if (this.responseHandler != null) {
                this.responseHandler.sendStartMessage();
            }
            makeRequestWithRetries();
            if (this.responseHandler != null) {
                this.responseHandler.sendFinishMessage();
            }
        } catch (IOException e) {
            if (this.responseHandler != null) {
                this.responseHandler.sendFinishMessage();
                if (this.isBinaryRequest) {
                    this.responseHandler.sendFailureMessage((Throwable) e, (byte[]) null);
                } else {
                    this.responseHandler.sendFailureMessage((Throwable) e, (String) null);
                }
            }
        }
    }

    private void makeRequest() throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            try {
                HttpResponse response = this.client.execute(this.request, this.context);
                if (!Thread.currentThread().isInterrupted() && this.responseHandler != null) {
                    this.responseHandler.sendResponseMessage(response);
                }
            } catch (IOException e) {
                if (!Thread.currentThread().isInterrupted()) {
                    throw e;
                }
            }
        }
    }

    private void makeRequestWithRetries() throws ConnectException {
        boolean retry = true;
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = this.client.getHttpRequestRetryHandler();
        while (retry) {
            try {
                makeRequest();
                return;
            } catch (UnknownHostException e) {
                if (this.responseHandler != null) {
                    this.responseHandler.sendFailureMessage((Throwable) e, "can't resolve host");
                    return;
                }
                return;
            } catch (SocketException e2) {
                if (this.responseHandler != null) {
                    this.responseHandler.sendFailureMessage((Throwable) e2, "can't resolve host");
                    return;
                }
                return;
            } catch (SocketTimeoutException e3) {
                if (this.responseHandler != null) {
                    this.responseHandler.sendFailureMessage((Throwable) e3, "socket time out");
                    return;
                }
                return;
            } catch (IOException e4) {
                cause = e4;
                int i = this.executionCount + 1;
                this.executionCount = i;
                retry = retryHandler.retryRequest(cause, i, this.context);
            } catch (NullPointerException e5) {
                cause = new IOException("NPE in HttpClient" + e5.getMessage());
                int i2 = this.executionCount + 1;
                this.executionCount = i2;
                retry = retryHandler.retryRequest(cause, i2, this.context);
            }
        }
        ConnectException ex = new ConnectException();
        ex.initCause(cause);
        throw ex;
    }
}
