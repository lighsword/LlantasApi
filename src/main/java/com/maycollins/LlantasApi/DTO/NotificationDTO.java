package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Integer notificationId;
    private String notificationType;  // "Alerta", "Informacion", "Advertencia", "Error"
    private String title;
    private String notificationMessage;
    private String priority;  // "Alta", "Media", "Baja"
    private String status;    // "No leida", "Leida", "Archivada"
    private LocalDateTime sendDate;
    private LocalDateTime readDate;
    private Integer userId;
}