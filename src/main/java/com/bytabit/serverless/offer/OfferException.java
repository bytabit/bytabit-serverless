/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.offer;

public class OfferException extends RuntimeException {

    public OfferException() {
        super();
    }

    public OfferException(String message) {
        super(message);
    }

    public OfferException(String message, Throwable cause) {
        super(message, cause);
    }

    public OfferException(Throwable cause) {
        super(cause);
    }

    protected OfferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
