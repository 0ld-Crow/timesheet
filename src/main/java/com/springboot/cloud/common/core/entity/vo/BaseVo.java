package com.springboot.cloud.common.core.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;

import java.util.Date;

/**
 * @ClassName BaseVo
 * @Description
 */
public class BaseVo {

    @ApiModelProperty(value = "id")
    int id;

    @ApiModelProperty(value = "附加字段")
    String attach;

    @ApiModelProperty(value = "备注")
    String remark;

    @ApiModelProperty(value = "是否删除(0:否|1:是)")
    int isDelete;

    @ApiModelProperty(value = "创建人")
    String createdBy;

    @ApiModelProperty(value = "更新人")
    String updatedBy;

    @ApiModelProperty(value = "创建时间")
    Date createdTime;

    @ApiModelProperty(value = "更新时间")
    Date updatedTime;

}
