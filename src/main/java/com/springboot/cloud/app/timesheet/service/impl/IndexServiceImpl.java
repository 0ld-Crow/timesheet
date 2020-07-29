package com.springboot.cloud.app.timesheet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Objects;
import com.springboot.cloud.app.timesheet.dao.MemberMapper;
import com.springboot.cloud.app.timesheet.dao.RoleMapper;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.app.timesheet.service.IGetuserinfoService;
import com.springboot.cloud.app.timesheet.service.IIndexService;
import com.springboot.cloud.common.core.entity.po.Role;
import com.springboot.cloud.common.core.entity.po.User;
import com.springboot.cloud.common.core.entity.vo.UserVo;
import com.springboot.cloud.common.core.exception.AccountException;
import com.springboot.cloud.common.core.exception.SystemErrorType;
import com.springboot.cloud.common.core.util.CommonUtil;
import com.springboot.cloud.jwt.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("indexService")
public class IndexServiceImpl implements IIndexService {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    IGetuserinfoService getuserinfoService;

    /**
     * 登入
     **/
    @Override
    public UserVo signin(HttpServletRequest request,JSONObject param) {
        String username = param.getString("username");
        String password = param.getString("password");

        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            subject.login(token);
        }catch (Exception e){
            if (e instanceof IncorrectCredentialsException){
                throw new IncorrectCredentialsException("账号或密码错误！");
            }
            throw e;
        }
        //从session中获取到user（user和member一样，即用户都是会员）
        Member member = (Member) CommonUtil.getSession("user");

        if (member == null){
            throw new RuntimeException();
        }else {
            String accessToken = JwtUtil.sign(member.getUsername(),member.getPassword(),24 * 60 * 60 * 1000);
            String refreshToken = JwtUtil.sign(member.getUsername(),member.getPassword(),15 * 24 * 60 * 60 * 1000);
            Date expireTime = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            //更新存在数据库中该会员的信息
            member.setAccessToken(accessToken);
            member.setRefreshToken(refreshToken);
            member.setLoginIp(CommonUtil.getIpAddr(request));
            member.setLoginTime(new Date());
            member.setExpireTime(expireTime);
            memberMapper.updateById(member);
            //生成uservo返回给前端
            UserVo userVo = new UserVo();
            userVo.setId(member.getId());
            userVo.setName(member.getRealName());
            userVo.setMobile(member.getPhone());
            userVo.setEnabled(true);
            userVo.setAccessToken(accessToken);
            userVo.setRefreshToken(refreshToken);
            return userVo;
        }
    }

    /**
     * 企业微信授权登录
     **/
    @Override
    public UserVo authorized(HttpServletRequest request,JSONObject param) {
        String code = param.getString("code");
        String userId = null;
        try {
             userId = getuserinfoService.getuserinfo(code);
            Member memberOld = memberMapper.getUserByUserId(userId);
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(memberOld.getUsername(),userId);
            subject.login(token);
        }catch (Exception e){
            if (e instanceof IncorrectCredentialsException){
                throw new IncorrectCredentialsException("账号不存在系统中");
            }
            e.printStackTrace();
//            throw e;
        }
        Member member = (Member) CommonUtil.getSession("user");
        if (member == null){
            throw new RuntimeException();
        }else {
            String accessToken = JwtUtil.sign(member.getUsername(),member.getPassword(),24 * 60 * 60 * 1000);
            String refreshToken = JwtUtil.sign(member.getUsername(),member.getPassword(),15 * 24 * 60 * 60 * 1000);
            Date expireTime = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            member.setAccessToken(accessToken);
            member.setRefreshToken(refreshToken);
            member.setLoginIp(CommonUtil.getIpAddr(request));
            member.setLoginTime(new Date());
            member.setExpireTime(expireTime);
            memberMapper.updateById(member);

            UserVo userVo = new UserVo();
            userVo.setId(Long.parseLong(userId));
            userVo.setName(member.getRealName());
            userVo.setMobile(member.getPhone());
            userVo.setEnabled(true);
            userVo.setAccessToken(accessToken);
            userVo.setRefreshToken(refreshToken);
            return userVo;
        }
    }

    /**
     * 重置/修改密码
     **/
    @Override
    public boolean resetPassword(JSONObject param,Member member) {
        String password = param.getString("password");
        String oldPassword = param.getString("oldPassword");
        String base64Pwd = CommonUtil.base64WithSalt(oldPassword,member.getSalt());
        if (!Objects.equal(member.getPassword(),base64Pwd)){
            throw new AccountException(SystemErrorType.ACCOUNT_VERIFY_ERROR,"旧密码输入错误！");
        }
        Map<String,Object> map = CommonUtil.base64(password);
        member.setPassword((String)map.get("hashedStrBase64"));
        member.setSalt((String)map.get("salt"));
        member.setUpdatedBy(CommonUtil.getCurrentUserId());
        member.setUpdatedTime(new Date());
        memberMapper.updateById(member);
        return true;
    }

    /**
     * 获取角色列表
     **/
    @Override
    public List<Role> getRoleList() {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("isDelete",0);
        return roleMapper.selectList(wrapper);
    }
}
