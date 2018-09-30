package com.pd.rt.utils;

/**
 * @author peramdy on 2018/9/30.
 */
public enum RtListenerMode {

    CONSUME_MODE_ORDERLY("Orderly"),
    CONSUME_MODE_CONCURRENTLY("Concurrently");

    /**
     * mode
     */
    private String mode;

    RtListenerMode(String mode) {
        this.mode = mode;
    }

}
