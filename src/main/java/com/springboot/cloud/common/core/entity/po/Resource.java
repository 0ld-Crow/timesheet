package com.springboot.cloud.common.core.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("resources")
public class Resource extends BasePo {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long domainId;
    private String code;
    private String name;
    private String type;
    private String url;
    private String method;
}
