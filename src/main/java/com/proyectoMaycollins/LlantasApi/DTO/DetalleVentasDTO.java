package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

@Data
public class DetalleVentasDTO {
    private Long id;
    private Long ventasId;
    private Long productosId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}

