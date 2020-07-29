package com.springboot.cloud.app.timesheet.rest;

import com.alibaba.fastjson.JSONObject;
import com.springboot.cloud.app.timesheet.dao.MemberMapper;
import com.springboot.cloud.app.timesheet.dao.RoleMapper;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.app.timesheet.service.IIndexService;
import com.springboot.cloud.common.core.entity.po.Role;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.springboot.cloud.common.core.entity.vo.UserVo;
import com.springboot.cloud.common.core.exception.AccountException;
import com.springboot.cloud.common.core.util.CommonUtil;
import com.springboot.cloud.common.core.util.ConstantUtil;
import com.springboot.cloud.common.core.util.ObjectUtil;
import com.springboot.cloud.enums.roleEnums;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Api(tags="【公共接口】")
@Slf4j
public class IndexController {

    @Autowired
    IIndexService indexService;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    RoleMapper roleMapper;

    @ApiOperation(value = "登录",httpMethod = ConstantUtil.HTTP_POST, notes = "登录")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
            @DynamicParameter(name = "username",value = "用户名",example = "superadmin",dataTypeClass = String.class),
            @DynamicParameter(name = "password",value = "密码",example = "123456",dataTypeClass = String.class)
    }))
    @PostMapping("/signin")
    public Result signin(@RequestBody JSONObject param, HttpServletRequest request){
        try {
            UserVo userVo = indexService.signin(request,param);
            return Result.success(userVo);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
    }
//==================================================================================================================================================================
    @ApiOperation(value = "企业微信授权登录",httpMethod = ConstantUtil.HTTP_POST, notes = "企业微信授权登录")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
            @DynamicParameter(name = "code",value = "企业微信回调值",dataTypeClass = String.class),
    }))
    @PostMapping("/authorized")
    public Result authorized(@RequestBody JSONObject param, HttpServletRequest request){
        try {
            UserVo userVo = indexService.authorized(request,param);
            return Result.success(userVo);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
    }
//==================================================================================================================================================================
    @ApiOperation(value = "登出", httpMethod = ConstantUtil.HTTP_POST, notes = "登出")
    @PostMapping("/signout")
    public  Result signout(HttpServletRequest request, HttpServletResponse response) {
        log.debug("--------------signout--------------");
        try {
            SecurityUtils.getSubject().logout(); //调用shiro删除所有session
            CommonUtil.setSession("user",null); //删除session
            return Result.success();
        } catch (Exception e) {
            return Result.fail();
        }
    }
//==================================================================================================================================================================
    @ApiOperation(value = "获取角色列表",httpMethod = ConstantUtil.HTTP_GET, notes = "获取角色列表")
    @GetMapping("/roleList")
    public Result<List<Role>> roleList(){
        try {
            return Result.success(indexService.getRoleList());
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
//==================================================================================================================================================================
    @ApiOperation(value = "新增一个角色", httpMethod = ConstantUtil.HTTP_POST, notes = "新增一个角色")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
            @DynamicParameter(name = "code",value = "角色编码",example = "1",dataTypeClass = String.class),
            @DynamicParameter(name = "name",value = "角色名称",example = "小明",dataTypeClass = String.class),
            @DynamicParameter(name = "description",value = "角色描述",example = "我是小明",dataTypeClass = String.class),
            @DynamicParameter(name = "sort",value = "角色排序",example = "1",dataTypeClass = Integer.class)
    }))
    @PostMapping("/role/addRole")
    public Result addRole(@RequestBody JSONObject param){
        try {
            Role role = (Role)ObjectUtil.map2ObjAndCast(param, Role.class);
            roleMapper.insert(role);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
        return Result.success();
    }
//==================================================================================================================================================================
    @ApiOperation(value = "删除一个角色", httpMethod = ConstantUtil.HTTP_POST, notes = "删除一个角色")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
            @DynamicParameter(name = "id",value = "角色id",example = "1",dataTypeClass = Long.class)
    }))
    @PostMapping("/role/delete")
    public Result removeRole(@RequestBody JSONObject param){
        try {
            Long id = param.getLong("id");
            if (id == null){
                return Result.fail("id不能为空");
            }
            int count = memberMapper.getUserByRoleId(id).size();
            if (count > 0){
                return Result.fail("尚有账号使用该角色，暂不能删除！");
            }else {
                Role role = new Role();
                role.setId(id);
                role.setIsDelete(1);
                roleMapper.updateById(role);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
        return Result.success();
    }
//==================================================================================================================================================================
    @ApiOperation(value = "重置/修改密码",httpMethod = ConstantUtil.HTTP_POST, notes = "重置/修改密码")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
            @DynamicParameter(name = "id",value = "用户id",example = "1",dataTypeClass = Long.class),
            @DynamicParameter(name = "password",value = "新密码",example = "123456",dataTypeClass = String.class),
            @DynamicParameter(name = "oldPassword",value = "旧密码",example = "654321",dataTypeClass = String.class)
    }))
    @PostMapping("/restPassword")
    public Result resetPassword(@RequestBody JSONObject param){
        Long id = param.getLong("id");
        String password = param.getString("password");
        String oldPassword = param.getString("oldPassword");
        if (Objects.isNull(id) || StringUtils.isBlank(password) || StringUtils.isBlank(oldPassword)){
            return Result.fail("用户id和密码必填！");
        }
        if (Objects.equals(password,oldPassword)){
            return Result.fail("新旧密码不能相同！");
        }
        Member member = memberMapper.getUserById(id);
        if (Objects.isNull(member)){
            return Result.fail("用户不存在！");
        }
        try {
            if (indexService.resetPassword(param,member)){
                return Result.success();
            }
        }catch (AccountException e){
            return Result.fail(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail();
    }
}
