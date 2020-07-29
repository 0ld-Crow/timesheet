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


@TableName("t_project")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_project对象", description="项目信息表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BasePo {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "项目信息表ID")
    Long id;

    @ApiModelProperty(value = "项目名称")
    String name;

    @ApiModelProperty(value = "是否禁用")
    Integer isBan;

    @ApiModelProperty(value = "附加字段")
    String attach;

    @ApiModelProperty(value = "备注")
    String remark;

    @ApiModelProperty(value = "是否删除")
    Integer isDelete;
}