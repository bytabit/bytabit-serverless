package com.bytabit.serverless.badge;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.bytabit.serverless.badge.model.Badge;
import com.bytabit.serverless.common.DynamoDBAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class BadgeManager {

    // get the table name from env. var. set in serverless.yml
    private static final String PRODUCTS_TABLE_NAME = System.getenv("PRODUCTS_TABLE_NAME");

    private final DynamoDBMapper dbMapper;

    public BadgeManager() {

        // build the dbMapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(PRODUCTS_TABLE_NAME))
                .build();

        // get the dbAdapter
        DynamoDBAdapter dbAdapter = DynamoDBAdapter.getInstance();

        // create the dbMapper with config
        this.dbMapper = dbAdapter.createDbMapper(mapperConfig);
    }

    public List<Badge> getByProfilePubKey(String profilePubKey) {

        HashMap<String, AttributeValue> attributes = new HashMap<>();
        attributes.put(":profilePubKey", new AttributeValue().withS(profilePubKey));

        DynamoDBQueryExpression<Badge> queryExp = new DynamoDBQueryExpression<Badge>()
                .withKeyConditionExpression("profilePubKey = :profilePubKey")
                .withExpressionAttributeValues(attributes);

        PaginatedQueryList<Badge> badges = this.dbMapper.query(Badge.class, queryExp);

        log.info("Badge getByProfilePubKey(): {}", badges);
        return badges;
    }

    public void put(Badge badge) {
        log.info("Badge put(): {}", badge.toString());
        this.dbMapper.save(badge);
    }
}
