package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Notificaciones;
import com.proyectoMaycollins.LlantasApi.Model.Usuarios;
import com.proyectoMaycollins.LlantasApi.Model.enums.EstadoNotificacion;
import com.proyectoMaycollins.LlantasApi.Model.enums.PrioridadNotificacion;
import com.proyectoMaycollins.LlantasApi.Repository.NotificacionesRepository;
import com.proyectoMaycollins.LlantasApi.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar la lógica de negocio de Notificaciones
 * Permite enviar, leer y gestionar notificaciones a los usuarios
 */
@Service
public class NotificacionesService {

    @Autowired
    private NotificacionesRepository notificacionesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    /**
     * Obtiene todas las notificaciones del sistema
     * @return Lista de todas las notificaciones
     */
    public List<Notificaciones> obtenerTodas() {
        return notificacionesRepository.findAll();
    }

    /**
     * Busca una notificación por su ID
     * @param id ID de la notificación
     * @return Optional con la notificación si existe
     */
    public Optional<Notificaciones> obtenerPorId(Integer id) {
        return notificacionesRepository.findById(id);
    }

    /**
     * Obtiene todas las notificaciones de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones del usuario
     */
    public List<Notificaciones> obtenerPorUsuario(Long usuarioId) {
        return notificacionesRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Obtiene las últimas 20 notificaciones de un usuario
     * Ordenadas por fecha descendente (más recientes primero)
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones recientes
     */
    public List<Notificaciones> obtenerRecientesPorUsuario(Long usuarioId) {
        return notificacionesRepository.findTop20ByUsuarioIdOrderByFechaEnvioDesc(usuarioId);
    }

    /**
     * Obtiene las notificaciones no leídas de un usuario
     * Incluye notificaciones con estado PENDIENTE y ENVIADA
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones no leídas
     */
    public List<Notificaciones> obtenerNoLeidasPorUsuario(Long usuarioId) {
        List<EstadoNotificacion> estadosNoLeidos = Arrays.asList(
            EstadoNotificacion.PENDIENTE,
            EstadoNotificacion.ENVIADA
        );
        return notificacionesRepository.findByUsuarioIdAndEstadoIn(usuarioId, estadosNoLeidos);
    }

    /**
     * Cuenta las notificaciones no leídas de un usuario
     * @param usuarioId ID del usuario
     * @return Cantidad de notificaciones sin leer
     */
    public Long contarNoLeidasPorUsuario(Long usuarioId) {
        List<EstadoNotificacion> estadosNoLeidos = Arrays.asList(
            EstadoNotificacion.PENDIENTE,
            EstadoNotificacion.ENVIADA
        );
        return notificacionesRepository.countByUsuarioIdAndEstadoIn(usuarioId, estadosNoLeidos);
    }

    /**
     * Obtiene notificaciones por prioridad
     * @param prioridad Prioridad de la notificación
     * @return Lista de notificaciones con esa prioridad
     */
    public List<Notificaciones> obtenerPorPrioridad(PrioridadNotificacion prioridad) {
        return notificacionesRepository.findByPrioridad(prioridad);
    }

    /**
     * Obtiene notificaciones por estado
     * @param estado Estado de la notificación
     * @return Lista de notificaciones con ese estado
     */
    public List<Notificaciones> obtenerPorEstado(EstadoNotificacion estado) {
        return notificacionesRepository.findByEstado(estado);
    }

    /**
     * Crea una nueva notificación
     * Establece automáticamente la fecha de envío y el estado inicial
     * @param notificacion Datos de la notificación
     * @return Notificación creada
     */
    @Transactional
    public Notificaciones crear(Notificaciones notificacion) {
        // Establecer la fecha de envío si no viene
        if (notificacion.getFechaEnvio() == null) {
            notificacion.setFechaEnvio(LocalDateTime.now());
        }

        // Establecer estado inicial si no viene
        if (notificacion.getEstado() == null) {
            notificacion.setEstado(EstadoNotificacion.PENDIENTE);
        }

        // Establecer prioridad por defecto si no viene
        if (notificacion.getPrioridad() == null) {
            notificacion.setPrioridad(PrioridadNotificacion.MEDIA);
        }

        // Guardar la notificación
        return notificacionesRepository.save(notificacion);
    }

    /**
     * Marca una notificación como leída
     * Actualiza el estado a LEIDA y registra la fecha de lectura
     * @param id ID de la notificación
     * @return Notificación actualizada
     * @throws RuntimeException si la notificación no existe
     */
    @Transactional
    public Notificaciones marcarComoLeida(Integer id) {
        // Buscar la notificación
        Notificaciones notificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

        // Actualizar estado y fecha de lectura
        notificacion.setEstado(EstadoNotificacion.LEIDA);
        notificacion.setFechaLeido(LocalDateTime.now());

        // Guardar cambios
        return notificacionesRepository.save(notificacion);
    }

    /**
     * Marca múltiples notificaciones como leídas
     * @param usuarioId ID del usuario
     * @return Cantidad de notificaciones actualizadas
     */
    @Transactional
    public int marcarTodasComoLeidasPorUsuario(Long usuarioId) {
        // Obtener todas las notificaciones no leídas del usuario
        List<Notificaciones> notificaciones = obtenerNoLeidasPorUsuario(usuarioId);

        // Marcar cada una como leída
        for (Notificaciones notificacion : notificaciones) {
            notificacion.setEstado(EstadoNotificacion.LEIDA);
            notificacion.setFechaLeido(LocalDateTime.now());
            notificacionesRepository.save(notificacion);
        }

        return notificaciones.size();
    }

    /**
     * Archiva una notificación
     * @param id ID de la notificación
     * @return Notificación actualizada
     * @throws RuntimeException si la notificación no existe
     */
    @Transactional
    public Notificaciones archivar(Integer id) {
        // Buscar la notificación
        Notificaciones notificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

        // Actualizar estado
        notificacion.setEstado(EstadoNotificacion.ARCHIVADA);

        // Guardar cambios
        return notificacionesRepository.save(notificacion);
    }

    /**
     * Elimina una notificación por su ID
     * @param id ID de la notificación a eliminar
     * @throws RuntimeException si la notificación no existe
     */
    @Transactional
    public void eliminar(Integer id) {
        // Verificar que la notificación existe
        if (!notificacionesRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada con ID: " + id);
        }

        // Eliminar la notificación
        notificacionesRepository.deleteById(id);
    }

    /**
     * Crea una notificación de stock bajo
     * Método auxiliar para facilitar la creación de alertas de inventario
     * @param usuarioId ID del usuario a notificar
     * @param nombreProducto Nombre del producto con stock bajo
     * @param stockActual Stock actual del producto
     * @return Notificación creada
     */
    @Transactional
    public Notificaciones crearNotificacionStockBajo(Long usuarioId, String nombreProducto, Integer stockActual) {
        // Buscar el usuario
        Usuarios usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        Notificaciones notificacion = new Notificaciones();
        notificacion.setUsuario(usuario);
        notificacion.setTitulo("Stock Bajo");
        notificacion.setMensajeNotificacion(
            String.format("El producto '%s' tiene solo %d unidades en stock", nombreProducto, stockActual)
        );
        notificacion.setTipoNotificacion(com.proyectoMaycollins.LlantasApi.Model.enums.TipoNotificacion.STOCK_BAJO);
        notificacion.setPrioridad(PrioridadNotificacion.ALTA);
        notificacion.setEstado(EstadoNotificacion.PENDIENTE);
        notificacion.setFechaEnvio(LocalDateTime.now());

        return notificacionesRepository.save(notificacion);
    }

    /**
     * Crea una notificación de venta realizada
     * Método auxiliar para notificar sobre nuevas ventas
     * @param usuarioId ID del usuario a notificar
     * @param montoVenta Monto de la venta
     * @param numeroComprobante Número de comprobante
     * @return Notificación creada
     */
    @Transactional
    public Notificaciones crearNotificacionVenta(Long usuarioId, Double montoVenta, String numeroComprobante) {
        // Buscar el usuario
        Usuarios usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        Notificaciones notificacion = new Notificaciones();
        notificacion.setUsuario(usuario);
        notificacion.setTitulo("Nueva Venta Registrada");
        notificacion.setMensajeNotificacion(
            String.format("Se ha registrado una venta por S/ %.2f - Comprobante: %s",
                         montoVenta, numeroComprobante)
        );
        notificacion.setTipoNotificacion(com.proyectoMaycollins.LlantasApi.Model.enums.TipoNotificacion.VENTA);
        notificacion.setPrioridad(PrioridadNotificacion.MEDIA);
        notificacion.setEstado(EstadoNotificacion.PENDIENTE);
        notificacion.setFechaEnvio(LocalDateTime.now());

        return notificacionesRepository.save(notificacion);
    }
}

