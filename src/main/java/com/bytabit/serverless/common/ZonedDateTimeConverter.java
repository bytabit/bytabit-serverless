/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.common;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ZonedDateTimeConverter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

    private final DateTimeFormatter dateFormat;

    public ZonedDateTimeConverter() {
        dateFormat = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("UTC"));
    }

    @Override
    public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(zonedDateTime.format(dateFormat));
    }

    @Override
    public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        try {
            return ZonedDateTime.parse(json.getAsString(), dateFormat);
        } catch (DateTimeParseException e) {
            throw new JsonParseException(e.getCause());
        }
    }
}
