package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.*;

import java.math.BigDecimal;

/**
 * DTO que retorna todos los precios de un producto (compra, venta, mayorista)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreciosProductoDTO {

    private Long productosId;
    private String codigoProducto;
    private String descripcion;

    // Precios actuales activos
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal precioMayorista;

    // Margen de ganancia calculado
    private BigDecimal margenVenta;      // (precioVenta - precioCompra)
    private BigDecimal margenMayorista;  // (precioMayorista - precioCompra)
    private Double porcentajeGananciaVenta;
    private Double porcentajeGananciaMayorista;

    // Información adicional
    private String marca;
    private String modelo;
    private Boolean activo;
}

