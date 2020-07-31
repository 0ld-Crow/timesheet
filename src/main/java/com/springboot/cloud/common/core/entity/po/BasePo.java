package com.springboot.cloud.common.core.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @ClassName BasePo
 * @Description
 */
@Data
public class BasePo implements Serializable {

    public final static String DEFAULT_USERNAME = "system";
    @JsonIgnore
    private Long id = 0L;
    @JsonIgnore
    private String createdBy = DEFAULT_USERNAME;
    @JsonIgnore
    private String updatedBy = DEFAULT_USERNAME;
//    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date createdTime = Date.from(ZonedDateTime.now().toInstant());
    @JsonIgnore
    private Date updatedTime = Date.from(ZonedDateTime.now().toInstant());

}

