package com.proyectoMaycollins.LlantasApi.Model.enums;

public enum TipoPrecio {
    COMPRA("Precio de Compra"),
    VENTA("Precio de Venta"),
    MAYORISTA("Precio Mayorista");

    private final String descripcion;

    TipoPrecio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

