package com.bytabit.serverless.common;

import lombok.Data;

import java.util.Map;

@Data
public class Response {

    private final String message;
    private final Map<String, Object> input;
}
