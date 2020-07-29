package com.springboot.cloud.common.core.exception;




public class AccountException extends BaseException {
    public AccountException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
