package com.springboot.cloud.app.timesheet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.cloud.app.timesheet.dao.JobMapper;
import com.springboot.cloud.app.timesheet.entity.po.Job;
import com.springboot.cloud.app.timesheet.service.IJobService;
import com.springboot.cloud.common.core.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName JobServiceImpl
 * @Description 岗位信息表 - 信息业务层实现
 */
@Service("jobService")
public class JobServiceImpl extends ServiceImpl<JobMapper, Job>  implements IJobService {

    @Autowired
    JobMapper jobMapper;

    /**
     * 查询岗位列表
     **/
    @Override
    public Map<String,Object> getJobList() {
        QueryWrapper<Job> wrapper = new QueryWrapper<>();
        int count = jobMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(jobMapper.selectList(wrapper),count);
    }

    /**
     * 分页查询岗位列表
     **/
    @Override
    public Map<String,Object> getJobListByPage(JSONObject param) {
        CommonUtil.defaultParam(param);
        QueryWrapper<Job> wrapper = queryJob(param);
        Page<Job> mPage = new Page<>(param.getLong("pageNum"),param.getLong("pageSize"));
        IPage<Job> page = page(mPage,wrapper);
        int count = jobMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(page.getRecords(),count);
    }

    private QueryWrapper<Job> queryJob(JSONObject param){
        List<Long> ids = new ArrayList<>();
        if (param.containsKey("ids") && StringUtils.isNotBlank(param.getString("ids"))){
            String idstr = param.getString("ids");
            ids.addAll(Stream.of(idstr.split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }
        if (param.containsKey("id")){
            ids.add(param.getLong("id"));
        }
        QueryWrapper<Job> wrapper = new QueryWrapper<>();
        wrapper.like(param.containsKey("name"),"name",param.getString("name"));
        wrapper.in(!ids.isEmpty(),"id",ids);
        wrapper.eq(param.containsKey("isBan"),"isBan",param.getInteger("isBan"));
        wrapper.eq(param.containsKey("isDelete"),"isDelete",param.getInteger("isDelete"));
        return wrapper;

    }
}