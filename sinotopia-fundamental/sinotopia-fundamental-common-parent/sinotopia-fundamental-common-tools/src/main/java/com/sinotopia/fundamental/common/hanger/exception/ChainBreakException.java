package com.sinotopia.fundamental.common.hanger.exception;

/**
 * 执行链条跳出异常
 */
public class ChainBreakException extends ChainException {
    public ChainBreakException() {
    }

    public ChainBreakException(String message) {
        super(message);
    }

    public ChainBreakException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChainBreakException(Throwable cause) {
        super(cause);
    }

    public ChainBreakException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
