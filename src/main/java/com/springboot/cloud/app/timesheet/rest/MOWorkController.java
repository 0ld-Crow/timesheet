package com.springboot.cloud.app.timesheet.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.cloud.app.timesheet.entity.form.WorkForm;
import com.springboot.cloud.app.timesheet.entity.form.WorkQueryForm;
import com.springboot.cloud.app.timesheet.entity.param.WorkQueryParam;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.springboot.cloud.app.timesheet.entity.po.Work;
import com.springboot.cloud.app.timesheet.entity.vo.SummaryVo;
import com.springboot.cloud.app.timesheet.entity.vo.WorkVo;
import com.springboot.cloud.app.timesheet.service.IAccessTokenService;
import com.springboot.cloud.app.timesheet.service.IProjectService;
import com.springboot.cloud.app.timesheet.service.ISendMessageService;
import com.springboot.cloud.app.timesheet.service.IWorkService;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.springboot.cloud.common.core.util.ConstantUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName WorkController
 * @Description 【移动端】工作记录表模块
 */
@RestController
@Api(tags="【移动端】工作记录表模块")
@Slf4j
@RequestMapping(value = "/mowork")
public class MOWorkController {

	@Autowired
	public IWorkService workService;
	@Autowired
	public IAccessTokenService accessTokenService;
	@Autowired
	public ISendMessageService sendMessageService;
	@Autowired
	public IProjectService projectService;


//==================================================================================================================================================================
	//原型：https://org.modao.cc/app/v9ch91gx1fq0hh4tz1i2dfgvik16jguv16px5or5937qzhoqr4sfsux47vi#screen=sF3A85C102F1569856283896
//==================================================================================================================================================================
	@ApiOperation(value = "新增1个或N个工作记录表", httpMethod = ConstantUtil.HTTP_POST, notes = "新增1个或N个工作记录表")
	@ApiOperationSupport(params = @DynamicParameters(name = "JSONArray", properties = {
			@DynamicParameter(name = "pId",value = "项目id",example = "1",dataTypeClass = Long.class),
			@DynamicParameter(name = "uId",value = "用户id",example = "1",dataTypeClass = Long.class),
			@DynamicParameter(name = "workDate",value = "工作日期",dataTypeClass = Date.class),
			@DynamicParameter(name = "hourTime",value = "工时",dataTypeClass = BigDecimal.class),
			@DynamicParameter(name = "description",value = "描述",dataTypeClass = String.class),
			@DynamicParameter(name = "attach",value = "附加字段",dataTypeClass = String.class),
			@DynamicParameter(name = "remark",value = "备注",dataTypeClass = String.class)
	}))
	@PostMapping("/saveWorkList")
	public Result<List<WorkVo>> saveWorkList(@Valid @RequestBody JSONArray param) {
		log.info("saveWorkList with name: {}", param);
		try {
			return Result.success(workService.saveWorkList(param));
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "修改1个或N个工作记录表", httpMethod = ConstantUtil.HTTP_POST, notes = "修改一个或N个工作记录表")
	@ApiOperationSupport(params = @DynamicParameters(name = "JSONArray", properties = {
			@DynamicParameter(name = "id",value = "id",example = "1",dataTypeClass = Long.class),
			@DynamicParameter(name = "pId",value = "项目id",example = "1",dataTypeClass = Long.class),
			@DynamicParameter(name = "uId",value = "用户id",example = "1",dataTypeClass = Long.class),
			@DynamicParameter(name = "workDate",value = "工作日期",dataTypeClass = Date.class),
			@DynamicParameter(name = "hourTime",value = "工时",dataTypeClass = BigDecimal.class),
			@DynamicParameter(name = "description",value = "描述",dataTypeClass = String.class),
			@DynamicParameter(name = "attach",value = "附加字段",dataTypeClass = String.class),
			@DynamicParameter(name = "remark",value = "备注",dataTypeClass = String.class)
	}))
	@PostMapping("/updateWorkList")
	public Result<List<WorkVo>> updateWorkList(@Valid @RequestBody JSONArray param) {
		log.info("updateWorkList with name: {}", param);
		try {
			return Result.success(workService.updateWorkList(param));
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "查询最近七天工作记录表汇总", httpMethod = ConstantUtil.HTTP_POST, notes = "查询最近七天工作记录表汇总")
	@PostMapping("/sevenday")
	//把7天内有填写的工作记录表和未填写工作记录表的某天的unfill（即2号一整天都没有填写工作记录表，那么就会生成一个2好的未填写工作记录表）
	public Result<List<SummaryVo>> sevenDay() {
		log.info("sevenday with name: {}");
		try {
			return Result.success(workService.sevenDay());
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "利用ids（一个或多个）去找到相对应的工作记录表汇总", httpMethod = ConstantUtil.HTTP_POST, notes = "利用ids（一个或多个）去找到相对应的工作记录表汇总")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
			@DynamicParameter(name = "ids",value = "用逗号分开每一个id(1,2,3,4)",example = "1,2,3,4",dataTypeClass = String.class)
	}))
	@PostMapping("/getWorksByIds")
	public Result<List<WorkVo>> getWorksByIds(@RequestBody JSONObject param) {
		log.info("oneDay with name: {}");
		String ids = param.getString("ids");
		if (ids == null || "".equals(ids.trim())){
			return Result.fail("ids 必填");
		}
		try {
			return Result.success(workService.getWorksByIds(ids));
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "查询所有项目的列表（为了在填写工作记录表的时候可以选取项目名称）", httpMethod = ConstantUtil.HTTP_GET, notes = "查询所有项目的列表（为了在填写工作记录表的时候可以选取项目名称）")
	@PostMapping("/getAllProjectList")
	public Result<List<Project>> getAllProjectList(){
		try {
			return Result.success(projectService.getAllProjectList());
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}



}
