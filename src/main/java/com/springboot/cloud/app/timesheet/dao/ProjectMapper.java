package com.springboot.cloud.app.timesheet.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.cloud.app.timesheet.entity.param.QueryParam;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.springboot.cloud.app.timesheet.entity.vo.ProjectWorkTimeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查询到某个项目的工时列表
     * 统计的截止时间为当前日期。禁用的项目截止日期为最后一个提交的日期
    **/
    @Select({
            "<script>",
            "SELECT",
            "   p.id AS pId,",
            "   p.NAME AS projectName,",
            "   MIN( w.workDate ) AS startDate,",
            "   (CASE WHEN p.isBan = 0 THEN (DATE_FORMAT( NOW(), '%Y-%m-%d' )) ELSE ( SELECT MIN( w1.workDate ) FROM t_work w1 WHERE w1.pId = p.id ) END ) AS endDate,",
            "   SUM( w.hourTime ) AS totalWorkDate,",
            "   ROUND(( SUM( w.hourTime )/ 8 ), 1 ) AS dayPerOne",
            "FROM",
            "   t_project p JOIN t_work w ON p.id = w.pId",
            "WHERE",
            "   w.isDelete = 0",
            "   AND p.isDelete = 0",
            "GROUP BY",
            "   p.id",
            "</script>"
    })
    public List<ProjectWorkTimeVo> getProjectWorkTime(@Param("param")JSONObject param);

    /**
     * 人员或岗位工时统计
    **/
    @Select({
            "<script>",
            "SELECT",
            "<if test=\"param.type == 'post'\">",
            "   m.job as postName,",
            "</if>",
            "<if test=\"param.type == 'person'\">",
            "   m.username as personName,",
            "</if>",
            "   SUM( w.hourTime ) AS totalWorkDate,",
            "   ROUND(( SUM( w.hourTime )/ 8 ), 1 ) AS dayPerOne",
            "FROM",
            "   t_project p",
            "   JOIN t_work w ON p.id = w.pId",
            "   JOIN t_member m ON m.id = w.uid",
            "WHERE",
            "   w.isDelete = 0 AND p.isBan = 0 AND p.isDelete = 0 AND m.isBan = 0 AND m.isDelete = 0",
            "<if test=\"param.pId != null and param.pId != ''\">",
            "   AND p.id = #{param.pId}",
            "</if>",
            "GROUP BY",
            "<if test=\"param.type == 'post'\">",
            "   m.job",
            "</if>",
            "<if test=\"param.type == 'person'\">",
            "   m.username",
            "</if>",
            "<if test=\"param.offset != null and param.offset != ''\">",
            "   LIMIT #{param.offset},#{max}",
            "</if>",
            "</script>"
    })
    public List<ProjectWorkTimeVo> getWorkTimeByPostOrPerson(@Param("param") JSONObject param);

    @Update({
            "<script>",
            "UPDATE t_project SET isDelete = 1 WHERE ",
            "id in ",
            "<foreach collection=\"ids.split(',')\" item=\"item\" open=\"(\" separator=\",\" close=\")\">",
            "   #{item}",
            "</foreach>",
            "</script>"
    })
    public Integer batchDelete(@Param("ids")String ids);
}