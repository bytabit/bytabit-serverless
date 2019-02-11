package com.bytabit.serverless.badge;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bytabit.serverless.badge.model.Badge;
import com.bytabit.serverless.common.ApiGatewayResponse;
import com.bytabit.serverless.common.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class PutBadgeHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {


    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {

            log.debug("input: {}", input);

            String profilePubKey = (String)input.get("pathParameters.profilePubKey");
            String id = (String)input.get("pathParameters.id");

            // get the 'body' from input
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

            // create the Product object for post
            Badge badge = new Badge();

            badge.setProfilePubKey(body.get("profilePubKey").asText());
            badge.setId(body.get("id").asText());

            BadgeManager badgeManager = new BadgeManager();
            badgeManager.put(badge);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(badge)
                    //.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            log.error("Error in putting badge: " + ex);

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
