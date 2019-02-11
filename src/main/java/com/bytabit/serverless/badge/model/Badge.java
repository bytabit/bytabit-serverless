package com.bytabit.serverless.badge.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.bytabit.serverless.profile.model.CurrencyCode;
import com.bytabit.serverless.profile.model.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@DynamoDBTable(tableName = "PLACEHOLDER_BADGE_TABLE")
@Data
public class Badge {

    public enum BadgeType {
        BETA_TESTER,
        OFFER_MAKER,
        DETAILS_VERIFIED
    }

    @DynamoDBHashKey
    private String profilePubKey;

    @DynamoDBRangeKey
    private String id;

    private BadgeType badgeType;

    private ZonedDateTime validFrom;

    private ZonedDateTime validTo;

    private CurrencyCode currencyCode;

    private PaymentMethod paymentMethod;

    private String detailsHash;

    private BigDecimal btcAmount;

    private String transactionId;

    private ZonedDateTime updated;
}
