package com.envoi.diploma.types.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum statuses implements AbstractENUM
{
    NOT_COMPLETED("Не выполнена"),
    COMPLETED("Выполнена"),
    DEADLINE_EXPIRED("Истёк срок сдачи"),
    WAITING_FOR_CHECK("Ожидание проверки"),
    REMARKS("Замечания");

    private final String value;

    statuses(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}

