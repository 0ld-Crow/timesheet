package com.springboot.cloud.common.core.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  类描述:  字典注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    /**
     * 方法描述:  数据code
     * @return 返回类型： String
     */
    String dicCode();

    /**
     * 方法描述:  数据Text
     * @return 返回类型： String
     */
    String dicText() default "";

    /**
     * 方法描述: 数据字典表
     * @return 返回类型： String
     */
    String dictTable() default "";
}

