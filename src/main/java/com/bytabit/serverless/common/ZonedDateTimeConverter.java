package com.bytabit.serverless.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.ZonedDateTime;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@DynamoDBTypeConverted(converter = ZonedDateTimeConverter.Converter.class)
public @interface ZonedDateTimeConverter {

    public static class Converter implements DynamoDBTypeConverter<String, ZonedDateTime> {

        @Override
        public String convert(final ZonedDateTime zonedDateTime) {
            return zonedDateTime.toString();
        }

        @Override
        public ZonedDateTime unconvert(final String zonedDateTimeString) {
            return ZonedDateTime.parse(zonedDateTimeString);
        }
    }
}
