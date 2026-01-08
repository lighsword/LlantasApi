package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "auditoria_accesos", indexes = {
    @Index(name = "idx_auditoria_usuario", columnList = "usuario_id"),
    @Index(name = "idx_auditoria_fecha", columnList = "fecha_hora"),
    @Index(name = "idx_auditoria_accion", columnList = "accion"),
    @Index(name = "idx_auditoria_ip", columnList = "ip_address")
})
public class AuditoriaAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditoria_id")
    private Long auditoriaId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "accion", length = 50, nullable = false)
    private String accion; // LOGIN, LOGOUT, REFRESH_TOKEN, ACCESS_DENIED, etc.

    @Column(name = "recurso")
    private String recurso;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "endpoint", length = 255)
    private String endpoint;

    @Column(name = "metodo_http", length = 10)
    private String metodoHttp;

    @Column(name = "exitoso")
    private Boolean exitoso;

    @Column(name = "mensaje", length = 500)
    private String mensaje;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuarios usuario;

    @PrePersist
    protected void onCreate() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
        if (exitoso == null) {
            exitoso = true;
        }
    }
}
