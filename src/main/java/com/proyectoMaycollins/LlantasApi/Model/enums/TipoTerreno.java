package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Tipos de terreno para llantas según el uso
 * - URBANA: Autos y camionetas en ciudad
 * - CARRETERA_PANAMERICANA: Alto kilometraje, calor, desgaste
 * - MIXTA_URBANA_RURAL: Pistas + trochas
 * - OFF_ROAD_TODO_TERRENO: 4x4, zonas arenosas
 * - CARGA_COMERCIAL: Camiones, vans, reparto
 * - PESADA_INDUSTRIAL: Volquetes, tráileres, pesca, puerto
 */
public enum TipoTerreno {
    URBANA,                     // Autos y camionetas en ciudad
    CARRETERA_PANAMERICANA,     // Alto kilometraje, calor, desgaste
    MIXTA_URBANA_RURAL,         // Pistas + trochas
    OFF_ROAD_TODO_TERRENO,      // 4x4, zonas arenosas
    CARGA_COMERCIAL,            // Camiones, vans, reparto
    PESADA_INDUSTRIAL;          // Volquetes, tráileres, pesca, puerto

    @JsonCreator
    public static TipoTerreno from(String value) {
        if (value == null) return null;
        return TipoTerreno.valueOf(value.trim().toUpperCase());
    }
}

