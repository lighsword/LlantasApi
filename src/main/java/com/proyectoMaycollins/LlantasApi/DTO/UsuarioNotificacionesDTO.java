package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

@Data
public class UsuarioNotificacionesDTO {
    private Long usuariosId;
    private String tipoNotificacion;
    private String canalNotificacion;
}

