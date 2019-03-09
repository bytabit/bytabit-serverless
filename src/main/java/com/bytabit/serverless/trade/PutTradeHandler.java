/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.trade;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bytabit.serverless.common.ApiGatewayResponse;
import com.bytabit.serverless.common.DateConverter;
import com.bytabit.serverless.common.Response;
import com.bytabit.serverless.common.WebRequestHandler;
import com.bytabit.serverless.trade.model.TradeServiceResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class PutTradeHandler extends WebRequestHandler {

    private TradeManager tradeManager = new TradeManager();

    private final Gson gson;

    public PutTradeHandler() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, new DateConverter())
                .create();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {

            log.debug("input: {}", input);

            String id = getPathParameter(input, "id");

            // get the body and badge json from input
            String tradeServiceResourceJson = getBody(input);
            TradeServiceResource tradeServiceResource = gson.fromJson(tradeServiceResourceJson, TradeServiceResource.class);
            tradeServiceResource.setId(id);

            tradeManager.put(tradeServiceResource);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(tradeServiceResource)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            log.error("Error in putting trade: {}\n{}", ex, ex.getStackTrace());

            // send the error response back
            Response responseBody = new Response("Error in putting trade: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
