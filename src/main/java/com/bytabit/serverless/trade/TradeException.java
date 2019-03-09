/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.trade;

public class TradeException extends RuntimeException {

    public TradeException() {
        super();
    }

    public TradeException(String message) {
        super(message);
    }

    public TradeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TradeException(Throwable cause) {
        super(cause);
    }

    protected TradeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
