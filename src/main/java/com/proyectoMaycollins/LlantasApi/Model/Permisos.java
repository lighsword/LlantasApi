package com.proyectoMaycollins.LlantasApi.Model;

import com.proyectoMaycollins.LlantasApi.Model.enums.Accion;
import com.proyectoMaycollins.LlantasApi.Model.enums.Modulo;
import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

/**
 * Matriz de permisos: Define qué roles pueden realizar qué acciones en qué módulos
 *
 * Ejemplo:
 * - Rol: VENDEDOR
 * - Modulo: VENTAS
 * - Accion: CREAR
 * - Permitido: true
 *
 * Esto significa que un VENDEDOR PUEDE CREAR ventas
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permisos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"rol", "modulo", "accion"})
})
public class Permisos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permiso_id")
    private Long permisoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 50)
    private Role rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "modulo", nullable = false, length = 50)
    private Modulo modulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false, length = 50)
    private Accion accion;

    @Column(name = "permitido", nullable = false)
    private Boolean permitido;

    @Column(name = "descripcion", length = 500)
    private String descripcion;
}

