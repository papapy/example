import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author xkey  2019/12/19 11:52
 */
public class QlikTicket {
    private static String xrfkey = "0123456789abcdef";

    public static String getTicket(String username) {
        String protocol = "https";
        System.out.println("protocol------------" + protocol);
        String defaultUserName = "username";
        HttpsURLConnection connection = null;
        OutputStreamWriter wr = null;
        BufferedReader in = null;
        if ("".equals(username) && !"".equals(defaultUserName)) {
            System.out.println("username is null, user defaultUserName");
            username = defaultUserName;
        }
        try {
            /************** BEGIN Certificate Acquisition **************/
            String cerPassword = "tykjtykj";
            String programPath = "D:\\zs\\";
            System.out.println("programPath" + programPath);
            File newFile = new File(programPath + "client.jks");
            File newFile1 = new File(programPath + "root.jks");
            String proxyCertPass = cerPassword;
            String rootCertPass = cerPassword;
            /************** END Certificate Acquisition **************/
            /************** BEGIN Certificate configuration for use in connection **************/
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(newFile), proxyCertPass.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, proxyCertPass.toCharArray());
//            KeyStore ksTrust = KeyStore.getInstance("JKS");
//            ksTrust.load(new FileInputStream(newFile1), rootCertPass.toCharArray());
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            tmf.init(ksTrust);
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(kmf.getKeyManagers(), new TrustManager[]{new TrustAnyTrustManager()}, null);
            SSLSocketFactory sslSocketFactory = context.getSocketFactory();
            /************** END Certificate configuration for use in connection **************/
            /************** BEGIN HTTPS Connection **************/

            String host = "qlik.tygps.com";
            String vproxy = "";
            String userDirectory = "WIN-1491M3TMI2C";
            String qpsUrl = protocol + "://" + host + ":4243/qps" + vproxy + "/ticket?xrfkey=" + xrfkey;
            System.out.println(qpsUrl);
            //Logger.info("qpsUrl------------" + qpsUrl);
            URL url = new URL(qpsUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslSocketFactory);
            connection.setRequestProperty("x-qlik-xrfkey", xrfkey);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setHostnameVerifier(new QlikTicket().new TrustAyHostnameVerifier());
            connection.setRequestMethod("POST");
            /************** BEGIN JSON Message to Qlik Sense Proxy API **************/
            String body = "{ 'UserId':'" + username + "','UserDirectory':'" + userDirectory + "',";
            body += "'Attributes': [],";
            body += "}";
            System.out.println("Request Body: " + body);
            /************** END JSON Message to Qlik Sense Proxy API **************/
            wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body);
            wr.flush(); //Get the response from the QPS BufferedReader
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            String data = builder.toString();
            System.out.println("The response from the server is: " + data);
            //Logger.info("The response from the server is: " + data);
//            JSONObject json = JSONObject.parseObject(data);
//            String ticket = json.getString("Ticket");
            connection.disconnect();
            /************** END HTTPS Connection **************/
            return data;
        } catch (Exception e) {
            //Logger.info("unknown error--->",e);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (wr != null) {
                try {
                    wr.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (null != connection) {
                connection.disconnect();
            }
        }
        return "";
    }

    public class TrustAyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    public static void main(String[] args) {
        String p = getTicket("ty002");
        System.out.println(p);
    }

}
