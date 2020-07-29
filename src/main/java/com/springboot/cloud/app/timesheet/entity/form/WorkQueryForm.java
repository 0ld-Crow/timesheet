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

/**
 * @ClassName WorkQueryForm
 * @Description 工作记录表
 * @Author cj
 * @Date 2019-11-11
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class WorkQueryForm extends BaseQueryForm<WorkQueryParam> {

	Integer isDelete;
}
