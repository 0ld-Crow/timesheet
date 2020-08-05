package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.cloud.app.timesheet.entity.form.MemberForm;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.app.timesheet.entity.vo.MemberVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface IMemberService extends IService<Member>{

    /**
     * 导出会员工时明细
    **/
    public void exportPersonProjectDetail(JSONObject json, HttpServletResponse response) throws Exception;

    /**
     * 新增一个员工
    **/
    public boolean savePerson(MemberForm memberForm);

    /**
     * 查询员工列表
     **/
    public Map<String,Object> getMemberList(JSONObject param);

    /**
     * 分页查询员工列表
     **/
    public Map<String,Object> getMemberListByPage(JSONObject param);

    /**
     * 批量删除员工
    **/
    public int batchDelete(JSONObject param);


    /**
     * 获取员工列表
    **/
    public Map<String,Object> getUserList(JSONObject param);


    /**
     * 更新一个员工的信息
    **/
    public boolean update(JSONObject param);

    /**
     * 导出员工的账号信息
    **/
    public void exportMember(JSONObject param,HttpServletResponse response)throws Exception;

    /**
     * 获取企业微信部门账号
     **/
    public boolean getUserList();
}
