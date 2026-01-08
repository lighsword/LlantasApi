package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacionesDTO {
    private Integer id;
    private Long usuariosId;
    private String titulo;
    private String mensajeNotificacion;
    private String tipoNotificacion;
    private String prioridad;
    private String estado;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaLeido;
}

