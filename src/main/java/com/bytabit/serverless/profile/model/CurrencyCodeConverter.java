package com.bytabit.serverless.profile.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@DynamoDBTypeConverted(converter = CurrencyCodeConverter.Converter.class)
public @interface CurrencyCodeConverter {

    public static class Converter implements DynamoDBTypeConverter<String, CurrencyCode> {

        @Override
        public String convert(final CurrencyCode currencyCode) {
            return currencyCode.name();
        }

        @Override
        public CurrencyCode unconvert(final String currencyCodeString) {
            return CurrencyCode.valueOf(currencyCodeString);
        }
    }
}
