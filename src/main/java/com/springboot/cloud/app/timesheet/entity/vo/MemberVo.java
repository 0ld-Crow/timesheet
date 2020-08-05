package com.springboot.cloud.app.timesheet.entity.vo;

import java.io.Serializable;
import java.util.Date;
import com.springboot.cloud.common.core.entity.vo.BaseVo;

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

    @ApiModelProperty(value = "用户ID",example = "1")
    Long uid;

    @ApiModelProperty(value = "用户姓名",example = "小明")
    String name;

    @ApiModelProperty(value = "用户名",example = "明哥")
    String username;

    @ApiModelProperty(value = "用户电话",example = "123456789")
    String phone;

    @ApiModelProperty(value = "角色ID",example = "1")
    Long roleId;

    @ApiModelProperty(value = "角色名称",example = "管理员")
    String roleName;

    @ApiModelProperty(value = "状态",example = "")
    String status;

    @ApiModelProperty(value = "岗位",example = "后端开发")
    String job;

    @ApiModelProperty(value = "是否删除",example = "0")
    int isDelete;
}