package com.springboot.cloud.app.timesheet.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.springboot.cloud.app.timesheet.entity.vo.SummaryVo;
import com.springboot.cloud.app.timesheet.service.*;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.cloud.app.timesheet.entity.vo.WorkVo;
import com.springboot.cloud.app.timesheet.entity.form.WorkForm;
import com.springboot.cloud.app.timesheet.entity.form.WorkQueryForm;
import com.springboot.cloud.app.timesheet.entity.param.WorkQueryParam;
import com.springboot.cloud.app.timesheet.entity.po.Work;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.springboot.cloud.common.core.util.CommonUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import com.springboot.cloud.common.core.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @ClassName WorkController
 * @Description 【PC端】工作记录表模块
 */
@RestController
@Api(tags="【PC端】工作记录表模块")
@Slf4j
@RequestMapping(value = "/pcwork")
public class PCWorkController {

	@Autowired
	public IWorkService workService;

	@Autowired
	public IAccessTokenService accessTokenService;


	@Autowired
	public ISendMessageService sendMessageService;
	@Autowired
	public IProjectService projectService;



	@ApiOperation(value = "新增一个工作记录表", httpMethod = ConstantUtil.HTTP_POST, notes = "新增一个工作记录表")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
			@DynamicParameter(name = "pId",value = "项目id",example = "1",dataTypeClass = Long.class),
			@DynamicParameter(name = "uId",value = "用户id",example = "1",dataTypeClass = Long.class),
			@DynamicParameter(name = "workDate",value = "工作日期",example = "yyyy-MM-dd",dataTypeClass = Date.class),
			@DynamicParameter(name = "hourTime",value = "工时",example = "4",dataTypeClass = BigDecimal.class),
			@DynamicParameter(name = "description",value = "描述",example = "描述",dataTypeClass = String.class),
			@DynamicParameter(name = "attach",value = "附加字段",example = "附加字段",dataTypeClass = String.class),
			@DynamicParameter(name = "remark",value = "备注",example = "备注",dataTypeClass = String.class)
	}))
	@PostMapping("/saveWork")
	public Result add(@Valid @RequestBody JSONObject param) {
		log.info("saveWork with name: {}", param);
		try {
		    return Result.success(workService.saveWork(param));
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "删除一个工作记录表", httpMethod = ConstantUtil.HTTP_DELETE, notes = "删除一个工作记录表 ")
	@ApiImplicitParam(name = "id", value = "工作记录表ID（在请求URL上）", required = true, dataType = "long")
	@DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable long id) {
		log.info("deleteWork with id: {}", id);
		try {
		    workService.removeById(id);
			return Result.success();
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "更新一个工作记录表",  httpMethod = ConstantUtil.HTTP_PUT, notes = "更新一个工作记录表")
	@ApiImplicitParam(name = "id", value = "工作记录表ID（在请求URL上）", required = true, dataType = "long")
	@PutMapping(value = "/{id}")
	public Result update(@PathVariable long id, @Valid @RequestBody  WorkForm workForm) {
		log.info("updateWork with name: {}", workForm);
		try {
		    Work work = workForm.toPo(Work.class);
            work.setId(id);
            workService.updateById(work);
			return Result.success();
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "查询一个工作记录表", httpMethod = ConstantUtil.HTTP_GET, notes = "查询一个工作记录表")
	@ApiImplicitParam(name = "id", value = "工作记录表ID（在请求URL上）", required = true, dataType = "long")
	@GetMapping(value = "/{id}")
    public Result<WorkVo> get(@PathVariable long id) {
		log.info("getWork with id: {}", id);
		try {
			Work work = workService.getById(id);
            return Result.success(work);
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail();
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "工作记录表 - 分页列表", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 有分页")
    @ApiImplicitParams({
             @ApiImplicitParam(name = "workQueryForm", value = "work查询参数", required = true, dataType = "workQueryForm"),
             @ApiImplicitParam(name = "pageNum", value = "分页数", required = true, dataType = "int"),
             @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "int")
    })
    @PostMapping("/query")
    public Result<Page<WorkVo>> query(@Valid WorkQueryForm workQueryForm,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize) {
         log.info("query with workQueryForm:{}", workQueryForm);
         if(workQueryForm == null){
             workQueryForm = new WorkQueryForm();
         }
         Page<Work> page = new Page<Work>(pageNum,pageSize);
//         page.setSearchCount(true);
         WorkQueryParam workQueryParam = workQueryForm.toParam(WorkQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         IPage<Work> ipage = workService.page(page,queryWrapper);
         IPage<WorkVo> ipageRes = new Page<WorkVo>();
         BeanUtils.copyProperties(ipage,ipageRes);
         List<Work> works = ipage.getRecords();
         List<WorkVo> workVos = works.stream().map(work -> {
         	WorkVo workVo = new WorkVo();
         	BeanUtils.copyProperties(work,workVo);
         	return workVo;
         }).collect(Collectors.toList());
         ipageRes.setRecords(workVos);
         return Result.success(ipageRes);
    }

   @ApiOperation(value = "工作记录表 - 无分页列表", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 无分页")
   @ApiImplicitParams({
                @ApiImplicitParam(name = "workQueryForm", value = "work查询参数", required = true, dataType = "workQueryForm"),
   })
   @PostMapping("/queryAll")
   public Result<List<Work>> queryAll(@Valid WorkQueryForm workQueryForm,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize) {
         log.info("query with workQueryForm:{}", workQueryForm);
         if(workQueryForm == null){
             workQueryForm = new WorkQueryForm();
         }
         Page<Work> page = new Page<Work>(pageNum,pageSize);
//         page.setSearchCount(true);
         WorkQueryParam workQueryParam = workQueryForm.toParam(WorkQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         List<Work> works = workService.list(queryWrapper);
         List<WorkVo> workVos = works.stream().map(work -> {
              WorkVo workVo = new WorkVo();
              BeanUtils.copyProperties(work,workVo);
              return workVo;
         }).collect(Collectors.toList());
         return Result.success(workVos);
    }
	//==================================================================================================================================================================
	@ApiOperation(value = "批量删除工作记录表",httpMethod = ConstantUtil.HTTP_POST,notes = "批量删除工作记录表")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
			@DynamicParameter(name = "ids",value = "逗号隔开的id(1,2,3,4)",example = "1,2,3",dataTypeClass = String.class)
	}))
    @RequestMapping("/batchDelete")
    public Result batchDelete(@RequestBody JSONObject param){
		String ids = param.getString("ids");
		if (ids == null || "".equals(ids.trim())){
			return Result.fail("ids 必填");
		}
		try {
			workService.batchDelete(param);
		}catch (Exception e){
			e.printStackTrace();
		}
		return Result.success();
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "分页查询工作记录表")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//			@DynamicParameter(name = "id",value = "工作明细id",example = "1",dataTypeClass = Long.class),
//			@DynamicParameter(name = "ids",value = "逗号隔开的工作明细id(1,2,3)",dataTypeClass = String.class),
//			@DynamicParameter(name = "uId",value = "用户id",dataTypeClass = Long.class),
//			@DynamicParameter(name = "workDate",value = "工作日期",example = "2019-11-11",dataTypeClass = String.class),
//			@DynamicParameter(name = "isDelete",value = "是否删除",example = "0",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pageSize",value = "10",example = "10",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pageNum",value = "1",example = "1",dataTypeClass = Integer.class)
	}))
	@PostMapping("/getWorkListByPage")
	public Result<List<WorkVo>> getWorkListByPage(@RequestBody JSONObject param){
		try {
			return Result.success(workService.getWorkListByPage(param));
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "查询工作记录表")
//	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//			@DynamicParameter(name = "id",value = "工作明细id",example = "1",dataTypeClass = Long.class),
//			@DynamicParameter(name = "ids",value = "逗号隔开的工作明细id(1,2,3)",dataTypeClass = String.class),
//			@DynamicParameter(name = "uId",value = "用户id",dataTypeClass = Long.class),
//			@DynamicParameter(name = "workDate",value = "工作日期",example = "2019-11-11",dataTypeClass = String.class),
//			@DynamicParameter(name = "isDelete",value = "是否删除",example = "0",dataTypeClass = Integer.class),
//	}))
	@PostMapping("/getWorkList")
	public Result<List<WorkVo>> getWorkList(@RequestBody JSONObject param){
		try {
			return Result.success(workService.getWorkList(param));
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}

}
