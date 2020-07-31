package com.springboot.cloud.app.timesheet.entity.param;

import com.springboot.cloud.common.core.entity.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @ClassName MemberQueryParam
 * @Description 会员信息表 - Param Object
 * @Author cj
 * @Date 2019-11-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberQueryParam extends BaseParam {

    /**主键*/
    private Long id;
    /**角色ID*/
    private Long roleId;
    /**账号*/
    private String username;
    /**密码*/
    private String password;
    /**盐*/
    private String salt;
    /**昵称*/
    private String nickName;
    /**性别（0:女|1:男|2:保密）*/
    private Integer sex;
    /**联系电话*/
    private String phone;
    /**邮箱*/
    private String email;
    /**职位*/
    private String job;
    /**头像*/
    private String photo;
    /**最后登录IP*/
    private String loginIp;
    /**最后登录时间*/
    private Date loginTime;
    /**登录次数*/
    private Integer loginQty;
    /**是否禁用（0:否|1:是）*/
    private Integer isBan;
    /**访问token*/
    private String accessToken;
    /**刷新token*/
    private String refreshToken;
    /**过期时间*/
    private Date expireTime;
    /**附加字段*/
    private String attach;
    /**备注*/
    private String remark;
    /**是否删除（0:否|1:是）*/
    private Integer isDelete;

}
