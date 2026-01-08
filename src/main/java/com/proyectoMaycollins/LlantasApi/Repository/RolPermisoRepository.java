package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, Long> {

    /**
     * Obtiene todos los permisos asignados a un rol
     */
    List<RolPermiso> findByRolId(Long rolId);

    /**
     * Verifica si un rol tiene un permiso específico
     */
    boolean existsByRolIdAndPermisoId(Long rolId, Long permisoId);

    /**
     * Elimina todos los permisos de un rol
     */
    void deleteByRolId(Long rolId);
}

