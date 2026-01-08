package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Productos;
import com.proyectoMaycollins.LlantasApi.Service.ProductosService;
import com.proyectoMaycollins.LlantasApi.Service.ProductoVisibilidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "📦 Productos", description = "Gestión de productos con sistema de visibilidad y soft delete")
@SecurityRequirement(name = "Bearer Authentication")
public class ProductosController {
    private final ProductosService productosService;
    private final ProductoVisibilidadService visibilidadService;

    public ProductosController(ProductosService productosService, ProductoVisibilidadService visibilidadService) {
        this.productosService = productosService;
        this.visibilidadService = visibilidadService;
    }

    @Operation(
            summary = "Listar todos los productos",
            description = "⚠️ ADMIN: Obtiene TODOS los productos (activos, inactivos, con/sin stock). " +
                         "Para catálogo público use /visibles"
    )
    @GetMapping
    public List<Productos> list() {
        return productosService.findAll();
    }

    @Operation(
            summary = "Productos VISIBLES en catálogo",
            description = "✅ Obtiene solo productos visibles (activo=true Y stock>0). " +
                         "Este endpoint es el que debe usar el FRONTEND para mostrar productos disponibles."
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos disponibles para la venta")
    @GetMapping("/visibles")
    public ResponseEntity<List<Productos>> getProductosVisibles() {
        List<Productos> productosVisibles = visibilidadService.obtenerProductosVisibles();
        return ResponseEntity.ok(productosVisibles);
    }

    @Operation(
            summary = "Productos AGOTADOS",
            description = "⚠️ Productos que están activos pero sin stock (stock=0). " +
                         "Estos productos NO se muestran en el catálogo pero pueden reabastecerse."
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos agotados temporalmente")
    @GetMapping("/agotados")
    public ResponseEntity<List<Productos>> getProductosAgotados() {
        List<Productos> productosAgotados = visibilidadService.obtenerProductosAgotados();
        return ResponseEntity.ok(productosAgotados);
    }

    @Operation(
            summary = "Productos DESCONTINUADOS",
            description = "🚫 Productos que ya no se venden (activo=false). " +
                         "Permanecen en BD para reportes históricos e integridad referencial."
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos descontinuados")
    @GetMapping("/descontinuados")
    public ResponseEntity<List<Productos>> getProductosDescontinuados() {
        List<Productos> descontinuados = visibilidadService.obtenerProductosDescontinuados();
        return ResponseEntity.ok(descontinuados);
    }

    @Operation(
            summary = "Estadísticas de visibilidad",
            description = "📊 Muestra cuántos productos hay en cada estado: visibles, agotados, descontinuados"
    )
    @GetMapping("/estadisticas-visibilidad")
    public ResponseEntity<ProductoVisibilidadService.VisibilidadStats> getEstadisticasVisibilidad() {
        ProductoVisibilidadService.VisibilidadStats stats = visibilidadService.obtenerEstadisticas();
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Obtener producto por ID", description = "Busca un producto específico por su identificador (incluye inactivos)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Productos> getById(
            @Parameter(description = "ID del producto", required = true) @PathVariable Long id) {
        return productosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Verificar visibilidad de producto",
            description = "Verifica si un producto específico es visible (activo=true y stock>0)"
    )
    @GetMapping("/{id}/visible")
    public ResponseEntity<Map<String, Object>> verificarVisibilidad(@PathVariable Long id) {
        boolean esVisible = visibilidadService.esProductoVisible(id);
        int stockTotal = visibilidadService.calcularStockTotal(id);

        Productos producto = productosService.findById(id).orElse(null);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(
                "productoId", id,
                "codigoProducto", producto.getCodigoProducto() != null ? producto.getCodigoProducto() : "",
                "esVisible", esVisible,
                "activo", producto.getActivo() != null ? producto.getActivo() : false,
                "stockTotal", stockTotal,
                "razon", !Boolean.TRUE.equals(producto.getActivo()) ? "Producto descontinuado" :
                        stockTotal == 0 ? "Sin stock disponible" : "Producto disponible"
        ));
    }

    @Operation(summary = "Buscar productos", description = "Busca productos por código o descripción (búsqueda parcial)")
    @GetMapping("/buscar")
    public List<Productos> search(
            @Parameter(description = "Texto a buscar en código o descripción", required = true)
            @RequestParam("q") String q) {
        return productosService.searchByNombre(q);
    }

    @Operation(summary = "Listar productos activos", description = "⚠️ Obtiene productos con activo=true (pueden tener o no stock)")
    @GetMapping("/activos")
    public List<Productos> activos() {
        return productosService.findActivos();
    }

    @Operation(summary = "Crear nuevo producto", description = "Registra un nuevo producto en el catálogo")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @PostMapping
    public ResponseEntity<Productos> create(@RequestBody Productos producto) {
        // Por defecto, nuevos productos se crean activos
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        Productos created = productosService.create(producto);
        return ResponseEntity.created(URI.create("/api/productos/" + created.getProductoId())).body(created);
    }

    @Operation(summary = "Actualizar producto", description = "Modifica los datos de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Productos> update(@PathVariable Long id, @RequestBody Productos cambios) {
        return productosService.update(id, cambios)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "DESACTIVAR producto (Soft Delete)",
            description = "🚫 Marca el producto como inactivo (activo=false) en lugar de eliminarlo. " +
                         "El producto permanece en BD para mantener integridad referencial en facturas, " +
                         "reportes históricos y declaraciones de impuestos. " +
                         "TODOS los usuarios recibirán una notificación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto desactivado y usuarios notificados"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, String>> desactivarProducto(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {

        try {
            String motivo = body != null ? body.get("motivo") : "Producto descontinuado";
            visibilidadService.desactivarProducto(id, motivo);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Producto desactivado exitosamente (soft delete)",
                    "accion", "El producto permanece en BD para reportes históricos",
                    "notificacion", "Todos los usuarios han sido notificados",
                    "motivo", motivo
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "REACTIVAR producto",
            description = "✅ Reactiva un producto previamente desactivado (activo=true). " +
                         "Si el producto tiene stock, los usuarios serán notificados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto reactivado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<Map<String, Object>> reactivarProducto(@PathVariable Long id) {
        try {
            visibilidadService.reactivarProducto(id);
            int stockTotal = visibilidadService.calcularStockTotal(id);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Producto reactivado exitosamente",
                    "stockTotal", stockTotal,
                    "notificacion", stockTotal > 0 ?
                            "Usuarios notificados - Producto disponible" :
                            "Sin stock - No se envió notificación"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "⚠️ ELIMINAR producto (NO RECOMENDADO)",
            description = "❌ Elimina físicamente el producto de la BD. " +
                         "⚠️ PELIGRO: Esto puede romper integridad referencial en facturas y reportes históricos. " +
                         "❌ NO USAR si el producto tiene ventas/compras registradas. " +
                         "✅ USE /desactivar en su lugar para soft delete seguro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado físicamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar - Producto tiene transacciones asociadas")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // NOTA: Este endpoint está deshabilitado por seguridad
        // Se recomienda usar /desactivar en su lugar
        return ResponseEntity.status(403).build(); // Forbidden

        // Si deseas habilitarlo, descomenta:
        // boolean deleted = productosService.delete(id);
        // return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
