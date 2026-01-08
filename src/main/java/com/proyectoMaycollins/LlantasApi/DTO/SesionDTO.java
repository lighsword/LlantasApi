package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SesionDTO {
    private Long sesionId;
    private String dispositivo;
    private String ipAddress;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaUltimaActividad;
    private Boolean activo;
    private String ubicacionAproximada; // Basado en IP
}

