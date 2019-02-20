package com.bytabit.serverless.profile.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@DynamoDBTypeConverted(converter = PaymentMethodConverter.Converter.class)
public @interface PaymentMethodConverter {

    public static class Converter implements DynamoDBTypeConverter<String, PaymentMethod> {

        @Override
        public String convert(final PaymentMethod paymentMethod) {
            return paymentMethod.name();
        }

        @Override
        public PaymentMethod unconvert(final String paymentMethodString) {
            return PaymentMethod.valueOf(paymentMethodString);
        }
    }
}
