package com.springboot.cloud.app.timesheet.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.cloud.app.timesheet.service.IAccessTokenService;
import com.springboot.cloud.app.timesheet.service.IGetuserinfoService;
import com.springboot.cloud.app.timesheet.util.MyX509TrustManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

/**
 * @ClassName GetuserinfoServiceImpl
 * @Description 企业微信授权后，获取userid
 */
@Service("getuserinfoService")
public class GetuserinfoServiceImpl implements IGetuserinfoService {


    @Autowired
    public IAccessTokenService accessTokenService;
    /**
     * 获取部门列表的请求URL
     */
    private String getuserinfoUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=";

    /**
     * 获取部门列表
     * */
    @Override
    public String getuserinfo(String code) throws Exception {
        Byte type = new Byte("1");
        System.out.println("111111111111111111111111");
        String access_token = accessTokenService.getToken(type);
        System.out.println(access_token);
        String requestUrl = this.getuserinfoUrl + access_token + "&code=" + code;//请求url
        String requestMethod = "GET";
        String outputStr = null;
        StringBuffer buffer = null;
        //创建SSLContext
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = { new MyX509TrustManager() };
        //初始化
        sslContext.init(null, tm, new java.security.SecureRandom());
        //获取SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(requestUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(requestMethod);
        //设置当前实例使用的SSLSoctetFactory
        conn.setSSLSocketFactory(ssf);
        conn.connect();
        //往服务器端写内容
        if (null != outputStr) {
            OutputStream os = conn.getOutputStream();
            os.write(outputStr.getBytes("utf-8"));
            os.close();
        }
        //读取服务器端返回的内容
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        buffer = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        JSONObject jsonObject = JSONObject.parseObject(buffer.toString());
        System.out.println("222222222222222222222222");
        System.out.println(jsonObject);
        System.out.println("333333333333333333333333333");
        String userid = jsonObject.getString("userid");
        System.out.println(userid);
        return userid;
    }
}
