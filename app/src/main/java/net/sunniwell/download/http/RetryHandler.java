package net.sunniwell.download.http;

import android.os.SystemClock;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import javax.net.ssl.SSLException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

class RetryHandler implements HttpRequestRetryHandler {
    private static final int RETRY_SLEEP_TIME_MILLIS = 1500;
    private static HashSet<Class<?>> exceptionBlacklist = new HashSet<>();
    private static HashSet<Class<?>> exceptionWhitelist = new HashSet<>();
    private final int maxRetries;

    static {
        exceptionWhitelist.add(NoHttpResponseException.class);
        exceptionWhitelist.add(UnknownHostException.class);
        exceptionWhitelist.add(SocketException.class);
        exceptionBlacklist.add(InterruptedIOException.class);
        exceptionBlacklist.add(SSLException.class);
    }

    public RetryHandler(int maxRetries2) {
        this.maxRetries = maxRetries2;
    }

    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        boolean sent;
        boolean retry = true;
        Boolean b = (Boolean) context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
        if (b == null || !b.booleanValue()) {
            sent = false;
        } else {
            sent = true;
        }
        if (executionCount > this.maxRetries) {
            retry = false;
        } else if (isInList(exceptionBlacklist, exception)) {
            retry = false;
        } else if (isInList(exceptionWhitelist, exception)) {
            retry = true;
        } else if (!sent) {
            retry = true;
        }
        if (retry) {
            if (((HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST)).getMethod().equals(HttpPost.METHOD_NAME)) {
                retry = false;
            } else {
                retry = true;
            }
        }
        if (retry) {
            SystemClock.sleep(1500);
        } else {
            exception.printStackTrace();
        }
        return retry;
    }

    /* access modifiers changed from: protected */
    public boolean isInList(HashSet<Class<?>> list, Throwable error) {
        Iterator<Class<?>> itr = list.iterator();
        while (itr.hasNext()) {
            if (((Class) itr.next()).isInstance(error)) {
                return true;
            }
        }
        return false;
    }
}
