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

/**
 * @ClassName UnfilledQueryForm
 * @Description 工作未填写表
 * @Author cj
 * @Date 2019-11-11
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnfilledQueryForm extends BaseQueryForm<UnfilledQueryParam> {

    Integer isDelete;
}