package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Nivel de cliente según su frecuencia y volumen de compras
 * - NUEVO: Cliente nuevo, primera compra
 * - FRECUENTE: Cliente que compra regularmente
 * - PREFERENCIAL: Cliente VIP con beneficios especiales
 */
public enum NivelCliente {
    NUEVO,          // Cliente nuevo, primera compra
    FRECUENTE,      // Cliente que compra regularmente
    PREFERENCIAL;   // Cliente VIP con beneficios especiales

    @JsonCreator
    public static NivelCliente from(String value) {
        if (value == null) return null;
        return NivelCliente.valueOf(value.trim().toUpperCase());
    }
}

