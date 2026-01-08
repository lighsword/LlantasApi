package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoriaProductosDTO {
    private Long categoriasId;
    private String compatibilidad;
    private String especificaciones;
    private String medida;
    private String modelo;
    private String nombreCategoria;
    private String tipoRefaccion;
    private Boolean verificarDisponibilidad;
    private Boolean activo;
    private String descripcion;
    private String nombre;
    private Long productosId;
    private LocalDateTime fechaCreacion;
}

