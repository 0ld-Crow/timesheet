package com.springboot.cloud.common.core.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
@TableName("t_roles")
public class Role extends BasePo {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "角色id")
    private Long id;
    @ApiModelProperty(value = "角色编码")
    private String code;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;
}
