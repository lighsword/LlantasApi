package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PrioridadNotificacion {
    BAJA,
    MEDIA,
    ALTA,
    URGENTE,
    CRITICA;

    @JsonCreator
    public static PrioridadNotificacion from(String value) {
        if (value == null) return null;
        return PrioridadNotificacion.valueOf(value.trim().toUpperCase());
    }
}

