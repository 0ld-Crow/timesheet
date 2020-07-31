package com.springboot.cloud.app.timesheet.entity.form;

import com.springboot.cloud.common.core.entity.form.BaseQueryForm;
import com.springboot.cloud.app.timesheet.entity.po.Work;
import com.springboot.cloud.app.timesheet.entity.param.WorkQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class WorkQueryForm extends BaseQueryForm<WorkQueryParam> {
	@ApiModelProperty(value = "是否删除（0为否，1为是）",example = "0")
	Integer isDelete;
}
