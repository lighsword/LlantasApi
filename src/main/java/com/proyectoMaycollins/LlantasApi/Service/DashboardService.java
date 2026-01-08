package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Productos;
import com.proyectoMaycollins.LlantasApi.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para obtener estadísticas y datos del Dashboard
 * Proporciona métricas clave del negocio en tiempo real
 */
@Service
public class DashboardService {

    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private DetalleVentasRepository detalleVentasRepository;

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    /**
     * Obtiene todas las estadísticas del dashboard en un solo objeto
     * Incluye: ventas, productos, clientes, alertas
     * @return Map con todas las estadísticas
     */
    public Map<String, Object> obtenerEstadisticasCompletas() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Estadísticas de ventas
        estadisticas.put("ventasHoy", obtenerVentasDelDia());
        estadisticas.put("ventasMes", obtenerVentasDelMes());
        estadisticas.put("ventasAnio", obtenerVentasDelAnio());

        // Productos
        estadisticas.put("productosMasVendidos", obtenerProductosMasVendidos(10));
        estadisticas.put("alertasStockBajo", obtenerProductosStockBajo(10));
        estadisticas.put("totalProductos", productosRepository.count());

        // Clientes
        estadisticas.put("totalClientes", clientesRepository.count());
        estadisticas.put("clientesActivos", clientesRepository.findByActivoTrue().size());

        // Resumen general
        estadisticas.put("resumen", obtenerResumenGeneral());

        return estadisticas;
    }

    /**
     * VENTAS DEL DÍA
     * Calcula el total de ventas realizadas en el día actual
     * @return Map con total y cantidad de ventas del día
     */
    public Map<String, Object> obtenerVentasDelDia() {
        // Obtener inicio y fin del día actual
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = LocalDate.now().atTime(LocalTime.MAX);

        // Buscar ventas del día
        List<com.proyectoMaycollins.LlantasApi.Model.Ventas> ventasDelDia =
                ventasRepository.findByFechaVentaBetween(inicioDia, finDia);

        // Calcular totales
        double totalVentas = ventasDelDia.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        int cantidadVentas = ventasDelDia.size();

        // Armar respuesta
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("total", totalVentas);
        resultado.put("cantidad", cantidadVentas);
        resultado.put("fecha", LocalDate.now().toString());
        resultado.put("ticketPromedio", cantidadVentas > 0 ? totalVentas / cantidadVentas : 0.0);

        return resultado;
    }

    /**
     * VENTAS DEL MES
     * Calcula el total de ventas realizadas en el mes actual
     * @return Map con total y cantidad de ventas del mes
     */
    public Map<String, Object> obtenerVentasDelMes() {
        // Obtener inicio y fin del mes actual
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioMes = hoy.withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMes = hoy.withDayOfMonth(hoy.lengthOfMonth()).atTime(LocalTime.MAX);

        // Buscar ventas del mes
        List<com.proyectoMaycollins.LlantasApi.Model.Ventas> ventasDelMes =
                ventasRepository.findByFechaVentaBetween(inicioMes, finMes);

        // Calcular totales
        double totalVentas = ventasDelMes.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        int cantidadVentas = ventasDelMes.size();

        // Armar respuesta
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("total", totalVentas);
        resultado.put("cantidad", cantidadVentas);
        resultado.put("mes", hoy.getMonth().toString());
        resultado.put("anio", hoy.getYear());
        resultado.put("ticketPromedio", cantidadVentas > 0 ? totalVentas / cantidadVentas : 0.0);

        return resultado;
    }

    /**
     * VENTAS DEL AÑO
     * Calcula el total de ventas realizadas en el año actual
     * @return Map con total y cantidad de ventas del año
     */
    public Map<String, Object> obtenerVentasDelAnio() {
        // Obtener inicio y fin del año actual
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioAnio = LocalDate.of(hoy.getYear(), 1, 1).atStartOfDay();
        LocalDateTime finAnio = LocalDate.of(hoy.getYear(), 12, 31).atTime(LocalTime.MAX);

        // Buscar ventas del año
        List<com.proyectoMaycollins.LlantasApi.Model.Ventas> ventasDelAnio =
                ventasRepository.findByFechaVentaBetween(inicioAnio, finAnio);

        // Calcular totales
        double totalVentas = ventasDelAnio.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        int cantidadVentas = ventasDelAnio.size();

        // Armar respuesta
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("total", totalVentas);
        resultado.put("cantidad", cantidadVentas);
        resultado.put("anio", hoy.getYear());
        resultado.put("promedioMensual", totalVentas / 12);
        resultado.put("ticketPromedio", cantidadVentas > 0 ? totalVentas / cantidadVentas : 0.0);

        return resultado;
    }

    /**
     * PRODUCTOS MÁS VENDIDOS
     * Obtiene los N productos más vendidos ordenados por cantidad
     * Utiliza los datos de detalle_ventas
     * Obtiene el stock desde la tabla INVENTARIO
     * @param limite Cantidad de productos a retornar
     * @return Lista de productos más vendidos con su información
     */
    public List<Map<String, Object>> obtenerProductosMasVendidos(int limite) {
        // Obtener productos más vendidos desde el repository
        List<Object[]> resultados = detalleVentasRepository.findProductosMasVendidos();

        // Transformar y limitar resultados
        return resultados.stream()
                .limit(limite)
                .map(resultado -> {
                    // resultado[0] = Producto, resultado[1] = totalVendido
                    com.proyectoMaycollins.LlantasApi.Model.Productos producto =
                            (com.proyectoMaycollins.LlantasApi.Model.Productos) resultado[0];
                    Long totalVendido = (Long) resultado[1];

                    // Obtener stock total desde inventario
                    Integer stockTotal = inventarioRepository.calcularStockTotalPorProducto(producto.getProductoId());

                    Map<String, Object> item = new HashMap<>();
                    item.put("id", producto.getProductoId());
                    item.put("descripcion", producto.getDescripcion()); // Usar descripcion en lugar de nombre
                    item.put("codigo", producto.getCodigoProducto());
                    item.put("cantidadVendida", totalVendido);
                    item.put("stockActual", stockTotal != null ? stockTotal : 0);
                    item.put("precioVenta", producto.getPrecioVenta());

                    return item;
                })
                .collect(Collectors.toList());
    }

    /**
     * ALERTAS DE STOCK BAJO
     * Obtiene productos con stock total menor al mínimo (por defecto 10)
     * Consulta el stock desde la tabla INVENTARIO sumando todos los almacenes
     * Útil para generar alertas en el dashboard
     * @param limite Cantidad de productos a mostrar
     * @return Lista de productos con stock bajo
     */
    public List<Map<String, Object>> obtenerProductosStockBajo(int limite) {
        int stockMinimo = 10; // Umbral de stock bajo

        // Obtener productos con stock bajo desde inventario
        List<Object[]> productosStockBajo = inventarioRepository.findProductosConStockBajo(stockMinimo);

        // Transformar y limitar resultados
        return productosStockBajo.stream()
                .limit(limite)
                .map(resultado -> {
                    // resultado[0] = productosId, resultado[1] = stockTotal
                    Long productosId = ((Number) resultado[0]).longValue();
                    Integer stockTotal = ((Number) resultado[1]).intValue();

                    // Obtener información del producto
                    Productos producto = productosRepository.findById(productosId).orElse(null);

                    if (producto == null) {
                        return null; // Saltar si el producto no existe
                    }

                    Map<String, Object> item = new HashMap<>();
                    item.put("id", producto.getProductoId());
                    item.put("descripcion", producto.getDescripcion()); // Usar descripcion
                    item.put("codigo", producto.getCodigoProducto());
                    item.put("stockActual", stockTotal);
                    item.put("precioVenta", producto.getPrecioVenta());

                    // Nivel de criticidad según el stock
                    String criticidad;
                    if (stockTotal == 0) {
                        criticidad = "CRÍTICO - SIN STOCK";
                    } else if (stockTotal < 5) {
                        criticidad = "URGENTE";
                    } else {
                        criticidad = "BAJO";
                    }
                    item.put("criticidad", criticidad);

                    return item;
                })
                .filter(item -> item != null) // Filtrar nulls
                .collect(Collectors.toList());
    }

    /**
     * RESUMEN GENERAL DEL NEGOCIO
     * Proporciona un resumen rápido con las métricas más importantes
     * @return Map con el resumen general
     */
    public Map<String, Object> obtenerResumenGeneral() {
        Map<String, Object> resumen = new HashMap<>();

        // Ventas totales del día
        Map<String, Object> ventasHoy = obtenerVentasDelDia();
        resumen.put("ventasHoy", ventasHoy.get("total"));
        resumen.put("cantidadVentasHoy", ventasHoy.get("cantidad"));

        // Total de productos
        long totalProductos = productosRepository.count();
        long productosActivos = productosRepository.findByActivoTrue().size();
        resumen.put("totalProductos", totalProductos);
        resumen.put("productosActivos", productosActivos);

        // Alertas de stock
        int productosStockBajo = inventarioRepository.findProductosConStockBajo(10).size();
        resumen.put("alertasStockBajo", productosStockBajo);

        // Clientes
        long totalClientes = clientesRepository.count();
        long clientesActivos = clientesRepository.findByActivoTrue().size();
        resumen.put("totalClientes", totalClientes);
        resumen.put("clientesActivos", clientesActivos);

        // Última actualización
        resumen.put("ultimaActualizacion", LocalDateTime.now().toString());

        return resumen;
    }

    /**
     * VENTAS POR RANGO DE FECHAS
     * Obtiene estadísticas de ventas en un rango de fechas personalizado
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Map con estadísticas del periodo
     */
    public Map<String, Object> obtenerVentasPorRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // Buscar ventas en el rango
        List<com.proyectoMaycollins.LlantasApi.Model.Ventas> ventas =
                ventasRepository.findByFechaVentaBetween(fechaInicio, fechaFin);

        // Calcular totales
        double totalVentas = ventas.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        int cantidadVentas = ventas.size();

        // Armar respuesta
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("total", totalVentas);
        resultado.put("cantidad", cantidadVentas);
        resultado.put("fechaInicio", fechaInicio.toString());
        resultado.put("fechaFin", fechaFin.toString());
        resultado.put("ticketPromedio", cantidadVentas > 0 ? totalVentas / cantidadVentas : 0.0);
        resultado.put("ventas", ventas);

        return resultado;
    }

    /**
     * COMPARATIVA MES ACTUAL VS MES ANTERIOR
     * Compara las ventas del mes actual con el mes anterior
     * @return Map con la comparativa
     */
    public Map<String, Object> compararMesActualVsAnterior() {
        LocalDate hoy = LocalDate.now();

        // Mes actual
        LocalDateTime inicioMesActual = hoy.withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMesActual = hoy.withDayOfMonth(hoy.lengthOfMonth()).atTime(LocalTime.MAX);

        // Mes anterior
        LocalDate mesAnterior = hoy.minusMonths(1);
        LocalDateTime inicioMesAnterior = mesAnterior.withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMesAnterior = mesAnterior.withDayOfMonth(mesAnterior.lengthOfMonth()).atTime(LocalTime.MAX);

        // Obtener ventas de ambos meses
        double ventasMesActual = ventasRepository.findByFechaVentaBetween(inicioMesActual, finMesActual).stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        double ventasMesAnterior = ventasRepository.findByFechaVentaBetween(inicioMesAnterior, finMesAnterior).stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        // Calcular diferencia y porcentaje
        double diferencia = ventasMesActual - ventasMesAnterior;
        double porcentajeCambio = ventasMesAnterior > 0
                ? (diferencia / ventasMesAnterior) * 100
                : 0.0;

        // Armar respuesta
        Map<String, Object> comparativa = new HashMap<>();
        comparativa.put("mesActual", ventasMesActual);
        comparativa.put("mesAnterior", ventasMesAnterior);
        comparativa.put("diferencia", diferencia);
        comparativa.put("porcentajeCambio", porcentajeCambio);
        comparativa.put("tendencia", diferencia >= 0 ? "POSITIVA" : "NEGATIVA");

        return comparativa;
    }
}

