package com.bytabit.serverless.badge;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bytabit.serverless.badge.model.Badge;
import com.bytabit.serverless.badge.model.BadgeRequest;
import com.bytabit.serverless.common.ApiGatewayResponse;
import com.bytabit.serverless.common.DateConverter;
import com.bytabit.serverless.common.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class PutBadgeHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private BadgeManager badgeManager = new BadgeManager();

    Gson gson;
    JsonParser jsonParser;

    public PutBadgeHandler() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, new DateConverter())
                .create();

        jsonParser = new JsonParser();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {

            log.debug("input: {}", input);

            String profilePubKey = (String) ((Map) input.get("pathParameters")).get("profilePubKey");
            String id = (String) ((Map) input.get("pathParameters")).get("id");

            // get the body and badge json from input
            String badgeRequestJson = (String) input.get("body");
            BadgeRequest badgeRequest = gson.fromJson(badgeRequestJson, BadgeRequest.class);
            Badge badge = badgeRequest.getBadge();

            if (!badge.getProfilePubKey().equals(profilePubKey) || !badge.getId().equals(id)) {
                throw new BadgeException(String.format("Request URL profilePubKey/id (%s/%s) don't match body: %s.",
                        profilePubKey, id, badgeRequestJson));
            }

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
