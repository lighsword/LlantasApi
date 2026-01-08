package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class HistorialPreciosDTO {
    private Long productosId;
    private String productoDescripcion;
    private String tipo;
    private BigDecimal precio;
    private OffsetDateTime fechaInicio;
    private Boolean activo;
}

