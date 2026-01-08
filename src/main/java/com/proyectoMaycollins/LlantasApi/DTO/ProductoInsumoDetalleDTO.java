package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductoInsumoDetalleDTO {
    private Long productoId;
    private String tipoInsumo;          // lubricante, sellador
    private BigDecimal contenidoNeto;
    private String unidadMedida;        // ml, L, g
    private LocalDate fechaVencimiento;
    private String usoRecomendado;      // montaje, reparación
}

