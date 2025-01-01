package com.maycollins.LlantasApi.enums;

public enum ProductStatus {
    ACTIVE("activo"),
    INACTIVE("inactivo"),
    DISCONTINUED("descontinuado");

    private final String value;

    ProductStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}