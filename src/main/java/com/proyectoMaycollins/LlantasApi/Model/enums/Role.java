package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Administrador"),
    VENDEDOR("Vendedor"),
    ALMACENISTA("Almacenista"),
    COMPRADOR("Comprador");

    private final String descripcion;

    Role(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonCreator
    public static Role from(String value) {
        if (value == null) return null;
        return Role.valueOf(value.trim().toUpperCase());
    }
}


