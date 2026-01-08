package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Compras;
import com.proyectoMaycollins.LlantasApi.Model.DetalleCompras;
import com.proyectoMaycollins.LlantasApi.Service.ComprasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar Compras
 * Expone endpoints para registrar compras a proveedores y sus detalles
 */
@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class ComprasController {

    @Autowired
    private ComprasService comprasService;

    /**
     * Obtiene todas las compras registradas
     * GET /api/compras
     * @return Lista de compras
     */
    @GetMapping
    public ResponseEntity<List<Compras>> obtenerTodas() {
        List<Compras> compras = comprasService.obtenerTodas();
        return ResponseEntity.ok(compras);
    }

    /**
     * Obtiene una compra específica por su ID
     * GET /api/compras/{id}
     * @param id ID de la compra
     * @return Compra encontrada o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Compras> obtenerPorId(@PathVariable Integer id) {
        return comprasService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los detalles de una compra específica
     * GET /api/compras/{id}/detalles
     * @param id ID de la compra
     * @return Lista de detalles de la compra
     */
    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleCompras>> obtenerDetalles(@PathVariable Integer id) {
        List<DetalleCompras> detalles = comprasService.obtenerDetallesCompra(id);
        return ResponseEntity.ok(detalles);
    }

    /**
     * Obtiene compras de un proveedor específico
     * GET /api/compras/proveedor/{proveedorId}
     * @param proveedorId ID del proveedor
     * @return Lista de compras del proveedor
     */
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Compras>> obtenerPorProveedor(@PathVariable Integer proveedorId) {
        List<Compras> compras = comprasService.obtenerPorProveedor(proveedorId);
        return ResponseEntity.ok(compras);
    }

    /**
     * Obtiene las últimas 10 compras registradas
     * GET /api/compras/recientes
     * @return Lista de compras recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<List<Compras>> obtenerRecientes() {
        List<Compras> compras = comprasService.obtenerUltimasCompras();
        return ResponseEntity.ok(compras);
    }

    /**
     * Obtiene compras en un rango de fechas
     * GET /api/compras/periodo?inicio=2024-01-01T00:00:00&fin=2024-12-31T23:59:59
     * @param inicio Fecha de inicio (formato: yyyy-MM-dd'T'HH:mm:ss)
     * @param fin Fecha de fin
     * @return Lista de compras en el rango
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<Compras>> obtenerPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Compras> compras = comprasService.obtenerPorRangoFechas(inicio, fin);
        return ResponseEntity.ok(compras);
    }

    /**
     * Calcula el total de compras en un periodo
     * GET /api/compras/total-periodo?inicio=2024-01-01T00:00:00&fin=2024-12-31T23:59:59
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Total de compras en el periodo
     */
    @GetMapping("/total-periodo")
    public ResponseEntity<Map<String, Double>> calcularTotalPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        Double total = comprasService.calcularTotalPeriodo(inicio, fin);
        return ResponseEntity.ok(Map.of("total", total));
    }

    /**
     * Crea una nueva compra sin detalles
     * POST /api/compras
     * @param compra Datos de la compra
     * @return Compra creada con status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<Compras> crear(@RequestBody Compras compra) {
        try {
            Compras nuevaCompra = comprasService.crear(compra);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCompra);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crea una compra completa con sus detalles
     * POST /api/compras/completa
     * Body: { "compra": {...}, "detalles": [...] }
     * Actualiza automáticamente el stock de los productos
     * @param request Objeto con la compra y sus detalles
     * @return Compra creada con status 201 (CREATED)
     */
    @PostMapping("/completa")
    public ResponseEntity<Compras> crearConDetalles(@RequestBody Map<String, Object> request) {
        try {
            // Extraer la compra y los detalles del request
            // Nota: En producción usar DTOs específicos
            Compras compra = new Compras(); // Mapear desde request.get("compra")
            List<DetalleCompras> detalles = List.of(); // Mapear desde request.get("detalles")

            Compras nuevaCompra = comprasService.crearConDetalles(compra, detalles);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCompra);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Agrega un detalle a una compra existente
     * POST /api/compras/{id}/detalles
     * Actualiza el total de la compra y el stock del producto
     * @param id ID de la compra
     * @param detalle Detalle a agregar
     * @return Detalle creado con status 201 (CREATED)
     */
    @PostMapping("/{id}/detalles")
    public ResponseEntity<DetalleCompras> agregarDetalle(
            @PathVariable Integer id,
            @RequestBody DetalleCompras detalle) {
        try {
            DetalleCompras nuevoDetalle = comprasService.agregarDetalle(id, detalle);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza una compra existente
     * PUT /api/compras/{id}
     * Solo actualiza la cabecera, no los detalles
     * @param id ID de la compra
     * @param compra Datos actualizados
     * @return Compra actualizada o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Compras> actualizar(
            @PathVariable Integer id,
            @RequestBody Compras compra) {
        try {
            Compras compraActualizada = comprasService.actualizar(id, compra);
            return ResponseEntity.ok(compraActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recalcula el total de una compra desde sus detalles
     * PUT /api/compras/{id}/recalcular-total
     * @param id ID de la compra
     * @return Compra con total actualizado
     */
    @PutMapping("/{id}/recalcular-total")
    public ResponseEntity<Compras> recalcularTotal(@PathVariable Integer id) {
        try {
            Compras compra = comprasService.recalcularTotal(id);
            return ResponseEntity.ok(compra);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina una compra y todos sus detalles
     * DELETE /api/compras/{id}
     * IMPORTANTE: Esta operación NO revierte el inventario
     * @param id ID de la compra a eliminar
     * @return 204 (NO CONTENT) si se eliminó, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            comprasService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

