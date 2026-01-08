package com.proyectoMaycollins.LlantasApi.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoMaycollins.LlantasApi.Model.Reportes;
import com.proyectoMaycollins.LlantasApi.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio para gestionar la lógica de negocio de Reportes
 * Genera reportes de ventas, inventario, productos más vendidos, etc.
 */
@Service
public class ReportesService {

    @Autowired
    private ReportesRepository reportesRepository;

    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private DetalleVentasRepository detalleVentasRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene todos los reportes generados
     * @return Lista de todos los reportes
     */
    public List<Reportes> obtenerTodos() {
        return reportesRepository.findAll();
    }

    /**
     * Busca un reporte por su ID
     * @param id ID del reporte
     * @return Optional con el reporte si existe
     */
    public Optional<Reportes> obtenerPorId(Integer id) {
        return reportesRepository.findById(id);
    }

    /**
     * Obtiene reportes por tipo
     * @param tipoReporte Tipo de reporte (ventas, inventario, etc.)
     * @return Lista de reportes de ese tipo
     */
    public List<Reportes> obtenerPorTipo(String tipoReporte) {
        return reportesRepository.findByTipoReporte(tipoReporte);
    }

    /**
     * Obtiene los últimos 10 reportes generados
     * @return Lista de reportes recientes
     */
    public List<Reportes> obtenerRecientes() {
        return reportesRepository.findTop10ByOrderByFechaGeneracionDesc();
    }

    /**
     * Genera un reporte de ventas en un periodo específico
     * Incluye: total de ventas, cantidad de ventas, ticket promedio
     * @param fechaInicio Fecha de inicio del periodo
     * @param fechaFin Fecha de fin del periodo
     * @return Reporte generado
     */
    @Transactional
    public Reportes generarReporteVentasPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        // Convertir fechas a LocalDateTime
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        // Obtener datos de ventas del periodo
        var ventas = ventasRepository.findByFechaVentaBetween(inicio, fin);

        // Calcular estadísticas
        double totalVentas = ventas.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        int cantidadVentas = ventas.size();
        double ticketPromedio = cantidadVentas > 0 ? totalVentas / cantidadVentas : 0.0;

        // Crear objeto de resumen
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("total_ventas", totalVentas);
        resumen.put("cantidad_ventas", cantidadVentas);
        resumen.put("ticket_promedio", ticketPromedio);
        resumen.put("fecha_inicio", fechaInicio.toString());
        resumen.put("fecha_fin", fechaFin.toString());

        // Crear objeto de detalle
        Map<String, Object> detalle = new HashMap<>();
        detalle.put("ventas", ventas);

        // Crear el reporte
        Reportes reporte = new Reportes();
        reporte.setNombreReporte("Reporte de Ventas - " + fechaInicio + " al " + fechaFin);
        reporte.setTipoReporte("VENTAS_PERIODO");
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setTituloImpreso("REPORTE DE VENTAS DEL PERIODO");

        try {
            reporte.setResumen(objectMapper.writeValueAsString(resumen));
            reporte.setDetalle(objectMapper.writeValueAsString(detalle));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir datos a JSON", e);
        }

        // Guardar el reporte
        return reportesRepository.save(reporte);
    }

    /**
     * Genera un reporte de los productos más vendidos
     * Lista los productos ordenados por cantidad vendida
     * @param limite Cantidad de productos a incluir en el reporte
     * @return Reporte generado
     */
    @Transactional
    public Reportes generarReporteProductosMasVendidos(int limite) {
        // Obtener los productos más vendidos
        var productosMasVendidos = detalleVentasRepository.findProductosMasVendidos();

        // Limitar la cantidad de resultados
        var productosLimitados = productosMasVendidos.stream()
                .limit(limite)
                .toList();

        // Crear resumen
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("total_productos", productosLimitados.size());
        resumen.put("limite", limite);

        // Crear detalle
        Map<String, Object> detalle = new HashMap<>();
        detalle.put("productos", productosLimitados);

        // Crear el reporte
        Reportes reporte = new Reportes();
        reporte.setNombreReporte("Top " + limite + " Productos Más Vendidos");
        reporte.setTipoReporte("PRODUCTOS_MAS_VENDIDOS");
        reporte.setFechaPuntual(LocalDateTime.now());
        reporte.setTituloImpreso("PRODUCTOS MÁS VENDIDOS");

        try {
            reporte.setResumen(objectMapper.writeValueAsString(resumen));
            reporte.setDetalle(objectMapper.writeValueAsString(detalle));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir datos a JSON", e);
        }

        return reportesRepository.save(reporte);
    }

    /**
     * Genera un reporte de inventario con stock bajo
     * Lista productos que tienen stock por debajo del mínimo
     * @param stockMinimo Cantidad mínima de stock considerada como "baja"
     * @return Reporte generado
     */
    @Transactional
    public Reportes generarReporteInventarioBajo(int stockMinimo) {
        // Obtener productos con stock bajo desde la tabla inventario
        List<Object[]> productosStockBajo = inventarioRepository.findProductosConStockBajo(stockMinimo);

        // Crear resumen
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("total_productos_stock_bajo", productosStockBajo.size());
        resumen.put("stock_minimo", stockMinimo);
        resumen.put("alerta", !productosStockBajo.isEmpty() ? "CRÍTICO" : "NORMAL");

        // Crear detalle con información de cada producto
        List<Map<String, Object>> detalleProductos = new ArrayList<>();
        for (Object[] resultado : productosStockBajo) {
            Long productosId = ((Number) resultado[0]).longValue();
            Integer stockTotal = ((Number) resultado[1]).intValue();

            // Obtener información del producto
            var productoOpt = productosRepository.findById(productosId);
            if (productoOpt.isPresent()) {
                var producto = productoOpt.get();
                Map<String, Object> infoProducto = new HashMap<>();
                infoProducto.put("id", producto.getProductoId());
                infoProducto.put("descripcion", producto.getDescripcion());
                infoProducto.put("stock_actual", stockTotal);
                infoProducto.put("codigo", producto.getCodigoProducto());
                infoProducto.put("precio_venta", producto.getPrecioVenta());
                detalleProductos.add(infoProducto);
            }
        }

        Map<String, Object> detalle = new HashMap<>();
        detalle.put("productos", detalleProductos);

        // Crear el reporte
        Reportes reporte = new Reportes();
        reporte.setNombreReporte("Reporte de Inventario Bajo - Stock < " + stockMinimo);
        reporte.setTipoReporte("INVENTARIO_BAJO");
        reporte.setFechaPuntual(LocalDateTime.now());
        reporte.setTituloImpreso("ALERTA: INVENTARIO CON STOCK BAJO");

        try {
            reporte.setResumen(objectMapper.writeValueAsString(resumen));
            reporte.setDetalle(objectMapper.writeValueAsString(detalle));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir datos a JSON", e);
        }

        return reportesRepository.save(reporte);
    }

    /**
     * Genera un reporte de los mejores clientes
     * Lista clientes ordenados por total de compras
     * @param limite Cantidad de clientes a incluir
     * @return Reporte generado
     */
    @Transactional
    public Reportes generarReporteClientesTop(int limite) {
        // Obtener los mejores clientes (ordenados por total de compras)
        var topClientes = clientesRepository.findTopClientesByTotalCompras();

        // Limitar la cantidad de resultados
        var clientesLimitados = topClientes.stream()
                .limit(limite)
                .toList();

        // Calcular totales
        int totalComprasAcumuladas = clientesLimitados.stream()
                .mapToInt(c -> c.getTotalCompras() != null ? c.getTotalCompras() : 0)
                .sum();

        // Crear resumen
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("total_clientes", clientesLimitados.size());
        resumen.put("compras_acumuladas", totalComprasAcumuladas);
        resumen.put("limite", limite);

        // Crear detalle
        List<Map<String, Object>> detalleClientes = new ArrayList<>();
        for (var cliente : clientesLimitados) {
            Map<String, Object> infoCliente = new HashMap<>();
            infoCliente.put("id", cliente.getId());
            infoCliente.put("nombre", cliente.getNombreCliente());
            infoCliente.put("total_compras", cliente.getTotalCompras());
            infoCliente.put("nivel", cliente.getNivelCliente());
            infoCliente.put("tipo", cliente.getTipoCliente());
            detalleClientes.add(infoCliente);
        }

        Map<String, Object> detalle = new HashMap<>();
        detalle.put("clientes", detalleClientes);

        // Crear el reporte
        Reportes reporte = new Reportes();
        reporte.setNombreReporte("Top " + limite + " Mejores Clientes");
        reporte.setTipoReporte("CLIENTES_TOP");
        reporte.setFechaPuntual(LocalDateTime.now());
        reporte.setTituloImpreso("MEJORES CLIENTES");

        try {
            reporte.setResumen(objectMapper.writeValueAsString(resumen));
            reporte.setDetalle(objectMapper.writeValueAsString(detalle));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir datos a JSON", e);
        }

        return reportesRepository.save(reporte);
    }

    /**
     * Elimina un reporte por su ID
     * @param id ID del reporte a eliminar
     * @throws RuntimeException si el reporte no existe
     */
    @Transactional
    public void eliminar(Integer id) {
        if (!reportesRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con ID: " + id);
        }
        reportesRepository.deleteById(id);
    }

    /**
     * Genera un reporte personalizado
     * Permite crear reportes con datos personalizados
     * @param reporte Datos del reporte
     * @return Reporte guardado
     */
    @Transactional
    public Reportes generarReportePersonalizado(Reportes reporte) {
        // Establecer fecha de generación si no viene
        if (reporte.getFechaPuntual() == null) {
            reporte.setFechaPuntual(LocalDateTime.now());
        }

        return reportesRepository.save(reporte);
    }
}

