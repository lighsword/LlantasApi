package com.proyectoMaycollins.LlantasApi.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RegistrarPrecioDTO {

    @NotNull(message = "El producto es requerido")
    private Long productosId;

    @NotNull(message = "El tipo de precio es requerido")
    private String tipo;  // COMPRA, VENTA, MAYORISTA

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    private Boolean activo = true;
}

