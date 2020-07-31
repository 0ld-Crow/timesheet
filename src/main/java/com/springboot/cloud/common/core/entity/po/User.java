package com.springboot.cloud.common.core.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
@TableName("users")
public class User extends BasePo{
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID",example = "1")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户手机号")
    private String mobile;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "")
    private Boolean enabled;

    @ApiModelProperty(value = "")
    private Boolean accountNonExpired;

    @ApiModelProperty(value = "")
    private Boolean credentialsNonExpired;

    @ApiModelProperty(value = "")
    private Boolean accountNonLocked;

    @ApiModelProperty(value = "")
    private Boolean isNew;
}