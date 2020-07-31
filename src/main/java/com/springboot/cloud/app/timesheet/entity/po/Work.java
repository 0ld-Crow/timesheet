package com.springboot.cloud.app.timesheet.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.common.core.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@TableName("t_work")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_work对象", description="工作记录表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Work extends BasePo {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "工作记录表ID",example = "1")
    Long id;

    @ApiModelProperty(value = "项目ID",example = "1")
    Long pId;

    @ApiModelProperty(value = "用户ID",example = "1")
    Long uId;
    //用mybatis-plus的话底层返回的时间默认是美国时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "员工缺勤的日期",example = "2020-07-27")
    Date workDate;

    @ApiModelProperty(value = "工作时长")
    BigDecimal hourTime;

    @ApiModelProperty(value = "工作描述")
    String description;

    @ApiModelProperty(value = "附加字段")
    String attach;

    @ApiModelProperty(value = "备注")
    String remark;

    @ApiModelProperty(value = "是否删除(0:否|1:是)")
    Integer isDelete;
}
