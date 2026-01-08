package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.DTO.ActualizarPreciosProductoDTO;
import com.proyectoMaycollins.LlantasApi.DTO.PrecioDTO;
import com.proyectoMaycollins.LlantasApi.DTO.PreciosProductoDTO;
import com.proyectoMaycollins.LlantasApi.Model.enums.TipoPrecio;
import com.proyectoMaycollins.LlantasApi.Service.PreciosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/precios")
@RequiredArgsConstructor
@Tag(name = "Precios", description = "API para gestión de precios de productos (Compra, Venta, Mayorista)")
public class PreciosController {

    private final PreciosService preciosService;

    /**
     * Crear o actualizar un precio individual
     */
    @PostMapping
    @Operation(summary = "Crear o actualizar un precio",
               description = "Registra un nuevo precio para un producto (COMPRA, VENTA o MAYORISTA). Desactiva automáticamente el precio anterior del mismo tipo.")
    public ResponseEntity<PrecioDTO> crearPrecio(@Valid @RequestBody PrecioDTO precioDTO) {
        try {
            PrecioDTO nuevoPrecio = preciosService.guardarPrecio(precioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrecio);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Actualizar todos los precios de un producto a la vez
     */
    @PutMapping("/producto")
    @Operation(summary = "Actualizar todos los precios de un producto",
               description = "Actualiza los tres tipos de precios (compra, venta, mayorista) de un producto en una sola operación")
    public ResponseEntity<PreciosProductoDTO> actualizarTodosLosPrecios(
            @Valid @RequestBody ActualizarPreciosProductoDTO dto) {
        try {
            PreciosProductoDTO resultado = preciosService.actualizarTodosLosPrecios(dto);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Obtener todos los precios activos de un producto
     */
    @GetMapping("/producto/{productosId}")
    @Operation(summary = "Obtener precios de un producto",
               description = "Retorna todos los precios activos de un producto con cálculo de márgenes de ganancia")
    public ResponseEntity<PreciosProductoDTO> obtenerPreciosProducto(
            @Parameter(description = "ID del producto") @PathVariable Long productosId) {
        try {
            PreciosProductoDTO precios = preciosService.obtenerPreciosProducto(productosId);
            return ResponseEntity.ok(precios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Obtener precio activo por tipo específico
     */
    @GetMapping("/producto/{productosId}/tipo/{tipo}")
    @Operation(summary = "Obtener precio por tipo",
               description = "Retorna el precio activo de un producto para un tipo específico (COMPRA, VENTA, MAYORISTA)")
    public ResponseEntity<PrecioDTO> obtenerPrecioPorTipo(
            @Parameter(description = "ID del producto") @PathVariable Long productosId,
            @Parameter(description = "Tipo de precio") @PathVariable TipoPrecio tipo) {
        try {
            PrecioDTO precio = preciosService.obtenerPrecioPorTipo(productosId, tipo);
            return ResponseEntity.ok(precio);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Obtener historial de precios de un producto por tipo
     */
    @GetMapping("/producto/{productosId}/historial/{tipo}")
    @Operation(summary = "Obtener historial de precios",
               description = "Retorna el historial completo de precios de un producto para un tipo específico, ordenado por fecha")
    public ResponseEntity<List<PrecioDTO>> obtenerHistorialPrecios(
            @Parameter(description = "ID del producto") @PathVariable Long productosId,
            @Parameter(description = "Tipo de precio") @PathVariable TipoPrecio tipo) {
        try {
            List<PrecioDTO> historial = preciosService.obtenerHistorialPrecios(productosId, tipo);
            return ResponseEntity.ok(historial);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Listar todos los precios activos
     */
    @GetMapping("/activos")
    @Operation(summary = "Listar todos los precios activos",
               description = "Retorna lista de todos los precios activos en el sistema")
    public ResponseEntity<List<PrecioDTO>> listarPreciosActivos() {
        List<PrecioDTO> precios = preciosService.listarPreciosActivos();
        return ResponseEntity.ok(precios);
    }

    /**
     * Listar precios de todos los productos con márgenes
     */
    @GetMapping("/productos")
    @Operation(summary = "Listar precios de todos los productos",
               description = "Retorna precios de todos los productos con cálculo de márgenes de ganancia")
    public ResponseEntity<List<PreciosProductoDTO>> listarTodosPreciosProductos() {
        List<PreciosProductoDTO> precios = preciosService.listarTodosPreciosProductos();
        return ResponseEntity.ok(precios);
    }

    /**
     * Desactivar un precio específico
     */
    @DeleteMapping("/{precioId}")
    @Operation(summary = "Desactivar un precio",
               description = "Desactiva un precio específico (no lo elimina, solo lo marca como inactivo)")
    public ResponseEntity<Void> desactivarPrecio(
            @Parameter(description = "ID del precio") @PathVariable Long precioId) {
        try {
            preciosService.desactivarPrecio(precioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obtener tipos de precio disponibles
     */
    @GetMapping("/tipos")
    @Operation(summary = "Obtener tipos de precio",
               description = "Retorna los tipos de precio disponibles en el sistema")
    public ResponseEntity<TipoPrecio[]> obtenerTiposPrecios() {
        return ResponseEntity.ok(TipoPrecio.values());
    }
}

