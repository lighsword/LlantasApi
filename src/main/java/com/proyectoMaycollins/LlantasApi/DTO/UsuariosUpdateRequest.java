package com.proyectoMaycollins.LlantasApi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import lombok.Data;

@Data
public class UsuariosUpdateRequest {
    private String email;
    private String nombre;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Role rol;
    private Boolean activo;
}

