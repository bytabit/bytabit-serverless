/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.common;

import lombok.Data;

import java.util.Map;

@Data
public class Response {

    private final String message;
    private final Map<String, Object> input;
}
