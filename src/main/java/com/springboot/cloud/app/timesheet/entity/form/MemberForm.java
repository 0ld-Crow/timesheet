package com.springboot.cloud.app.timesheet.entity.form;

import com.springboot.cloud.common.core.entity.form.BaseForm;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Pattern;
/**
 * @ClassName MemberForm
 * @Description 会员信息表
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm extends BaseForm<Member> {
    @ApiModelProperty(value = "用户id")
    Long id;
    @ApiModelProperty(value = "角色id(1员工|2人员)",example = "(1员工|2人员)")
    Long roleId;
    @ApiModelProperty(value = "真实姓名")
    String realName;
    @ApiModelProperty(value = "岗位属性")
    String job;
    @ApiModelProperty(value = "用户名")
    String username;
    @ApiModelProperty(value = "密码")
    String password;
    @ApiModelProperty(value = "附加字段")
    String attach ;
    @ApiModelProperty(value = "备注")
    String remark ;
//    @ApiModelProperty(value = "是否删除")
//    Integer isDelete;
//    @ApiModelProperty(value = "是否禁用")
//    Integer isBan;
    @ApiModelProperty(value = "salt")
    String salt;
}