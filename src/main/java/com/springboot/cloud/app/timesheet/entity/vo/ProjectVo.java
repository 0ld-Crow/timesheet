package com.springboot.cloud.app.timesheet.entity.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.common.core.entity.vo.BaseVo;
import com.springboot.cloud.common.core.aspect.Dict;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVo extends BaseVo {

    @ApiModelProperty(value = "ProjectVoID",example = "1")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "是否禁用(0:否|1:是)")
    private Integer isBan;

    @ApiModelProperty(value = "是否删除(0:否|1:是)")
    private Integer isDelete;

    @ApiModelProperty(value = "附加字段")
    private String attach;

    @ApiModelProperty(value = "备注")
    private String remark;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
//    @ApiModelProperty(value = "创建时间", example = "2019-07-01 17:00:00")
//    Date createdTime;


}