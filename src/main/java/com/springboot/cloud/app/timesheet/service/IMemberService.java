package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.cloud.app.timesheet.entity.form.MemberForm;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.app.timesheet.entity.vo.MemberVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IMemberService
 * @Description 用户信息表 - 抽象信息业务层接口
 */
public interface IMemberService extends IService<Member>{

    /**
     * 导出会用户时明细
    **/
    public void exportPersonProjectDetail(JSONObject json, HttpServletResponse response) throws Exception;

    /**
     * 新增一个用户
    **/
    public boolean savePerson(MemberForm memberForm);

    /**
     * 查询用户列表
     **/
    public Map<String,Object> getMemberList(JSONObject param);

    /**
     * 分页查询用户列表
     **/
    public Map<String,Object> getMemberListByPage(JSONObject param);

    /**
     * 批量删除用户
    **/
    public int batchDelete(JSONObject param);


    /**
     * 获取用户列表
    **/
    public Map<String,Object> getUserList(JSONObject param);


    /**
     * 更新一个用户的信息
    **/
    public boolean update(JSONObject param);

    /**
     * 导出用户的账号信息
    **/
    public void exportMember(JSONObject param,HttpServletResponse response)throws Exception;

    /**
     * 获取企业微信部门账号
     **/
    public boolean getUserList();
}
