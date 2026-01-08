package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

@Data
public class DetalleVentaItemDTO {
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}

