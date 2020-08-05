package com.springboot.cloud.app.timesheet.entity.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.common.core.entity.vo.BaseVo;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *  当天未填写工作记录表的人的unfilled表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnfilledVo extends BaseVo {

    @ApiModelProperty(value = "未填写的工作记录表ID",example = "1")
    Long id;

    @ApiModelProperty(value = "用户ID",example = "1")
    Long uId;

    @ApiModelProperty(value = "用户名称",example = "小明")
    String userName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty(value = "工作日期")
    Date workDate;

    @ApiModelProperty(value = "附加字段",example = "附加字段")
    String attach;

    @ApiModelProperty(value = "备注",example = "备注")
    String remark;
}