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



@TableName("t_unfilled")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_unfilled对象", description="未填写的工作记录表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unfilled extends BasePo {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "未填写的工作记录表ID")
    Long id;

    @ApiModelProperty(value = "员工的id")
    Long uId;

    @ApiModelProperty(value = "员工缺勤的日期")
    Date workDate;

    @ApiModelProperty(value = "附加字段")
    String attach;

    @ApiModelProperty(value = "备注")
    String remark;

    @ApiModelProperty(value = "是否删除(0:否|1:是)")
    Integer isDelete;
}