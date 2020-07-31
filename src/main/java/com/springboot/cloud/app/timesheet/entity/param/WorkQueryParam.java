package com.springboot.cloud.app.timesheet.entity.param;

import com.springboot.cloud.common.core.entity.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @ClassName WorkQueryParam
 * @Description 工作记录表 - Param Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class WorkQueryParam extends BaseParam {

    /**主键*/
    private Long id;
    /**工作日期*/
    private Date workDate;
    /**工作时长*/
    private String hourTime;
    /**工作描述*/
    private String description;
    /**附加字段*/
    private String attach;
    /**备注*/
    private String remark;
    /**是否删除（0:否|1:是）*/
    private Integer isDelete;

}
