package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuariosDTO {
    private Long id;
    private String email;
    private String nombre;
    private String password;
    private String rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}

