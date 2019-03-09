/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.badge;

public class BadgeException extends RuntimeException {

    public BadgeException() {
        super();
    }

    public BadgeException(String message) {
        super(message);
    }

    public BadgeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadgeException(Throwable cause) {
        super(cause);
    }

    protected BadgeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
