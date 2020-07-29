package com.springboot.cloud.app.timesheet.service;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName IDepartmentService
 * @Description 获取企业微信部门列表
 * @Author ljc
 * @Date: 2019-11-25
 */
public interface IDepartmentService{

    public JSONArray getDepartmentList() throws Exception;
}
