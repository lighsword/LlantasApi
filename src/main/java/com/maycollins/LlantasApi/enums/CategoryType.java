package com.maycollins.LlantasApi.enums;

import lombok.Getter;

@Getter
public enum CategoryType {
    LLANTA("llanta"),
    HERRAMIENTA("herramienta"),
    REFACCION("refaccion");

    private final String value;

    CategoryType(String value) {
        this.value = value;
    }

    public static CategoryType fromValue(String value) {
        for (CategoryType type : CategoryType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Categoría no válida: " + value);
    }
}