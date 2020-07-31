package com.springboot.cloud.app.timesheet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.cloud.app.timesheet.entity.po.Permisson;
import com.springboot.cloud.common.core.entity.po.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PermissonMapper extends BaseMapper<Permisson> {

}
