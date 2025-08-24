package com.byaffe.learningking.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public  class UtcLocalDateTimeDeserializer extends LocalDateTimeDeserializer {


        @Override
        public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws java.io.IOException, com.fasterxml.jackson.core.JsonProcessingException {
            ZonedDateTime zdt = super.deserialize(p, ctxt).atZone(ZoneId.of("UTC"));
            return zdt.toLocalDateTime(); 
        }
    }