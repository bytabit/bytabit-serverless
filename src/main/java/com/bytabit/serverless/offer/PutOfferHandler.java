/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.offer;

import com.amazonaws.services.lambda.runtime.Context;
import com.bytabit.serverless.common.ApiGatewayResponse;
import com.bytabit.serverless.common.DateConverter;
import com.bytabit.serverless.common.Response;
import com.bytabit.serverless.common.WebRequestHandler;
import com.bytabit.serverless.offer.model.Offer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class PutOfferHandler extends WebRequestHandler {

    private OfferManager offerManager = new OfferManager();

    private final Gson gson;

    public PutOfferHandler() {
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
            String offerJson = getBody(input);
            Offer offer = gson.fromJson(offerJson, Offer.class);

            offer.setId(id);

            offerManager.put(offer);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(offer)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            log.error("Error in putting offer: {}\n{}", ex, ex.getStackTrace());

            // send the error response back
            Response responseBody = new Response("Error in putting offer: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
