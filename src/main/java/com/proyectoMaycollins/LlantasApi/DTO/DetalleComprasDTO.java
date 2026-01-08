package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleComprasDTO {
    private Integer comprasId;
    private Long productosId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}

