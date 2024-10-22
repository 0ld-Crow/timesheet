package com.springboot.cloud.app.timesheet.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.common.core.entity.vo.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工作记录汇总
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryVo extends BaseVo  {

	@ApiModelProperty(value = "ids",example = "1")
	Long ids;

	@ApiModelProperty(value = "用户ID",example = "1")
	Long uId;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	@ApiModelProperty(value = "工作日期",example = "1")
	Date workDate;

	@ApiModelProperty(value = "类型：1：:已提交，0：未提交",example = "1")
	int type;

}
