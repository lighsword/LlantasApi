package com.proyectoMaycollins.LlantasApi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuariosCreateRequest {
    @Email @NotBlank
    private String email;
    @NotBlank
    private String nombre;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Role rol;
    private Boolean activo;
}

