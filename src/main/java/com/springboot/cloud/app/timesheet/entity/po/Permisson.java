package com.springboot.cloud.app.timesheet.entity.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.springboot.cloud.common.core.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@TableName("t_permisson")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_permisson对象", description="权限信息表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permisson extends BasePo {


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "权限表的ID",example = "1")
    Long id;

    @ApiModelProperty(value = "角色的ID",example = "1")
    Long rolesId;

    @ApiModelProperty(value = "描述",example = "描述")
    String description;

    @ApiModelProperty(value = "是否删除(0:否|1:是)",example = "1")
    Integer isDelete;
}
