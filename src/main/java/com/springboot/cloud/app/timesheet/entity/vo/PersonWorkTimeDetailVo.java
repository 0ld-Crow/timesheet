package com.springboot.cloud.app.timesheet.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 人员工时明细
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonWorkTimeDetailVo {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty(value = "工作日期",example = "2020-11-11")
    Date workDate;

    @ApiModelProperty(value = "人员名字",example = "小明")
    String realName;

    @ApiModelProperty(value = "项目名称",example = "商城项目")
    String projectName;

    @ApiModelProperty(value = "工作工时",example = "5")
    BigDecimal hourTime;

    @ApiModelProperty(value = "描述",example = "描述")
    String description;
}
