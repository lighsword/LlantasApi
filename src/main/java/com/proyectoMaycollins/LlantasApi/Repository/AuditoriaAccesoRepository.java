package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.AuditoriaAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaAccesoRepository extends JpaRepository<AuditoriaAcceso, Long> {

    List<AuditoriaAcceso> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    List<AuditoriaAcceso> findByAccionAndFechaHoraAfter(String accion, LocalDateTime fecha);

    @Query("SELECT a FROM AuditoriaAcceso a WHERE a.usuarioId = :usuarioId AND a.fechaHora BETWEEN :fechaInicio AND :fechaFin ORDER BY a.fechaHora DESC")
    List<AuditoriaAcceso> findByUsuarioIdAndFechaHoraBetween(
        @Param("usuarioId") Long usuarioId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );

    // ========== Consultas para Alertas de Seguridad ==========

    /**
     * Cuenta IPs distintas por usuario desde una fecha
     */
    @Query("SELECT a.usuarioId, COUNT(DISTINCT a.ipAddress) FROM AuditoriaAcceso a " +
           "WHERE a.accion = 'LOGIN' AND a.fechaHora > :desde AND a.usuarioId IS NOT NULL " +
           "GROUP BY a.usuarioId HAVING COUNT(DISTINCT a.ipAddress) > 1")
    List<Object[]> contarIpsPorUsuarioDesde(@Param("desde") LocalDateTime desde);

    /**
     * Cuenta accesos denegados por usuario desde una fecha
     */
    @Query("SELECT a.usuarioId, COUNT(a) FROM AuditoriaAcceso a " +
           "WHERE a.accion = 'ACCESS_DENIED' AND a.fechaHora > :desde AND a.usuarioId IS NOT NULL " +
           "GROUP BY a.usuarioId")
    List<Object[]> contarAccesosDenegadosPorUsuarioDesde(@Param("desde") LocalDateTime desde);

    /**
     * Cuenta registros por acción desde una fecha
     */
    @Query("SELECT COUNT(a) FROM AuditoriaAcceso a WHERE a.accion = :accion AND a.fechaHora > :desde")
    long contarPorAccionDesde(@Param("accion") String accion, @Param("desde") LocalDateTime desde);

    /**
     * Cuenta IPs únicas desde una fecha
     */
    @Query("SELECT COUNT(DISTINCT a.ipAddress) FROM AuditoriaAcceso a WHERE a.fechaHora > :desde AND a.ipAddress IS NOT NULL")
    long contarIpsUnicasDesde(@Param("desde") LocalDateTime desde);

    /**
     * Obtiene auditorías por IP
     */
    List<AuditoriaAcceso> findByIpAddressOrderByFechaHoraDesc(String ipAddress);

    /**
     * Busca accesos recientes por usuario y acción
     */
    @Query("SELECT a FROM AuditoriaAcceso a WHERE a.usuarioId = :usuarioId AND a.accion = :accion AND a.fechaHora > :desde ORDER BY a.fechaHora DESC")
    List<AuditoriaAcceso> findRecentesPorUsuarioYAccion(
        @Param("usuarioId") Long usuarioId,
        @Param("accion") String accion,
        @Param("desde") LocalDateTime desde
    );
}

