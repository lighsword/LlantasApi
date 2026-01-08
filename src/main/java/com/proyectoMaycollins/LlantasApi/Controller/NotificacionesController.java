package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Notificaciones;
import com.proyectoMaycollins.LlantasApi.Model.enums.EstadoNotificacion;
import com.proyectoMaycollins.LlantasApi.Model.enums.PrioridadNotificacion;
import com.proyectoMaycollins.LlantasApi.Service.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar Notificaciones
 * Expone endpoints para crear, leer y gestionar notificaciones del sistema
 */
@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionesController {

    @Autowired
    private NotificacionesService notificacionesService;

    /**
     * Obtiene todas las notificaciones del sistema
     * GET /api/notificaciones
     * @return Lista de notificaciones
     */
    @GetMapping
    public ResponseEntity<List<Notificaciones>> obtenerTodas() {
        List<Notificaciones> notificaciones = notificacionesService.obtenerTodas();
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtiene una notificación por su ID
     * GET /api/notificaciones/{id}
     * @param id ID de la notificación
     * @return Notificación encontrada o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notificaciones> obtenerPorId(@PathVariable Integer id) {
        return notificacionesService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las notificaciones de un usuario
     * GET /api/notificaciones/usuario/{usuarioId}
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificaciones>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        List<Notificaciones> notificaciones = notificacionesService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtiene las últimas 20 notificaciones de un usuario
     * GET /api/notificaciones/usuario/{usuarioId}/recientes
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones recientes
     */
    @GetMapping("/usuario/{usuarioId}/recientes")
    public ResponseEntity<List<Notificaciones>> obtenerRecientesPorUsuario(@PathVariable Long usuarioId) {
        List<Notificaciones> notificaciones = notificacionesService.obtenerRecientesPorUsuario(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtiene las notificaciones no leídas de un usuario
     * GET /api/notificaciones/usuario/{usuarioId}/no-leidas
     * Incluye notificaciones con estado PENDIENTE y ENVIADA
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones sin leer
     */
    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificaciones>> obtenerNoLeidasPorUsuario(@PathVariable Long usuarioId) {
        List<Notificaciones> notificaciones = notificacionesService.obtenerNoLeidasPorUsuario(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Cuenta las notificaciones no leídas de un usuario
     * GET /api/notificaciones/usuario/{usuarioId}/contar-no-leidas
     * @param usuarioId ID del usuario
     * @return Cantidad de notificaciones sin leer
     */
    @GetMapping("/usuario/{usuarioId}/contar-no-leidas")
    public ResponseEntity<Map<String, Long>> contarNoLeidasPorUsuario(@PathVariable Long usuarioId) {
        Long cantidad = notificacionesService.contarNoLeidasPorUsuario(usuarioId);
        return ResponseEntity.ok(Map.of("cantidad", cantidad));
    }

    /**
     * Obtiene notificaciones por prioridad
     * GET /api/notificaciones/prioridad/{prioridad}
     * @param prioridad Prioridad (BAJA, MEDIA, ALTA, URGENTE, CRITICA)
     * @return Lista de notificaciones con esa prioridad
     */
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<Notificaciones>> obtenerPorPrioridad(
            @PathVariable PrioridadNotificacion prioridad) {
        List<Notificaciones> notificaciones = notificacionesService.obtenerPorPrioridad(prioridad);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtiene notificaciones por estado
     * GET /api/notificaciones/estado/{estado}
     * @param estado Estado (PENDIENTE, ENVIADA, LEIDA, ARCHIVADA, ELIMINADA)
     * @return Lista de notificaciones con ese estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificaciones>> obtenerPorEstado(
            @PathVariable EstadoNotificacion estado) {
        List<Notificaciones> notificaciones = notificacionesService.obtenerPorEstado(estado);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Crea una nueva notificación
     * POST /api/notificaciones
     * Establece automáticamente fecha de envío y estado inicial
     * @param notificacion Datos de la notificación
     * @return Notificación creada con status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<Notificaciones> crear(@RequestBody Notificaciones notificacion) {
        try {
            Notificaciones nuevaNotificacion = notificacionesService.crear(notificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Marca una notificación como leída
     * PUT /api/notificaciones/{id}/marcar-leida
     * Actualiza el estado a LEIDA y registra la fecha de lectura
     * @param id ID de la notificación
     * @return Notificación actualizada o 404 si no existe
     */
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<Notificaciones> marcarComoLeida(@PathVariable Integer id) {
        try {
            Notificaciones notificacion = notificacionesService.marcarComoLeida(id);
            return ResponseEntity.ok(notificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Marca todas las notificaciones de un usuario como leídas
     * PUT /api/notificaciones/usuario/{usuarioId}/marcar-todas-leidas
     * @param usuarioId ID del usuario
     * @return Cantidad de notificaciones actualizadas
     */
    @PutMapping("/usuario/{usuarioId}/marcar-todas-leidas")
    public ResponseEntity<Map<String, Integer>> marcarTodasComoLeidas(@PathVariable Long usuarioId) {
        int cantidad = notificacionesService.marcarTodasComoLeidasPorUsuario(usuarioId);
        return ResponseEntity.ok(Map.of("actualizadas", cantidad));
    }

    /**
     * Archiva una notificación
     * PUT /api/notificaciones/{id}/archivar
     * @param id ID de la notificación
     * @return Notificación archivada o 404 si no existe
     */
    @PutMapping("/{id}/archivar")
    public ResponseEntity<Notificaciones> archivar(@PathVariable Integer id) {
        try {
            Notificaciones notificacion = notificacionesService.archivar(id);
            return ResponseEntity.ok(notificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina una notificación por su ID
     * DELETE /api/notificaciones/{id}
     * @param id ID de la notificación a eliminar
     * @return 204 (NO CONTENT) si se eliminó, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            notificacionesService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una notificación de stock bajo (método auxiliar)
     * POST /api/notificaciones/stock-bajo
     * @param request Objeto con usuarioId, nombreProducto y stockActual
     * @return Notificación creada
     */
    @PostMapping("/stock-bajo")
    public ResponseEntity<Notificaciones> crearNotificacionStockBajo(@RequestBody Map<String, Object> request) {
        try {
            Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
            String nombreProducto = request.get("nombreProducto").toString();
            Integer stockActual = Integer.valueOf(request.get("stockActual").toString());

            Notificaciones notificacion = notificacionesService.crearNotificacionStockBajo(
                    usuarioId, nombreProducto, stockActual);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crea una notificación de venta (método auxiliar)
     * POST /api/notificaciones/venta
     * @param request Objeto con usuarioId, montoVenta y numeroComprobante
     * @return Notificación creada
     */
    @PostMapping("/venta")
    public ResponseEntity<Notificaciones> crearNotificacionVenta(@RequestBody Map<String, Object> request) {
        try {
            Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
            Double montoVenta = Double.valueOf(request.get("montoVenta").toString());
            String numeroComprobante = request.get("numeroComprobante").toString();

            Notificaciones notificacion = notificacionesService.crearNotificacionVenta(
                    usuarioId, montoVenta, numeroComprobante);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

