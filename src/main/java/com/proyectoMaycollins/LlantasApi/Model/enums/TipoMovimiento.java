package com.proyectoMaycollins.LlantasApi.Model.enums;

public enum TipoMovimiento {
    ENTRADA,        // Entrada de productos (compras, devoluciones de clientes)
    SALIDA,         // Salida de productos (ventas, devoluciones a proveedores)
    AJUSTE,         // Ajuste de inventario (corrección de stock)
    TRANSFERENCIA   // Transferencia entre almacenes
}

