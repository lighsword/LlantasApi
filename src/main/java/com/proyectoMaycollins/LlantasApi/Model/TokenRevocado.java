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
@Table(name = "tokens_revocados", indexes = {
    @Index(name = "idx_tokens_revocados_jti", columnList = "jti")
})
public class TokenRevocado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revocacion_id")
    private Long revocacionId;

    @Column(name = "jti", nullable = false, unique = true)
    private String jti;

    @Column(name = "fecha_revocacion")
    private LocalDateTime fechaRevocacion;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuarios usuario;

    @PrePersist
    protected void onCreate() {
        if (fechaRevocacion == null) {
            fechaRevocacion = LocalDateTime.now();
        }
    }
}
