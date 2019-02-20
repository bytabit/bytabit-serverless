package com.bytabit.serverless.badge.model;

import com.bytabit.serverless.profile.model.CurrencyCode;
import com.bytabit.serverless.profile.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Builder
public class Badge {

    public enum BadgeType {
        BETA_TESTER,
        OFFER_MAKER,
        DETAILS_VERIFIED
    }

    private String profilePubKey;

    private String id;

    private BadgeType badgeType;

    private ZonedDateTime validFrom;

    private ZonedDateTime validTo;

    private CurrencyCode currencyCode;

    private PaymentMethod paymentMethod;

    private String detailsHash;

    private Date updated;

    // payment information
    // TODO move this to another table and/or remove after payment is confirmed

    @NonNull
    private BigDecimal btcAmount;

    private String transactionId;
}
