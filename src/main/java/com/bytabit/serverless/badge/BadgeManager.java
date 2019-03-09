/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.badge;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.bytabit.serverless.badge.model.Badge;
import com.bytabit.serverless.common.DateConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class BadgeManager {

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(client);

    private final String BADGE_TABLE = System.getenv("BADGE_TABLE");
    private final Table table = dynamoDB.getTable(BADGE_TABLE);
    private final Gson gson;

    public BadgeManager() {

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, new DateConverter())
                .create();
    }

    public List<Badge> getByProfilePubKey(String profilePubKey) {

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("profilePubKey = :profilePubKey")
                .withValueMap(new ValueMap()
                        .withString(":profilePubKey", profilePubKey));

        List<Badge> badges = StreamSupport.stream(table.query(querySpec).spliterator(), false)
                .map(Item::toJSON)
                .map(json -> gson.fromJson(json, Badge.class))
                .collect(Collectors.toList());

        log.info("Badge getByProfilePubKey(): {}", badges);

        return badges;
    }

    public void put(Badge badge) {

        // TODO validate badge parameters

        badge.setUpdated(new Date());

        String badgeJson = gson.toJson(badge);
        log.info("Badge put(): {}", badgeJson);

        table.putItem(Item.fromJSON(badgeJson));
    }
}
