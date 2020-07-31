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



@TableName("t_job")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_job对象", description="岗位信息表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job extends BasePo {
    

	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "岗位信息表ID",example = "1")
	Long id;

	@ApiModelProperty(value = "岗位名称",example = "后端开发工程师")
 	String name;

	@ApiModelProperty(value = "是否禁用(0:否|1:是)",example = "0")
	Integer isBan;

	@ApiModelProperty(value = "附加字段",example = "附加字段")
	String attach;

	@ApiModelProperty(value = "备注",example = "备注")
	String remark;

	@ApiModelProperty(value = "是否删除(0:否|1:是)",example = "0")
	Integer isDelete;
}