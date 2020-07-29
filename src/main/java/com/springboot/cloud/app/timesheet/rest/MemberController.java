package com.springboot.cloud.app.timesheet.rest;

import ch.qos.logback.core.util.ContextUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.cloud.app.timesheet.service.IUserService;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.cloud.app.timesheet.entity.vo.MemberVo;
import com.springboot.cloud.app.timesheet.entity.form.MemberForm;
import com.springboot.cloud.app.timesheet.entity.form.MemberQueryForm;
import com.springboot.cloud.app.timesheet.entity.param.MemberQueryParam;
import com.springboot.cloud.app.timesheet.service.IMemberService;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.springboot.cloud.common.core.exception.AccountException;
import com.springboot.cloud.common.core.exception.SystemErrorType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import com.springboot.cloud.common.core.util.ConstantUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.stream.Collectors;


@RestController
@Api(tags="【PC端】用户模块")
@Slf4j
@RequestMapping(value = "/member")
public class MemberController {

	@Autowired
	public IMemberService memberService;


	@ApiOperation(value = "新增一个用户（信息）", httpMethod = ConstantUtil.HTTP_POST, notes = "新增一个用户（信息）")
	@PostMapping("/saveMember")
	public Result add(@Valid @RequestBody MemberForm memberForm) {
		log.info("saveMember with name: {}", memberForm);
		try {
		    if (StringUtils.isBlank(memberForm.getRealName())){
		        return Result.fail("真实姓名必填");
            }
            if (StringUtils.isBlank(memberForm.getPassword())){
                return Result.fail("密码必填");
            }
		    return Result.success(memberService.savePerson(memberForm));
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e.getMessage());
		}
	}
    //==================================================================================================================================================================
	@ApiOperation(value = "更新一个用户的信息",  httpMethod = ConstantUtil.HTTP_POST, notes = "更新一个用户的信息")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
            @DynamicParameter(name = "id",value = "用户id",example = "1",dataTypeClass = Long.class),
            @DynamicParameter(name = "roleId",value = "角色id",example = "1",dataTypeClass = Long.class),
            @DynamicParameter(name = "realName",value = "真实名称",example = "小明",dataTypeClass = String.class),
            @DynamicParameter(name = "username",value = "用户名",example = "小明",dataTypeClass = String.class),
            @DynamicParameter(name = "phone",value = "手机号码",example = "123456789",dataTypeClass = String.class),
            @DynamicParameter(name = "isBan",value = "是否禁用（0为否｜1为是）",example = "0",dataTypeClass = Integer.class)
    }))
	@PostMapping(value = "/update")
	public Result update(@RequestBody JSONObject param) {
	    Long id = param.getLong("id");
	    if (id == null){
	        return Result.fail("用户id必填!");
        }
		try {
		    memberService.update(param);
			return Result.success();
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
    //==================================================================================================================================================================
	@ApiOperation(value = "查看某一个用户的信息", httpMethod = ConstantUtil.HTTP_GET, notes = "查看某一个用户的信息 ")
	@ApiImplicitParam(name = "id", value = "用户ID（在请求的URL上）", required = true,  dataType = "long")
	@GetMapping(value = "/{id}")
    public Result<MemberVo> get(@PathVariable long id) {
		log.info("getMember with id: {}", id);
		try {
			Member member = memberService.getById(id);
            return Result.success(member);
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail();
		}
	}
    //==================================================================================================================================================================
	@ApiOperation(value = "通过username来模糊查询用户列表（查询结果有分页）", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 有分页")
    @ApiImplicitParams({
 //            @ApiImplicitParam(name = "memberQueryForm", value = "member查询参数", required = true, dataType = "memberQueryForm"),
             @ApiImplicitParam(name = "pageNum", value = "分页数", required = true, dataType = "int"),
             @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "int")
    })
    @PostMapping("/query")
    public Result<Page<MemberVo>> query(@Valid @RequestBody MemberQueryForm memberQueryForm,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize) {
         log.info("query with memberQueryForm:{}", memberQueryForm);
         if(memberQueryForm == null){
             memberQueryForm = new MemberQueryForm();
         }
         Page<Member> page = new Page<Member>(pageNum,pageSize);
//         page.setSearchCount(true);
         MemberQueryParam memberQueryParam = memberQueryForm.toParam(MemberQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         queryWrapper.like(null != memberQueryParam.getUsername(), "username",memberQueryParam.getUsername());
//         queryWrapper.like(null != memberQueryParam.getNickName(), "nick_name",memberQueryParam.getNickName());
         IPage<Member> ipage = memberService.page(page,queryWrapper);
         IPage<MemberVo> ipageRes = new Page<MemberVo>();
         BeanUtils.copyProperties(ipage,ipageRes);
         List<Member> members = ipage.getRecords();
         List<MemberVo> memberVos = members.stream().map(member -> {
         	MemberVo memberVo = new MemberVo();
         	BeanUtils.copyProperties(member,memberVo);
         	return memberVo;
         }).collect(Collectors.toList());
         ipageRes.setRecords(memberVos);
         return Result.success(ipageRes);
    }
    //==================================================================================================================================================================
   @ApiOperation(value = "通过username来模糊查询用户列表（查询结果无分页）", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 无分页")
   @PostMapping("/queryAll")
   public Result<List<MemberVo>> queryAll(@Valid @RequestBody MemberQueryForm memberQueryForm) {
         log.info("query with memberQueryForm:{}", memberQueryForm);
         if(memberQueryForm == null){
             memberQueryForm = new MemberQueryForm();
         }
//         Page<Member> page = new Page<Member>(pageNum,pageSize);
//         page.setSearchCount(true);
         MemberQueryParam memberQueryParam = memberQueryForm.toParam(MemberQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         queryWrapper.like(null != memberQueryParam.getUsername(), "username",memberQueryParam.getUsername());
//         queryWrapper.like(null != memberQueryParam.getNickName(), "nick_name",memberQueryParam.getNickName());
         List<Member> members = memberService.list(queryWrapper);
         List<MemberVo> memberVos = members.stream().map(member -> {
              MemberVo memberVo = new MemberVo();
              BeanUtils.copyProperties(member,memberVo);
              return memberVo;
         }).collect(Collectors.toList());
         return Result.success(memberVos);
    }
    //==================================================================================================================================================================
    @ApiOperation(value = "导出用户工时明细",httpMethod = ConstantUtil.HTTP_POST,notes = "导出用户工时明细")
    @PostMapping("/export")
    public void exportPersonProjectDetail(@RequestBody JSONObject json, HttpServletResponse response){
	    try {
	        memberService.exportPersonProjectDetail(json,response);
        }catch (Exception e){
	        e.printStackTrace();
        }
    }
    //==================================================================================================================================================================
    @ApiOperation(value = "导出所有用户的部分信息",httpMethod = ConstantUtil.HTTP_POST,notes = "导出所有用户的部分信息")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//            @DynamicParameter(name = "id",value = "用户id",example = "1",dataTypeClass = Long.class),
            @DynamicParameter(name = "ids",value = "逗号隔开的用户id(1,2,3)",dataTypeClass = String.class),
            @DynamicParameter(name = "roleId",value = "角色id",dataTypeClass = Long.class)
    }))
    @PostMapping("/exportMember")
    public void exportMemberList(@RequestBody JSONObject param,HttpServletResponse response){
	    try {
	        memberService.exportMember(param,response);
        }catch (Exception e){
	        e.printStackTrace();
        }
    }
    //==================================================================================================================================================================
    @ApiOperation(value = "分页查询用户列表",httpMethod = ConstantUtil.HTTP_POST,notes = "分页查询用户列表")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//            @DynamicParameter(name = "id",value = "用户id",example = "1",dataTypeClass = Long.class),
//            @DynamicParameter(name = "ids",value = "逗号隔开的用户id(1,2,3)",dataTypeClass = String.class),
//            @DynamicParameter(name = "roleId",value = "角色id(1人员)",example = "1",dataTypeClass = Long.class),
//            @DynamicParameter(name = "realName",value = "真实名称",dataTypeClass = String.class),
//            @DynamicParameter(name = "username",value = "用户名",dataTypeClass = String.class),
//            @DynamicParameter(name = "job",value = "岗位属性",dataTypeClass = String.class),
//            @DynamicParameter(name = "isBan",value = "是否禁用",example = "1",dataTypeClass = Integer.class),
//            @DynamicParameter(name = "isDelete",value = "是否删除",example = "1",dataTypeClass = Integer.class),
            @DynamicParameter(name = "pageSize",value = "10",example = "10",dataTypeClass = Integer.class),
            @DynamicParameter(name = "pageNum",value = "1",example = "1",dataTypeClass = Integer.class)
    }))
    @PostMapping("/getMemberListByPage")
    public Result<List<MemberVo>> getMemberListByPage(@RequestBody JSONObject param){
        try {
            return Result.success(memberService.getMemberListByPage(param));
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //==================================================================================================================================================================
    @ApiOperation(value = "查询用户列表",httpMethod = ConstantUtil.HTTP_POST,notes = "查询用户列表")
//    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//            @DynamicParameter(name = "id",value = "用户id",example = "1",dataTypeClass = Long.class),
//            @DynamicParameter(name = "ids",value = "逗号隔开的用户id(1,2,3)",dataTypeClass = String.class),
//            @DynamicParameter(name = "roleId",value = "角色id(1人员)",example = "1",dataTypeClass = Long.class),
//            @DynamicParameter(name = "realName",value = "真实名称",dataTypeClass = String.class),
//            @DynamicParameter(name = "username",value = "用户名",dataTypeClass = String.class),
//            @DynamicParameter(name = "job",value = "岗位属性",dataTypeClass = String.class),
//            @DynamicParameter(name = "isBan",value = "是否禁用",example = "1",dataTypeClass = Integer.class),
//            @DynamicParameter(name = "isDelete",value = "是否删除",example = "1",dataTypeClass = Integer.class),
//    }))
    @PostMapping("/getMemberList")
    public Result<List<MemberVo>> getMemberList(@RequestBody JSONObject param){
        try {
            return Result.success(memberService.getMemberList(param));
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //==================================================================================================================================================================
    @ApiOperation(value = "批量删除用户信息",httpMethod = ConstantUtil.HTTP_POST,notes = "批量删除用户信息")
    @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
            @DynamicParameter(name = "ids",value = "逗号隔开的id(1,2,3,4)", example = "1,2,4",dataTypeClass = String.class),
    }))
    @PostMapping("/batchDelete")
    public Result batchDelete(@RequestBody JSONObject param){
        String ids = param.getString("ids");
        if (StringUtils.isBlank(ids)){
            return Result.fail("ids 必填");
        }
        try {
            memberService.batchDelete(param);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.success();
    }
    //==================================================================================================================================================================
    @ApiOperation(value = "获取企业微信通讯录并同步", httpMethod = ConstantUtil.HTTP_POST, notes = "获取企业微信通讯录会员Userid")
    @PostMapping("/getUserList")
    public Result getUserList() {
        log.info("getUserList with name: {}");
        try {
            return Result.success(memberService.getUserList());
        } catch (Exception e) {
            log.error("{}", e);
            return Result.fail(e);
        }
    }
}
