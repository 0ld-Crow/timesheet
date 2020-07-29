package com.springboot.cloud.app.timesheet.entity.param;

import com.springboot.cloud.common.core.entity.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @ClassName JobQueryParam
 * @Description 岗位信息表 - Param Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobQueryParam extends BaseParam {

    /**主键*/
    private Long id;
    /**岗位名称*/
    private String name;
    /**是否禁用*/
    private Integer isBan;
    /**附加字段*/
    private String attach;
    /**备注*/
    private String remark;
    /**是否删除*/
    private Integer isDelete;

}
