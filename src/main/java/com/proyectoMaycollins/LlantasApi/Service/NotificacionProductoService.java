package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Notificaciones;
import com.proyectoMaycollins.LlantasApi.Model.Productos;
import com.proyectoMaycollins.LlantasApi.Model.Usuarios;
import com.proyectoMaycollins.LlantasApi.Repository.NotificacionesRepository;
import com.proyectoMaycollins.LlantasApi.Repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionProductoService {

    private final NotificacionesRepository notificacionesRepository;
    private final UsuariosRepository usuariosRepository;

    /**
     * Notifica a TODOS los usuarios cuando un producto se agota (stock = 0)
     */
    @Transactional
    public void notificarProductoAgotado(Productos producto) {
        // Obtener todos los usuarios activos
        List<Usuarios> usuariosActivos = usuariosRepository.findAll()
                .stream()
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .toList();

        String mensaje = String.format(
                "⚠️ PRODUCTO AGOTADO: '%s' (Código: %s) ya no tiene stock disponible. " +
                "El producto ha sido ocultado automáticamente del catálogo hasta nueva reposición.",
                producto.getDescripcion(),
                producto.getCodigoProducto()
        );

        // Crear notificación para cada usuario
        for (Usuarios usuario : usuariosActivos) {
            Notificaciones notificacion = new Notificaciones();
            notificacion.setTitulo("Producto Agotado");
            notificacion.setMensajeNotificacion(mensaje); // ✅ Campo correcto
            // Sin setTipo, setFechaCreacion, setLeido - Usar valores por defecto o enum

            notificacionesRepository.save(notificacion);

            log.info("📢 Notificación de producto agotado enviada a usuario: {} - Producto: {}",
                    usuario.getEmail(), producto.getCodigoProducto());
        }

        log.warn("⚠️ PRODUCTO AGOTADO: {} ({}) - {} usuarios notificados",
                producto.getCodigoProducto(), producto.getDescripcion(), usuariosActivos.size());
    }

    /**
     * Notifica cuando un producto inactivo recibe stock nuevamente
     */
    @Transactional
    public void notificarProductoReactivado(Productos producto, int nuevoStock) {
        List<Usuarios> usuariosActivos = usuariosRepository.findAll()
                .stream()
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .toList();

        String mensaje = String.format(
                "✅ PRODUCTO DISPONIBLE: '%s' (Código: %s) ha sido reabastecido con %d unidades. " +
                "El producto está nuevamente visible en el catálogo.",
                producto.getDescripcion(),
                producto.getCodigoProducto(),
                nuevoStock
        );

        for (Usuarios usuario : usuariosActivos) {
            Notificaciones notificacion = new Notificaciones();
            notificacion.setTitulo("Producto Reabastecido");
            notificacion.setMensajeNotificacion(mensaje);

            notificacionesRepository.save(notificacion);
        }

        log.info("✅ PRODUCTO REACTIVADO: {} ({}) con {} unidades - {} usuarios notificados",
                producto.getCodigoProducto(), producto.getDescripcion(), nuevoStock, usuariosActivos.size());
    }

    /**
     * Notifica cuando un producto es desactivado manualmente (ya no se vende)
     */
    @Transactional
    public void notificarProductoDesactivado(Productos producto, String motivo) {
        List<Usuarios> usuariosActivos = usuariosRepository.findAll()
                .stream()
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .toList();

        String mensaje = String.format(
                "🚫 PRODUCTO DESCONTINUADO: '%s' (Código: %s) ha sido desactivado y ya no está disponible para la venta. " +
                "Motivo: %s. El producto permanece en la base de datos para reportes históricos.",
                producto.getDescripcion(),
                producto.getCodigoProducto(),
                motivo != null ? motivo : "No especificado"
        );

        for (Usuarios usuario : usuariosActivos) {
            Notificaciones notificacion = new Notificaciones();
            notificacion.setTitulo("Producto Descontinuado");
            notificacion.setMensajeNotificacion(mensaje);

            notificacionesRepository.save(notificacion);
        }

        log.info("🚫 PRODUCTO DESACTIVADO: {} ({}) - Motivo: {} - {} usuarios notificados",
                producto.getCodigoProducto(), producto.getDescripcion(), motivo, usuariosActivos.size());
    }
}

