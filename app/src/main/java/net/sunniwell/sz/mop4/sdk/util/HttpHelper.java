package net.sunniwell.sz.mop4.sdk.util;

import java.net.URI;
import java.security.KeyStore;
import java.util.List;
import net.sunniwell.app.linktaro.nettv.Constants.PageEpgConstants;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

/* renamed from: net.sunniwell.sz.mop4.sdk.util.HttpHelper */
public class HttpHelper {
    private DefaultHttpClient mClient;
    private boolean mRedirect;
    private RedirectHandler mRedirectHandler = new RedirectHandler() {
        public boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
            return false;
        }

        public URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
            return null;
        }
    };
    private Object mSyncObject = new Object();

    public HttpHelper() {
    }

    public HttpHelper(boolean redirect) {
        this.mRedirect = redirect;
    }

    public boolean connect() {
        boolean ret;
        synchronized (this.mSyncObject) {
            ret = false;
            this.mClient = getHttpClient();
            if (this.mClient != null) {
                if (this.mRedirect) {
                    this.mClient.setRedirectHandler(this.mRedirectHandler);
                }
                ret = true;
            }
        }
        return ret;
    }

    public HttpResponse doGet(String url) {
        return doGet(url, null);
    }

    public HttpResponse doGet(String url, List<NameValuePair> dataList) {
        HttpResponse execute;
        synchronized (this.mSyncObject) {
            if (dataList != null) {
                int i = 0;
                while (i < dataList.size()) {
                    try {
                        NameValuePair pair = (NameValuePair) dataList.get(i);
                        if (i != 0 || url.indexOf("?") >= 0) {
                            url = new StringBuilder(String.valueOf(url)).append("&").append(pair.getName()).append("=").append(pair.getValue()).toString();
                        } else {
                            url = new StringBuilder(String.valueOf(url)).append("?").append(pair.getName()).append("=").append(pair.getValue()).toString();
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            execute = this.mClient.execute(new HttpGet(url));
        }
        return execute;
    }

    public HttpResponse doPost(String url) {
        return doPost(url, (String) null);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.http.HttpResponse doPost(java.lang.String r7, java.util.List<org.apache.http.message.BasicNameValuePair> r8) {
        /*
            r6 = this;
            java.lang.Object r4 = r6.mSyncObject
            monitor-enter(r4)
            org.apache.http.client.methods.HttpPost r2 = new org.apache.http.client.methods.HttpPost     // Catch:{ Exception -> 0x0021 }
            r2.<init>(r7)     // Catch:{ Exception -> 0x0021 }
            java.lang.String r3 = "Content-Type"
            java.lang.String r5 = "application/x-www-form-urlencoded;charset=UTF-8"
            r2.setHeader(r3, r5)     // Catch:{ Exception -> 0x0021 }
            org.apache.http.client.entity.UrlEncodedFormEntity r1 = new org.apache.http.client.entity.UrlEncodedFormEntity     // Catch:{ Exception -> 0x0021 }
            java.lang.String r3 = "UTF-8"
            r1.<init>(r8, r3)     // Catch:{ Exception -> 0x0021 }
            r2.setEntity(r1)     // Catch:{ Exception -> 0x0021 }
            org.apache.http.impl.client.DefaultHttpClient r3 = r6.mClient     // Catch:{ Exception -> 0x0021 }
            org.apache.http.HttpResponse r3 = r3.execute(r2)     // Catch:{ Exception -> 0x0021 }
            monitor-exit(r4)     // Catch:{ all -> 0x0028 }
        L_0x0020:
            return r3
        L_0x0021:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0028 }
            monitor-exit(r4)     // Catch:{ all -> 0x0028 }
            r3 = 0
            goto L_0x0020
        L_0x0028:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0028 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.util.HttpHelper.doPost(java.lang.String, java.util.List):org.apache.http.HttpResponse");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.http.HttpResponse doPost(java.lang.String r6, java.lang.String r7) {
        /*
            r5 = this;
            java.lang.Object r3 = r5.mSyncObject
            monitor-enter(r3)
            org.apache.http.client.methods.HttpPost r1 = new org.apache.http.client.methods.HttpPost     // Catch:{ Exception -> 0x0021 }
            r1.<init>(r6)     // Catch:{ Exception -> 0x0021 }
            java.lang.String r2 = "Content-Type"
            java.lang.String r4 = "application/x-www-form-urlencoded;charset=UTF-8"
            r1.setHeader(r2, r4)     // Catch:{ Exception -> 0x0021 }
            org.apache.http.entity.StringEntity r2 = new org.apache.http.entity.StringEntity     // Catch:{ Exception -> 0x0021 }
            java.lang.String r4 = "UTF-8"
            r2.<init>(r7, r4)     // Catch:{ Exception -> 0x0021 }
            r1.setEntity(r2)     // Catch:{ Exception -> 0x0021 }
            org.apache.http.impl.client.DefaultHttpClient r2 = r5.mClient     // Catch:{ Exception -> 0x0021 }
            org.apache.http.HttpResponse r2 = r2.execute(r1)     // Catch:{ Exception -> 0x0021 }
            monitor-exit(r3)     // Catch:{ all -> 0x0028 }
        L_0x0020:
            return r2
        L_0x0021:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0028 }
            monitor-exit(r3)     // Catch:{ all -> 0x0028 }
            r2 = 0
            goto L_0x0020
        L_0x0028:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0028 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.util.HttpHelper.doPost(java.lang.String, java.lang.String):org.apache.http.HttpResponse");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.http.HttpResponse doPost(java.lang.String r7, org.json.JSONObject r8) {
        /*
            r6 = this;
            java.lang.Object r4 = r6.mSyncObject
            monitor-enter(r4)
            org.apache.http.client.methods.HttpPost r1 = new org.apache.http.client.methods.HttpPost     // Catch:{ Exception -> 0x002a }
            r1.<init>(r7)     // Catch:{ Exception -> 0x002a }
            java.lang.String r3 = "Content-Type"
            java.lang.String r5 = "application/x-www-form-urlencoded;charset=UTF-8"
            r1.setHeader(r3, r5)     // Catch:{ Exception -> 0x002a }
            org.apache.http.entity.StringEntity r2 = new org.apache.http.entity.StringEntity     // Catch:{ Exception -> 0x002a }
            java.lang.String r3 = r8.toString()     // Catch:{ Exception -> 0x002a }
            java.lang.String r5 = "UTF-8"
            r2.<init>(r3, r5)     // Catch:{ Exception -> 0x002a }
            java.lang.String r3 = "application/json"
            r2.setContentType(r3)     // Catch:{ Exception -> 0x002a }
            r1.setEntity(r2)     // Catch:{ Exception -> 0x002a }
            org.apache.http.impl.client.DefaultHttpClient r3 = r6.mClient     // Catch:{ Exception -> 0x002a }
            org.apache.http.HttpResponse r3 = r3.execute(r1)     // Catch:{ Exception -> 0x002a }
            monitor-exit(r4)     // Catch:{ all -> 0x0031 }
        L_0x0029:
            return r3
        L_0x002a:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0031 }
            monitor-exit(r4)     // Catch:{ all -> 0x0031 }
            r3 = 0
            goto L_0x0029
        L_0x0031:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0031 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.util.HttpHelper.doPost(java.lang.String, org.json.JSONObject):org.apache.http.HttpResponse");
    }

    public void disconnect() {
        synchronized (this.mSyncObject) {
            if (this.mClient != null) {
                this.mClient.getConnectionManager().shutdown();
            }
        }
    }

    private DefaultHttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "ISO-8859-1");
            HttpProtocolParams.setUseExpectContinue(params, true);
            ConnManagerParams.setTimeout(params, 10000);
            HttpConnectionParams.setConnectionTimeout(params, PageEpgConstants.PZQB_HIDE_TIME);
            HttpConnectionParams.setSoTimeout(params, PageEpgConstants.PZQB_HIDE_TIME);
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
            schReg.register(new Scheme("https", sf, 443));
            return new DefaultHttpClient(new ThreadSafeClientConnManager(params, schReg), params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
