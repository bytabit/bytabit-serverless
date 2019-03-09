/*
 * Copyright 2019 Bytabit AB
 */

package com.bytabit.serverless.common;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private DateFormat dateFormat;

    public DateConverter() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.format(date));
    }

    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        try {
            return dateFormat.parse(json.getAsString());
        } catch (ParseException e) {
            throw new JsonParseException(e.getCause());
        }
    }
}
