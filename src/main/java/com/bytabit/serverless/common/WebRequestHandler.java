/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.common;

import java.util.Map;

public abstract class WebRequestHandler implements com.amazonaws.services.lambda.runtime.RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    public String getPathParameter(Map<String, Object> input, String name) {
        return getParameter(input, "pathParameters", name);
    }

    public String getQueryStringParameter(Map<String, Object> input, String name) {
        return getParameter(input, "queryStringParameters", name);
    }

    public String getBody(Map<String, Object> input) {
        return input.containsKey("body") ? (String) input.get("body") : null;
    }

    private String getParameter(Map<String, Object> input, String type, String name) {

        if (input.containsKey(type) && input.get(type) != null) {
            return  ((Map) input.get(type)).containsKey(name)
                    ? (String) ((Map) input.get(type)).get(name) : null;
        } else {
            return null;
        }
    }
}
