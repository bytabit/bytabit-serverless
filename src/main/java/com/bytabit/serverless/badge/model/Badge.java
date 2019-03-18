/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.badge.model;

import com.bytabit.serverless.profile.model.CurrencyCode;
import com.bytabit.serverless.profile.model.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
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

    private Date validFrom;

    private Date validTo;

    private CurrencyCode currencyCode;

    private PaymentMethod paymentMethod;

    private String detailsHash;

    private Date updated;

    // payment information
    // TODO move this to another table and/or remove after payment is confirmed

    @JsonIgnore
    private BigDecimal btcAmount;

    @JsonIgnore
    private String transactionHash;
}
