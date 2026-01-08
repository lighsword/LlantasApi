package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReportesDTO {
    private Long id;
    private String nombreReporte;
    private String tipoReporte;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime fechaPuntual;
    private LocalDateTime fechaGeneracion;
    private Long generadoPorId;
    private String tituloImpreso;
    private String resumen; // JSON en String
    private String detalle; // JSON en String obligatorio
    private String versionDatos;
    private String formatoSalida;
    private Boolean firmado;
    private String observaciones;
    private Integer proveedoresId; // en schema es BIGINT, pero proveedores_id en proveedores es INTEGER; aquí usaré Integer para id de proveedor
    private Integer almacenesId;
}

