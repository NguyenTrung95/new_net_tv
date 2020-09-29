package net.sunniwell.sz.mop4.sdk.util;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.sunniwell.app.linktaro.nettv.Constants.PageEpgConstants;
import org.apache.commons.net.imap.IMAPSClient;
import org.apache.http.conn.ssl.SSLSocketFactory;

/* renamed from: net.sunniwell.sz.mop4.sdk.util.SSLSocketFactoryEx */
public class SSLSocketFactoryEx extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance(IMAPSClient.DEFAULT_PROTOCOL);

    public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
        this.sslContext.init(null, new TrustManager[]{tm}, null);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        Socket sk = this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        sk.setSoTimeout(PageEpgConstants.PZQB_HIDE_TIME);
        return sk;
    }

    public Socket createSocket() throws IOException {
        Socket sk = this.sslContext.getSocketFactory().createSocket();
        sk.setSoTimeout(PageEpgConstants.PZQB_HIDE_TIME);
        return sk;
    }
}
