package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.*;
import com.proyectoMaycollins.LlantasApi.Model.enums.EstadoVenta;
import com.proyectoMaycollins.LlantasApi.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar ventas con funcionalidades completas:
 * - Gestión automática de stock en INVENTARIO
 * - Validación de disponibilidad en INVENTARIO
 * - Aplicación de promociones
 * - Cálculo de totales y descuentos
 */
@Service
public class VentasService {

    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private DetalleVentasRepository detalleVentasRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private PromocionesRepository promocionesRepository;

    @Autowired
    private NotificacionesService notificacionesService;

    @Autowired
    private InventarioRepository inventarioRepository;

    /**
     * Obtiene todas las ventas del sistema
     * @return Lista de ventas
     */
    public List<Ventas> list() {
        return ventasRepository.findAll();
    }

    /**
     * Obtiene ventas con paginación
     * Útil para listados grandes con muchos registros
     * @param pageable Configuración de paginación (página, tamaño, orden)
     * @return Página de ventas
     */
    public Page<Ventas> listPaginado(Pageable pageable) {
        return ventasRepository.findAll(pageable);
    }

    /**
     * Obtiene una venta por su ID
     * @param id ID de la venta
     * @return Optional con la venta si existe
     */
    public Optional<Ventas> get(Long id) {
        return ventasRepository.findById(id);
    }

    /**
     * Obtiene todas las ventas de un cliente específico
     * @param clienteId ID del cliente
     * @return Lista de ventas del cliente
     */
    public List<Ventas> byCliente(Long clienteId) {
        return ventasRepository.findByClienteId(clienteId);
    }

    /**
     * Obtiene ventas filtradas por estado
     * @param estado Estado de la venta (PENDIENTE, RECOGER, VENDIDO)
     * @return Lista de ventas con ese estado
     */
    public List<Ventas> byEstado(EstadoVenta estado) {
        return ventasRepository.findByEstado(estado);
    }

    /**
     * Busca ventas en un rango de fechas
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Lista de ventas en el rango
     */
    public List<Ventas> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventasRepository.findByFechaVentaBetween(inicio, fin);
    }

    /**
     * Obtiene todos los detalles de una venta
     * @param ventaId ID de la venta
     * @return Lista de detalles de la venta
     */
    public List<DetalleVentas> detalles(Long ventaId) {
        return detalleVentasRepository.findByVentaId(ventaId);
    }

    /**
     * Crea una nueva venta SIN actualizar stock ni aplicar promociones
     * Método simple para crear la cabecera de la venta
     * @param venta Datos de la venta
     * @return Venta creada
     */
    @Transactional
    public Ventas save(Ventas venta) {
        // Establecer fecha de venta si no viene
        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(LocalDateTime.now());
        }

        // Establecer estado inicial si no viene
        if (venta.getEstado() == null) {
            venta.setEstado(EstadoVenta.PENDIENTE);
        }

        return ventasRepository.save(venta);
    }

    /**
     * Crea una venta completa con detalles, validaciones y actualizaciones automáticas
     * FUNCIONALIDADES IMPLEMENTADAS:
     * 1. Valida stock disponible de cada producto
     * 2. Aplica promociones activas automáticamente
     * 3. Calcula descuentos según tipo de promoción
     * 4. Actualiza el stock de productos
     * 5. Crea notificaciones si el stock queda bajo
     * 6. Calcula el total final de la venta
     *
     * @param venta Cabecera de la venta (cliente, usuario, etc.)
     * @param detalles Lista de productos vendidos
     * @return Venta creada con todos los cálculos aplicados
     * @throws RuntimeException si no hay stock suficiente
     */
    @Transactional
    public Ventas crearVentaCompleta(Ventas venta, List<DetalleVentas> detalles) {
        // PASO 1: VALIDAR STOCK DISPONIBLE ANTES DE CONFIRMAR LA VENTA
        validarStockDisponible(detalles);

        // PASO 2: CREAR LA VENTA (CABECERA)
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado(EstadoVenta.PENDIENTE);
        venta.setDescuentoAplicado(0.0);
        Ventas ventaCreada = ventasRepository.save(venta);

        // PASO 3: PROCESAR CADA DETALLE DE LA VENTA
        double subtotalVenta = 0.0;

        for (DetalleVentas detalle : detalles) {
            // Establecer la venta en el detalle
            detalle.setVenta(ventaCreada);

            // Obtener el producto
            Productos producto = productosRepository.findById(detalle.getProducto().getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado con ID: " + detalle.getProducto().getProductoId()));

            // Establecer el precio unitario desde el producto
            detalle.setPrecioUnitario(producto.getPrecioVenta() != null
                    ? producto.getPrecioVenta().doubleValue()
                    : 0.0);

            // Calcular el subtotal del detalle
            double subtotal = detalle.getPrecioUnitario() * detalle.getCantidad();
            detalle.setSubtotal(subtotal);

            // Sumar al subtotal de la venta
            subtotalVenta += subtotal;

            // Guardar el detalle
            detalleVentasRepository.save(detalle);

            // PASO 4: ACTUALIZAR EL STOCK DEL PRODUCTO (RESTAR)
            actualizarStockPorVenta(producto.getProductoId(), detalle.getCantidad());
        }

        // PASO 5: APLICAR PROMOCIONES ACTIVAS AUTOMÁTICAMENTE
        double descuentoTotal = aplicarPromociones(ventaCreada, subtotalVenta);

        // PASO 6: CALCULAR EL TOTAL FINAL
        double totalFinal = subtotalVenta - descuentoTotal;
        ventaCreada.setTotal(totalFinal);
        ventaCreada.setDescuentoAplicado(descuentoTotal);

        // PASO 7: GUARDAR LA VENTA ACTUALIZADA
        ventaCreada = ventasRepository.save(ventaCreada);

        // PASO 8: CREAR NOTIFICACIÓN DE VENTA
        if (venta.getUsuario() != null && venta.getUsuario().getId() != null) {
            notificacionesService.crearNotificacionVenta(
                    venta.getUsuario().getId(),
                    totalFinal,
                    venta.getNumeroComprobante()
            );
        }

        return ventaCreada;
    }

    /**
     * VALIDACIÓN DE STOCK DISPONIBLE EN INVENTARIO
     * Verifica que todos los productos tengan stock suficiente en inventario
     * antes de confirmar la venta
     *
     * @param detalles Lista de detalles de la venta
     * @throws RuntimeException si algún producto no tiene stock suficiente
     */
    private void validarStockDisponible(List<DetalleVentas> detalles) {
        for (DetalleVentas detalle : detalles) {
            // Obtener el producto
            Productos producto = productosRepository.findById(detalle.getProducto().getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado con ID: " + detalle.getProducto().getProductoId()));

            // Obtener stock total del producto en todos los almacenes
            Integer stockTotalDisponible = inventarioRepository.calcularStockTotalPorProducto(producto.getProductoId());
            if (stockTotalDisponible == null) {
                stockTotalDisponible = 0;
            }

            // Validar que hay suficiente stock
            if (stockTotalDisponible < detalle.getCantidad()) {
                throw new RuntimeException(
                        String.format(
                                "Stock insuficiente para el producto con código '%s'. " +
                                "Stock disponible: %d, Cantidad solicitada: %d",
                                producto.getCodigoProducto(),
                                stockTotalDisponible,
                                detalle.getCantidad()
                        )
                );
            }
        }
    }

    /**
     * ACTUALIZACIÓN AUTOMÁTICA DE STOCK EN INVENTARIO POR VENTA
     * Reduce el stock del producto en la tabla INVENTARIO cuando se realiza una venta
     * Si el stock queda bajo (menos de 10), genera una notificación automática
     *
     * Estrategia: Descuenta primero del almacén con más stock disponible
     *
     * @param productoId ID del producto
     * @param cantidadVendida Cantidad vendida a restar del stock
     */
    private void actualizarStockPorVenta(Long productoId, Integer cantidadVendida) {
        // Obtener el producto para validar que existe
        Productos producto = productosRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Obtener registros de inventario para este producto, ordenados por cantidad descendente
        List<Inventario> inventarios = inventarioRepository.findByProductoIdOrderByCantidadDesc(productoId);

        if (inventarios.isEmpty()) {
            throw new RuntimeException(
                    "No hay registros de inventario para el producto con código: " + producto.getCodigoProducto()
            );
        }

        // Descontar del inventario (estrategia: del almacén con más stock primero)
        Integer cantidadRestante = cantidadVendida;

        for (Inventario inventario : inventarios) {
            if (cantidadRestante <= 0) {
                break; // Ya se descontó todo
            }

            Integer stockDisponible = inventario.getCantidad() != null ? inventario.getCantidad() : 0;

            if (stockDisponible > 0) {
                // Calcular cuánto se puede descontar de este almacén
                Integer aDescontar = Math.min(stockDisponible, cantidadRestante);

                // Actualizar el stock en este almacén
                inventario.setCantidad(stockDisponible - aDescontar);
                inventario.setFechaActualizacion(OffsetDateTime.now());
                inventarioRepository.save(inventario);

                // Reducir la cantidad restante
                cantidadRestante -= aDescontar;
            }
        }

        // Si aún queda cantidad por descontar, hay un error de validación
        if (cantidadRestante > 0) {
            throw new RuntimeException(
                    String.format(
                            "Error al actualizar inventario. Faltaron %d unidades por descontar. " +
                            "Esto no debería ocurrir si la validación funcionó correctamente.",
                            cantidadRestante
                    )
            );
        }

        // ALERTA: Calcular el stock total restante y crear notificación si está bajo
        Integer stockTotal = inventarioRepository.calcularStockTotalPorProducto(productoId);
        if (stockTotal != null && stockTotal < 10) {
            // Buscar al administrador (usuarioId = 1) o al usuario actual
            Long usuarioId = 1L; // Por defecto al admin

            notificacionesService.crearNotificacionStockBajo(
                    usuarioId,
                    producto.getCodigoProducto() + " - " + producto.getDescripcion(),
                    stockTotal
            );
        }
    }

    /**
     * APLICACIÓN AUTOMÁTICA DE PROMOCIONES
     * Busca promociones activas y las aplica según el tipo:
     *
     * TIPOS DE PROMOCIÓN:
     * - PORCENTAJE: Descuento porcentual sobre el total (ej: 10%)
     * - MONTO_FIJO: Descuento de un monto fijo (ej: S/ 50)
     * - DOS_POR_UNO: 50% de descuento
     * - DESCUENTO_CANTIDAD: Si compra X cantidad, aplica descuento
     * - DESCUENTO_TEMPORADA: Descuento por temporada
     *
     * @param venta Venta a la que aplicar las promociones
     * @param subtotal Subtotal de la venta antes de descuentos
     * @return Monto total de descuento aplicado
     */
    private double aplicarPromociones(Ventas venta, double subtotal) {
        List<Promociones> promocionesActivas = promocionesRepository.findByActivaTrue();

        double descuentoTotal = 0.0;
        LocalDateTime ahora = LocalDateTime.now();

        for (Promociones promocion : promocionesActivas) {
            boolean enPeriodo = true;

            if (promocion.getFechaInicio() != null && promocion.getFechaFin() != null) {
                LocalDateTime inicio = promocion.getFechaInicio().atStartOfDay();
                LocalDateTime fin = promocion.getFechaFin().atTime(23, 59, 59);
                enPeriodo = ahora.isAfter(inicio) && ahora.isBefore(fin);
            }

            if (!enPeriodo) {
                continue;
            }

            double descuento = 0.0;
            String tipo = promocion.getTipoDescuento() != null ? promocion.getTipoDescuento().toUpperCase() : "";
            double valor = promocion.getValorDescuento() != null ? promocion.getValorDescuento().doubleValue() : 0.0;

            switch (tipo) {
                case "PORCENTAJE":
                    descuento = subtotal * (valor / 100.0);
                    break;
                case "MONTO_FIJO":
                    descuento = valor;
                    break;
                case "DOS_POR_UNO":
                    descuento = subtotal * 0.5;
                    break;
                case "DESCUENTO_CANTIDAD":
                case "DESCUENTO_TEMPORADA":
                    descuento = subtotal * (valor / 100.0);
                    break;
                default:
                    descuento = 0.0;
            }

            descuentoTotal += descuento;
        }

        if (descuentoTotal > subtotal) {
            descuentoTotal = subtotal;
        }

        return descuentoTotal;
    }

    /**
     * Agrega un detalle a una venta existente
     * Actualiza el stock y recalcula el total
     * @param detalle Detalle a agregar
     * @return Detalle guardado
     */
    @Transactional
    public DetalleVentas addDetalle(DetalleVentas detalle) {
        // Validar stock antes de agregar
        List<DetalleVentas> detalles = List.of(detalle);
        validarStockDisponible(detalles);

        // Guardar el detalle
        DetalleVentas detalleGuardado = detalleVentasRepository.save(detalle);

        // Actualizar stock
        actualizarStockPorVenta(detalle.getProducto().getProductoId(), detalle.getCantidad());

        // Recalcular total de la venta
        Ventas venta = ventasRepository.findById(detalle.getVenta().getId())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        double nuevoTotal = venta.getTotal() + detalle.getSubtotal();
        venta.setTotal(nuevoTotal);
        ventasRepository.save(venta);

        return detalleGuardado;
    }

    /**
     * Actualiza el estado de una venta
     * @param id ID de la venta
     * @param nuevoEstado Nuevo estado
     * @return Venta actualizada
     */
    @Transactional
    public Ventas actualizarEstado(Long id, EstadoVenta nuevoEstado) {
        Ventas venta = ventasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        venta.setEstado(nuevoEstado);
        return ventasRepository.save(venta);
    }

    /**
     * Elimina una venta
     * IMPORTANTE: NO revierte el stock
     * @param id ID de la venta a eliminar
     */
    @Transactional
    public void eliminar(Long id) {
        if (!ventasRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        ventasRepository.deleteById(id);
    }
}
