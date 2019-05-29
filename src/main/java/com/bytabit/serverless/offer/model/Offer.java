/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.offer.model;

import com.bytabit.serverless.profile.model.CurrencyCode;
import com.bytabit.serverless.profile.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Offer {

    public enum OfferType {
        BUY,
        SELL
    }

    private String id;

    private OfferType offerType;

    private String makerProfilePubKey;

    private CurrencyCode currencyCode;

    private PaymentMethod paymentMethod;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private BigDecimal price;

    private Date updated;

    private String signature;
}
