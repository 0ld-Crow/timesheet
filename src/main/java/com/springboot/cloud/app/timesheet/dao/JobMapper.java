package com.springboot.cloud.app.timesheet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.cloud.app.timesheet.entity.po.Job;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface JobMapper extends BaseMapper<Job> {

}