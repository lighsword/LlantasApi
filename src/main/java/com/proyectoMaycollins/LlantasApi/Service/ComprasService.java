package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Compras;
import com.proyectoMaycollins.LlantasApi.Model.DetalleCompras;
import com.proyectoMaycollins.LlantasApi.Model.Inventario;
import com.proyectoMaycollins.LlantasApi.Model.Productos;
import com.proyectoMaycollins.LlantasApi.Repository.ComprasRepository;
import com.proyectoMaycollins.LlantasApi.Repository.DetalleComprasRepository;
import com.proyectoMaycollins.LlantasApi.Repository.InventarioRepository;
import com.proyectoMaycollins.LlantasApi.Repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar la lógica de negocio de Compras
 * Maneja la creación de compras, actualización de inventario y cálculos
 */
@Service
public class ComprasService {

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private DetalleComprasRepository detalleComprasRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    /**
     * Obtiene todas las compras registradas
     * @return Lista de todas las compras
     */
    public List<Compras> obtenerTodas() {
        return comprasRepository.findAll();
    }

    /**
     * Busca una compra por su ID
     * @param id ID de la compra
     * @return Optional con la compra si existe
     */
    public Optional<Compras> obtenerPorId(Integer id) {
        return comprasRepository.findById(id);
    }

    /**
     * Obtiene todas las compras de un proveedor específico
     * @param proveedorId ID del proveedor
     * @return Lista de compras del proveedor
     */
    public List<Compras> obtenerPorProveedor(Integer proveedorId) {
        return comprasRepository.findByProveedorId(proveedorId);
    }

    /**
     * Obtiene las últimas 10 compras registradas
     * @return Lista de compras recientes
     */
    public List<Compras> obtenerUltimasCompras() {
        return comprasRepository.findTop10ByOrderByFechaCompraDesc();
    }

    /**
     * Obtiene compras en un rango de fechas
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Lista de compras en el rango
     */
    public List<Compras> obtenerPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return comprasRepository.findByFechaCompraBetween(inicio, fin);
    }

    /**
     * Crea una nueva compra sin detalles
     * La fecha se establece automáticamente al momento actual
     * @param compra Datos de la compra
     * @return Compra creada
     */
    @Transactional
    public Compras crear(Compras compra) {
        // Establecer la fecha actual si no viene en el objeto
        if (compra.getFechaCompra() == null) {
            compra.setFechaCompra(LocalDateTime.now());
        }

        // Inicializar total en 0 si no viene
        if (compra.getTotal() == null) {
            compra.setTotal(BigDecimal.ZERO);
        }

        // Guardar la compra
        return comprasRepository.save(compra);
    }

    /**
     * Crea una compra completa con sus detalles
     * Actualiza automáticamente el inventario y el stock de productos
     * @param compra Cabecera de la compra
     * @param detalles Lista de productos comprados
     * @return Compra creada con sus detalles
     * @throws RuntimeException si algún producto no existe
     */
    @Transactional
    public Compras crearConDetalles(Compras compra, List<DetalleCompras> detalles) {
        // 1. Crear la compra (cabecera)
        Compras compraCreada = crear(compra);

        // 2. Calcular el total de la compra y procesar cada detalle
        BigDecimal totalCompra = BigDecimal.ZERO;

        for (DetalleCompras detalle : detalles) {
            // Establecer el ID de la compra en el detalle
            detalle.setCompraId(compraCreada.getId());

            // Calcular el subtotal del detalle
            BigDecimal subtotal = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setSubtotal(subtotal);

            // Sumar al total de la compra
            totalCompra = totalCompra.add(subtotal);

            // Guardar el detalle
            detalleComprasRepository.save(detalle);

            // 3. Actualizar el stock del producto
            actualizarStockProducto(detalle.getProductoId(), detalle.getCantidad());
        }

        // 4. Actualizar el total de la compra
        compraCreada.setTotal(totalCompra);
        return comprasRepository.save(compraCreada);
    }

    /**
     * Agrega un detalle a una compra existente
     * Actualiza el total de la compra y el stock del producto
     * @param compraId ID de la compra
     * @param detalle Detalle a agregar
     * @return Detalle creado
     * @throws RuntimeException si la compra no existe o el producto no existe
     */
    @Transactional
    public DetalleCompras agregarDetalle(Integer compraId, DetalleCompras detalle) {
        // Verificar que la compra existe
        Compras compra = comprasRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + compraId));

        // Establecer el ID de la compra
        detalle.setCompraId(compraId);

        // Calcular el subtotal
        BigDecimal subtotal = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));
        detalle.setSubtotal(subtotal);

        // Guardar el detalle
        DetalleCompras detalleCreado = detalleComprasRepository.save(detalle);

        // Actualizar el stock del producto
        actualizarStockProducto(detalle.getProductoId(), detalle.getCantidad());

        // Actualizar el total de la compra
        BigDecimal nuevoTotal = compra.getTotal().add(subtotal);
        compra.setTotal(nuevoTotal);
        comprasRepository.save(compra);

        return detalleCreado;
    }

    /**
     * Obtiene todos los detalles de una compra
     * @param compraId ID de la compra
     * @return Lista de detalles de la compra
     */
    public List<DetalleCompras> obtenerDetallesCompra(Integer compraId) {
        return detalleComprasRepository.findByCompraId(compraId);
    }

    /**
     * Actualiza una compra existente
     * Solo actualiza los campos de la cabecera, no los detalles
     * @param id ID de la compra
     * @param compraActualizada Datos actualizados
     * @return Compra actualizada
     * @throws RuntimeException si la compra no existe
     */
    @Transactional
    public Compras actualizar(Integer id, Compras compraActualizada) {
        // Verificar que la compra existe
        Compras compraExistente = comprasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));

        // Actualizar campos (solo los que no son detalles)
        if (compraActualizada.getProveedor() != null) {
            compraExistente.setProveedor(compraActualizada.getProveedor());
        }
        if (compraActualizada.getUsuario() != null) {
            compraExistente.setUsuario(compraActualizada.getUsuario());
        }
        if (compraActualizada.getFechaCompra() != null) {
            compraExistente.setFechaCompra(compraActualizada.getFechaCompra());
        }
        // Nota: El total se recalcula desde los detalles, no se actualiza directamente

        return comprasRepository.save(compraExistente);
    }

    /**
     * Elimina una compra y todos sus detalles
     * IMPORTANTE: Esta operación NO revierte el inventario
     * @param id ID de la compra a eliminar
     * @throws RuntimeException si la compra no existe
     */
    @Transactional
    public void eliminar(Integer id) {
        // Verificar que la compra existe
        if (!comprasRepository.existsById(id)) {
            throw new RuntimeException("Compra no encontrada con ID: " + id);
        }

        // Eliminar primero los detalles
        detalleComprasRepository.deleteByCompraId(id);

        // Eliminar la compra
        comprasRepository.deleteById(id);
    }

    /**
     * Calcula el total de compras en un periodo
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Total de compras en el periodo
     */
    public Double calcularTotalPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        Double total = comprasRepository.calcularTotalComprasPeriodo(inicio, fin);
        return total != null ? total : 0.0;
    }

    /**
     * Método privado para actualizar el inventario de un producto después de una compra
     * Incrementa el stock del producto en la tabla INVENTARIO
     * Si el producto no tiene registro en inventario, crea uno nuevo en el almacén principal (ID=1)
     *
     * @param productoId ID del producto
     * @param cantidad Cantidad comprada a agregar al inventario
     * @throws RuntimeException si el producto no existe
     */
    private void actualizarStockProducto(Long productoId, Integer cantidad) {
        // Buscar el producto para validar que existe
        Productos producto = productosRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        // Buscar si ya existe inventario para este producto
        List<Inventario> inventarios = inventarioRepository.findByProductoIdOrderByCantidadDesc(productoId);

        if (inventarios.isEmpty()) {
            // Si no existe inventario, crear uno nuevo en el almacén principal (ID=1)
            Inventario nuevoInventario = new Inventario();
            nuevoInventario.setProductoId(productoId.intValue());
            nuevoInventario.setAlmacenId(1); // Almacén principal por defecto
            nuevoInventario.setCantidad(cantidad);
            nuevoInventario.setFechaActualizacion(java.time.OffsetDateTime.now());

            inventarioRepository.save(nuevoInventario);
        } else {
            // Si ya existe, actualizar el primer almacén (el que tiene más stock)
            Inventario inventario = inventarios.get(0);
            Integer cantidadActual = inventario.getCantidad() != null ? inventario.getCantidad() : 0;
            inventario.setCantidad(cantidadActual + cantidad);
            inventario.setFechaActualizacion(java.time.OffsetDateTime.now());

            inventarioRepository.save(inventario);
        }
    }

    /**
     * Recalcula el total de una compra desde sus detalles
     * Útil cuando se agregan o eliminan detalles
     * @param compraId ID de la compra
     * @return Compra con el total actualizado
     */
    @Transactional
    public Compras recalcularTotal(Integer compraId) {
        Compras compra = comprasRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + compraId));

        // Calcular el total desde la base de datos
        Double total = detalleComprasRepository.calcularTotalCompra(compraId);
        compra.setTotal(BigDecimal.valueOf(total != null ? total : 0.0));

        return comprasRepository.save(compra);
    }
}

