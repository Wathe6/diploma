package com.envoi.diploma.types.enums;

public interface AbstractENUM
{
    String getValue();
    static <T extends Enum<T> & AbstractENUM> T fromValue(Class<T> enumClass, String value)
    {
        for (T constant : enumClass.getEnumConstants()) {
            if (constant.getValue().equals(value)) {
                return constant;
            }
        }
        return null;
    }
}
