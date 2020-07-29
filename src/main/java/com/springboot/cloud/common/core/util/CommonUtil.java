package com.springboot.cloud.common.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.springboot.cloud.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class CommonUtil {

    //数字校验
    private final static Pattern PATTERN_NUMBER           = Pattern.compile("^[0-9]+(.[0-9]+)?$");

    /**
     *
     * @Description 判断是否为数字格式不限制位数
     * @param obj 参数
     * @return 如果全为数字返回true;否则返回false
     */
    public static boolean isNumber(Object obj){
        if (obj == null) {
            return false;
        }
        return (PATTERN_NUMBER).matcher(String.valueOf(obj)).matches();
    }

    /**
     *
     * @Description 去掉小数后多余0
     * @param obj 参数
     * @return 处理后的结果
     */
    public static String trimZero(Object obj) {
        if (obj == null) {
            return null;
        }
        String str = String.valueOf(obj);
        if (str.indexOf(".") > 0) {
            //去掉多余的0
            str = str.replaceAll("0+?$", "");
            //如最后一位是.则去掉
            str = str.replaceAll("[.]$", "");
        }
        return str;
    }

    /**
     *
     * @Description base64加密
     * @param str 需要加密的内容
     * @return String 加密后的数据
     */
    public final static Map<String, Object> base64(String str) {
        Map<String, Object> map = new HashMap<String, Object>();
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedStrBase64 = new Sha256Hash(str, salt, 1024).toBase64();

        map.put("salt", salt);
        map.put("hashedStrBase64", hashedStrBase64);
        return map;
    }

    public final static String base64WithSalt(String str,String salt){
        if (StringUtils.isBlank(salt)){
            return str;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        return new Sha256Hash(str, salt, 1024).toBase64();
    }

    /**
     *
     * @Description 获取当前的session--shiro内置
     * @param key 名称
     * @return Object session值
     */
    public final static Object getSession(Object key){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser) {
            Session session = currentUser.getSession();
            if(null != session) {
                return session.getAttribute(key);
            }
        }
        return null;
    }

    public final static void setSession(Object key,Object value){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser) {
            Session session = currentUser.getSession();
            if(null != session) {
                //System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
                session.setAttribute(key, value);
            }
        }
    }

    /**
     * @Description 获取请求IP地址
     * @param request 请求内容
     * @return IP地址
     */
    public final static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            //获取本机的ip
            if ("127.0.0.1".equals(ip) || ip.endsWith("0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }

        //对于通过多个代理的情况,第一个IP为客户端真实IP
        if (StringUtils.isNotBlank(ip) && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        if (StringUtils.isBlank(ip)) {
            ip = "--";
        }
        return ip;
    }

    /**
     *
     * @Description 将对象转换成json字符串
     * @param obj 需要转换的数据
     * @return String 转换后的数据
     */
    public final static String toJSONString(Object obj) {
        TypeUtils.compatibleWithJavaBean = true; //和java bean 字段属性保持一致,否则首字符会被转化为小写
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect); //增加序列化防止过滤掉null, $ref属性
    }

    /**
     * 默认分页
    * @author llc
    * @Date 2019/11/13
    **/
    public static void defaultParam(JSONObject json){
        int pageNum = 1;
        int pageSize = 10;
        if (json.containsKey("pageSize")){
            pageSize = json.getInteger("pageSize");
            if (pageSize < 0)
                pageSize = 10;
        }
        if (json.containsKey("pageNum")){
            pageNum = json.getInteger("pageNum");
            if (pageNum <= 0)
                pageNum = 1;
        }
        int offset = pageSize * (pageNum - 1);
        json.put("offset",offset);
        json.put("max",pageSize);
        json.put("pageNum",pageNum);
        json.put("pageSize",pageSize);
    }

    public static String getCurrentUserId(){
        Member member = (Member) getSession("user");
        if (member != null){
            return member.getId().toString();
        }
        return "System";
    }


    public static String resultResponse(Result result){
        JSONObject json = new JSONObject();
        if (StringUtils.isNotBlank(result.getCode())){
            json.put("code",result.getCode());
        }
        return null;
    }

    public static Map<String,Object> wrapPageList(Collection list,int count){
        Map<String,Object> result = new HashMap<>();
        result.put("body",list);
        result.put("count",count);
        return result;
    }


    public static Long getLoginUserId(){
        Member member = (Member) getSession("user");
        if (member != null){
            return member.getId();
        }
        return 0L;
    }
}
