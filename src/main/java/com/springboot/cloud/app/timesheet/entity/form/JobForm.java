package com.springboot.cloud.app.timesheet.entity.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.springboot.cloud.common.core.entity.form.BaseForm;
import com.springboot.cloud.app.timesheet.entity.po.Job;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Pattern;



@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobForm extends BaseForm<Job> {

	@ApiModelProperty(value = "岗位ID",example = "1")
	Long id;
	@ApiModelProperty(value = "岗位名称",example = "后端开发")
	String name;
	@ApiModelProperty(value = "是否禁用（0为否，1为是）",example = "1")
	Integer isBan;
	@ApiModelProperty(value = "附加字段",example = "附加字段")
	String attach;
	@ApiModelProperty(value = "备注",example = "备注")
	String remark;
	@ApiModelProperty(value = "是否删除（0为否，1为是）",example = "1")
	Integer isDelete;
}