package com.springboot.cloud.app.timesheet.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.springboot.cloud.app.timesheet.entity.po.AccessToken;
import com.springboot.cloud.app.timesheet.service.IAccessTokenService;
import com.springboot.cloud.app.timesheet.service.ISendMessageService;
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
 * @ClassName SendMessageServiceImpl
 * @Description 发现企业微信信息通知
 */
@Service("sendMessageService")
public class SendMessageServiceImpl implements ISendMessageService {

    @Autowired
    public IAccessTokenService accessTokenService;

    /**
     * 获取发送消息的请求
     */
    private static String getUserUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";


    //发送信息
    @Override
    public String sendMessage() throws Exception {
        JSONObject insertdata = new JSONObject();
        JSONObject content = new JSONObject();
        content.put("content","\"工时总结。点击<a href=\\\"http://www.baidu.com\\\">进行填写</a>，避免忘记填写。");
        insertdata.put("touser","LinJiancheng");//这里是接收信息的人员id（多个人用 |  隔开）
        insertdata.put("toparty","11");
        insertdata.put("msgtype","text");
        insertdata.put("agentid",1000005);//此处一定要与上面的自建应用AgentId对应
        insertdata.put("text",content);
        String res = this.sendWxMessage(insertdata);
        return res;
    }

    //发送信息
    @Override
    public String sendWxMessage(JSONObject outputStr) throws Exception {
        Byte type = new Byte("0");
        String access_token = accessTokenService.getToken(type);
        String requestUrl= getUserUrl + access_token;
        String requestMethod = "POST";
        StringBuffer buffer = null;
        //创建SSLContext
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = { new MyX509TrustManager() };
        //初始化
        sslContext.init(null, tm, new java.security.SecureRandom());
        ;
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
            os.write(outputStr.toString().getBytes("utf-8"));
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
        JSONObject json1 = JSONObject.parseObject(buffer.toString());
        String msg = json1 .getString("errmsg");
        return msg ;

    }
}
