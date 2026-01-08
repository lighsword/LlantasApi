package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

@Data
public class ClientesDTO {
    private Long id;
    private String nombreCliente;
    private String email;
    private String telefonoCliente;
    private String direccion;
    private String documentoIdentidad;
    private String tipoCliente;
    private String nivelCliente;
    private Integer totalCompras;
    private Boolean activo;
}

