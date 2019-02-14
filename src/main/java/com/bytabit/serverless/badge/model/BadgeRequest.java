package com.bytabit.serverless.badge.model;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class BadgeRequest {

    @NonNull
    private Badge badge;

    @NonNull
    private BigDecimal btcAmount;

    private String transactionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadgeRequest that = (BadgeRequest) o;
        return badge.equals(that.badge) &&
                btcAmount.compareTo(that.btcAmount) == 0 &&
                Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(badge, btcAmount, transactionId);
    }
}
