package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * @ClassName ISendMessageService
 * @Description 获取企业微信发送信息通知
 */
public interface ISendMessageService {

    //发送信息
    public String sendMessage() throws Exception;
    //发送信息
    public String sendWxMessage(JSONObject outputStr) throws Exception;

}
