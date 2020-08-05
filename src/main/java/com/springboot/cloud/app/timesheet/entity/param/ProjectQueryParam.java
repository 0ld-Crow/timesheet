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
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectQueryParam extends BaseParam {


    @ApiModelProperty(value = "主键id",example = "1")
    private Long id;
    @ApiModelProperty(value = "项目名称",example = "商城项目")
    private String name;
    @ApiModelProperty(value = "是否禁用（0:否|1:是）",example = "0")
    private Integer isBan;
    @ApiModelProperty(value = "附加字段",example = "附加字段")
    private String attach;
    @ApiModelProperty(value = "备注",example = "备注")
    private String remark;
    @ApiModelProperty(value = "是否删除（0:否|1:是）",example = "0")
    private Integer isDelete;

}
