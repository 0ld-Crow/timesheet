package com.springboot.cloud.common.core.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.common.core.exception.BaseException;
import com.springboot.cloud.common.core.exception.ErrorType;
import com.springboot.cloud.common.core.exception.SystemErrorType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
//import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.time.ZoneId;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Result<T>
 * @Description
 */
@ApiModel(value = "返回说明", description = "rest请求的返回模型，所有rest正常都返回该类的对象")
@Getter
public class Result<T> {

    public static final String SUCCESSFUL_CODE = "000000";
    public static final String SUCCESSFUL_MESG = "处理成功";
    public static final String WARNINGFUL_CODE = "500000";

    @ApiModelProperty(value = "处理结果code", required = true)
    private String code;
    @ApiModelProperty(value = "处理结果描述信息")
    private String mesg;
    @ApiModelProperty(value = "请求结果生成时间戳")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Instant timestamp;
    @ApiModelProperty(value = "处理结果数据信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result() {
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType
     */
    public Result(ErrorType errorType) {
        this.code = errorType.getCode();
        this.mesg = errorType.getMesg();
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType
     * @param data
     */
    public Result(ErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }

    /**
     * @param message
     */
    public Result(String message) {
        this.code = "500";
        this.mesg = message;
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param mesg
     * @param data
     */
    private Result(String code, String mesg, T data) {
        this.code = code;
        this.mesg = mesg;
        this.data = data;
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static Result success(Object data) {
        return new Result<>(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static Result fail() {
        return new Result(SystemErrorType.SYSTEM_ERROR);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException
     * @return Result
     */
    public static Result fail(BaseException baseException) {
        return fail(baseException, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static Result fail(BaseException baseException, Object data) {
        return new Result<>(baseException.getErrorType(), data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType
     * @param data
     * @return Result
     */
    public static Result fail(ErrorType errorType, Object data) {
        return new Result<>(errorType, data);
    }

    /**
     * 系统异常类并返回结果数据
     *failWithCustomerMessage
     * @param errorType
     * @param data
     * @return Result
     */
    public static Result failCus(ErrorType errorType, Object data) {
        Result result = new Result<>(errorType, data);
//        if(data instanceof String){
//             result.data = errorType.getMesg();
//             result.mesg = (String)data;
//        }else if(data instanceof OAuth2Exception){
//             result.mesg = errorType.getMesg();
//             Map dtMap = new HashMap<>();
//             dtMap.put("error",((OAuth2Exception) data).getOAuth2ErrorCode());
//             dtMap.put("error_description",errorType.getMesg());
//             result.data = dtMap;
//
//        }
        return result;
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType
     * @return Result
     */
    public static Result fail(ErrorType errorType) {
        return Result.fail(errorType, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static Result fail(Object data) {
        return new Result<>(SystemErrorType.SYSTEM_ERROR, data);
    }

    /**
     * 系统异常类并返回警告内容
     *
     * @param mesg
     * @return Result
     */
    public static Result fail(String mesg) {
        return new Result<>(WARNINGFUL_CODE, mesg, null);
    }

    /**
     * 成功code=000000
     *
     * @return true/false
     */
//    @JsonIgnore
//    public boolean isSuccess() {
//        return SUCCESSFUL_CODE.equals(this.code);
//    }

    /**
     * 失败
     *
     * @return true/false
     */
//    @JsonIgnore
//    public boolean isFail() {
//        return !isSuccess();
//    }
}
