package com.proyectoMaycollins.LlantasApi.Model;

import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad para mapear la tabla roles_permisos de la BD
 * Relaciona roles con permisos específicos
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles_permisos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"rol_id", "permiso_id"})
})
public class RolPermiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_permiso_id")
    private Long rolPermisoId;

    @Column(name = "rol_id", nullable = false)
    private Long rolId;

    @Column(name = "permiso_id", nullable = false)
    private Long permisoId;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permiso_id", insertable = false, updatable = false)
    private Permisos permiso;

    @PrePersist
    protected void onCreate() {
        if (fechaAsignacion == null) {
            fechaAsignacion = LocalDateTime.now();
        }
    }
}

