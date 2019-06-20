/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.trade;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.bytabit.serverless.common.DateConverter;
import com.bytabit.serverless.trade.model.TradeServiceResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class TradeManager {

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(client);

    private final String TRADE_TABLE = System.getenv("TRADE_TABLE");
    private final Table table = dynamoDB.getTable(TRADE_TABLE);
    private final Index offerIndex = table.getIndex("offerIndex");

    private final Gson gson;

    public TradeManager() {

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, new DateConverter())
                .create();
    }

    public List<TradeServiceResource> getById(String id, long version) {

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("id = :id AND version > :version")
                .withValueMap(new ValueMap().withString(":id", id).withLong(":version", version));

        List<TradeServiceResource> trades = StreamSupport.stream(table.query(querySpec).spliterator(), false)
                .map(Item::toJSON)
                .map(json -> gson.fromJson(json, TradeServiceResource.class))
                .collect(Collectors.toList());

        log.info("Trade getById(): {}", trades);

        return trades;
    }

    public List<TradeServiceResource> getByOfferId(String offerId, long version) {

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("offerId = :offerId")
                .withFilterExpression("version > :version")
                .withValueMap(new ValueMap().withString(":offerId", offerId).withLong(":version", version));

        List<TradeServiceResource> trades = StreamSupport.stream(offerIndex.query(querySpec).spliterator(), false)
                .map(Item::toJSON)
                .map(json -> gson.fromJson(json, TradeServiceResource.class))
                .collect(Collectors.toList());

        log.info("Trade getByOfferId(): {}", trades);

        return trades;
    }

    public List<TradeServiceResource> getAllArbitrate(long version) {

        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression("arbitrate = :arbitrate AND version > :version")
                .withValueMap(new ValueMap().withBoolean(":arbitrate", true).withLong(":version", version));

        List<TradeServiceResource> trades = StreamSupport.stream(table.scan(scanSpec).spliterator(), false)
                .map(Item::toJSON)
                .map(json -> gson.fromJson(json, TradeServiceResource.class))
                .collect(Collectors.toList());

        log.info("Trade getAllArbitrate(): {}", trades);

        return trades;
    }

    public void put(TradeServiceResource tradeServiceResource) {

        // TODO validate trade parameters

        tradeServiceResource.setVersion(new Date().getTime());

        String tradeRequestJson = gson.toJson(tradeServiceResource);
        log.info("Trade put(): {}", tradeRequestJson);

        table.putItem(Item.fromJSON(tradeRequestJson));
    }
}
