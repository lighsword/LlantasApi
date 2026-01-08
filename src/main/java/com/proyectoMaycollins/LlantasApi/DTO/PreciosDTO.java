package com.proyectoMaycollins.LlantasApi.DTO;

import java.time.OffsetDateTime;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PreciosDTO {
    private Boolean activo;
    private OffsetDateTime fechaInicio;
    private BigDecimal precio;
    private String tipo;
    private String productoDescripcion;
    private Long productosId;
    private Long preciosId;
}






