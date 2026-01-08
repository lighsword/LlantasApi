package com.proyectoMaycollins.LlantasApi.Model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Tipos de cliente del negocio
 * - PARTICULAR: Personas naturales (uso personal)
 * - EMPRESA: Negocios, talleres, flotas
 * - TALLER_MECANICO: Compra frecuente, posible precio especial
 * - TRANSPORTE_FLOTA: Taxis, buses, camiones
 * - DISTRIBUIDOR_REVENDEDOR: Compra al por mayor
 * - GOBIERNO_INSTITUCION: Licitaciones o compras formales
 */
public enum TipoCliente {
    PARTICULAR,              // Personas naturales (uso personal)
    EMPRESA,                 // Negocios, talleres, flotas
    TALLER_MECANICO,        // Compra frecuente, posible precio especial
    TRANSPORTE_FLOTA,       // Taxis, buses, camiones
    DISTRIBUIDOR_REVENDEDOR, // Compra al por mayor
    GOBIERNO_INSTITUCION;   // Licitaciones o compras formales

    @JsonCreator
    public static TipoCliente from(String value) {
        if (value == null) return null;
        return TipoCliente.valueOf(value.trim().toUpperCase());
    }
}

