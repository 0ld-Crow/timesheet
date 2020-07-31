package com.springboot.cloud.app.timesheet.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.cloud.app.timesheet.entity.param.QueryParam;
import com.springboot.cloud.app.timesheet.entity.vo.ProjectWorkTimeVo;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.cloud.app.timesheet.entity.vo.ProjectVo;
import com.springboot.cloud.app.timesheet.entity.form.ProjectForm;
import com.springboot.cloud.app.timesheet.entity.form.ProjectQueryForm;
import com.springboot.cloud.app.timesheet.entity.param.ProjectQueryParam;
import com.springboot.cloud.app.timesheet.service.IProjectService;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import com.springboot.cloud.common.core.util.ConstantUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@Api(tags="【PC端】项目模块")
@Slf4j
@RequestMapping(value = "/project")
public class ProjectController {

	@Autowired
	public IProjectService projectService;

	@ApiOperation(value = "新增一个项目（信息）", httpMethod = ConstantUtil.HTTP_POST, notes = "新增一个项目（信息）")
	@PostMapping("/saveProject")
	public Result add(@Valid @RequestBody ProjectForm projectForm) {
		log.info("saveProject with name: {}", projectForm);
		try {
		    Project project = projectForm.toPo(Project.class);
		    return Result.success(projectService.save(project));
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "修改某一个项目的信息",  httpMethod = ConstantUtil.HTTP_PUT, notes = "修改某一个项目的信息")
	@ApiImplicitParam(name = "id", value = "项目ID（在请求的URL上）", dataType = "projectQueryForm")
	@PutMapping(value = "/update/{id}")
	public Result update(@PathVariable long id, @Valid @RequestBody ProjectForm projectForm) {
		log.info("updateProject with name: {}", projectForm);
		try {
		    Project project = projectForm.toPo(Project.class);
            project.setId(id);
            projectService.updateById(project);
			return Result.success();
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "查询某一个项目的信息", httpMethod = ConstantUtil.HTTP_GET, notes = "查询某一个项目的信息 ")
	@ApiImplicitParam(name = "id", value = "项目ID（在请求的URL上）", required = true, dataType = "ProjectForm")
	@GetMapping(value = "/{id}")
    public Result<ProjectVo> get(@PathVariable long id) {
		log.info("getProject with id: {}", id);
		try {
			Project project = projectService.getById(id);
            return Result.success(project);
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail();
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "通过name进行模糊查询（结果有分页）", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 有分页")
    @ApiImplicitParams({
             @ApiImplicitParam(name = "pageNum", value = "分页数", dataType = "int"),
             @ApiImplicitParam(name = "pageSize", value = "分页大小",dataType = "int")
    })
    @PostMapping("/query")
    public Result<Page<Project>> query(@Valid @RequestBody ProjectQueryForm projectQueryForm,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize) {
         log.info("query with projectQueryForm:{}", projectQueryForm);
         if(projectQueryForm == null){
             projectQueryForm = new ProjectQueryForm();
         }
         Page<Project> page = new Page<Project>(pageNum,pageSize);
//         page.setSearchCount(true);
         ProjectQueryParam projectQueryParam = projectQueryForm.toParam(ProjectQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         queryWrapper.like(null != projectQueryParam.getName(), "name",projectQueryParam.getName());
         IPage<Project> ipage = projectService.page(page,queryWrapper);
         IPage<ProjectVo> ipageRes = new Page<ProjectVo>();
         BeanUtils.copyProperties(ipage,ipageRes);
         List<Project> projects = ipage.getRecords();
         List<ProjectVo> projectVos = projects.stream().map(project -> {
         	ProjectVo projectVo = new ProjectVo();
         	BeanUtils.copyProperties(project,projectVo);
         	return projectVo;
         }).collect(Collectors.toList());
         ipageRes.setRecords(projectVos);
         return Result.success(ipageRes);
    }
//==================================================================================================================================================================
   @ApiOperation(value = "通过name进行模糊查询（结果无分页）", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 无分页")
//   @ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//		   @DynamicParameter(name = "name",value = "项目名称",dataTypeClass = String.class),
//		   @DynamicParameter(name = "isBan",value = "是否禁用",example = "1",dataTypeClass = Integer.class),
//		   @DynamicParameter(name = "isDelete",value = "是否删除",example = "1",dataTypeClass = Integer.class),
//		   @DynamicParameter(name = "pageSize",value = "10",example = "1",dataTypeClass = Integer.class),
//		   @DynamicParameter(name = "pageNum",value = "1",example = "1",dataTypeClass = Integer.class)
//   }))
   @PostMapping("/queryAll")
   public Result<List<Project>> queryAll(@Valid @RequestBody ProjectQueryForm projectQueryForm) {
         log.info("query with projectQueryForm:{}", projectQueryForm);
         if(projectQueryForm == null){
             projectQueryForm = new ProjectQueryForm();
         }
//         Page<Project> page = new Page<Project>(pageNum,pageSize);
//         page.setSearchCount(true);
         ProjectQueryParam projectQueryParam = projectQueryForm.toParam(ProjectQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         queryWrapper.like(null != projectQueryParam.getName(), "name",projectQueryParam.getName());
         List<Project> projects = projectService.list(queryWrapper);
         List<ProjectVo> projectVos = projects.stream().map(project -> {
              ProjectVo projectVo = new ProjectVo();
              BeanUtils.copyProperties(project,projectVo);
              return projectVo;
         }).collect(Collectors.toList());
         return Result.success(projectVos);
    }
//==================================================================================================================================================================
    @ApiOperation(value = "查询到所有项目的工时列表",httpMethod = ConstantUtil.HTTP_POST,notes ="查询到所有项目的工时列表")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//			@DynamicParameter(name = "pageSize",value = "10",example = "1",dataTypeClass = Integer.class),
//			@DynamicParameter(name = "pageNum",value = "1",example = "1",dataTypeClass = Integer.class)
	}))
    @PostMapping("/projectWorkTime")
    public Result<List<ProjectWorkTimeVo>> projectWorkTime(@RequestBody JSONObject param){
		try {
			return Result.success(projectService.projectWorkTime(param));
		}catch (Exception e){
			e.printStackTrace();
		}
		return Result.fail();
	}
//==================================================================================================================================================================
	@ApiOperation(value = "导出某个项目的工时列表",httpMethod = ConstantUtil.HTTP_POST,notes ="导出某个项目的工时列表")
	@ApiOperationSupport(params = @DynamicParameters(name = "param", properties = {
			@DynamicParameter(name = "type",value = "(project|项目工时统计,post|按岗位导出,person|按人员导出)",dataTypeClass = String.class),
			@DynamicParameter(name = "pId",value = "项目id(type为post、person时有效且必填)",example = "1",dataTypeClass = Long.class)
	}))
	@PostMapping("/export")
	public void exportStatistics(@RequestBody JSONObject param, HttpServletResponse response){
		String type = param.getString("type");
		try {
			if (StringUtils.isBlank(type)){
				response.getWriter().write(JSON.toJSONString(Result.fail("type必填")));
				return;
			}
			if (StringUtils.isBlank(type) && Objects.isNull(param.getLong("pId"))){
				response.getWriter().write(JSON.toJSONString(Result.fail("pId必填")));
				return;
			}
			if ("project".equals(type)){
				projectService.exportAllProjectWorkTime(param,response);
			}else if ("post".equals(type)){
				projectService.exportWorkTimeByPost(param,response);
			}else if ("person".equals(type)){
				projectService.exportWorkTimeByPerson(param,response);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "查询到某个项目的工时列表明细",httpMethod = ConstantUtil.HTTP_POST,notes ="查询到某个项目的工时列表明细")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//			@DynamicParameter(name = "pageSize",value = "10",example = "10",dataTypeClass = Integer.class),
//			@DynamicParameter(name = "pageNum",value = "1",example = "1",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pId",value = "项目id",example = "1",dataTypeClass = Long.class),
			@DynamicParameter(name = "type",value = "post|按岗位属性、person|按人员",example = "post|按岗位属性、person|按人员",dataTypeClass = String.class)
	}))
	@PostMapping("/getProjectWorkTimeDetail")
	public Result<List<ProjectWorkTimeVo>> projectWorkTimeDetail(@RequestBody JSONObject param){
		Long pId = param.getLong("pId");
		String type = param.getString("type");
		if (pId == null){
			return Result.fail("pId 必填");
		}
		if (StringUtils.isBlank(type)){
			return Result.fail("type 必填");
		}
		try {
			List<ProjectWorkTimeVo> projectWorkTimeVos = projectService.projectWorkTimeDetail(param);
			return Result.success(projectWorkTimeVos);
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "批量删除项目",httpMethod = ConstantUtil.HTTP_POST,notes ="批量删除项目")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
			@DynamicParameter(name = "ids",value = "逗号隔开的id（1，2，3）",example = "1,2,3",dataTypeClass = String.class)
	}))
	@PostMapping("/batchDelete")
	public Result batchDelete(@RequestBody JSONObject param){
		String ids = param.getString("ids");
		if (ids == null || "".equals(ids.trim())){
			return Result.fail("ids 必填");
		}
		try {
			projectService.batchDelete(param);
		}catch (Exception e){
			e.printStackTrace();
		}
		return Result.success();
	}
//==================================================================================================================================================================
	@ApiOperation(value = "分页查询项目列表",httpMethod = ConstantUtil.HTTP_POST,notes ="分页查询项目列表")
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//			@DynamicParameter(name = "id",value = "岗位id",example = "1",dataTypeClass = Long.class),
//			@DynamicParameter(name = "ids",value = "逗号隔开的岗位id(1,2,3)",dataTypeClass = String.class),
//			@DynamicParameter(name = "name",value = "项目名称",dataTypeClass = String.class),
//			@DynamicParameter(name = "isBan",value = "是否禁用",example = "0",dataTypeClass = Integer.class),
//			@DynamicParameter(name = "isDelete",value = "是否删除",example = "0",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pageSize",value = "10",example = "10",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pageNum",value = "1",example = "1",dataTypeClass = Integer.class)
	}))
	@PostMapping("/getProjectListByPage")
	public Result<List<Project>> getProjectListByPage(@RequestBody JSONObject param){
		try {
			return Result.success(projectService.getProjectListByPage(param));
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "查询项目列表",httpMethod = ConstantUtil.HTTP_POST,notes ="查询项目列表")
//	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
//			@DynamicParameter(name = "id",value = "岗位id",example = "1",dataTypeClass = Long.class),
//			@DynamicParameter(name = "ids",value = "逗号隔开的岗位id(1,2,3)",dataTypeClass = String.class),
//			@DynamicParameter(name = "name",value = "项目名称",dataTypeClass = String.class),
//			@DynamicParameter(name = "isBan",value = "是否禁用",example = "0",dataTypeClass = Integer.class),
//			@DynamicParameter(name = "isDelete",value = "是否删除",example = "0",dataTypeClass = Integer.class),
//	}))
	@PostMapping("/getProjectList")
	public Result<List<Project>> getProjectList(@RequestBody JSONObject param){
		try {
			return Result.success(projectService.getProjectList(param));
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}
}
