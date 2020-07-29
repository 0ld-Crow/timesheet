package com.springboot.cloud.app.timesheet.service;


import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName IGetuserinfoService
 * @Description 企业微信授权登录
 */
public interface IGetuserinfoService {

    public String getuserinfo(String code) throws Exception;
}
