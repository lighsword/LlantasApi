package com.proyectoMaycollins.LlantasApi.Model.enums;

/**
 * Módulos del sistema
 * Cada módulo agrupa funcionalidades relacionadas
 */
public enum Modulo {
    PRODUCTOS("Gestión de Productos"),
    VENTAS("Gestión de Ventas"),
    COMPRAS("Gestión de Compras"),
    INVENTARIO("Gestión de Inventario"),
    CLIENTES("Gestión de Clientes"),
    PROVEEDORES("Gestión de Proveedores"),
    PRECIOS("Gestión de Precios"),
    PROMOCIONES("Gestión de Promociones"),
    REPORTES("Reportes y Análisis"),
    USUARIOS("Gestión de Usuarios y Roles"),
    ALMACENES("Gestión de Almacenes"),
    CATEGORIAS("Gestión de Categorías"),
    NOTIFICACIONES("Sistema de Notificaciones"),
    DASHBOARD("Panel de Control"),
    AUDITORIA("Auditoría del Sistema");

    private final String descripcion;

    Modulo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

