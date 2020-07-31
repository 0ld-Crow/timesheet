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
@ApiModel(value="t_member对象", description="员工信息表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BasePo {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "会员信息表ID",example = "1")
    Long id;

    @ApiModelProperty(value = "角色ID(1人员|2员工)",example = "1")
    Long roleId;

    @ApiModelProperty(value = "真实姓名",example = "邢小明")
    String realName;

    @ApiModelProperty(value = "岗位属性",example = "后端开发工程师")
    String job;

    @ApiModelProperty(value = "电话",example = "123456789")
    String phone;

    @ApiModelProperty(value = "用户名",example = "小明哥")
    String username;

    @ApiModelProperty(value = "密码",example = "123456")
    String password;

    @ApiModelProperty(value = "附加字段",example = "附加字段")
    String attach ;

    @ApiModelProperty(value = "备注",example = "备注")
    String remark ;

    @ApiModelProperty(value = "是否删除(0:否|1:是)",example = "0")
    Integer isDelete;

    @ApiModelProperty(value = "是否禁用(0:否|1:是)",example = "0")
    Integer isBan;

    @ApiModelProperty(value = "salt",example = "salt")
    String salt;

    @ApiModelProperty(value = "访问token",example = "faitibrhafbkjahfgahfau")
    String accessToken;

    @ApiModelProperty(value = "刷新token",example = "ruowiethiuerwhtivruevybitu")
    String refreshToken;

    @ApiModelProperty(value = "最后登录IP",example = "172.18.1.29")
    String loginIp;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "过期时间",example = "2020-07-27")
    Date expireTime;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "最后登录时间",example = "2020-07-27")
    Date loginTime;

    @ApiModelProperty(value = "用户ID",example = "")
    String userId;
}