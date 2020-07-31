package com.springboot.cloud.app.timesheet.entity.form;

import com.springboot.cloud.common.core.entity.form.BaseQueryForm;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.springboot.cloud.app.timesheet.entity.param.ProjectQueryParam;
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
public class ProjectQueryForm extends BaseQueryForm<ProjectQueryParam> {

//    @ApiModelProperty(value = "项目ID",example = "1")
//    private Long id;
    @ApiModelProperty(value = "项目名称（如果是用name俩模糊查询的话就只需要填写这个）",example = "商城项目")
    private String name;
//    @ApiModelProperty(value = "是否禁用（0为否，1为是）",example = "1")
//    private Integer isBan;
//    @ApiModelProperty(value = "附加字段",example = "附加字段")
//    private String attach;
//    @ApiModelProperty(value = "备注",example = "备注")
//    private String remark;
//    @ApiModelProperty(value = "是否删除（0为否，1为是）",example = "1")
//    private Integer isDelete;

}