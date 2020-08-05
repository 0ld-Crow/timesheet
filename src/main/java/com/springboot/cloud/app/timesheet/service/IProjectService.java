package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.cloud.app.timesheet.entity.param.QueryParam;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.springboot.cloud.app.timesheet.entity.vo.ProjectWorkTimeVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface IProjectService extends IService<Project>{
    /**
     * 查询到某个项目的工时列表
    **/
    public List<ProjectWorkTimeVo> projectWorkTime(JSONObject param);

    /**
     * 导出所有项目工时统计
    **/
    public void exportAllProjectWorkTime(JSONObject json, HttpServletResponse response) throws Exception;

    /**
     * 按岗位导出工时统计
    **/
    public void exportWorkTimeByPost(JSONObject json, HttpServletResponse response) throws Exception;

    /**
     * 按人导出工时统计
    **/
    public void exportWorkTimeByPerson(JSONObject json, HttpServletResponse response) throws Exception;

    /**
     * 查询到某个项目的工时列表明细
    **/
    public List<ProjectWorkTimeVo> projectWorkTimeDetail(JSONObject param);

    /**
     * 批量删除
    **/
    public void batchDelete(JSONObject param);

    /**
     * 查询项目列表
    **/
    public Map<String,Object> getProjectList(JSONObject param);


    /**
     * 查询所有项目的列表
     **/
    public List<Project> getAllProjectList();


    /**
     * 分页查询项目列表
    **/
    public Map<String,Object> getProjectListByPage(JSONObject param);
}
