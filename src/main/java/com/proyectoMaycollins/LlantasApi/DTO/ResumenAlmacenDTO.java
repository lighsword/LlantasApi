package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ResumenAlmacenDTO {
    private Integer almacenId;
    private String almacenNombre;
    private BigDecimal capacidadMaxima;
    private Integer totalProductosDiferentes;
    private Integer totalEntradas;
    private Integer totalSalidas;
    private Integer stockActual;
    private BigDecimal capacidadDisponible;
    private String ultimoMovimiento;
}

