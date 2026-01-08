package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Notificaciones;
import com.proyectoMaycollins.LlantasApi.Model.enums.EstadoNotificacion;
import com.proyectoMaycollins.LlantasApi.Model.enums.PrioridadNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar las notificaciones del sistema
 * Permite enviar alertas y mensajes a los usuarios
 */
@Repository
public interface NotificacionesRepository extends JpaRepository<Notificaciones, Integer> {

    /**
     * Busca todas las notificaciones de un usuario específico
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones del usuario
     */
    List<Notificaciones> findByUsuarioId(Long usuarioId);

    /**
     * Busca notificaciones por estado
     * @param estado Estado de la notificación (PENDIENTE, ENVIADA, LEIDA, etc.)
     * @return Lista de notificaciones con ese estado
     */
    List<Notificaciones> findByEstado(EstadoNotificacion estado);

    /**
     * Busca notificaciones de un usuario con un estado específico
     * @param usuarioId ID del usuario
     * @param estado Estado de la notificación
     * @return Lista de notificaciones filtradas
     */
    List<Notificaciones> findByUsuarioIdAndEstado(Long usuarioId, EstadoNotificacion estado);

    /**
     * Busca notificaciones por prioridad
     * @param prioridad Prioridad (BAJA, MEDIA, ALTA, URGENTE, CRITICA)
     * @return Lista de notificaciones con esa prioridad
     */
    List<Notificaciones> findByPrioridad(PrioridadNotificacion prioridad);

    /**
     * Busca notificaciones no leídas de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones pendientes o enviadas (no leídas)
     */
    List<Notificaciones> findByUsuarioIdAndEstadoIn(Long usuarioId, List<EstadoNotificacion> estados);

    /**
     * Cuenta las notificaciones no leídas de un usuario
     * @param usuarioId ID del usuario
     * @param estados Lista de estados a contar
     * @return Cantidad de notificaciones no leídas
     */
    Long countByUsuarioIdAndEstadoIn(Long usuarioId, List<EstadoNotificacion> estados);

    /**
     * Obtiene las últimas notificaciones de un usuario ordenadas por fecha
     * @param usuarioId ID del usuario
     * @return Lista de las 20 notificaciones más recientes
     */
    List<Notificaciones> findTop20ByUsuarioIdOrderByFechaEnvioDesc(Long usuarioId);
}

