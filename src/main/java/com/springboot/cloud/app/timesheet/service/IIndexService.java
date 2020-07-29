package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONObject;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.common.core.entity.po.Role;
import com.springboot.cloud.common.core.entity.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IIndexService {

    /**
     * 登入
    **/
    public UserVo signin(HttpServletRequest request, JSONObject param);

    /**
     * 重置/修改密码
    **/
    public boolean resetPassword(JSONObject param, Member member);

    /**
     * 获取角色列表
    **/
    public List<Role> getRoleList();


    /**
     * 企业微信授权登录
     **/
    public UserVo authorized(HttpServletRequest request, JSONObject param);
}
