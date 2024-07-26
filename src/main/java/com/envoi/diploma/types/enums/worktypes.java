package com.envoi.diploma.types.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum worktypes implements AbstractENUM
{
    LR("Лр"),
    PR("Пр"),
    KP("КП"),
    KR("Кр"),
    RR("Рр"),
    VKR("ВКР"),
    PRACTICE("практика");

    private final String value;

    worktypes(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}
