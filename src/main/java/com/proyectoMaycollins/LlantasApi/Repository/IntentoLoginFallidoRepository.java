package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.IntentoLoginFallido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IntentoLoginFallidoRepository extends JpaRepository<IntentoLoginFallido, Long> {

    @Query("SELECT COUNT(i) FROM IntentoLoginFallido i WHERE i.email = :email AND i.fechaHora > :desde")
    long contarIntentosPorEmail(@Param("email") String email, @Param("desde") LocalDateTime desde);

    @Query("SELECT COUNT(i) FROM IntentoLoginFallido i WHERE i.ipAddress = :ip AND i.fechaHora > :desde")
    long contarIntentosPorIp(@Param("ip") String ip, @Param("desde") LocalDateTime desde);

    @Modifying
    @Query("DELETE FROM IntentoLoginFallido i WHERE i.fechaHora < :fecha")
    void eliminarIntentosAntiguos(@Param("fecha") LocalDateTime fecha);

    @Modifying
    @Query("DELETE FROM IntentoLoginFallido i WHERE i.email = :email")
    void limpiarIntentosPorEmail(@Param("email") String email);

    @Query("SELECT i FROM IntentoLoginFallido i WHERE i.email = :email AND i.bloqueadoHasta > :ahora")
    IntentoLoginFallido findBloqueoPorEmail(@Param("email") String email, @Param("ahora") LocalDateTime ahora);

    // ========== Consultas para Alertas de Seguridad ==========

    /**
     * Agrupa intentos fallidos por IP desde una fecha
     */
    @Query("SELECT i.ipAddress, COUNT(i) FROM IntentoLoginFallido i " +
           "WHERE i.fechaHora > :desde GROUP BY i.ipAddress ORDER BY COUNT(i) DESC")
    List<Object[]> contarIntentosPorIpDesde(@Param("desde") LocalDateTime desde);

    /**
     * Agrupa intentos fallidos por email desde una fecha
     */
    @Query("SELECT i.email, COUNT(i) FROM IntentoLoginFallido i " +
           "WHERE i.fechaHora > :desde GROUP BY i.email ORDER BY COUNT(i) DESC")
    List<Object[]> contarIntentosPorEmailDesde(@Param("desde") LocalDateTime desde);

    /**
     * Cuenta total de intentos desde una fecha
     */
    @Query("SELECT COUNT(i) FROM IntentoLoginFallido i WHERE i.fechaHora > :desde")
    long contarIntentosDesde(@Param("desde") LocalDateTime desde);

    /**
     * Obtiene IPs con más intentos fallidos
     */
    @Query("SELECT i.ipAddress, COUNT(i) as cantidad FROM IntentoLoginFallido i " +
           "WHERE i.fechaHora > :desde GROUP BY i.ipAddress HAVING COUNT(i) >= :umbral ORDER BY cantidad DESC")
    List<Object[]> findIpsSospechosas(@Param("desde") LocalDateTime desde, @Param("umbral") long umbral);
}

