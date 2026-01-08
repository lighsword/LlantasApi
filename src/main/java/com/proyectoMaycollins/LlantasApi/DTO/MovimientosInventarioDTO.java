package com.proyectoMaycollins.LlantasApi.DTO;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class MovimientosInventarioDTO {


    private String observaciones;
    private String almacenDestinoNombre;
    private Integer almacenDestinoId;
    private Long referenciaId;
    private String referenciaTipo;
    private String usuarioNombre;
    private Integer usuarioId;
    private OffsetDateTime fechaMovimiento;
    private Integer cantidadNueva;
    private Integer cantidadAnterior;
    private Integer cantidad;
    private String tipoMovimiento;
    private String productoDescripcion;
    private Long productoId;
    private String almacenNombre;
    private Integer almacenId;
    private Long movimientoId;

}