package com.envoi.diploma.types.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum reviewers implements AbstractENUM
{
    DEVELOPER("Проработчик"),
    FIRST("1й"),
    SECOND("2й");

    private final String value;
    reviewers(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}
