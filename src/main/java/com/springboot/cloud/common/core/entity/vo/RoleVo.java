package com.springboot.cloud.common.core.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
public class RoleVo extends BaseVo {

    @ApiModelProperty(value = "角色id")
    private Long id;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "角色状态")
    private Boolean status;

    @ApiModelProperty(value = "")
    private Long domianId;

    @ApiModelProperty(value = "角色创建时间")
    private Date createdTime;
}
