package com.springboot.cloud.app.timesheet.service;


import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName IDepartmentService
 * @Description 获取企业微信部门成员列表
 */
public interface IUserService {
    /**
     * 获取部门列表
     * */
    public JSONArray getUserList(String departmentId) throws Exception;
}
