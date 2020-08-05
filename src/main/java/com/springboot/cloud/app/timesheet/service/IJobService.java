package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.cloud.app.timesheet.entity.po.Job;

import java.util.Map;


public interface IJobService extends IService<Job>{

    /**
     * 查询岗位列表
     **/
    public Map<String,Object> getJobList();

    /**
     * 分页查询岗位列表
     **/
    public Map<String,Object> getJobListByPage(JSONObject param);
}
