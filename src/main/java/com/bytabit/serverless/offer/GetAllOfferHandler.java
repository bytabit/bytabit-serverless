/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.offer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bytabit.serverless.common.ApiGatewayResponse;
import com.bytabit.serverless.common.Response;
import com.bytabit.serverless.common.WebRequestHandler;
import com.bytabit.serverless.offer.model.Offer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class GetAllOfferHandler extends WebRequestHandler {

    private OfferManager offerManager = new OfferManager();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {

            log.debug("input: {}", input);

            List<Offer> offers = offerManager.getAll();

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(offers)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            log.error("Error in get offers: {}\n{}", ex, ex.getStackTrace());

            // send the error response back
            Response responseBody = new Response("Error in getting offers: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }

    }

}
