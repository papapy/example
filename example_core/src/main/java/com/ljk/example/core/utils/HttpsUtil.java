package com.ljk.example.core.utils;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author xkey  2019/12/20 10:01
 */
public class HttpsUtil {

    public static void main(String[] args) throws Exception {
        HttpsURLConnection urlConnection = getHttpsUrlConnection("https://win0h19irblo9t:4243/qps/ticket?Xrfkey=0123456789abcdef", "POST");
        urlConnection.setRequestProperty("X-Qlik-Xrfkey", "0123456789abcdef");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        setBytesToStream(urlConnection.getOutputStream(), "{ 'UserId':'administrator','UserDirectory':'WIN0H19IRBLO9T','Attributes': []}".getBytes());

//        HttpsURLConnection urlConnection = getHttpsUrlConnection("https://www.baidu.com", "GET");

        System.out.println(new String(getBytesFromStream(urlConnection.getInputStream())));
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static final class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private static HttpsURLConnection getHttpsUrlConnection(String uri, String method) throws Exception {
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream("E:\\DESKTOP-LLIADE5\\client.pfx"), "".toCharArray());
        KeyManagerFactory keymanagerfactory = KeyManagerFactory.getInstance("SunX509");
        keymanagerfactory.init(keystore, "".toCharArray());
        TrustManagerFactory trustmanagerfactory = TrustManagerFactory.getInstance("SunX509");
        trustmanagerfactory.init(keystore);


        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
//            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
            ctx.init(keymanagerfactory.getKeyManagers(), new TrustManager[]{new TrustAnyTrustManager()}, null);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SSLSocketFactory ssf = Objects.requireNonNull(ctx).getSocketFactory();

        URL url = new URL(uri);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        httpsConn.setSSLSocketFactory(ssf);
//        httpsConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
//        httpsConn.setRequestProperty("Authorization","username");
//        httpsConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        /*
        在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
        则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。
        策略可以是基于证书的或依赖于其他验证方案。
        当验证 URL 主机名使用的默认规则失败时使用这些回调。
         */
        httpsConn.setHostnameVerifier((arg0, arg1) -> true);
        httpsConn.setRequestMethod(method);
        httpsConn.setDoInput(true);
        httpsConn.setDoOutput(true);
        return httpsConn;
    }

    private static byte[] getBytesFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baas = new ByteArrayOutputStream();
        byte[] kb = new byte[1024];
        int len;
        while ((len = is.read(kb)) != -1) {
            baas.write(kb, 0, len);
        }
        byte[] bytes = baas.toByteArray();
        baas.close();
        is.close();
        return bytes;
    }

    private static void setBytesToStream(OutputStream os, byte[] bytes) throws IOException {
        ByteArrayInputStream baas = new ByteArrayInputStream(bytes);
        byte[] kb = new byte[1024];
        int len;
        while ((len = baas.read(kb)) != -1) {
            os.write(kb, 0, len);
        }
        os.flush();
        os.close();
        baas.close();
    }

    public static byte[] doGet(String uri) throws Exception {
        HttpsURLConnection httpsConn = getHttpsUrlConnection(uri, "GET");
        return getBytesFromStream(httpsConn.getInputStream());
    }

    public static byte[] doPost(String uri, String data) throws Exception {
        HttpsURLConnection httpsConn = getHttpsUrlConnection(uri, "POST");
        setBytesToStream(httpsConn.getOutputStream(), data.getBytes());
        return getBytesFromStream(httpsConn.getInputStream());
    }
}
