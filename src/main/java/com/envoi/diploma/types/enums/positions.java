package com.envoi.diploma.types.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum positions implements AbstractENUM
{
    ASSISTANT("Ассистент"),
    LECTURER("Преподаватель"),
    SENIOR_LECTURER("Ст. преподаватель"),
    ASSOCIATE_PROFESSOR("Доцент"),
    PROFESSOR("Профессор"),
    HEAD_OF_DEPARTMENT("Зав. кафедры");

    private final String value;

    positions(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}