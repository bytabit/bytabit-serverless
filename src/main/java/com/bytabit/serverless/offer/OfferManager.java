/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.offer;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.bytabit.serverless.common.DateConverter;
import com.bytabit.serverless.offer.model.Offer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class OfferManager {

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(client);

    private final String OFFER_TABLE = System.getenv("OFFER_TABLE");
    private final Table table = dynamoDB.getTable(OFFER_TABLE);
    private final Gson gson;

    public OfferManager() {

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, new DateConverter())
                .create();
    }

    public List<Offer> getAll() {

        List<Offer> badges = StreamSupport.stream(table.scan().spliterator(), false)
                .map(Item::toJSON)
                .map(json -> gson.fromJson(json, Offer.class))
                .collect(Collectors.toList());

        log.info("Offer getAll(): {}", badges);

        return badges;
    }

    public List<Offer> getById(String id) {

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("id = :id")
                .withValueMap(new ValueMap().withString(":id", id));

        List<Offer> offers = StreamSupport.stream(table.query(querySpec).spliterator(), false)
                .map(Item::toJSON)
                .map(json -> gson.fromJson(json, Offer.class))
                .collect(Collectors.toList());

        log.info("Offer getById(): {}", offers);

        return offers;
    }

    public void put(Offer offer) {

        // TODO validate offer parameters

        offer.setUpdated(new Date());

        String offerJson = gson.toJson(offer);
        log.info("Offer put(): {}", offerJson);

        table.putItem(Item.fromJSON(offerJson));
    }
}
