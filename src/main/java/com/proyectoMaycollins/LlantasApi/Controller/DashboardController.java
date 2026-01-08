package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el Dashboard y Estadísticas
 * Proporciona endpoints para obtener métricas del negocio
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "📊 Dashboard", description = "Estadísticas y métricas del negocio en tiempo real")
@SecurityRequirement(name = "Bearer Authentication")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Obtiene todas las estadísticas del dashboard en un solo endpoint
     * GET /api/dashboard
     * Incluye: ventas del día/mes/año, productos más vendidos, alertas, resumen
     * @return Map con todas las estadísticas
     */
    @Operation(
            summary = "Dashboard completo",
            description = "Obtiene todas las estadísticas del negocio: ventas, productos más vendidos, stock bajo, clientes"
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerDashboardCompleto() {
        Map<String, Object> estadisticas = dashboardService.obtenerEstadisticasCompletas();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtiene las ventas del día actual
     * GET /api/dashboard/ventas/hoy
     * @return Total y cantidad de ventas del día
     */
    @Operation(summary = "Ventas del día", description = "Obtiene el total de ventas realizadas hoy")
    @GetMapping("/ventas/hoy")
    public ResponseEntity<Map<String, Object>> obtenerVentasDelDia() {
        Map<String, Object> ventas = dashboardService.obtenerVentasDelDia();
        return ResponseEntity.ok(ventas);
    }

    /**
     * Obtiene las ventas del mes actual
     * GET /api/dashboard/ventas/mes
     * @return Total y cantidad de ventas del mes
     */
    @Operation(summary = "Ventas del mes", description = "Obtiene el total de ventas del mes actual")
    @GetMapping("/ventas/mes")
    public ResponseEntity<Map<String, Object>> obtenerVentasDelMes() {
        Map<String, Object> ventas = dashboardService.obtenerVentasDelMes();
        return ResponseEntity.ok(ventas);
    }

    /**
     * Obtiene las ventas del año actual
     * GET /api/dashboard/ventas/anio
     * @return Total y cantidad de ventas del año
     */
    @Operation(summary = "Ventas del año", description = "Obtiene el total de ventas del año actual")
    @GetMapping("/ventas/anio")
    public ResponseEntity<Map<String, Object>> obtenerVentasDelAnio() {
        Map<String, Object> ventas = dashboardService.obtenerVentasDelAnio();
        return ResponseEntity.ok(ventas);
    }

    /**
     * Obtiene ventas en un rango de fechas personalizado
     * GET /api/dashboard/ventas/rango?inicio=2026-01-01T00:00:00&fin=2026-01-31T23:59:59
     * @param inicio Fecha de inicio (formato: yyyy-MM-dd'T'HH:mm:ss)
     * @param fin Fecha de fin
     * @return Estadísticas del periodo
     */
    @Operation(summary = "Ventas por rango de fechas", description = "Obtiene ventas en un rango de fechas personalizado")
    @GetMapping("/ventas/rango")
    public ResponseEntity<Map<String, Object>> obtenerVentasPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        Map<String, Object> ventas = dashboardService.obtenerVentasPorRango(inicio, fin);
        return ResponseEntity.ok(ventas);
    }

    /**
     * Obtiene los N productos más vendidos
     * GET /api/dashboard/productos-mas-vendidos?limite=10
     * @param limite Cantidad de productos a retornar (por defecto 10)
     * @return Lista de productos más vendidos
     */
    @Operation(summary = "Productos más vendidos", description = "Obtiene los N productos más vendidos")
    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<List<Map<String, Object>>> obtenerProductosMasVendidos(
            @RequestParam(defaultValue = "10") int limite) {
        List<Map<String, Object>> productos = dashboardService.obtenerProductosMasVendidos(limite);
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene productos con stock bajo (alertas)
     * GET /api/dashboard/alertas-stock?limite=10
     * @param limite Cantidad de alertas a mostrar (por defecto 10)
     * @return Lista de productos con stock bajo
     */
    @Operation(summary = "Alertas de stock", description = "Obtiene productos con stock bajo (alertas)")
    @GetMapping("/alertas-stock")
    public ResponseEntity<List<Map<String, Object>>> obtenerAlertasStock(
            @RequestParam(defaultValue = "10") int limite) {
        List<Map<String, Object>> alertas = dashboardService.obtenerProductosStockBajo(limite);
        return ResponseEntity.ok(alertas);
    }

    /**
     * Obtiene un resumen general del negocio
     * GET /api/dashboard/resumen
     * Incluye: ventas del día, total productos, alertas, clientes
     * @return Map con el resumen general
     */
    @Operation(summary = "Resumen general", description = "Obtiene un resumen general del negocio")
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumenGeneral() {
        Map<String, Object> resumen = dashboardService.obtenerResumenGeneral();
        return ResponseEntity.ok(resumen);
    }

    /**
     * Compara ventas del mes actual vs mes anterior
     * GET /api/dashboard/comparativa-mensual
     * @return Comparativa con diferencias y porcentajes
     */
    @Operation(summary = "Comparativa mensual", description = "Compara ventas del mes actual vs mes anterior")
    @GetMapping("/comparativa-mensual")
    public ResponseEntity<Map<String, Object>> compararMeses() {
        Map<String, Object> comparativa = dashboardService.compararMesActualVsAnterior();
        return ResponseEntity.ok(comparativa);
    }
}

