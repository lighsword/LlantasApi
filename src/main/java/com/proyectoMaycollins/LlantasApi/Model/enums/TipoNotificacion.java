package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoNotificacion {
    VENTA,
    STOCK_BAJO,
    PROMOCION,
    ALERTA,
    RECORDATORIO,
    SISTEMA,
    REPORTE;

    @JsonCreator
    public static TipoNotificacion from(String value) {
        if (value == null) return null;
        return TipoNotificacion.valueOf(value.trim().toUpperCase());
    }
}

