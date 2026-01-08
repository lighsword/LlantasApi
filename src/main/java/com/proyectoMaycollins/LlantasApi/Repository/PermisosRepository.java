package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Permisos;
import com.proyectoMaycollins.LlantasApi.Model.enums.Accion;
import com.proyectoMaycollins.LlantasApi.Model.enums.Modulo;
import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermisosRepository extends JpaRepository<Permisos, Long> {

    /**
     * Busca un permiso específico
     */
    Optional<Permisos> findByRolAndModuloAndAccion(Role rol, Modulo modulo, Accion accion);

    /**
     * Obtiene todos los permisos de un rol
     */
    List<Permisos> findByRol(Role rol);

    /**
     * Obtiene todos los permisos de un rol que están permitidos
     */
    List<Permisos> findByRolAndPermitidoTrue(Role rol);

    /**
     * Obtiene todos los permisos de un módulo específico
     */
    List<Permisos> findByModulo(Modulo modulo);

    /**
     * Verifica si un rol tiene permiso para una acción en un módulo
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM Permisos p " +
           "WHERE p.rol = :rol AND p.modulo = :modulo AND p.accion = :accion AND p.permitido = true")
    boolean tienePermiso(Role rol, Modulo modulo, Accion accion);
}

