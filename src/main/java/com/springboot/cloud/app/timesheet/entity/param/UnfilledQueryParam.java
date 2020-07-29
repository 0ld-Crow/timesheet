package com.springboot.cloud.app.timesheet.entity.param;

import com.springboot.cloud.common.core.entity.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @ClassName UnfilledQueryParam
 * @Description 工作未填写表 - Param Object
 * @Author cj
 * @Date 2019-11-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnfilledQueryParam extends BaseParam {

    /**主键*/
    private Long id;
    /**未填日期*/
    private Date workDate;
    /**附加字段*/
    private String attach;
    /**备注*/
    private String remark;
    /**是否删除（0:否|1:是）*/
    private Integer isDelete;

}
