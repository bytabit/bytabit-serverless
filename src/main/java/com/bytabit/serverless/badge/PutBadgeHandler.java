/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.badge;

import com.amazonaws.services.lambda.runtime.Context;
import com.bytabit.serverless.badge.model.Badge;
import com.bytabit.serverless.badge.model.BadgeRequest;
import com.bytabit.serverless.common.ApiGatewayResponse;
import com.bytabit.serverless.common.Response;
import com.bytabit.serverless.common.WebRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class PutBadgeHandler extends WebRequestHandler {

    private BadgeManager badgeManager = new BadgeManager();

    public PutBadgeHandler() {
        super();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            log.debug("input: {}", input);

            String profilePubKey = getPathParameter(input, "profilePubKey");
            String id = getPathParameter(input, "id");

            // get the body and badge json from input
            String badgeRequestJson = getBody(input);
            BadgeRequest badgeRequest = gson.fromJson(badgeRequestJson, BadgeRequest.class);
            Badge badge = badgeRequest.getBadge();

            badge.setProfilePubKey(profilePubKey);
            badge.setId(id);

            badgeManager.put(badge);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(badge)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            log.error("Error in putting badge: {}\n{}", ex, ex.getStackTrace());

            // send the error response back
            Response responseBody = new Response("Error in putting badge: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
