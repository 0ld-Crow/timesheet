package com.springboot.cloud.app.timesheet.entity.param;

import com.springboot.cloud.common.core.entity.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @ClassName ProjectQueryParam
 * @Description 项目信息表 - Param Object
 * @Author cj
 * @Date 2019-11-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectQueryParam extends BaseParam {

    /**主键*/
    @ApiModelProperty(value = "主键id")
    private Long id;
    /**项目名称*/
    @ApiModelProperty(value = "项目名称")
    private String name;
    /**是否禁用（0:否|1:是）*/
    @ApiModelProperty(value = "是否禁用")
    private Integer isBan;
    /**附加字段*/
    @ApiModelProperty(value = "附加字段")
    private String attach;
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String remark;
    /**是否删除（0:否|1:是）*/
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

}
