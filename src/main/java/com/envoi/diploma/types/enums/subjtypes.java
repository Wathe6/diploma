package com.envoi.diploma.types.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum subjtypes implements AbstractENUM
{
    EXAM("Экзамен"),
    CREDIT("Зачёт"),
    DIFF_CREDIT("Дифф. зачёт");
    private final String value;

    subjtypes(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}