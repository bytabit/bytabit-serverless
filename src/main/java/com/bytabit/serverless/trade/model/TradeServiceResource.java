/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.trade.model;

import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class TradeServiceResource {

    @NonNull
    private String id;

    @NonNull
    private Long version;

    @NonNull
    private String offerId;

    @NonNull
    private Boolean arbitrate;

    @NonNull
    private String trade;

    private JsonObject tradeUnencrypted;

}
