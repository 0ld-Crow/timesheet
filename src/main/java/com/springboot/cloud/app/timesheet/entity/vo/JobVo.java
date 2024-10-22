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
public class JobVo extends BaseVo  {

	@ApiModelProperty(value = "岗位信息表ID",example = "1")
	Long id;

}