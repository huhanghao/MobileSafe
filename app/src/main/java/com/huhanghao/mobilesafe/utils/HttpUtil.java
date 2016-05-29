package com.huhanghao.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by huhanghao on 2016/4/4.
 */
public class HttpUtil {

    /** 发送get请求
     * @param urlString
     * @return
     */
    public static String get(String urlString)
    {
        String result = null;
        try {
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            // 进行数据传输
            if(200 == conn.getResponseCode())
            {
                InputStream input = conn.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len =input.read(buffer))!= -1)
                {
                    out.write(buffer,0,len);
                }
                out.flush();
                byte[] bytes = out.toByteArray();
                input.close();
                result = new String(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
