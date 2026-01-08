package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoPromocion {
    PORCENTAJE,
    MONTO_FIJO,
    DOS_POR_UNO,
    DESCUENTO_CANTIDAD,
    DESCUENTO_TEMPORADA;

    @JsonCreator
    public static TipoPromocion from(String value) {
        if (value == null) return null;
        return TipoPromocion.valueOf(value.trim().toUpperCase());
    }
}

