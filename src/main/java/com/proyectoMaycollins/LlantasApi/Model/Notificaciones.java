package com.proyectoMaycollins.LlantasApi.Model;

import com.proyectoMaycollins.LlantasApi.Model.enums.EstadoNotificacion;
import com.proyectoMaycollins.LlantasApi.Model.enums.PrioridadNotificacion;
import com.proyectoMaycollins.LlantasApi.Model.enums.TipoNotificacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notificaciones")
public class Notificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificacion_id")
    private Integer id;

    @Column(name = "usuario_id", insertable = false, updatable = false)
    private Long usuarioId;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    private String titulo;

    @Column(name = "mensaje_notificacion")
    private String mensajeNotificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion")
    private TipoNotificacion tipoNotificacion;

    @Enumerated(EnumType.STRING)
    private PrioridadNotificacion prioridad;

    @Enumerated(EnumType.STRING)
    private EstadoNotificacion estado;

    @Column(name = "fecha_envio", insertable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_leido")
    private LocalDateTime fechaLeido;
}

