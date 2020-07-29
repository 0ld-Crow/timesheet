package com.springboot.cloud.app.timesheet.entity.vo;

import java.io.Serializable;
import java.util.Date;
import com.springboot.cloud.common.core.entity.vo.BaseVo;
import com.springboot.cloud.common.core.aspect.Dict;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVo extends BaseVo {

    @ApiModelProperty(value = "用户ID")
    Long uid;

    @ApiModelProperty(value = "用户姓名")
    String name;

    @ApiModelProperty(value = "用户名")
    String username;

    @ApiModelProperty(value = "用户电话")
    String phone;

    @ApiModelProperty(value = "角色ID")
    Long roleId;

    @ApiModelProperty(value = "角色名称")
    String roleName;

    @ApiModelProperty(value = "状态")
    String status;

    @ApiModelProperty(value = "岗位")
    String job;
}