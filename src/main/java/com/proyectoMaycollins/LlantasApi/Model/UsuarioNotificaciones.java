package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario_notificaciones")
@IdClass(UsuarioNotificaciones.UsuarioNotificacionesId.class)
public class UsuarioNotificaciones {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Id
    @Column(name = "tipo_notificacion")
    private String tipoNotificacion;

    @Column(name = "canal_notificacion")
    private String canalNotificacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuarios usuario;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UsuarioNotificacionesId implements Serializable {
        private Long usuarioId;
        private String tipoNotificacion;
    }
}

