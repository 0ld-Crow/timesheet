package com.springboot.cloud.app.timesheet.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.common.core.entity.vo.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目工时统计列表
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWorkTimeVo extends BaseVo {

    @ApiModelProperty(value = "项目id")
    Long pId;

    @ApiModelProperty(value = "项目名称")
    String projectName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty(value = "起始日期")
    Date startDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty(value = "截止日期")
    Date endDate;

    @ApiModelProperty(value = "工时总计")
    BigDecimal totalWorkDate;

    @ApiModelProperty(value = "人日总计")
    BigDecimal dayPerOne;

    @ApiModelProperty(value = "人员名称")
    String personName;

    @ApiModelProperty(value = "岗位名称")
    String postName;
}
