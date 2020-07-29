package com.springboot.cloud.app.timesheet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.cloud.app.timesheet.entity.po.AccessToken;
import com.springboot.cloud.app.timesheet.entity.vo.AccessTokenVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface AccessTokenMapper extends BaseMapper<AccessToken> {

    @Select({
            "<script>",
            "SELECT * from t_access_token WHERE type = #{type} limit 1",
            "</script>"
    })
    public AccessTokenVo getToken(@Param("type")Byte type);
}