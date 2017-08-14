package com.sinotopia.fundamental.common.hanger.exception;

/**
 * 执行链条通用异常
 */
public class ChainException extends RuntimeException {

    public ChainException() {
    }

    public ChainException(String message) {
        super(message);
    }

    public ChainException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChainException(Throwable cause) {
        super(cause);
    }

    public ChainException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
