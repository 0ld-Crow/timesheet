package com.springboot.cloud.app.timesheet.entity.form;

import com.springboot.cloud.common.core.entity.form.BaseForm;
import com.springboot.cloud.app.timesheet.entity.po.Work;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;



@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkForm extends BaseForm<Work> {
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "工作日期",example = "2020-07-27")
	Date workDate;
	@ApiModelProperty(value = "人员id",example = "1")
	Long uId;
	@ApiModelProperty(value = "项目id",example = "1")
	Long pId;
	@ApiModelProperty(value = "工时",example = "1")
	BigDecimal hourTime;
	@ApiModelProperty(value = "描述",example = "描述")
	String description;
}
