/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.badge.model;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class BadgeRequest {

    @NonNull
    private Badge badge;

    @NonNull
    private BigDecimal btcAmount;

    private String transactionId;
}
