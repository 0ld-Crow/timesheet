package com.springboot.cloud.common.core.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
public class RoleVo extends BaseVo {

    @ApiModelProperty(value = "角色id",example = "1")
    private Long id;

    @ApiModelProperty(value = "角色编码",example = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色名称",example = "小明")
    private String name;

    @ApiModelProperty(value = "描述",example = "描述")
    private String description;

    @ApiModelProperty(value = "角色状态",example = "角色状态")
    private Boolean status;

//    @ApiModelProperty(value = "",example = "1")
//    private Long domianId;

    @ApiModelProperty(value = "角色创建时间",example = "2020-11-11")
    @JsonFormat(pattern = "yyyy-MM-dd ", timezone="GMT+8")
    private Date createdTime;
}
