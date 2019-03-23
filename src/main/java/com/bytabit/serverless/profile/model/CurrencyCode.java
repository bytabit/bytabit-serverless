/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.profile.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public enum CurrencyCode {

    SEK(0, new BigDecimal("1000.00").setScale(0, RoundingMode.HALF_UP), PaymentMethod.SWISH, PaymentMethod.MG, PaymentMethod.WU),
    USD(2, new BigDecimal("100.00").setScale(2, RoundingMode.HALF_UP), PaymentMethod.MG, PaymentMethod.WU),
    EUR(2, new BigDecimal("100.00").setScale(2, RoundingMode.HALF_UP), PaymentMethod.SEPA, PaymentMethod.MG, PaymentMethod.WU);

    CurrencyCode(int scale, BigDecimal maxTradeAmount, PaymentMethod... paymentMethods) {
        this.scale = scale;
        this.paymentMethods = Arrays.asList(paymentMethods);
    }

    private int scale;

    private BigDecimal maxTradeAmount;

    private List<PaymentMethod> paymentMethods;


    public int getScale() {
        return scale;
    }

    public BigDecimal getMaxTradeAmount() {
        return maxTradeAmount;
    }

    public List<PaymentMethod> paymentMethods() {
        return paymentMethods;
    }
}
