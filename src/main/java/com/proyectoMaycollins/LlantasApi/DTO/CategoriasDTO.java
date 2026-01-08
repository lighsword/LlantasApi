package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

@Data
public class CategoriasDTO {
    private Long categoriasId;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}

