package com.springboot.cloud.app.timesheet.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.cloud.app.timesheet.entity.po.Unfilled;
import com.springboot.cloud.app.timesheet.entity.vo.TodayNoReportPersonVo;
import com.springboot.cloud.app.timesheet.entity.vo.UnfilledVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Mapper
@Repository
public interface UnfilledMapper extends BaseMapper<Unfilled> {

    @Select({
            "<script>",
            "select ",
            "   u.id,u.uId,u.workDate,u.attach,u.remark,m.realName as userName",
            "from ",
            "   t_unfilled u join t_member m on u.uid = m.id",
            "where u.isDelete = 0 and m.isBan = 0 and m.isDelete = 0",
            "   ORDER BY u.workDate",
            "<if test=\"param.offset != null and param.max != null\">",
            "   LIMIT #{param.offset},#{param.max}",
            "</if>",
            "</script>"
    })
    public List<UnfilledVo> getUnfillList(@Param("param")JSONObject param);


    @Select({
            "<script>",
            "select ",
            "   count(1)",
            "from ",
            "   t_unfilled u join t_member m on u.uid = m.id",
            "where u.isDelete = 0 and m.isBan = 0 and m.isDelete = 0",
            "</script>"
    })
    public int getUnfillListCount(@Param("param")JSONObject param);


    @Select({
            "<script>",
            "SELECT",
            "   m.id memberId,  m.realName",
            "FROM",
            "   t_member m",
            "WHERE",
            "   m.isBan = 0 and m.isDelete = 0",
            "   and (",
            "   SELECT",
            "       count( 1 )",
            "   FROM",
            "       t_work w",
            "   WHERE",
            "       w.uId = m.id AND w.workDate = DATE_FORMAT( date_add(curdate(), interval -1 day), '%Y-%m-%d' )) = 0",
            "</script>"
    })
    public List<TodayNoReportPersonVo> getNoReportPerson();



    @Update({
            "<script>",
            "UPDATE t_unfilled SET isDelete = 1 WHERE ",
            "workDate = #{workDate} and uId = #{uId}",
            "</script>"
    })
    public Integer deleteUnfilled(@Param("workDate") Date workDate,@Param("uId") Long uId);

}