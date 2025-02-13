package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.DTO.CategoriaProductoDTO;
import com.proyectoMaycollins.LlantasApi.Model.CategoriaProducto;
import com.proyectoMaycollins.LlantasApi.Service.CategoriaProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categoría de Producto", description = "API para gestionar categorías de productos")
public class CategoriaProductoController {
    private final CategoriaProductoService categoriaProductoService;

    @Autowired
    public CategoriaProductoController(CategoriaProductoService categoriaProductoService) {
        this.categoriaProductoService = categoriaProductoService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<CategoriaProducto> crear(@Valid @RequestBody CategoriaProductoDTO dto) {
        return new ResponseEntity<>(categoriaProductoService.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría existente")
    public ResponseEntity<CategoriaProducto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaProductoDTO dto) {
        return ResponseEntity.ok(categoriaProductoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaProductoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<CategoriaProducto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaProductoService.encontrarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<CategoriaProducto>> listarTodos() {
        return ResponseEntity.ok(categoriaProductoService.listarTodos());
    }
}
