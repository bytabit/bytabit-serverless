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
import com.bytabit.serverless.badge.BadgeManager;
import com.bytabit.serverless.badge.model.Badge;
import com.bytabit.serverless.common.DateConverter;
import com.bytabit.serverless.offer.model.Offer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class OfferManager {

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(client);

    private final String OFFER_TABLE = System.getenv("OFFER_TABLE");
    private final Table table = dynamoDB.getTable(OFFER_TABLE);
    private final Gson gson;
    private final DateFormat dateFormat;

    private final BadgeManager badgeManager;

    public OfferManager() {

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, new DateConverter())
                .create();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        badgeManager = new BadgeManager();
    }

    public List<Offer> getAll() {

        List<Offer> offers = StreamSupport.stream(table.scan().spliterator(), false)
                .map(Item::toJSON)
                .map(json -> gson.fromJson(json, Offer.class))
                .collect(Collectors.toList());

        // remove stale offers that haven't been updated in the past 10 minutes
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, -5);
        Date cutoffTime = c.getTime();

        offers.stream().filter(o -> o.getUpdated().compareTo(cutoffTime) < 0)
                .forEach(o -> {
                    table.deleteItem("id", o.getId());
                    log.info("Removed offer not updated since {}: {}", dateFormat.format(cutoffTime), o);
                });

        List<Offer> freshOffers = offers.stream().filter(o -> o.getUpdated().compareTo(cutoffTime) >= 0)
                .collect(Collectors.toList());

        log.info("Offer getAll(): {}", freshOffers);

        return freshOffers;
    }

    public Offer getById(String id) {

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("id = :id")
                .withValueMap(new ValueMap().withString(":id", id));

        Offer offer = StreamSupport.stream(table.query(querySpec).spliterator(), false)
                .map(Item::toJSON)
                .map(json -> gson.fromJson(json, Offer.class))
                .findFirst()
                .orElseThrow(() -> new OfferException(String.format("Offer with id: %s not found", id)));

        log.info("Offer getById(): {}", offer);

        return offer;
    }

    public Offer delete(String id) {

        Offer offer = getById(id);

        table.deleteItem("id", offer.getId());

        log.info("Offer deleteById(): {}", offer);

        return offer;
    }

    public void put(Offer offer) {

        // TODO validate offer parameters

        offer.setUpdated(new Date());

        String offerJson = gson.toJson(offer);
        log.info("Offer put(): {}", offerJson);

        Date currentDate = new Date();
        List<Badge> badges = badgeManager.getByProfilePubKey(offer.getMakerProfilePubKey());
        boolean hasBadge = badges.stream()
                .filter(b -> b.getBadgeType().equals(Badge.BadgeType.OFFER_MAKER))
                .filter(b -> b.getValidFrom().compareTo(currentDate) <= 0 && b.getValidTo().compareTo(currentDate) >= 0)
                .anyMatch(b -> b.getCurrencyCode().equals(offer.getCurrencyCode()));

        if (hasBadge) {
            table.putItem(Item.fromJSON(offerJson));
        } else {
            throw new OfferException(String.format("No OFFER_MAKER badge found for currency %s", offer.getCurrencyCode()));
        }
    }
}
