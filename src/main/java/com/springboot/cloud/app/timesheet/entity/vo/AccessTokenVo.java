package com.springboot.cloud.app.timesheet.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.common.core.entity.vo.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenVo{

	@ApiModelProperty(value = "AccessTokenVoID",example = "1")
	Long id;

	@ApiModelProperty(value = "accessToke",example = "dskjhfisdhfbhfvhefvhioeahfioahvuia")
	String accessToke;

	@ApiModelProperty(value = "过期时间戳",example = "100000")
	Long expiresIn;

	@ApiModelProperty(value = "类型，0：普通类型，1：用于通讯录使用",example = "0")
	Byte type;

}
