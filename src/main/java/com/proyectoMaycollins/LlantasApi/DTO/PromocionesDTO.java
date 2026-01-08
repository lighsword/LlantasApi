package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PromocionesDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String tipo;
    private Double valor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
}

