package com.RedBus.Operator.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String timeString = p.getValueAsString();
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("h:mm a"));
    }
}
