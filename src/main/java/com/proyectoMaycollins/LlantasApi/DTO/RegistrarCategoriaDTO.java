package com.proyectoMaycollins.LlantasApi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarCategoriaDTO {

    @NotBlank(message = "El nombre de la categoría es requerido")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no debe exceder 1000 caracteres")
    private String descripcion;

    @Builder.Default
    private Boolean activo = true;
}

