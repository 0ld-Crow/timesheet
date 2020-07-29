package com.springboot.cloud.app.timesheet.entity.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.common.core.entity.vo.BaseVo;
import com.springboot.cloud.common.core.aspect.Dict;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @ClassName UnfilledVo
 * @Description 未填写的工作记录表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnfilledVo extends BaseVo {

    @ApiModelProperty(value = "未填写的工作记录表ID")
    Long id;

    @ApiModelProperty(value = "用户ID")
    Long uId;

    @ApiModelProperty(value = "用户名称")
    String userName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty(value = "工作日期")
    Date workDate;

    @ApiModelProperty(value = "附加字段")
    String attach;

    @ApiModelProperty(value = "备注")
    String remark;
}