package com.springboot.cloud.app.timesheet.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryParam {
    @ApiModelProperty(value = "页数",example = "100")
    Integer pageNum;
    @ApiModelProperty(value = "一页大小",example = "15")
    Integer pageSize;
}
