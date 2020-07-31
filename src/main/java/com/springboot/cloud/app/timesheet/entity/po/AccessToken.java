package com.springboot.cloud.app.timesheet.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.springboot.cloud.common.core.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@TableName("t_access_token")
/*
@EqualsAndHashCode注解的作用就是自动的给model bean实现equals方法和hashcode方法。
【他默认是不调用父类的方法的】
 */
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_access_token对象", description="AccessToken表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken{

    //数据库中自己实现了自增长
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "AccessToken表ID",example = "1")
    Long id;
    @ApiModelProperty(value = "accessToke",example = "dafgfffgnfgdsfgnytjukuiiipowaqw")
    String accessToke;
    @ApiModelProperty(value = "过期时间戳",example = "100000")
    Long expiresIn;
    @ApiModelProperty(value = "类型，0：普通类型，1：用于通讯录使用",example = "0")
    Byte type;
}
