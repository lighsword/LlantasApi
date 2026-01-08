package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ComprasDTO {
    private Integer id;
    private Integer proveedoresId;
    private Long usuariosId;
    private LocalDateTime fechaCompra;
    private BigDecimal total;
}

