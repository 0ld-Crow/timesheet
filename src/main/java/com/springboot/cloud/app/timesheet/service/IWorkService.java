package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.cloud.app.timesheet.entity.form.WorkForm;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.springboot.cloud.app.timesheet.entity.po.Work;

import com.springboot.cloud.app.timesheet.entity.vo.SummaryVo;
import com.springboot.cloud.app.timesheet.entity.vo.WorkVo;
import com.springboot.cloud.common.core.entity.vo.Result;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface IWorkService extends IService<Work>{

    /**
     * 批量删除工作记录表
     **/
    public int batchDelete(JSONObject param);

    /**
     * 新增1个工作记录表
     **/
    public boolean saveWork(JSONObject param) throws Exception;


    /**
     * 新增N个工作记录表
     **/
    public List<WorkVo> saveWorkList(JSONArray param) throws Exception;

    /**
     * 修改N个工作记录表
     **/
    public List<WorkVo> updateWorkList(JSONArray param) throws Exception;

    /**
     * 查询最近七天工作记录表汇总
     **/
    public List<SummaryVo> sevenDay() throws Exception;

    /**
     * 查询最近七天工作记录表汇总的某一天(或几天)情况
     **/
    public List<WorkVo> getWorksByIds(@RequestBody String ids) throws Exception;

    /**
     * 查询工作列表
     **/
    public Map<String,Object> getWorkList(JSONObject param);

    /**
     * 分页查询工作列表
     **/
    public Map<String,Object> getWorkListByPage(JSONObject param);

    /**
     * 通过模糊查询（日期，员工姓名，项目名称）查询work列表
     **/
    public Map<String,Object> getWorkListByDateUsernameProjectname(JSONObject param);


}
