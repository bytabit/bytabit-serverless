/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.offer;

import com.amazonaws.services.lambda.runtime.Context;
import com.bytabit.serverless.common.ApiGatewayResponse;
import com.bytabit.serverless.common.Response;
import com.bytabit.serverless.common.WebRequestHandler;
import com.bytabit.serverless.offer.model.Offer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class PutOfferHandler extends WebRequestHandler {

    private OfferManager offerManager = new OfferManager();

    public PutOfferHandler() {
        super();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {

            String id = getPathParameter(input, "id");

            // get the body and badge json from input
            String offerJson = getBody(input);
            Offer offer = gson.fromJson(offerJson, Offer.class);

            if (!offer.getId().equals(id)) {
                throw new OfferException("PutOffer request path id does not match offer body id.");
            }

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
