package com.springboot.cloud.app.timesheet.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当天没有进行汇报的人
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayNoReportPersonVo {

    @ApiModelProperty(value = "人员id",example = "1")
    Long memberId;

    @ApiModelProperty(value = "姓名",example = "小明")
    String realName;
}
