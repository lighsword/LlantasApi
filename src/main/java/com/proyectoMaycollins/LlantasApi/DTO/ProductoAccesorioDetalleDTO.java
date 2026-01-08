package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoAccesorioDetalleDTO {
    private Long productoId;
    private String tipoAccesorio;       // tapacubo, cadena
    private String material;
    private String color;
    private BigDecimal diametroAplicable;
    private String vehiculoAplicable;
    private String presentacion;        // unidad, par, juego
}

