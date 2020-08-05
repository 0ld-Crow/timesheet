package com.springboot.cloud.common.core.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;


@Data
public class BasePo implements Serializable {
    @ApiModelProperty(value = "默认用户名")
    public final static String DEFAULT_USERNAME = "system";
    @JsonIgnore
    @ApiModelProperty(value = "主键ID")
    private Long id = 0L;
    @JsonIgnore
    @ApiModelProperty(value = "创建者（默认是system）")
    private String createdBy = DEFAULT_USERNAME;
    @JsonIgnore
    @ApiModelProperty(value = "更新者（默认是system）")
    private String updatedBy = DEFAULT_USERNAME;
    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date createdTime = Date.from(ZonedDateTime.now().toInstant());
    @JsonIgnore
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date updatedTime = Date.from(ZonedDateTime.now().toInstant());

}

