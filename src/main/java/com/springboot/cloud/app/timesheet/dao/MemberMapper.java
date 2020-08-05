package com.springboot.cloud.app.timesheet.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.app.timesheet.entity.vo.PersonWorkTimeDetailVo;
import com.springboot.cloud.common.core.entity.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface MemberMapper extends BaseMapper<Member> {


    @Select({
            "<script>",
            "SELECT",
            "   w.workDate, m.realName, p.name as projectName, w.hourTime, w.description",
            "FROM",
            "   t_work w JOIN t_project p ON w.pId = p.id JOIN t_member m ON w.uId = m.id",
            "WHERE",
            "   w.isDelete = 0 and p.isBan = 0 and p.isDelete = 0 and m.isBan = 0 and m.isDelete = 0",
            "<if test=\"param.workDateStart != null\">",
            "   AND w.workDate >= #{param.workDateStart} ",
            "</if>",
            "<if test=\"param.workDateEnd != null\">",
            "   AND #{param.workDateEnd} >= w.workDate",
            "</if>",
            "   ORDER BY w.workDate",
            "<if test=\"param.offset != null and param.offset != ''\">",
            "   LIMIT #{param.offset},#{max}",
            "</if>",
            "</script>"
    })
    public List<PersonWorkTimeDetailVo> personProjectDetail(@Param("param")JSONObject json);

    @Update({
            "<script>",
            "UPDATE t_member SET isDelete = 1 WHERE ",
            "id in ",
            "<foreach collection=\"ids.split(',')\" item=\"item\" open=\"(\" separator=\",\" close=\")\">",
            "   #{item}",
            "</foreach>",
            "</script>"
    })
    public Integer batchDelete(@Param("ids")String ids);

    @Select({
            "<script>",
            "select * from t_member where username = #{username} limit 1",
            "</script>"
    })
    public Member getUserByUsername(@Param("username") String username);

    @Select({
            "<script>",
            "select * from t_member where phone = #{phone} limit 1",
            "</script>"
    })
    public Member getUserByPhone(@Param("phone") String phone);

    @Select({
            "<script>",
            "select * from t_member where id = #{id} limit 1",
            "</script>"
    })
    public Member getUserById(@Param("id") Long id);

    @Select({
            "<script>",
            "select * from t_member where roleId = #{roleId} and isDelete = 0",
            "</script>"
    })
    public List<Member> getUserByRoleId(@Param("roleId")Long id);

    @Select({
            "<script>",
            "select * from t_member where userId = #{userId} limit 1",
            "</script>"
    })
    public Member getUserByUserId(@Param("userId") String userId);
}