package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.*;
import com.proyectoMaycollins.LlantasApi.Service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/productos/detalles")
@Tag(name = "🔧 Detalles de Productos", description = "Gestión de detalles específicos por categoría")
public class ProductoDetallesController {

    private final ProductoLlantaDetalleService llantaService;
    private final ProductoHerramientaDetalleService herramientaService;
    private final ProductoRefaccionDetalleService refaccionService;
    private final ProductoAccesorioDetalleService accesorioService;
    private final ProductoInsumoDetalleService insumoService;

    public ProductoDetallesController(
            ProductoLlantaDetalleService llantaService,
            ProductoHerramientaDetalleService herramientaService,
            ProductoRefaccionDetalleService refaccionService,
            ProductoAccesorioDetalleService accesorioService,
            ProductoInsumoDetalleService insumoService) {
        this.llantaService = llantaService;
        this.herramientaService = herramientaService;
        this.refaccionService = refaccionService;
        this.accesorioService = accesorioService;
        this.insumoService = insumoService;
    }

    // ========== LLANTA DETALLES ==========
    @Operation(summary = "Obtener detalles de llanta")
    @GetMapping("/llanta/{id}")
    public ResponseEntity<ProductoLlantaDetalle> getLlantaDetalle(@PathVariable Long id) {
        return llantaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear detalles de llanta")
    @PostMapping("/llanta")
    public ResponseEntity<ProductoLlantaDetalle> createLlantaDetalle(@RequestBody ProductoLlantaDetalle detalle) {
        ProductoLlantaDetalle created = llantaService.create(detalle);
        return ResponseEntity.created(URI.create("/api/productos/detalles/llanta/" + created.getProductoId())).body(created);
    }

    @Operation(summary = "Actualizar detalles de llanta")
    @PutMapping("/llanta/{id}")
    public ResponseEntity<ProductoLlantaDetalle> updateLlantaDetalle(@PathVariable Long id, @RequestBody ProductoLlantaDetalle cambios) {
        return llantaService.update(id, cambios)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar detalles de llanta")
    @DeleteMapping("/llanta/{id}")
    public ResponseEntity<Void> deleteLlantaDetalle(@PathVariable Long id) {
        boolean deleted = llantaService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ========== HERRAMIENTA DETALLES ==========
    @Operation(summary = "Obtener detalles de herramienta")
    @GetMapping("/herramienta/{id}")
    public ResponseEntity<ProductoHerramientaDetalle> getHerramientaDetalle(@PathVariable Long id) {
        return herramientaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear detalles de herramienta")
    @PostMapping("/herramienta")
    public ResponseEntity<ProductoHerramientaDetalle> createHerramientaDetalle(@RequestBody ProductoHerramientaDetalle detalle) {
        ProductoHerramientaDetalle created = herramientaService.create(detalle);
        return ResponseEntity.created(URI.create("/api/productos/detalles/herramienta/" + created.getProductoId())).body(created);
    }

    @Operation(summary = "Actualizar detalles de herramienta")
    @PutMapping("/herramienta/{id}")
    public ResponseEntity<ProductoHerramientaDetalle> updateHerramientaDetalle(@PathVariable Long id, @RequestBody ProductoHerramientaDetalle cambios) {
        return herramientaService.update(id, cambios)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar detalles de herramienta")
    @DeleteMapping("/herramienta/{id}")
    public ResponseEntity<Void> deleteHerramientaDetalle(@PathVariable Long id) {
        boolean deleted = herramientaService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ========== REFACCION DETALLES ==========
    @Operation(summary = "Obtener detalles de refacción")
    @GetMapping("/refaccion/{id}")
    public ResponseEntity<ProductoRefaccionDetalle> getRefaccionDetalle(@PathVariable Long id) {
        return refaccionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear detalles de refacción")
    @PostMapping("/refaccion")
    public ResponseEntity<ProductoRefaccionDetalle> createRefaccionDetalle(@RequestBody ProductoRefaccionDetalle detalle) {
        ProductoRefaccionDetalle created = refaccionService.create(detalle);
        return ResponseEntity.created(URI.create("/api/productos/detalles/refaccion/" + created.getProductoId())).body(created);
    }

    @Operation(summary = "Actualizar detalles de refacción")
    @PutMapping("/refaccion/{id}")
    public ResponseEntity<ProductoRefaccionDetalle> updateRefaccionDetalle(@PathVariable Long id, @RequestBody ProductoRefaccionDetalle cambios) {
        return refaccionService.update(id, cambios)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar detalles de refacción")
    @DeleteMapping("/refaccion/{id}")
    public ResponseEntity<Void> deleteRefaccionDetalle(@PathVariable Long id) {
        boolean deleted = refaccionService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ========== ACCESORIO DETALLES ==========
    @Operation(summary = "Obtener detalles de accesorio")
    @GetMapping("/accesorio/{id}")
    public ResponseEntity<ProductoAccesorioDetalle> getAccesorioDetalle(@PathVariable Long id) {
        return accesorioService.findByProductoId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear detalles de accesorio")
    @PostMapping("/accesorio")
    public ResponseEntity<ProductoAccesorioDetalle> createAccesorioDetalle(@RequestBody ProductoAccesorioDetalle detalle) {
        ProductoAccesorioDetalle created = accesorioService.save(detalle);
        return ResponseEntity.created(URI.create("/api/productos/detalles/accesorio/" + created.getProductoId())).body(created);
    }

    @Operation(summary = "Actualizar detalles de accesorio")
    @PutMapping("/accesorio/{id}")
    public ResponseEntity<ProductoAccesorioDetalle> updateAccesorioDetalle(@PathVariable Long id, @RequestBody ProductoAccesorioDetalle cambios) {
        return accesorioService.findByProductoId(id).map(existente -> {
            cambios.setProductoId(id);
            return ResponseEntity.ok(accesorioService.save(cambios));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar detalles de accesorio")
    @DeleteMapping("/accesorio/{id}")
    public ResponseEntity<Void> deleteAccesorioDetalle(@PathVariable Long id) {
        accesorioService.deleteByProductoId(id);
        return ResponseEntity.noContent().build();
    }

    // ========== INSUMO DETALLES ==========
    @Operation(summary = "Obtener detalles de insumo")
    @GetMapping("/insumo/{id}")
    public ResponseEntity<ProductoInsumoDetalle> getInsumoDetalle(@PathVariable Long id) {
        return insumoService.findByProductoId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear detalles de insumo")
    @PostMapping("/insumo")
    public ResponseEntity<ProductoInsumoDetalle> createInsumoDetalle(@RequestBody ProductoInsumoDetalle detalle) {
        ProductoInsumoDetalle created = insumoService.save(detalle);
        return ResponseEntity.created(URI.create("/api/productos/detalles/insumo/" + created.getProductoId())).body(created);
    }

    @Operation(summary = "Actualizar detalles de insumo")
    @PutMapping("/insumo/{id}")
    public ResponseEntity<ProductoInsumoDetalle> updateInsumoDetalle(@PathVariable Long id, @RequestBody ProductoInsumoDetalle cambios) {
        return insumoService.findByProductoId(id).map(existente -> {
            cambios.setProductoId(id);
            return ResponseEntity.ok(insumoService.save(cambios));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar detalles de insumo")
    @DeleteMapping("/insumo/{id}")
    public ResponseEntity<Void> deleteInsumoDetalle(@PathVariable Long id) {
        insumoService.deleteByProductoId(id);
        return ResponseEntity.noContent().build();
    }
}

