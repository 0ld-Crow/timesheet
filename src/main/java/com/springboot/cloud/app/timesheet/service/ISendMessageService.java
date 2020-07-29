package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * @ClassName ISendMessageService
 * @Description 获取企业微信发送信息通知
 * @Author ljc
 * @Date: 2019-11-27
 */
public interface ISendMessageService {


    public String sendMessage() throws Exception;

    public String sendWxMessage(JSONObject outputStr) throws Exception;

}
