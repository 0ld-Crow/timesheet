package com.springboot.cloud.common.core.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;

import java.util.Date;


public class BaseVo {

    @ApiModelProperty(value = "id",example = "1")
    int id;

    @ApiModelProperty(value = "附加字段",example = "附加字段")
    String attach;

    @ApiModelProperty(value = "备注",example = "备注")
    String remark;

    @ApiModelProperty(value = "是否删除(0:否|1:是)",example = "0")
    int isDelete;

    @ApiModelProperty(value = "创建人",example = "小明")
    String createdBy;

    @ApiModelProperty(value = "更新人",example = "小红")
    String updatedBy;

    @ApiModelProperty(value = "创建时间",example = "2020-11-11")
    @JsonFormat(pattern = "yyyy-MM-dd ", timezone="GMT+8")
    Date createdTime;

    @ApiModelProperty(value = "更新时间",example = "2020-11-11")
    @JsonFormat(pattern = "yyyy-MM-dd ", timezone="GMT+8")
    Date updatedTime;

}
