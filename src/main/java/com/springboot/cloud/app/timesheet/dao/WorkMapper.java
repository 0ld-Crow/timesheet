package com.springboot.cloud.app.timesheet.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.cloud.app.timesheet.entity.form.WorkForm;
import com.springboot.cloud.app.timesheet.entity.po.Work;
import com.springboot.cloud.app.timesheet.entity.vo.SummaryVo;
import com.springboot.cloud.app.timesheet.entity.vo.WorkVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Mapper
@Repository
public interface WorkMapper extends BaseMapper<Work> {

    @Update({
            "<script>",
            "UPDATE t_work SET isDelete = 1 WHERE ",
            "id in ",
            "<foreach collection=\"ids.split(',')\" item=\"item\" open=\"(\" separator=\",\" close=\")\">",
            "   #{item}",
            "</foreach>",
            "</script>"
    })
    public Integer batchDelete(@Param("ids")String ids);


    @Select({
            "<script>",
            "SELECT",
            "*",
            "FROM",
            "(",
            "    SELECT",
            "   group_concat(id) as ids,",
            "    workDate,",
            "    uId,",
            "    1 AS type",
            "    FROM",
            "    t_work",
            "    WHERE",
            "    isDelete = 0",
            "    GROUP BY",
            "    uid,",
            "    workDate UNION ALL",
            "    SELECT",
            "   0 as ids,",
            "    workDate,",
            "    uId,",
            "    0 AS type",
            "    FROM",
            "    t_unfilled",
            "    WHERE",
            "    isDelete = 0",
            "    GROUP BY",
            "    uid,",
            "    workDate",
            ") pp",
            "WHERE",
            "pp.workDate >= #{startWorkDate}",
//            "AND pp.uId = #{uid}",
            "ORDER BY workDate ASC",
            "</script>"
    })
    //@Param("uid")int uid,
    public List<SummaryVo> sevenDay( @Param("startWorkDate") String startWorkDate);


    @Select({
            "<script>",
            "SELECT * from t_work WHERE isDelete = 0 and ",
            "id in ",
            "<foreach collection=\"ids.split(',')\" item=\"item\" open=\"(\" separator=\",\" close=\")\">",
            "   #{item}",
            "</foreach>",
            "</script>"
    })
    public List<WorkVo> getWorksByIds(@Param("ids")String ids);
}