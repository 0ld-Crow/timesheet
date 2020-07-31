package com.springboot.cloud.app.timesheet.entity.form;

import com.springboot.cloud.common.core.entity.form.BaseQueryForm;
import com.springboot.cloud.app.timesheet.entity.po.Job;
import com.springboot.cloud.app.timesheet.entity.param.JobQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;




@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobQueryForm extends BaseQueryForm<JobQueryParam> {

//	@ApiModelProperty(value = "岗位ID",example = "1")
//	Long id;
	@ApiModelProperty(value = "岗位名称（通过岗位名称来模糊查询的话只需要填写这个类中的岗位名称属性",example = "后")
	String name;
}