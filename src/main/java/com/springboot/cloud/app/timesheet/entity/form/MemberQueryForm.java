package com.springboot.cloud.app.timesheet.entity.form;

import com.springboot.cloud.common.core.entity.form.BaseQueryForm;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.app.timesheet.entity.param.MemberQueryParam;
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
public class MemberQueryForm extends BaseQueryForm<MemberQueryParam> {

    @ApiModelProperty(value = "是否删除（0为否，1为是）",example = "1")
    Integer isDelete;
    @ApiModelProperty(value = "用户名称",example = "小明")
    String username;
//    @ApiModelProperty(value = "",example = "")
//    String nick_name;
}