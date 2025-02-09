package com.springboot.cloud.app.timesheet.entity.form;

import com.springboot.cloud.common.core.entity.form.BaseForm;
import com.springboot.cloud.app.timesheet.entity.po.Unfilled;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;


@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnfilledForm extends BaseForm<Unfilled> {
    @ApiModelProperty(value = "员工的id",example = "1")
    Long uId;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "工作日期",example = "2020-07-27")
    Date workDate;
    @ApiModelProperty(value = "附加字段",example = "附加字段")
    String attach;
    @ApiModelProperty(value = "备注",example = "备注")
    String remark;
    @ApiModelProperty(value = "是否删除（0为否，1为是）",example = "0")
    Integer isDelete;
}