package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProductoDTO {
    private String nombreCategoria;
    private String modelo;
    private String medida;
    private String especificaciones;
    private String tipoRefaccion;
    private String compatibilidad;
}