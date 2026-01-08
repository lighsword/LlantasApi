package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Inventario;
import com.proyectoMaycollins.LlantasApi.Model.Productos;
import com.proyectoMaycollins.LlantasApi.Repository.InventarioRepository;
import com.proyectoMaycollins.LlantasApi.Repository.ProductosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar la visibilidad de productos basada en stock y estado activo
 *
 * Reglas de visibilidad:
 * 1. Producto con activo=false → NUNCA visible (descontinuado)
 * 2. Producto con activo=true y stock=0 → NO visible (agotado temporalmente)
 * 3. Producto con activo=true y stock>0 → VISIBLE
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoVisibilidadService {

    private final ProductosRepository productosRepository;
    private final InventarioRepository inventarioRepository;
    private final NotificacionProductoService notificacionProductoService;

    /**
     * Verifica si un producto es visible en el catálogo
     */
    public boolean esProductoVisible(Long productoId) {
        Productos producto = productosRepository.findById(productoId)
                .orElse(null);

        if (producto == null || !Boolean.TRUE.equals(producto.getActivo())) {
            return false;
        }

        // Verificar stock total en todos los almacenes
        int stockTotal = calcularStockTotal(productoId);
        return stockTotal > 0;
    }

    /**
     * Calcula el stock total de un producto en todos los almacenes
     */
    public int calcularStockTotal(Long productoId) {
        List<Inventario> inventarios = inventarioRepository.findByProductoId(productoId.intValue());
        return inventarios.stream()
                .mapToInt(inv -> inv.getCantidad() != null ? inv.getCantidad() : 0)
                .sum();
    }

    /**
     * Obtiene todos los productos VISIBLES (activo=true y stock>0)
     */
    public List<Productos> obtenerProductosVisibles() {
        List<Productos> productosActivos = productosRepository.findByActivoTrue();

        return productosActivos.stream()
                .filter(producto -> {
                    int stock = calcularStockTotal(producto.getProductoId());
                    return stock > 0;
                })
                .toList();
    }

    /**
     * Obtiene productos agotados (activo=true pero stock=0)
     */
    public List<Productos> obtenerProductosAgotados() {
        List<Productos> productosActivos = productosRepository.findByActivoTrue();

        return productosActivos.stream()
                .filter(producto -> {
                    int stock = calcularStockTotal(producto.getProductoId());
                    return stock == 0;
                })
                .toList();
    }

    /**
     * Obtiene productos descontinuados (activo=false)
     */
    public List<Productos> obtenerProductosDescontinuados() {
        return productosRepository.findByActivoFalse();
    }

    /**
     * Verifica el stock de un producto y notifica si está agotado
     * Se debe llamar después de cada venta o movimiento de inventario
     */
    @Transactional
    public void verificarYNotificarStock(Long productoId) {
        Productos producto = productosRepository.findById(productoId)
                .orElse(null);

        if (producto == null || !Boolean.TRUE.equals(producto.getActivo())) {
            return;
        }

        int stockTotal = calcularStockTotal(productoId);

        if (stockTotal == 0) {
            log.warn("⚠️ Stock agotado detectado - Producto: {} ({})",
                    producto.getCodigoProducto(), producto.getDescripcion());

            // Notificar a todos los usuarios
            notificacionProductoService.notificarProductoAgotado(producto);
        }
    }

    /**
     * Desactiva un producto (soft delete) y notifica
     * El producto permanece en BD para reportes históricos
     */
    @Transactional
    public void desactivarProducto(Long productoId, String motivo) {
        Productos producto = productosRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!Boolean.TRUE.equals(producto.getActivo())) {
            log.warn("Producto {} ya está inactivo", productoId);
            return;
        }

        producto.setActivo(false);
        productosRepository.save(producto);

        log.info("🚫 Producto desactivado (soft delete): {} - Motivo: {}",
                producto.getCodigoProducto(), motivo);

        // Notificar a todos los usuarios
        notificacionProductoService.notificarProductoDesactivado(producto, motivo);
    }

    /**
     * Reactiva un producto y notifica si tiene stock
     */
    @Transactional
    public void reactivarProducto(Long productoId) {
        Productos producto = productosRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (Boolean.TRUE.equals(producto.getActivo())) {
            log.warn("Producto {} ya está activo", productoId);
            return;
        }

        producto.setActivo(true);
        productosRepository.save(producto);

        int stockTotal = calcularStockTotal(productoId);

        log.info("✅ Producto reactivado: {} - Stock actual: {}",
                producto.getCodigoProducto(), stockTotal);

        // Solo notificar si tiene stock disponible
        if (stockTotal > 0) {
            notificacionProductoService.notificarProductoReactivado(producto, stockTotal);
        }
    }

    /**
     * Obtiene estadísticas de visibilidad
     */
    public VisibilidadStats obtenerEstadisticas() {
        List<Productos> todosProductos = productosRepository.findAll();
        List<Productos> visibles = obtenerProductosVisibles();
        List<Productos> agotados = obtenerProductosAgotados();
        List<Productos> descontinuados = obtenerProductosDescontinuados();

        return new VisibilidadStats(
                todosProductos.size(),
                visibles.size(),
                agotados.size(),
                descontinuados.size()
        );
    }

    /**
     * DTO para estadísticas de visibilidad
     */
    public record VisibilidadStats(
            int totalProductos,
            int productosVisibles,
            int productosAgotados,
            int productosDescontinuados
    ) {}
}

