/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.common;

public class HashException extends RuntimeException {

    public HashException() {
        super();
    }

    public HashException(String message) {
        super(message);
    }

    public HashException(String message, Throwable cause) {
        super(message, cause);
    }

    public HashException(Throwable cause) {
        super(cause);
    }
}


