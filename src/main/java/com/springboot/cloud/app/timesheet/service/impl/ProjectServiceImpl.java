package com.springboot.cloud.app.timesheet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.cloud.app.timesheet.dao.ProjectMapper;
import com.springboot.cloud.app.timesheet.entity.param.QueryParam;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.springboot.cloud.app.timesheet.entity.vo.ProjectWorkTimeVo;
import com.springboot.cloud.app.timesheet.service.IProjectService;
import com.springboot.cloud.common.core.util.CommonUtil;
import com.springboot.cloud.common.core.util.ObjectUtil;
import com.springboot.cloud.common.core.util.PoiExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project>  implements IProjectService {
    @Autowired
    ProjectMapper projectMapper;

    /**
     * 查询到某个项目的工时列表
     **/
    @Override
    public List<ProjectWorkTimeVo> projectWorkTime(JSONObject param) {
        CommonUtil.defaultParam(param);
        return projectMapper.getProjectWorkTime(param);
    }

    /**
     * 导出所有项目工时统计
     **/
    @Override
    public void exportAllProjectWorkTime(JSONObject json, HttpServletResponse response) throws Exception{
        String sheetName = "项目工时统计";
        String[] keys = {"projectName","startDate","endDate","totalWorkDate","dayPerOne"};
        String[] columns = {"项目名称","起始日期","截止日期","工时总计","人日总计"};
        List<ProjectWorkTimeVo> projectWorkTimeVos = projectMapper.getProjectWorkTime(new JSONObject());
        List<JSONObject> jsonObjects = ObjectUtil.obj2JsonList(projectWorkTimeVos);
        ByteArrayInputStream bais = PoiExcelUtil.exportExcel(jsonObjects,sheetName,keys,columns);
        PoiExcelUtil.writeToOutput(bais,response,sheetName);
    }

    /**
     * 按岗位导出工时统计
     **/
    @Override
    public void exportWorkTimeByPost(JSONObject json, HttpServletResponse response) throws Exception{
        String sheetName = "岗位工时统计";
        String[] keys = {"postName","totalWorkDate","dayPerOne"};
        String[] columns = {"岗位属性","工时总计","人日总计"};
        json.put("type","post");
        List<ProjectWorkTimeVo> workTimeVos = projectMapper.getWorkTimeByPostOrPerson(json);
        List<JSONObject> jsonObjects = ObjectUtil.obj2JsonList(workTimeVos);
        ByteArrayInputStream bais = PoiExcelUtil.exportExcel(jsonObjects,sheetName,keys,columns);
        PoiExcelUtil.writeToOutput(bais,response,sheetName);
    }

    /**
     * 按人导出工时统计
     **/
    @Override
    public void exportWorkTimeByPerson(JSONObject json, HttpServletResponse response) throws Exception {
        String sheetName = "人员工时统计";
        String[] keys = {"personName","totalWorkDate","dayPerOne"};
        String[] columns = {"人员","工时总计","人日总计"};
        json.put("type","person");
        List<ProjectWorkTimeVo> workTimeVos = projectMapper.getWorkTimeByPostOrPerson(json);
        List<JSONObject> jsonObjects = ObjectUtil.obj2JsonList(workTimeVos);
        ByteArrayInputStream bais = PoiExcelUtil.exportExcel(jsonObjects,sheetName,keys,columns);
        PoiExcelUtil.writeToOutput(bais,response,sheetName);
    }

    /**
     * 查询到某个项目的工时列表明细
     **/
    @Override
    public List<ProjectWorkTimeVo> projectWorkTimeDetail(JSONObject param) {
        CommonUtil.defaultParam(param);
        return projectMapper.getWorkTimeByPostOrPerson(param);
    }

    /**
     * 批量删除
     **/
    @Override
    public void batchDelete(JSONObject param) {
        String ids = param.getString("ids");
        projectMapper.batchDelete(ids);
    }

    /**
     * 查询项目列表
     **/
    @Override
    public Map<String,Object> getProjectList(JSONObject param) {
        QueryWrapper<Project> wrapper = queryProject(param);
        int count = projectMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(projectMapper.selectList(wrapper),count);
    }

    /**
     * 查询所有项目的列表
     **/
    @Override
    public List<Project> getAllProjectList() {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        return projectMapper.selectList(wrapper);
    }

    /**
     * 分页查询项目列表
     **/
    @Override
    public Map<String,Object> getProjectListByPage(JSONObject param) {
        CommonUtil.defaultParam(param);
        QueryWrapper<Project> wrapper = queryProject(param);
        Page<Project> mPage = new Page<>(param.getLong("pageNum"),param.getLong("pageSize"));
        IPage<Project> page = page(mPage,wrapper);
        int count = projectMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(page.getRecords(),count);
    }

    private QueryWrapper<Project> queryProject(JSONObject param){
        List<Long> ids = new ArrayList<>();
        if (param.containsKey("ids") || StringUtils.isNotBlank(param.getString("ids"))){
            String idstr = param.getString("ids");
            ids.addAll(Stream.of(idstr.split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }

        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        if (param.containsKey("id")){
            ids.add(param.getLong("id"));
        }
        wrapper.in(!ids.isEmpty(),"id",ids);
        wrapper.like(param.containsKey("name"),"name",param.getString("name"));
        wrapper.eq(param.containsKey("isBan"),"isBan",param.getInteger("isBan"));
        wrapper.eq(param.containsKey("isDelete"),"isDelete",param.getInteger("isDelete"));
        return wrapper;

    }

}