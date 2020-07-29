package com.springboot.cloud.app.timesheet.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.cloud.app.timesheet.entity.vo.JobVo;
import com.springboot.cloud.app.timesheet.entity.form.JobForm;
import com.springboot.cloud.app.timesheet.entity.form.JobQueryForm;
import com.springboot.cloud.app.timesheet.entity.param.JobQueryParam;
import com.springboot.cloud.app.timesheet.service.IJobService;
import com.springboot.cloud.app.timesheet.entity.po.Job;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.springboot.cloud.common.core.util.CommonUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import com.springboot.cloud.common.core.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import java.util.stream.Collectors;


@RestController
@Api(tags="【PC端】岗位模块")
@Slf4j
@RequestMapping(value = "/job")

public class JobController {

	@Autowired
	public IJobService jobService;

	@ApiOperation(value = "新增一个岗位", httpMethod = ConstantUtil.HTTP_POST, notes = "新增一个岗位")
	@PostMapping("/saveJob")
	public Result add(@Valid @RequestBody JobForm jobForm) {
		log.info("saveJob with name: {}", jobForm);
		try {
			jobForm.setIsDelete(0);
			jobForm.setIsBan(0);
			jobForm.setCreatedBy(CommonUtil.getCurrentUserId());
			jobForm.setUpdatedBy(CommonUtil.getCurrentUserId());
			jobForm.setCreatedTime(new Date());
			jobForm.setUpdatedTime(new Date());
		    Job job = jobForm.toPo(Job.class);
		    return Result.success(jobService.save(job));
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "删除一个岗位", httpMethod = ConstantUtil.HTTP_DELETE, notes = "删除一个岗位 ")
	@ApiImplicitParam(name = "id", value = "岗位ID（位于请求的URL中）", example = "1",required = true, dataType = "long")
	@DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable  long id) {
		log.info("deleteJob with id: {}", id);
		try {
		    jobService.removeById(id);
			return Result.success();
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "修改一个岗位的信息",  httpMethod = ConstantUtil.HTTP_PUT, notes = "修改一个岗位的信息")
	@PutMapping(value = "/{id}")
	@ApiImplicitParam(name = "id", value = "岗位ID（位于请求的URL中）", example = "1",required = true, dataType = "long")
	public Result update( @PathVariable  long id,@Valid @RequestBody JobForm jobForm) {
		log.info("updateJob with name: {}", jobForm);
		try {
		    Job job = jobForm.toPo(Job.class);
            job.setId(id);
            jobService.updateById(job);
			return Result.success();
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "查看一个岗位的信息", httpMethod = ConstantUtil.HTTP_GET, notes = "查看一个岗位的信息 ")
	@ApiImplicitParam(name = "id", value = "岗位ID（位于请求的URL中）", example = "1",required = true, dataType = "long")
	@GetMapping(value = "/{id}")
    public Result<JobVo> get(@PathVariable  long id) {
		log.info("getJob with id: {}", id);
		try {
			Job job = jobService.getById(id);
            return Result.success(job);
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail();
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "通过岗位名称来模糊查询（生成的岗位列表有分页）", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 有分页")
    @ApiImplicitParams({
//             @ApiImplicitParam(name = "jobQueryForm", value = "job查询参数", required = true, dataType = "jobQueryForm"),
             @ApiImplicitParam(name = "pageNum", value = "分页数", example = "2",required = true, dataType = "int"),
             @ApiImplicitParam(name = "pageSize", value = "分页大小", example = "15",required = true, dataType = "int")
    })
    @PostMapping("/query")
	//@RequestParam(defaultValue = "1")  @RequestParam(defaultValue = "10")
    public Result<Page<JobVo>> query(@Valid  @RequestBody JobQueryForm jobQueryForm,  @RequestParam(defaultValue = "1") int pageNum,  @RequestParam(defaultValue = "10") int pageSize) {
         log.info("query with jobQueryForm:{}", jobQueryForm);
         if(jobQueryForm == null){
             jobQueryForm = new JobQueryForm();
         }
         Page<Job> page = new Page<Job>(pageNum,pageSize);
//         page.setSearchCount(true);
         JobQueryParam jobQueryParam = jobQueryForm.toParam(JobQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         queryWrapper.like(null != jobQueryParam.getName(), "name",jobQueryParam.getName());
         IPage<Job> ipage = jobService.page(page,queryWrapper);
         IPage<JobVo> ipageRes = new Page<JobVo>();
         BeanUtils.copyProperties(ipage,ipageRes);
         List<Job> jobs = ipage.getRecords();
         List<JobVo> jobVos = jobs.stream().map(job -> {
         	JobVo jobVo = new JobVo();
         	BeanUtils.copyProperties(job,jobVo);
         	return jobVo;
         }).collect(Collectors.toList());
         ipageRes.setRecords(jobVos);
         return Result.success(ipageRes);
    }
	//==================================================================================================================================================================
   @ApiOperation(value = "通过岗位名称来模糊查询（生成的岗位列表没分页）", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 无分页")
   @PostMapping("/queryAll")
   public Result<List<JobVo>> queryAll(@Valid  @RequestBody JobQueryForm jobQueryForm) {
         log.info("query with jobQueryForm:{}", jobQueryForm);
         if(jobQueryForm == null){
             jobQueryForm = new JobQueryForm();
         }
//         Page<Job> page = new Page<Job>(pageNum,pageSize);
//         page.setSearchCount(true);
         JobQueryParam jobQueryParam = jobQueryForm.toParam(JobQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         queryWrapper.like(null != jobQueryParam.getName(), "name",jobQueryParam.getName());
         List<Job> jobs = jobService.list(queryWrapper);
         List<JobVo> jobVos = jobs.stream().map(job -> {
              JobVo jobVo = new JobVo();
              BeanUtils.copyProperties(job,jobVo);
              return jobVo;
         }).collect(Collectors.toList());
         return Result.success(jobVos);
    }
	//==================================================================================================================================================================
	@ApiOperation(value = "分页查询所有岗位列表")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//			@DynamicParameter(name = "id",value = "岗位id",example = "1",dataTypeClass = Long.class),
//			@DynamicParameter(name = "ids",value = "逗号隔开的岗位id(1,2,3)",dataTypeClass = String.class),
//			@DynamicParameter(name = "name",value = "岗位名称",dataTypeClass = String.class),
//			@DynamicParameter(name = "isBan",value = "是否禁用",example = "1",dataTypeClass = Integer.class),
//			@DynamicParameter(name = "isDelete",value = "是否删除",example = "1",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pageSize",value = "每一页的大小",example = "10",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pageNum",value = "总共页数",example = "5",dataTypeClass = Integer.class)
	}))
	@PostMapping("/getJobListByPage")
	public Result<List<Job>> getJobListByPage(@RequestBody JSONObject param){
		try {
			return Result.success(jobService.getJobListByPage(param));
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}
	//==================================================================================================================================================================
	@ApiOperation(value = "查询所有岗位列表")
//	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//			@DynamicParameter(name = "id",value = "岗位id",example = "1",dataTypeClass = Long.class),
//			@DynamicParameter(name = "ids",value = "逗号隔开的岗位id(1,2,3)",dataTypeClass = String.class),
//			@DynamicParameter(name = "name",value = "岗位名称",dataTypeClass = String.class),
//			@DynamicParameter(name = "isBan",value = "是否禁用",example = "1",dataTypeClass = Integer.class),
//			@DynamicParameter(name = "isDelete",value = "是否删除",example = "1",dataTypeClass = Integer.class)
//	}))
	@PostMapping("/getJobList")
	//@RequestBody JSONObject param
	public Result<List<Job>> getJobList(){
		try {
			return Result.success(jobService.getJobList());
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}
}
