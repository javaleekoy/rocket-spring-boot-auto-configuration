package com.pd.rt.exception;

/**
 * @author peramdy on 2018/9/28.
 */
public class RtException extends RuntimeException {

    public RtException() {
        super();
    }

    public RtException(String message) {
        super(message);
    }

    public RtException(String message, Throwable cause) {
        super(message, cause);
    }

    public RtException(Throwable cause) {
        super(cause);
    }

    protected RtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
