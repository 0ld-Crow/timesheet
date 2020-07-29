package com.springboot.cloud.app.timesheet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.cloud.app.timesheet.dao.AccessTokenMapper;
import com.springboot.cloud.app.timesheet.entity.po.AccessToken;
import com.springboot.cloud.app.timesheet.entity.vo.AccessTokenVo;
import com.springboot.cloud.app.timesheet.service.IAccessTokenService;
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
import java.util.Date;


/**
 * @ClassName AccessTokenServiceImpl
 * @Description accessToken - 业务层实现
 */
@Service("accessTokenService")
public class AccessTokenServiceImpl extends ServiceImpl<AccessTokenMapper, AccessToken>  implements IAccessTokenService {

    @Autowired
    AccessTokenMapper accessTokenMapper;


    /**
     *  企业Id
     */
    private static String corpid = "wxb032377d516cc4a5";

    /**
     * secret管理组的凭证密钥
     */
    private static String corpsecret = "wT51SIKRwHGRiJ8qAFkF_obmV9mPmxN37WhWpHW3ots";


    /**
     * secret管理组的凭证密钥,通讯录凭证秘钥
     */
    private static String secret = "3L657cmHuCMldg-6KKbfg8UUX2RlUigCftLxPnYmatA";


    /**
     * 获取ToKen的请求,普通接口token获取地址
     */
    private static String GetTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+ corpsecret;


    /**
     * 获取ToKen的请求，部分接口token，通讯录凭证调用接口
     */
    private static String GetSendMsgTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+secret;

    @Override
    public String getToken(Byte type){
        AccessTokenVo accessTokenVo = new AccessTokenVo();
        accessTokenVo = accessTokenMapper.getToken(type);
        String accessToen = null;
        Long time= new Date().getTime() / 1000;
        if(accessTokenVo.getExpiresIn() > time){
            accessToen = accessTokenVo.getAccessToke();
        }else{
            try {
                JSONObject jsonObject;
                if(type == 0){
                    jsonObject = getAccessToken();
                }else{
                    jsonObject = getSendMsgToken();
                }
                AccessToken AccessTokenPo = new AccessToken();
                AccessTokenPo.setAccessToke(jsonObject.getString("access_token"));
                AccessTokenPo.setExpiresIn(time + jsonObject.getLong("expires_in"));
                AccessTokenPo.setId(accessTokenVo.getId());
                AccessTokenPo.setType(type);
                accessTokenMapper.updateById(AccessTokenPo);
                accessToen  = jsonObject.getString("access_token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return accessToen;
    }


    /**
     * 获取token,用于普通接口使用
     * */
    public static JSONObject getAccessToken() throws Exception {
        String requestUrl = GetTokenUrl;//注意与AgenId（若有） 对应使用
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
        return jsonObject;
    }


    /**
     * 获取token,用于通讯录接口使用
     * */
    public static JSONObject getSendMsgToken() throws Exception {
        String requestUrl = GetSendMsgTokenUrl;//注意与AgenId（若有） 对应使用
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
        return jsonObject;
    }


}