package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.SesionActiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionActivaRepository extends JpaRepository<SesionActiva, Long> {

    Optional<SesionActiva> findByJtiAccessAndActivoTrue(String jtiAccess);

    Optional<SesionActiva> findByRefreshTokenAndActivoTrue(String refreshToken);

    List<SesionActiva> findByUsuarioIdAndActivoTrue(Long usuarioId);

    @Query("SELECT COUNT(s) FROM SesionActiva s WHERE s.usuarioId = :usuarioId AND s.activo = true")
    long contarSesionesActivasPorUsuario(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("UPDATE SesionActiva s SET s.activo = false WHERE s.usuarioId = :usuarioId AND s.activo = true")
    void revocarTodasLasSesionesPorUsuario(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("UPDATE SesionActiva s SET s.activo = false WHERE s.jtiAccess = :jtiAccess")
    void revocarSesionPorJti(@Param("jtiAccess") String jtiAccess);

    @Modifying
    @Query("DELETE FROM SesionActiva s WHERE s.fechaExpiracion < :fecha")
    void eliminarSesionesExpiradas(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT s FROM SesionActiva s WHERE s.usuarioId = :usuarioId ORDER BY s.fechaCreacion DESC")
    List<SesionActiva> obtenerHistorialSesionesPorUsuario(@Param("usuarioId") Long usuarioId);

    boolean existsByJtiAccessAndActivoTrue(String jtiAccess);
}

