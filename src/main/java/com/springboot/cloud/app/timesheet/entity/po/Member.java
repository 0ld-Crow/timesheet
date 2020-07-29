package com.springboot.cloud.app.timesheet.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.springboot.cloud.common.core.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;


@TableName("t_member")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_member对象", description="会员信息表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BasePo {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "会员信息表ID")
    Long id;

    @ApiModelProperty(value = "角色ID(1人员|2员工)")
    Long roleId;

    @ApiModelProperty(value = "真实姓名")
    String realName;

    @ApiModelProperty(value = "岗位属性")
    String job;

    @ApiModelProperty(value = "电话")
    String phone;

    @ApiModelProperty(value = "用户名")
    String username;

    @ApiModelProperty(value = "密码")
    String password;

    @ApiModelProperty(value = "附加字段")
    String attach ;

    @ApiModelProperty(value = "备注")
    String remark ;

    @ApiModelProperty(value = "是否删除(0:否|1:是)")
    Integer isDelete;

    @ApiModelProperty(value = "是否禁用(0:否|1:是)")
    Integer isBan;

    @ApiModelProperty(value = "盐")
    String salt;

    @ApiModelProperty(value = "访问token")
    String accessToken;

    @ApiModelProperty(value = "刷新token")
    String refreshToken;

    @ApiModelProperty(value = "最后登录IP")
    String loginIp;

    @ApiModelProperty(value = "过期时间")
    Date expireTime;

    @ApiModelProperty(value = "最后登录时间")
    Date loginTime;

    @ApiModelProperty(value = "用户ID")
    String userId;
}