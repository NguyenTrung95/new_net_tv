package net.sunniwell.app.linktaro.tools;

import android.util.Log;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLHandshakeException;

public class HttpClient4Utils {
    private static final int SSL_DEFAULT_PORT = 443;
    private static final String SSL_DEFAULT_SCHEME = "https";
    private static final String TAG = HttpClient4Utils.class.getSimpleName();
    private int connectTimeout = 5000;
    private int readTimeout = 8000;
    private HttpRequestInterceptor requestInterceptor;
    private HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= 3) {
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            if ((exception instanceof SSLHandshakeException) || (((HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST)) instanceof HttpEntityEnclosingRequest)) {
                return false;
            }
            return true;
        }
    };
    private HttpResponseInterceptor responseInterceptor;

    public DefaultHttpClient getDefaultHttpClient() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
        httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        httpclient.getParams().setIntParameter("http.connection.timeout", this.connectTimeout);
        httpclient.getParams().setIntParameter("http.socket.timeout", this.readTimeout);
        httpclient.setHttpRequestRetryHandler(this.requestRetryHandler);
        httpclient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler());
        httpclient.setRedirectHandler(new DefaultRedirectHandler());
        if (this.responseInterceptor != null) {
            httpclient.addResponseInterceptor(this.responseInterceptor);
        }
        if (this.requestInterceptor != null) {
            httpclient.addRequestInterceptor(this.requestInterceptor);
        }
        return httpclient;
    }

    public boolean checkNetwork(String url) {
        boolean flag = false;
        HttpClient client = new DefaultHttpClient();
        client.getParams().setIntParameter("http.socket.timeout", 5000);
        client.getParams().setIntParameter("http.connection.timeout", 5000);
        try {
            HttpGet request = new HttpGet(url);
            if (client.execute(request).getStatusLine().getStatusCode() == 200) {
                flag = true;
            }
            request.abort();
            return flag;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getHttpResponseResult(String url, Map<String, String> params) {
        String ret = XmlPullParser.NO_NAMESPACE;
        try {
            if (StringUtils.isNotEmpty(url)) {
                return getMethodString(url.trim(), params);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:27:0x0092=Splitter:B:27:0x0092, B:20:0x0084=Splitter:B:20:0x0084} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getMethod(java.lang.String r10, java.util.Map<java.lang.String, java.lang.String> r11) throws java.lang.Exception {
        /*
            r9 = this;
            boolean r6 = net.sunniwell.app.linktaro.tools.StringUtils.isBlank(r10)
            if (r6 == 0) goto L_0x000e
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "请求url为空!"
            r6.<init>(r7)
            throw r6
        L_0x000e:
            java.util.List r5 = r9.getParamsList(r11)
            if (r5 == 0) goto L_0x003f
            int r6 = r5.size()
            if (r6 <= 0) goto L_0x003f
            java.lang.String r6 = "UTF-8"
            java.lang.String r1 = org.apache.http.client.utils.URLEncodedUtils.format(r5, r6)
            java.lang.String r6 = "?"
            int r6 = r10.indexOf(r6)
            if (r6 >= 0) goto L_0x0064
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = java.lang.String.valueOf(r10)
            r6.<init>(r7)
            java.lang.String r7 = "?"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r1)
            java.lang.String r10 = r6.toString()
        L_0x003f:
            java.lang.String r6 = TAG
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = "...url...========"
            r7.<init>(r8)
            java.lang.StringBuilder r7 = r7.append(r10)
            java.lang.String r7 = r7.toString()
            android.util.Log.d(r6, r7)
            org.apache.http.impl.client.DefaultHttpClient r4 = r9.getDefaultHttpClient()
            r2 = 0
            org.apache.http.client.methods.HttpGet r3 = new org.apache.http.client.methods.HttpGet     // Catch:{ ClientProtocolException -> 0x0083, IOException -> 0x0091 }
            r3.<init>(r10)     // Catch:{ ClientProtocolException -> 0x0083, IOException -> 0x0091 }
            r4.execute(r3)     // Catch:{ ClientProtocolException -> 0x00a0, IOException -> 0x009d, all -> 0x009a }
            r9.abortConnection(r3, r4)
            return
        L_0x0064:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r7 = 0
            java.lang.String r8 = "?"
            int r8 = r10.indexOf(r8)
            int r8 = r8 + 1
            java.lang.String r7 = r10.substring(r7, r8)
            java.lang.String r7 = java.lang.String.valueOf(r7)
            r6.<init>(r7)
            java.lang.StringBuilder r6 = r6.append(r1)
            java.lang.String r10 = r6.toString()
            goto L_0x003f
        L_0x0083:
            r0 = move-exception
        L_0x0084:
            net.sunniwell.app.linktaro.tools.NetServiceException r6 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x008c }
            java.lang.String r7 = "客户端连接协议错误"
            r6.<init>(r7, r0)     // Catch:{ all -> 0x008c }
            throw r6     // Catch:{ all -> 0x008c }
        L_0x008c:
            r6 = move-exception
        L_0x008d:
            r9.abortConnection(r2, r4)
            throw r6
        L_0x0091:
            r0 = move-exception
        L_0x0092:
            net.sunniwell.app.linktaro.tools.NetServiceException r6 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x008c }
            java.lang.String r7 = "IO操作异常"
            r6.<init>(r7, r0)     // Catch:{ all -> 0x008c }
            throw r6     // Catch:{ all -> 0x008c }
        L_0x009a:
            r6 = move-exception
            r2 = r3
            goto L_0x008d
        L_0x009d:
            r0 = move-exception
            r2 = r3
            goto L_0x0092
        L_0x00a0:
            r0 = move-exception
            r2 = r3
            goto L_0x0084
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.HttpClient4Utils.getMethod(java.lang.String, java.util.Map):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00cc  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x00c1=Splitter:B:22:0x00c1, B:31:0x00d4=Splitter:B:31:0x00d4} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getMethodString(java.lang.String r14, java.util.Map<java.lang.String, java.lang.String> r15) throws java.lang.Exception {
        /*
            r13 = this;
            java.lang.String r9 = ""
            r6 = 0
            boolean r10 = net.sunniwell.app.linktaro.tools.StringUtils.isBlank(r14)
            if (r10 == 0) goto L_0x0011
            java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
            java.lang.String r11 = "请求url为空!"
            r10.<init>(r11)
            throw r10
        L_0x0011:
            java.util.List r7 = r13.getParamsList(r15)
            if (r7 == 0) goto L_0x0056
            int r10 = r7.size()
            if (r10 <= 0) goto L_0x0056
            java.lang.String r10 = "UTF-8"
            java.lang.String r2 = org.apache.http.client.utils.URLEncodedUtils.format(r7, r10)
            java.lang.String r10 = TAG
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r12 = "__formatParams__"
            r11.<init>(r12)
            java.lang.StringBuilder r11 = r11.append(r2)
            java.lang.String r11 = r11.toString()
            android.util.Log.d(r10, r11)
            java.lang.String r10 = "?"
            int r10 = r14.indexOf(r10)
            if (r10 >= 0) goto L_0x00a1
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = java.lang.String.valueOf(r14)
            r10.<init>(r11)
            java.lang.String r11 = "?"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r2)
            java.lang.String r14 = r10.toString()
        L_0x0056:
            java.lang.String r10 = TAG
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r12 = "...url...========"
            r11.<init>(r12)
            java.lang.StringBuilder r11 = r11.append(r14)
            java.lang.String r11 = r11.toString()
            android.util.Log.d(r10, r11)
            org.apache.http.impl.client.DefaultHttpClient r5 = r13.getDefaultHttpClient()
            org.apache.http.params.HttpParams r10 = r5.getParams()
            java.lang.String r11 = "http.socket.timeout"
            r12 = 15000(0x3a98, float:2.102E-41)
            r10.setIntParameter(r11, r12)
            org.apache.http.params.HttpParams r10 = r5.getParams()
            java.lang.String r11 = "http.connection.timeout"
            r12 = 8000(0x1f40, float:1.121E-41)
            r10.setIntParameter(r11, r12)
            r3 = 0
            org.apache.http.client.methods.HttpGet r4 = new org.apache.http.client.methods.HttpGet     // Catch:{ ClientProtocolException -> 0x00c0, IOException -> 0x00d3 }
            r4.<init>(r14)     // Catch:{ ClientProtocolException -> 0x00c0, IOException -> 0x00d3 }
            org.apache.http.HttpResponse r8 = r5.execute(r4)     // Catch:{ ClientProtocolException -> 0x00e2, IOException -> 0x00df, all -> 0x00dc }
            org.apache.http.HttpEntity r1 = r8.getEntity()     // Catch:{ ClientProtocolException -> 0x00e2, IOException -> 0x00df, all -> 0x00dc }
            java.lang.String r10 = "UTF-8"
            java.lang.String r9 = org.apache.http.util.EntityUtils.toString(r1, r10)     // Catch:{ ClientProtocolException -> 0x00e2, IOException -> 0x00df, all -> 0x00dc }
            if (r6 == 0) goto L_0x009d
            r6.close()
        L_0x009d:
            r13.abortConnection(r4, r5)
            return r9
        L_0x00a1:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r11 = 0
            java.lang.String r12 = "?"
            int r12 = r14.indexOf(r12)
            int r12 = r12 + 1
            java.lang.String r11 = r14.substring(r11, r12)
            java.lang.String r11 = java.lang.String.valueOf(r11)
            r10.<init>(r11)
            java.lang.StringBuilder r10 = r10.append(r2)
            java.lang.String r14 = r10.toString()
            goto L_0x0056
        L_0x00c0:
            r0 = move-exception
        L_0x00c1:
            net.sunniwell.app.linktaro.tools.NetServiceException r10 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x00c9 }
            java.lang.String r11 = "客户端连接协议错误"
            r10.<init>(r11, r0)     // Catch:{ all -> 0x00c9 }
            throw r10     // Catch:{ all -> 0x00c9 }
        L_0x00c9:
            r10 = move-exception
        L_0x00ca:
            if (r6 == 0) goto L_0x00cf
            r6.close()
        L_0x00cf:
            r13.abortConnection(r3, r5)
            throw r10
        L_0x00d3:
            r0 = move-exception
        L_0x00d4:
            net.sunniwell.app.linktaro.tools.NetServiceException r10 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x00c9 }
            java.lang.String r11 = "IO操作异常"
            r10.<init>(r11, r0)     // Catch:{ all -> 0x00c9 }
            throw r10     // Catch:{ all -> 0x00c9 }
        L_0x00dc:
            r10 = move-exception
            r3 = r4
            goto L_0x00ca
        L_0x00df:
            r0 = move-exception
            r3 = r4
            goto L_0x00d4
        L_0x00e2:
            r0 = move-exception
            r3 = r4
            goto L_0x00c1
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.HttpClient4Utils.getMethodString(java.lang.String, java.util.Map):java.lang.String");
    }

    public void postMethod(String url, Map<String, String> params) throws Exception {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("请求url为空!");
        }
        UrlEncodedFormEntity formEntity = null;
        if (params != null) {
            try {
                formEntity = new UrlEncodedFormEntity(getParamsList(params), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new NetServiceException("不支持的编码集", e);
            }
        }
        DefaultHttpClient httpClient = getDefaultHttpClient();
        HttpPost hp = new HttpPost(url);
        if (formEntity != null) {
            hp.setEntity(formEntity);
        }
        try {
            httpClient.execute(hp);
            abortConnection(hp, httpClient);
        } catch (ClientProtocolException e2) {
            throw new NetServiceException("客户端连接协议错误", e2);
        } catch (IOException e3) {
            throw new NetServiceException("IO操作异常", e3);
        } catch (Throwable th) {
            abortConnection(hp, httpClient);
            throw th;
        }
    }

    public String postMethodString(String url, Map<String, String> params) throws Exception {
        String str = XmlPullParser.NO_NAMESPACE;
        InputStream inStream = null;
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("请求url为空!");
        }
        UrlEncodedFormEntity formEntity = null;
        if (params != null) {
            try {
                formEntity = new UrlEncodedFormEntity(getParamsList(params), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new NetServiceException("不支持的编码集", e);
            }
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        if (formEntity != null) {
            post.setEntity(formEntity);
        }
        try {
            String ret = EntityUtils.toString(httpClient.execute(post).getEntity(), "UTF-8");
            if (inStream != null) {
                inStream.close();
            }
            abortConnection(post, httpClient);
            return ret;
        } catch (ClientProtocolException e2) {
            throw new NetServiceException("客户端连接协议错误", e2);
        } catch (IOException e3) {
            throw new NetServiceException("IO操作异常", e3);
        } catch (Throwable th) {
            if (inStream != null) {
                inStream.close();
            }
            abortConnection(post, httpClient);
            throw th;
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:28:0x0075=Splitter:B:28:0x0075, B:21:0x0067=Splitter:B:21:0x0067} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void postMethod(java.lang.String r14, java.util.Map<java.lang.String, java.lang.String> r15, java.net.URL r16, java.lang.String r17, java.net.URL r18, java.lang.String r19) throws java.lang.Exception {
        /*
            r13 = this;
            boolean r11 = net.sunniwell.app.linktaro.tools.StringUtils.isBlank(r14)
            if (r11 == 0) goto L_0x000e
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.String r12 = "请求url为空!"
            r11.<init>(r12)
            throw r11
        L_0x000e:
            r3 = 0
            if (r15 == 0) goto L_0x001c
            org.apache.http.client.entity.UrlEncodedFormEntity r3 = new org.apache.http.client.entity.UrlEncodedFormEntity     // Catch:{ UnsupportedEncodingException -> 0x005d }
            java.util.List r11 = r13.getParamsList(r15)     // Catch:{ UnsupportedEncodingException -> 0x005d }
            java.lang.String r12 = "UTF-8"
            r3.<init>(r11, r12)     // Catch:{ UnsupportedEncodingException -> 0x005d }
        L_0x001c:
            org.apache.http.impl.client.DefaultHttpClient r6 = r13.getDefaultHttpClient()
            r4 = 0
            r0 = r16
            r1 = r17
            java.security.KeyStore r7 = r13.createKeyStore(r0, r1)     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            r0 = r18
            r1 = r17
            java.security.KeyStore r10 = r13.createKeyStore(r0, r1)     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            org.apache.http.conn.ssl.SSLSocketFactory r9 = new org.apache.http.conn.ssl.SSLSocketFactory     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            r0 = r17
            r9.<init>(r7, r0, r10)     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            org.apache.http.conn.scheme.Scheme r8 = new org.apache.http.conn.scheme.Scheme     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            java.lang.String r11 = "https"
            r12 = 443(0x1bb, float:6.21E-43)
            r8.<init>(r11, r9, r12)     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            org.apache.http.conn.ClientConnectionManager r11 = r6.getConnectionManager()     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            org.apache.http.conn.scheme.SchemeRegistry r11 = r11.getSchemeRegistry()     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            r11.register(r8)     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            org.apache.http.client.methods.HttpPost r5 = new org.apache.http.client.methods.HttpPost     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            r5.<init>(r14)     // Catch:{ NoSuchAlgorithmException -> 0x0066, KeyStoreException -> 0x0074, CertificateException -> 0x007d, FileNotFoundException -> 0x0086, IOException -> 0x008f, UnrecoverableKeyException -> 0x0098, KeyManagementException -> 0x00a1 }
            if (r3 == 0) goto L_0x0056
            r5.setEntity(r3)     // Catch:{ NoSuchAlgorithmException -> 0x00bf, KeyStoreException -> 0x00bc, CertificateException -> 0x00b9, FileNotFoundException -> 0x00b6, IOException -> 0x00b3, UnrecoverableKeyException -> 0x00b0, KeyManagementException -> 0x00ad, all -> 0x00aa }
        L_0x0056:
            r6.execute(r5)     // Catch:{ NoSuchAlgorithmException -> 0x00bf, KeyStoreException -> 0x00bc, CertificateException -> 0x00b9, FileNotFoundException -> 0x00b6, IOException -> 0x00b3, UnrecoverableKeyException -> 0x00b0, KeyManagementException -> 0x00ad, all -> 0x00aa }
            r13.abortConnection(r5, r6)
            return
        L_0x005d:
            r2 = move-exception
            net.sunniwell.app.linktaro.tools.NetServiceException r11 = new net.sunniwell.app.linktaro.tools.NetServiceException
            java.lang.String r12 = "不支持的编码集"
            r11.<init>(r12, r2)
            throw r11
        L_0x0066:
            r2 = move-exception
        L_0x0067:
            net.sunniwell.app.linktaro.tools.NetServiceException r11 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x006f }
            java.lang.String r12 = "指定的加密算法不可用"
            r11.<init>(r12, r2)     // Catch:{ all -> 0x006f }
            throw r11     // Catch:{ all -> 0x006f }
        L_0x006f:
            r11 = move-exception
        L_0x0070:
            r13.abortConnection(r4, r6)
            throw r11
        L_0x0074:
            r2 = move-exception
        L_0x0075:
            net.sunniwell.app.linktaro.tools.NetServiceException r11 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x006f }
            java.lang.String r12 = "keytore解析异常"
            r11.<init>(r12, r2)     // Catch:{ all -> 0x006f }
            throw r11     // Catch:{ all -> 0x006f }
        L_0x007d:
            r2 = move-exception
        L_0x007e:
            net.sunniwell.app.linktaro.tools.NetServiceException r11 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x006f }
            java.lang.String r12 = "信任证书过期或解析异常"
            r11.<init>(r12, r2)     // Catch:{ all -> 0x006f }
            throw r11     // Catch:{ all -> 0x006f }
        L_0x0086:
            r2 = move-exception
        L_0x0087:
            net.sunniwell.app.linktaro.tools.NetServiceException r11 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x006f }
            java.lang.String r12 = "keystore文件不存在"
            r11.<init>(r12, r2)     // Catch:{ all -> 0x006f }
            throw r11     // Catch:{ all -> 0x006f }
        L_0x008f:
            r2 = move-exception
        L_0x0090:
            net.sunniwell.app.linktaro.tools.NetServiceException r11 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x006f }
            java.lang.String r12 = "I/O操作失败或中断 "
            r11.<init>(r12, r2)     // Catch:{ all -> 0x006f }
            throw r11     // Catch:{ all -> 0x006f }
        L_0x0098:
            r2 = move-exception
        L_0x0099:
            net.sunniwell.app.linktaro.tools.NetServiceException r11 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x006f }
            java.lang.String r12 = "keystore中的密钥无法恢复异常"
            r11.<init>(r12, r2)     // Catch:{ all -> 0x006f }
            throw r11     // Catch:{ all -> 0x006f }
        L_0x00a1:
            r2 = move-exception
        L_0x00a2:
            net.sunniwell.app.linktaro.tools.NetServiceException r11 = new net.sunniwell.app.linktaro.tools.NetServiceException     // Catch:{ all -> 0x006f }
            java.lang.String r12 = "处理密钥管理的操作异常"
            r11.<init>(r12, r2)     // Catch:{ all -> 0x006f }
            throw r11     // Catch:{ all -> 0x006f }
        L_0x00aa:
            r11 = move-exception
            r4 = r5
            goto L_0x0070
        L_0x00ad:
            r2 = move-exception
            r4 = r5
            goto L_0x00a2
        L_0x00b0:
            r2 = move-exception
            r4 = r5
            goto L_0x0099
        L_0x00b3:
            r2 = move-exception
            r4 = r5
            goto L_0x0090
        L_0x00b6:
            r2 = move-exception
            r4 = r5
            goto L_0x0087
        L_0x00b9:
            r2 = move-exception
            r4 = r5
            goto L_0x007e
        L_0x00bc:
            r2 = move-exception
            r4 = r5
            goto L_0x0075
        L_0x00bf:
            r2 = move-exception
            r4 = r5
            goto L_0x0067
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.HttpClient4Utils.postMethod(java.lang.String, java.util.Map, java.net.URL, java.lang.String, java.net.URL, java.lang.String):void");
    }

    /* JADX INFO: finally extract failed */
    private KeyStore createKeyStore(URL url, String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        if (url == null) {
            throw new IllegalArgumentException("Keystore url may not be null");
        }
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream is = null;
        try {
            InputStream is2 = url.openStream();
            keystore.load(is2, password != null ? password.toCharArray() : null);
            if (is2 != null) {
                is2.close();
            }
            return keystore;
        } catch (Throwable th) {
            if (is != null) {
                is.close();
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public List<NameValuePair> getParamsList(Map<String, String> paramsMap) {
        if (paramsMap == null || paramsMap.size() == 0) {
            return null;
        }
        List<NameValuePair> params = new ArrayList<>();
        for (Entry<String, String> map : paramsMap.entrySet()) {
            params.add(new BasicNameValuePair((String) map.getKey(), (String) map.getValue()));
            Log.d(TAG, "_____params___&&&&&&&" + ((String) map.getValue()));
        }
        return params;
    }

    /* access modifiers changed from: protected */
    public String getParamsList1(Map<String, String> paramsMap) {
        String paramss = null;
        if (paramsMap == null || paramsMap.size() == 0) {
            return null;
        }
        List<NameValuePair> params = new ArrayList<>();
        for (Entry<String, String> map : paramsMap.entrySet()) {
            params.add(new BasicNameValuePair((String) map.getKey(), (String) map.getValue()));
            Log.d(TAG, "_____params___&&&&&&&" + ((String) map.getValue()));
            paramss = new StringBuilder(String.valueOf((String) map.getKey())).append("=").append((String) map.getValue()).append("&").toString();
        }
        return paramss.substring(0, paramss.length() - 1);
    }

    private void abortConnection(HttpRequestBase hrb, HttpClient httpClient) {
        if (hrb != null) {
            hrb.abort();
        }
        if (httpClient != null) {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout2) {
        this.connectTimeout = connectTimeout2;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout2) {
        this.readTimeout = readTimeout2;
    }

    public HttpResponseInterceptor getResponseInterceptor() {
        return this.responseInterceptor;
    }

    public void setResponseInterceptor(HttpResponseInterceptor responseInterceptor2) {
        this.responseInterceptor = responseInterceptor2;
    }

    public HttpRequestInterceptor getRequestInterceptor() {
        return this.requestInterceptor;
    }

    public void setRequestInterceptor(HttpRequestInterceptor requestInterceptor2) {
        this.requestInterceptor = requestInterceptor2;
    }

    public String postMethodXMLString(String url, String xmlData) throws Exception {
        String str = XmlPullParser.NO_NAMESPACE;
        InputStream inStream = null;
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("请求url为空!");
        }
        StringEntity entity = new StringEntity(xmlData, "UTF-8");
        entity.setContentType("text/xml charset=utf-8");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        if (entity != null) {
            post.setEntity(entity);
        }
        try {
            String result = EntityUtils.toString(httpClient.execute(post).getEntity(), "UTF-8");
            if (inStream != null) {
                inStream.close();
            }
            abortConnection(post, httpClient);
            return result;
        } catch (ClientProtocolException e) {
            throw new NetServiceException("客户端连接协议错误", e);
        } catch (IOException e2) {
            throw new NetServiceException("IO操作异常", e2);
        } catch (Throwable th) {
            if (inStream != null) {
                inStream.close();
            }
            abortConnection(post, httpClient);
            throw th;
        }
    }
}
