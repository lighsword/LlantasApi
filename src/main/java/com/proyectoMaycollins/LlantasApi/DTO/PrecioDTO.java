package com.proyectoMaycollins.LlantasApi.DTO;

import com.proyectoMaycollins.LlantasApi.Model.enums.TipoPrecio;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * DTO para crear o actualizar un precio de producto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrecioDTO {

    private Long preciosId;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productosId;

    @NotNull(message = "El tipo de precio es obligatorio")
    private TipoPrecio tipo;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    private OffsetDateTime fechaInicio;

    private Boolean activo;

    // Información adicional del producto (solo lectura)
    private String codigoProducto;
    private String descripcionProducto;
}

