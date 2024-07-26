package com.envoi.diploma.types.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum roles implements AbstractENUM, GrantedAuthority
{
    ADMIN("ADMIN"),
    STUDENT("STUDENT"),
    HEADMAN("HEADMAN"),//староста
    TEACHER("TEACHER"),
    HEAD("HEAD"), //зав. кафедры
    CURATOR("CURATOR");
    private final String value;

    roles(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String getAuthority()
    {
        return this.value;
    }
}
