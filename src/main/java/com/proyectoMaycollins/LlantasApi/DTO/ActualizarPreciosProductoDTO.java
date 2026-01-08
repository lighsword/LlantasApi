package com.proyectoMaycollins.LlantasApi.DTO;

import com.proyectoMaycollins.LlantasApi.Model.enums.TipoPrecio;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO para actualizar múltiples precios de un producto de una vez
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarPreciosProductoDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productosId;

    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a 0")
    private BigDecimal precioCompra;

    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;

    @DecimalMin(value = "0.01", message = "El precio mayorista debe ser mayor a 0")
    private BigDecimal precioMayorista;
}

