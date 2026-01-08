package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Reportes;
import com.proyectoMaycollins.LlantasApi.Service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar Reportes
 * Expone endpoints para generar reportes de ventas, inventario, productos, clientes, etc.
 */
@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReportesController {

    @Autowired
    private ReportesService reportesService;

    /**
     * Obtiene todos los reportes generados
     * GET /api/reportes
     * @return Lista de reportes
     */
    @GetMapping
    public ResponseEntity<List<Reportes>> obtenerTodos() {
        List<Reportes> reportes = reportesService.obtenerTodos();
        return ResponseEntity.ok(reportes);
    }

    /**
     * Obtiene un reporte por su ID
     * GET /api/reportes/{id}
     * @param id ID del reporte
     * @return Reporte encontrado o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reportes> obtenerPorId(@PathVariable Integer id) {
        return reportesService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene reportes por tipo
     * GET /api/reportes/tipo/{tipo}
     * @param tipo Tipo de reporte (VENTAS_PERIODO, PRODUCTOS_MAS_VENDIDOS, etc.)
     * @return Lista de reportes de ese tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reportes>> obtenerPorTipo(@PathVariable String tipo) {
        List<Reportes> reportes = reportesService.obtenerPorTipo(tipo);
        return ResponseEntity.ok(reportes);
    }

    /**
     * Obtiene los últimos 10 reportes generados
     * GET /api/reportes/recientes
     * @return Lista de reportes recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<List<Reportes>> obtenerRecientes() {
        List<Reportes> reportes = reportesService.obtenerRecientes();
        return ResponseEntity.ok(reportes);
    }

    /**
     * Genera un reporte de ventas en un periodo
     * POST /api/reportes/ventas-periodo
     * Body: { "fechaInicio": "2024-01-01", "fechaFin": "2024-12-31" }
     * Incluye: total de ventas, cantidad de ventas, ticket promedio
     * @param request Objeto con fechaInicio y fechaFin
     * @return Reporte generado con status 201 (CREATED)
     */
    @PostMapping("/ventas-periodo")
    public ResponseEntity<Reportes> generarReporteVentasPeriodo(@RequestBody Map<String, Object> request) {
        try {
            // Extraer parámetros del request
            LocalDate fechaInicio = LocalDate.parse(request.get("fechaInicio").toString());
            LocalDate fechaFin = LocalDate.parse(request.get("fechaFin").toString());

            // Generar el reporte
            Reportes reporte = reportesService.generarReporteVentasPeriodo(
                    fechaInicio, fechaFin);

            return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Genera un reporte de productos más vendidos
     * POST /api/reportes/productos-mas-vendidos
     * Body: { "limite": 10 }
     * Lista los N productos más vendidos ordenados por cantidad
     * @param request Objeto con limite
     * @return Reporte generado con status 201 (CREATED)
     */
    @PostMapping("/productos-mas-vendidos")
    public ResponseEntity<Reportes> generarReporteProductosMasVendidos(@RequestBody Map<String, Object> request) {
        try {
            // Extraer parámetros
            int limite = Integer.parseInt(request.getOrDefault("limite", 10).toString());

            // Generar el reporte
            Reportes reporte = reportesService.generarReporteProductosMasVendidos(limite);

            return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Genera un reporte de inventario con stock bajo
     * POST /api/reportes/inventario-bajo
     * Body: { "stockMinimo": 10 }
     * Lista productos con stock por debajo del mínimo especificado
     * @param request Objeto con stockMinimo
     * @return Reporte generado con status 201 (CREATED)
     */
    @PostMapping("/inventario-bajo")
    public ResponseEntity<Reportes> generarReporteInventarioBajo(@RequestBody Map<String, Object> request) {
        try {
            // Extraer parámetros
            int stockMinimo = Integer.parseInt(request.getOrDefault("stockMinimo", 10).toString());

            // Generar el reporte
            Reportes reporte = reportesService.generarReporteInventarioBajo(stockMinimo);

            return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Genera un reporte de los mejores clientes
     * POST /api/reportes/clientes-top
     * Body: { "limite": 20 }
     * Lista los N mejores clientes ordenados por total de compras
     * @param request Objeto con limite
     * @return Reporte generado con status 201 (CREATED)
     */
    @PostMapping("/clientes-top")
    public ResponseEntity<Reportes> generarReporteClientesTop(@RequestBody Map<String, Object> request) {
        try {
            // Extraer parámetros
            int limite = Integer.parseInt(request.getOrDefault("limite", 20).toString());

            // Generar el reporte
            Reportes reporte = reportesService.generarReporteClientesTop(limite);

            return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Genera un reporte personalizado
     * POST /api/reportes/personalizado
     * Permite crear reportes con datos personalizados
     * @param reporte Datos del reporte personalizado
     * @return Reporte creado con status 201 (CREATED)
     */
    @PostMapping("/personalizado")
    public ResponseEntity<Reportes> generarReportePersonalizado(@RequestBody Reportes reporte) {
        try {
            Reportes nuevoReporte = reportesService.generarReportePersonalizado(reporte);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoReporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un reporte por su ID
     * DELETE /api/reportes/{id}
     * @param id ID del reporte a eliminar
     * @return 204 (NO CONTENT) si se eliminó, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            reportesService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

