package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoHerramienta {
    MANUAL,
    ELECTRICA,
    NEUMATICA,
    HIDRAULICA,
    MECANICA,
    AUTOMOTRIZ,
    LLAVE,
    DESTORNILLADOR,
    ALICATE,
    MARTILLO;

    @JsonCreator
    public static TipoHerramienta from(String value) {
        if (value == null) return null;
        return TipoHerramienta.valueOf(value.trim().toUpperCase());
    }
}

