package com.springboot.cloud.common.core.entity.vo;

import com.springboot.cloud.common.core.entity.po.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
public class UserVo extends BaseVo{

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "用户手机号")
    private String mobile;

    @ApiModelProperty(value = "")
    private Boolean enabled;

    @ApiModelProperty(value = "")
    private String[] scope;

    @ApiModelProperty(value = "用户的权限")
    private String[] permission;

    @ApiModelProperty(value = "")
    private Boolean accountNonLocked;

    @ApiModelProperty(value = "")
    private Boolean isNew;

    @ApiModelProperty(value = "")
    private String accessToken;

    @ApiModelProperty(value = "")
    private String refreshToken;
}