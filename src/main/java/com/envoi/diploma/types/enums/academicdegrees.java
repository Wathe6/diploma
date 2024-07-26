package com.envoi.diploma.types.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum academicdegrees implements AbstractENUM
{
    CANDIDATE_OF_TECHNICAL_SCIENCES("к. т. н."),
    CANDIDATE_OF_PHYSICAL_AND_MATHEMATICAL_SCIENCES("к. ф.-м. н."),
    DOCTOR_OF_TECHNICAL_SCIENCES("д. т. н."),
    DOCTOR_OF_PHYSICAL_AND_MATHEMATICAL_SCIENCES("д. ф.-м. н."),
    UNKNOWN("NULL");

    private final String value;
    academicdegrees(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}