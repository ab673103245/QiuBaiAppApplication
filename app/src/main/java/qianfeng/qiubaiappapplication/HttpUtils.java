package qianfeng.qiubaiappapplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class HttpUtils {

    public static byte[] getHttp(String httpUrl)
    {
        HttpURLConnection con = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            URL url = new URL(httpUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5*1000);
            con.connect();
            if(con.getResponseCode() == 200)
            {
                is = con.getInputStream();
                int len = 0;
                byte[] b = new byte[1024];
                while((len = is.read(b))!=-1)
                {
                    bos.write(b,0,len);
                    bos.flush();
                }
                return bos.toByteArray();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
