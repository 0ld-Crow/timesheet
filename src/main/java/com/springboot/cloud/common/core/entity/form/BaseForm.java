package com.springboot.cloud.common.core.entity.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.cloud.common.core.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @ClassName BaseForm
 * @Description
 */
@ApiModel
@Slf4j
@Data
public class BaseForm<T extends BasePo> {

    @ApiModelProperty(value = "创建用户ID", example = "1")
    private String createdBy = "System";

    @ApiModelProperty(value = "更新用户ID", example = "1")
    private String updatedBy = "System";

    @ApiModelProperty(value = "创建时间", example = "2019-07-01 17:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdTime = Date.from(ZonedDateTime.now().toInstant());

    @ApiModelProperty(value = "更新时间", example = "2019-07-01 17:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedTime = Date.from(ZonedDateTime.now().toInstant());

    /**
     * From转化为Po，进行后续业务处理
     *
     * @param clazz
     * @return
     */
    public T toPo(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Po NewInstance Error");
        }
        BeanUtils.copyProperties(this, t);
        return t;
    }

}
