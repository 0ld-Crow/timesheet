package com.springboot.cloud.app.timesheet.entity.form;

import com.springboot.cloud.common.core.entity.form.BaseQueryForm;
import com.springboot.cloud.app.timesheet.entity.po.Unfilled;
import com.springboot.cloud.app.timesheet.entity.param.UnfilledQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnfilledQueryForm extends BaseQueryForm<UnfilledQueryParam> {
    @ApiModelProperty(value = "是否删除（0为否，1为是）",example = "0")
    Integer isDelete;
}