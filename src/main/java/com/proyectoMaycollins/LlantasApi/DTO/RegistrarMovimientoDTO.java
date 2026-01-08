package com.proyectoMaycollins.LlantasApi.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RegistrarMovimientoDTO {

    @NotNull(message = "El almacén es requerido")
    private Integer almacenId;

    @NotNull(message = "El producto es requerido")
    private Long productoId;

    @NotNull(message = "El tipo de movimiento es requerido")
    private String tipoMovimiento;  // ENTRADA, SALIDA, AJUSTE, TRANSFERENCIA

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;

    @NotNull(message = "El usuario es requerido")
    private Integer usuarioId;

    private String referenciaTipo;  // COMPRA, VENTA, AJUSTE, TRANSFERENCIA

    private Long referenciaId;

    private Integer almacenDestinoId;  // Solo para transferencias

    private String observaciones;
}

