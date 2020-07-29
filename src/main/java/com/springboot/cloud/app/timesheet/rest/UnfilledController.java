package com.springboot.cloud.app.timesheet.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.cloud.app.timesheet.entity.vo.UnfilledVo;
import com.springboot.cloud.app.timesheet.entity.form.UnfilledForm;
import com.springboot.cloud.app.timesheet.entity.form.UnfilledQueryForm;
import com.springboot.cloud.app.timesheet.entity.param.UnfilledQueryParam;
import com.springboot.cloud.app.timesheet.service.IUnfilledService;
import com.springboot.cloud.app.timesheet.entity.po.Unfilled;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import com.springboot.cloud.common.core.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @ClassName UnfilledController
 * @Description 旷工表
 */
@RestController
@Api(tags="【PC端】旷工模块")
@Slf4j
@RequestMapping(value = "/unfilled")
public class UnfilledController {

	@Autowired
	public IUnfilledService unfilledService;

	@ApiOperation(value = "新增一个旷工表", httpMethod = ConstantUtil.HTTP_POST, notes = "新增一个旷工表")
	@PostMapping("/")
	public Result add(@Valid @RequestBody  UnfilledForm unfilledForm) {
		log.info("saveUnfilled with name: {}", unfilledForm);
		try {
		    Unfilled unfilled = unfilledForm.toPo(Unfilled.class);
		    return Result.success(unfilledService.save(unfilled));
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "删除一个旷工表", httpMethod = ConstantUtil.HTTP_DELETE, notes = "删除一个旷工表")
	@ApiImplicitParam(name = "id", value = "旷工表ID（在请求URL上）", required = true, dataType = "long")
	@DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable long id) {
		log.info("deleteUnfilled with id: {}", id);
		try {
		    unfilledService.removeById(id);
			return Result.success();
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "修改一个旷工表",  httpMethod = ConstantUtil.HTTP_PUT, notes = "修改一个旷工表")
	@ApiImplicitParam(name = "id", value = "旷工表ID（在请求URL上）", required = true, dataType = "long")
	@PutMapping(value = "/{id}")
	public Result update(@PathVariable long id, @Valid @RequestBody UnfilledForm unfilledForm) {
		log.info("updateUnfilled with name: {}", unfilledForm);
		try {
		    Unfilled unfilled = unfilledForm.toPo(Unfilled.class);
            unfilled.setId(id);
            unfilledService.updateById(unfilled);
			return Result.success();
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail(e);
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "查看一个旷工表", httpMethod = ConstantUtil.HTTP_GET, notes = "查看一个旷工表 ")
	@ApiImplicitParam(name = "id", value = "旷工表ID（在请求URL上）", required = true, dataType = "long")
	@GetMapping(value = "/{id}")
    public Result<UnfilledVo> get(@PathVariable long id) {
		log.info("getUnfilled with id: {}", id);
		try {
			Unfilled unfilled = unfilledService.getById(id);
            return Result.success(unfilled);
		} catch (Exception e) {
			log.error("{}", e);
			return Result.fail();
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "旷工表 - 分页列表", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 有分页")
    @ApiImplicitParams({
             @ApiImplicitParam(name = "unfilledQueryForm", value = "unfilled查询参数", required = true, dataType = "unfilledQueryForm"),
             @ApiImplicitParam(name = "pageNum", value = "分页数", required = true, dataType = "int"),
             @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "int")
    })
    @PostMapping("/query")
    public Result<Page<UnfilledVo>> query(@Valid UnfilledQueryForm unfilledQueryForm,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize) {
         log.info("query with unfilledQueryForm:{}", unfilledQueryForm);
         if(unfilledQueryForm == null){
             unfilledQueryForm = new UnfilledQueryForm();
         }
         Page<Unfilled> page = new Page<Unfilled>(pageNum,pageSize);
//         page.setSearchCount(true);
         UnfilledQueryParam unfilledQueryParam = unfilledQueryForm.toParam(UnfilledQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         IPage<Unfilled> ipage = unfilledService.page(page,queryWrapper);
         IPage<UnfilledVo> ipageRes = new Page<UnfilledVo>();
         BeanUtils.copyProperties(ipage,ipageRes);
         List<Unfilled> unfilleds = ipage.getRecords();
         List<UnfilledVo> unfilledVos = unfilleds.stream().map(unfilled -> {
         	UnfilledVo unfilledVo = new UnfilledVo();
         	BeanUtils.copyProperties(unfilled,unfilledVo);
         	return unfilledVo;
         }).collect(Collectors.toList());
         ipageRes.setRecords(unfilledVos);
         return Result.success(ipageRes);
    }
//==================================================================================================================================================================
   @ApiOperation(value = "旷工表 - 无分页列表", httpMethod = ConstantUtil.HTTP_POST,notes ="根据条件搜索模版数据 - 无分页")
   @ApiImplicitParams({
                @ApiImplicitParam(name = "unfilledQueryForm", value = "unfilled查询参数", required = true, dataType = "unfilledQueryForm"),
   })
   @PostMapping("/queryAll")
   public Result<List<UnfilledVo>> queryAll(@Valid UnfilledQueryForm unfilledQueryForm,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize) {
         log.info("query with unfilledQueryForm:{}", unfilledQueryForm);
         if(unfilledQueryForm == null){
             unfilledQueryForm = new UnfilledQueryForm();
         }
         Page<Unfilled> page = new Page<Unfilled>(pageNum,pageSize);
//         page.setSearchCount(true);
         UnfilledQueryParam unfilledQueryParam = unfilledQueryForm.toParam(UnfilledQueryParam.class);
         QueryWrapper queryWrapper = new QueryWrapper();
         List<Unfilled> unfilleds = unfilledService.list(queryWrapper);
         List<UnfilledVo> unfilledVos = unfilleds.stream().map(unfilled -> {
              UnfilledVo unfilledVo = new UnfilledVo();
              BeanUtils.copyProperties(unfilled,unfilledVo);
              return unfilledVo;
         }).collect(Collectors.toList());
         return Result.success(unfilledVos);
    }
//==================================================================================================================================================================
    @ApiOperation(value = "分页获取所有旷工表",httpMethod = ConstantUtil.HTTP_POST)
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
			@DynamicParameter(name = "pageSize",value = "每一页的大小",example = "10",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pageNum",value = "总页数",example = "1",dataTypeClass = Integer.class)
	}))
    @PostMapping("/getUnfilledList")
    public Result getUnfilledList(@RequestBody JSONObject json){
		try {
			return Result.success(unfilledService.getPersonUnreportList(json));
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
	}
//==================================================================================================================================================================
	@ApiOperation(value = "分页导出所有旷工人员列表",httpMethod = ConstantUtil.HTTP_POST)
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
			@DynamicParameter(name = "pageSize",value = "每一页的大小",example = "10",dataTypeClass = Integer.class),
			@DynamicParameter(name = "pageNum",value = "总页数",example = "1",dataTypeClass = Integer.class)
	}))
	@PostMapping("/export")
	public void exportUnfilledList(@RequestBody JSONObject json, HttpServletResponse response){
		try {
			unfilledService.exportPersonUnreportList(json,response);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
//==================================================================================================================================================================
	@ApiOperation(value = "批量删除旷工表",httpMethod = ConstantUtil.HTTP_POST)
	@ApiOperationSupport(params = @DynamicParameters(name = "json", properties = {
			@DynamicParameter(name = "ids",value = "逗号隔开的id(1,2,3,4)",example = "1,2,3",dataTypeClass = String.class)
	}))
	@PostMapping("/batchDelete")
	public Result batchDeleteUnreport(@RequestBody JSONObject param){
		try {
			unfilledService.batchDeleteUnreport(param);
		}catch (Exception e){
			e.printStackTrace();
			return Result.fail();
		}
		return Result.success();
	}
}
