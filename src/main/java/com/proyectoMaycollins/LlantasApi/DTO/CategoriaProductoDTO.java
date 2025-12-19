package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProductoDTO {
    private String nombre;
    private String descripcion;
    private Boolean activo;
}