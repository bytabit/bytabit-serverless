/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.offer.model;

import com.bytabit.serverless.profile.model.CurrencyCode;
import com.bytabit.serverless.profile.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Offer {

    public enum OfferType {
        BUY,
        SELL
    }

    @NonNull
    private String id;

    @NonNull
    private OfferType offerType;

    @NonNull
    private String makerProfilePubKey;

    @NonNull
    private CurrencyCode currencyCode;

    @NonNull
    private PaymentMethod paymentMethod;

    @NonNull
    private BigDecimal minAmount;

    @NonNull
    private BigDecimal maxAmount;

    @NonNull
    private BigDecimal price;

    @NonNull
    private Date updated;

    @NonNull
    private String signature;
}
