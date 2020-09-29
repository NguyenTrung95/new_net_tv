package net.sunniwell.sz.mop4.sdk.soap;

import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils */
public class SoapHttpUtils {
    private static final MyHostnameVerifier HOSTNAME_VERIFIER = new MyHostnameVerifier(null);
    private static final String TAG = "SoapHttps";
    private static X509TrustManager xtm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };
    private static X509TrustManager[] xtmArray = {xtm};

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$MyHostnameVerifier */
    private static class MyHostnameVerifier implements HostnameVerifier {
        private MyHostnameVerifier() {
        }

        /* synthetic */ MyHostnameVerifier(MyHostnameVerifier myHostnameVerifier) {
            this();
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$SoapResponse */
    public static class SoapResponse {
        public String body;
        public String epgs;
        public String epgsToken;
        public String location;
        public String ois;
        public String oisToken;
        public int statusCode;
    }

    public static SoapResponse post(String url, String content) {
        if (url == null || url.isEmpty() || !url.contains("https://")) {
            return httpPost(url, content);
        }
        return httpsPost(url, content);
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x0129  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils.SoapResponse httpPost(java.lang.String r16, java.lang.String r17) {
        /*
            r3 = 0
            r9 = 0
            net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$SoapResponse r10 = new net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$SoapResponse     // Catch:{ Exception -> 0x012f }
            r10.<init>()     // Catch:{ Exception -> 0x012f }
            byte[] r4 = r17.getBytes()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.net.URL r12 = new java.net.URL     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r0 = r16
            r12.<init>(r0)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.net.URLConnection r13 = r12.openConnection()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r0 = r13
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r3 = r0
            r13 = 5000(0x1388, float:7.006E-42)
            r3.setConnectTimeout(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r13 = 5000(0x1388, float:7.006E-42)
            r3.setReadTimeout(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r13 = "POST"
            r3.setRequestMethod(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r13 = 1
            r3.setDoOutput(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r13 = 1
            r3.setDoInput(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r13 = "Content-Type"
            java.lang.String r14 = "application/x-www-form-urlencoded;charset=UTF-8"
            r3.setRequestProperty(r13, r14)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r13 = "Content-Length"
            int r14 = r4.length     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r14 = java.lang.String.valueOf(r14)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r3.setRequestProperty(r13, r14)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r13 = "Connection"
            java.lang.String r14 = "close"
            r3.setRequestProperty(r13, r14)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r3.connect()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.io.OutputStream r8 = r3.getOutputStream()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r8.write(r4)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r8.flush()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r8.close()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            int r13 = r3.getResponseCode()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r10.statusCode = r13     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            int r13 = r10.statusCode     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r14 = 302(0x12e, float:4.23E-43)
            if (r13 != r14) goto L_0x0074
            java.lang.String r13 = "Location"
            java.lang.String r13 = r3.getHeaderField(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r10.location = r13     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
        L_0x006d:
            if (r3 == 0) goto L_0x0072
            r3.disconnect()
        L_0x0072:
            r9 = r10
        L_0x0073:
            return r9
        L_0x0074:
            int r13 = r10.statusCode     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r14 = 200(0xc8, float:2.8E-43)
            if (r13 != r14) goto L_0x010d
            int r13 = r16.length()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            int r13 = r13 + -5
            r0 = r16
            java.lang.String r7 = r0.substring(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r7 == 0) goto L_0x00d8
            java.lang.String r13 = "login"
            boolean r13 = r7.equalsIgnoreCase(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r13 == 0) goto L_0x00d8
            java.lang.String r13 = "Token"
            java.lang.String r11 = r3.getHeaderField(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r11 == 0) goto L_0x00a2
            java.lang.String r13 = ""
            boolean r13 = r11.equals(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r13 != 0) goto L_0x00a2
            r10.oisToken = r11     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
        L_0x00a2:
            java.lang.String r13 = "EPGS-Token"
            java.lang.String r11 = r3.getHeaderField(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r11 == 0) goto L_0x00b4
            java.lang.String r13 = ""
            boolean r13 = r11.equals(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r13 != 0) goto L_0x00b4
            r10.epgsToken = r11     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
        L_0x00b4:
            java.lang.String r13 = "OIS"
            java.lang.String r11 = r3.getHeaderField(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r11 == 0) goto L_0x00c6
            java.lang.String r13 = ""
            boolean r13 = r11.equals(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r13 != 0) goto L_0x00c6
            r10.ois = r11     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
        L_0x00c6:
            java.lang.String r13 = "EPGS"
            java.lang.String r11 = r3.getHeaderField(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r11 == 0) goto L_0x00d8
            java.lang.String r13 = ""
            boolean r13 = r11.equals(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r13 != 0) goto L_0x00d8
            r10.epgs = r11     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
        L_0x00d8:
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.io.InputStreamReader r13 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.io.InputStream r14 = r3.getInputStream()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r13.<init>(r14)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r1.<init>(r13)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            int r6 = r3.getContentLength()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            if (r6 <= 0) goto L_0x00fc
            char[] r2 = new char[r6]     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r13 = 0
            r1.read(r2, r13, r6)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r13 = java.lang.String.copyValueOf(r2)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r13 = r13.trim()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            r10.body = r13     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
        L_0x00fc:
            r1.close()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            goto L_0x006d
        L_0x0101:
            r5 = move-exception
            r9 = r10
        L_0x0103:
            r5.printStackTrace()     // Catch:{ all -> 0x012d }
            if (r3 == 0) goto L_0x0073
            r3.disconnect()
            goto L_0x0073
        L_0x010d:
            java.lang.String r13 = "SoapHttps"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r15 = "Soap error ,error code = "
            r14.<init>(r15)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            int r15 = r10.statusCode     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            java.lang.String r14 = r14.toString()     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            android.util.Log.d(r13, r14)     // Catch:{ Exception -> 0x0101, all -> 0x0125 }
            goto L_0x006d
        L_0x0125:
            r13 = move-exception
            r9 = r10
        L_0x0127:
            if (r3 == 0) goto L_0x012c
            r3.disconnect()
        L_0x012c:
            throw r13
        L_0x012d:
            r13 = move-exception
            goto L_0x0127
        L_0x012f:
            r5 = move-exception
            goto L_0x0103
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapHttpUtils.httpPost(java.lang.String, java.lang.String):net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$SoapResponse");
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x01a7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils.SoapResponse httpsPost(java.lang.String r20, java.lang.String r21) {
        /*
            r5 = 0
            r12 = 0
            net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$SoapResponse r13 = new net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$SoapResponse     // Catch:{ Exception -> 0x01ad }
            r13.<init>()     // Catch:{ Exception -> 0x01ad }
            byte[] r6 = r21.getBytes()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.net.URL r16 = new java.net.URL     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r16
            r1 = r20
            r0.<init>(r1)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.net.URLConnection r17 = r16.openConnection()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r17
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r5 = r0
            boolean r0 = r5 instanceof javax.net.ssl.HttpsURLConnection     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = r0
            if (r17 == 0) goto L_0x004f
            java.lang.String r17 = "TLS"
            javax.net.ssl.SSLContext r7 = javax.net.ssl.SSLContext.getInstance(r17)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = 0
            r0 = r17
            javax.net.ssl.KeyManager[] r0 = new javax.net.ssl.KeyManager[r0]     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = r0
            javax.net.ssl.X509TrustManager[] r18 = xtmArray     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.security.SecureRandom r19 = new java.security.SecureRandom     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r19.<init>()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r17
            r1 = r18
            r2 = r19
            r7.init(r0, r1, r2)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            javax.net.ssl.SSLSocketFactory r14 = r7.getSocketFactory()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r5.setSSLSocketFactory(r14)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$MyHostnameVerifier r17 = HOSTNAME_VERIFIER     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r17
            r5.setHostnameVerifier(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
        L_0x004f:
            r17 = 5000(0x1388, float:7.006E-42)
            r0 = r17
            r5.setConnectTimeout(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = 5000(0x1388, float:7.006E-42)
            r0 = r17
            r5.setReadTimeout(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.lang.String r17 = "POST"
            r0 = r17
            r5.setRequestMethod(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = 1
            r0 = r17
            r5.setDoOutput(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = 1
            r0 = r17
            r5.setDoInput(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.lang.String r17 = "Content-Type"
            java.lang.String r18 = "application/x-www-form-urlencoded;charset=UTF-8"
            r0 = r17
            r1 = r18
            r5.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.lang.String r17 = "Content-Length"
            int r0 = r6.length     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r18 = r0
            java.lang.String r18 = java.lang.String.valueOf(r18)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r17
            r1 = r18
            r5.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.lang.String r17 = "Connection"
            java.lang.String r18 = "close"
            r0 = r17
            r1 = r18
            r5.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r5.connect()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.io.OutputStream r11 = r5.getOutputStream()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r11.write(r6)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r11.flush()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r11.close()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            int r17 = r5.getResponseCode()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r17
            r13.statusCode = r0     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            int r0 = r13.statusCode     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = r0
            r18 = 302(0x12e, float:4.23E-43)
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x00cf
            java.lang.String r17 = "Location"
            r0 = r17
            java.lang.String r17 = r5.getHeaderField(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r17
            r13.location = r0     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
        L_0x00c8:
            if (r5 == 0) goto L_0x00cd
            r5.disconnect()
        L_0x00cd:
            r12 = r13
        L_0x00ce:
            return r12
        L_0x00cf:
            int r0 = r13.statusCode     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = r0
            r18 = 200(0xc8, float:2.8E-43)
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x0189
            int r17 = r20.length()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            int r17 = r17 + -5
            r0 = r20
            r1 = r17
            java.lang.String r10 = r0.substring(r1)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r10 == 0) goto L_0x014d
            java.lang.String r17 = "login"
            r0 = r17
            boolean r17 = r10.equalsIgnoreCase(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r17 == 0) goto L_0x014d
            java.lang.String r17 = "Token"
            r0 = r17
            java.lang.String r15 = r5.getHeaderField(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r15 == 0) goto L_0x010b
            java.lang.String r17 = ""
            r0 = r17
            boolean r17 = r15.equals(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r17 != 0) goto L_0x010b
            r13.oisToken = r15     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
        L_0x010b:
            java.lang.String r17 = "EPGS-Token"
            r0 = r17
            java.lang.String r15 = r5.getHeaderField(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r15 == 0) goto L_0x0121
            java.lang.String r17 = ""
            r0 = r17
            boolean r17 = r15.equals(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r17 != 0) goto L_0x0121
            r13.epgsToken = r15     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
        L_0x0121:
            java.lang.String r17 = "OIS"
            r0 = r17
            java.lang.String r15 = r5.getHeaderField(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r15 == 0) goto L_0x0137
            java.lang.String r17 = ""
            r0 = r17
            boolean r17 = r15.equals(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r17 != 0) goto L_0x0137
            r13.ois = r15     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
        L_0x0137:
            java.lang.String r17 = "EPGS"
            r0 = r17
            java.lang.String r15 = r5.getHeaderField(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r15 == 0) goto L_0x014d
            java.lang.String r17 = ""
            r0 = r17
            boolean r17 = r15.equals(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r17 != 0) goto L_0x014d
            r13.epgs = r15     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
        L_0x014d:
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.io.InputStreamReader r17 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.io.InputStream r18 = r5.getInputStream()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17.<init>(r18)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r17
            r3.<init>(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            int r9 = r5.getContentLength()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            if (r9 <= 0) goto L_0x0178
            char[] r4 = new char[r9]     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r17 = 0
            r0 = r17
            r3.read(r4, r0, r9)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.lang.String r17 = java.lang.String.copyValueOf(r4)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.lang.String r17 = r17.trim()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r0 = r17
            r13.body = r0     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
        L_0x0178:
            r3.close()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            goto L_0x00c8
        L_0x017d:
            r8 = move-exception
            r12 = r13
        L_0x017f:
            r8.printStackTrace()     // Catch:{ all -> 0x01ab }
            if (r5 == 0) goto L_0x00ce
            r5.disconnect()
            goto L_0x00ce
        L_0x0189:
            java.lang.String r17 = "SoapHttps"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.lang.String r19 = "Soap error ,error code = "
            r18.<init>(r19)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            int r0 = r13.statusCode     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            r19 = r0
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            java.lang.String r18 = r18.toString()     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            android.util.Log.d(r17, r18)     // Catch:{ Exception -> 0x017d, all -> 0x01a3 }
            goto L_0x00c8
        L_0x01a3:
            r17 = move-exception
            r12 = r13
        L_0x01a5:
            if (r5 == 0) goto L_0x01aa
            r5.disconnect()
        L_0x01aa:
            throw r17
        L_0x01ab:
            r17 = move-exception
            goto L_0x01a5
        L_0x01ad:
            r8 = move-exception
            goto L_0x017f
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.p008sz.mop4.sdk.soap.SoapHttpUtils.httpsPost(java.lang.String, java.lang.String):net.sunniwell.sz.mop4.sdk.soap.SoapHttpUtils$SoapResponse");
    }
}
