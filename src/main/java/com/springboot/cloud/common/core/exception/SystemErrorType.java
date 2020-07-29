package com.springboot.cloud.common.core.exception;

import lombok.Getter;



@Getter
public enum SystemErrorType implements ErrorType {

    //系统异常
    SYSTEM_ERROR("-1", "系统异常"),

    //系统繁忙,请稍候再试
    SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),

    //服务未找到
    GATEWAY_NOT_FOUND_SERVICE("010404", "服务未找到"),

    //网关异常
    GATEWAY_ERROR("010500", "网关异常"),

    //网关超时
    GATEWAY_CONNECT_TIME_OUT("010002", "网关超时"),

    //请求参数校验不通过
    OBJECT_DUPLICATED("020002", "请求参数已存在，参数名为%s"),

    //请求参数校验不通过
    ARGUMENT_NOT_VALID("020000", "请求参数校验不通过"),

    //上传文件大小超过限制
    UPLOAD_FILE_SIZE_LIMIT("020001", "上传文件大小超过限制"),

    ACCOUNT_VERIFY_ERROR("030000", "账号验证错误"),

    TOKEN_UNVALID("030000", "Token验证失败"),

    ACCOUNT_INVALID("030000", "账号已失效，请重新登陆！"),

    UN_AUTHORIZATION("034000","未授权的访问!");

    /**
     * 错误类型码
     */
    private String code;

    /**
     * 错误类型描述信息
     */
    private String mesg;

    SystemErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }

    SystemErrorType(ErrorType errorType,String ... params) {
        this.code = errorType.getCode();
        this.mesg = String.format(errorType.getMesg(),params);
    }
}
