import com.ljk.example.core.dht.*;
import com.ljk.example.core.utils.HttpUtils;
import com.ljk.example.core.utils.HttpsUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * @author xkey  2019/6/27 11:02
 */
public class Test {

    public static void main(String[] args) throws Exception {
        HttpURLConnection httpsConn = (HttpURLConnection) new URL("http://fr.ur.com.cn:3000/api/session").openConnection();
        httpsConn.setRequestMethod("POST");
        httpsConn.setDoInput(true);
        httpsConn.setDoOutput(true);
        httpsConn.setRequestProperty("Accept", "application/json");
        httpsConn.setRequestProperty("Content-Type", "application/json");
//        httpsConn.setRequestProperty("Origin", "http://fr.ur.com.cn:3000");
//        httpsConn.setRequestProperty("Referer", "http://fr.ur.com.cn:3000/auth/login");

        String params = "{\"username\": \"jaden.zhang@analyticservice.net\", \"password\": \"1qaz@WSX\"}";
        OutputStream os = httpsConn.getOutputStream();
        ByteArrayInputStream baas = new ByteArrayInputStream(params.getBytes());
        byte[] kb = new byte[1024];
        int len;
        while ((len = baas.read(kb)) != -1) {
            os.write(kb, 0, len);
        }
        os.flush();
        os.close();
        baas.close();

        InputStream is = httpsConn.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = is.read(kb)) != -1) {
            byteArrayOutputStream.write(kb, 0, length);
        }
        System.out.println(new String(byteArrayOutputStream.toByteArray()));
        byteArrayOutputStream.close();
        is.close();
    }
}
