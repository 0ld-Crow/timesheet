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

    @ApiModelProperty(value = "用户ID",example = "1")
    private Long id;

    @ApiModelProperty(value = "用户名称",example = "小明")
    private String name;

    @ApiModelProperty(value = "用户手机号",example = "1")
    private String mobile;

    @ApiModelProperty(value = "",example = "")
    private Boolean enabled;

    @ApiModelProperty(value = "",example = "")
    private String[] scope;

    @ApiModelProperty(value = "用户的权限",example = "[1,2,3,4,5]")
    private String[] permission;

    @ApiModelProperty(value = "",example = "")
    private Boolean accountNonLocked;

    @ApiModelProperty(value = "",example = "")
    private Boolean isNew;

    @ApiModelProperty(value = "",example = "")
    private String accessToken;

    @ApiModelProperty(value = "",example = "")
    private String refreshToken;
}