package com.envoi.diploma.types.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum academictitles implements AbstractENUM
{
    ASSOCIATE_PROFESSOR("Доцент"),
    PROFESSOR("Профессор"),
    UNKNOWN("NULL");

    private final String value;

    academictitles(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}