package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EstadoNotificacion {
    PENDIENTE,
    ENVIADA,
    LEIDA,
    ARCHIVADA,
    ELIMINADA;

    @JsonCreator
    public static EstadoNotificacion from(String value) {
        if (value == null) return null;
        return EstadoNotificacion.valueOf(value.trim().toUpperCase());
    }
}

