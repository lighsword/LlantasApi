package com.proyectoMaycollins.LlantasApi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRegisterRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String nombre;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotNull
    private Role rol;
}
