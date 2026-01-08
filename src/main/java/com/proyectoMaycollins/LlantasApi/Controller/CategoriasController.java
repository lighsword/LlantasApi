package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.DTO.RegistrarCategoriaDTO;
import com.proyectoMaycollins.LlantasApi.Model.Categorias;
import com.proyectoMaycollins.LlantasApi.Service.CategoriasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "📂 Categorías", description = "Gestión de categorías de productos")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoriasController {
    private final CategoriasService categoriasService;

    public CategoriasController(CategoriasService categoriasService) {
        this.categoriasService = categoriasService;
    }

    @Operation(summary = "Listar todas las categorías", description = "Obtiene la lista completa de categorías registradas")
    @GetMapping
    public List<Categorias> list() {
        return categoriasService.findAll();
    }

    @Operation(summary = "Obtener categoría por ID", description = "Busca una categoría específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categorias> getById(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        return categoriasService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar categoría por nombre", description = "Busca una categoría por su nombre exacto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Categorias> getByNombre(
            @Parameter(description = "Nombre de la categoría", required = true)
            @PathVariable String nombre) {
        return categoriasService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar nueva categoría",
            description = "Crea una nueva categoría de producto en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "La categoría ya existe")
    })
    @PostMapping
    public ResponseEntity<Categorias> create(
            @Valid @RequestBody RegistrarCategoriaDTO registrarDTO) {

        // Verificar si la categoría ya existe
        var existente = categoriasService.findByNombre(registrarDTO.getNombre());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Crear nueva categoría
        Categorias categoria = Categorias.builder()
                .nombre(registrarDTO.getNombre())
                .descripcion(registrarDTO.getDescripcion())
                .activo(registrarDTO.getActivo())
                .build();

        Categorias created = categoriasService.create(categoria);
        return ResponseEntity.created(URI.create("/api/categorias/" + created.getCategoriaId()))
                .body(created);
    }

    @Operation(summary = "Actualizar categoría", description = "Modifica los datos de una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categorias> update(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RegistrarCategoriaDTO cambiosDTO) {

        return categoriasService.findById(id).map(existente -> {
            existente.setNombre(cambiosDTO.getNombre());
            existente.setDescripcion(cambiosDTO.getDescripcion());
            existente.setActivo(cambiosDTO.getActivo());
            return ResponseEntity.ok(categoriasService.update(id, existente).get());
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        boolean deleted = categoriasService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

