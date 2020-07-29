package com.springboot.cloud.app.timesheet.service;


import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName IDepartmentService
 * @Description 获取企业微信部门成员列表
 * @Author ljc
 * @Date: 2019-11-25
 */
public interface IUserService {

    public JSONArray getUserList(String departmentId) throws Exception;
}
