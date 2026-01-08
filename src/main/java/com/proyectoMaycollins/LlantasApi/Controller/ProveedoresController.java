package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Proveedores;
import com.proyectoMaycollins.LlantasApi.Service.ProveedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar Proveedores
 * Expone endpoints para operaciones CRUD de proveedores
 */
@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedoresController {

    @Autowired
    private ProveedoresService proveedoresService;

    /**
     * Obtiene todos los proveedores registrados
     * GET /api/proveedores
     * @return Lista de proveedores
     */
    @GetMapping
    public ResponseEntity<List<Proveedores>> obtenerTodos() {
        List<Proveedores> proveedores = proveedoresService.obtenerTodos();
        return ResponseEntity.ok(proveedores);
    }

    /**
     * Obtiene un proveedor por su ID
     * GET /api/proveedores/{id}
     * @param id ID del proveedor
     * @return Proveedor encontrado o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Proveedores> obtenerPorId(@PathVariable Integer id) {
        return proveedoresService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca un proveedor por su email
     * GET /api/proveedores/email/{email}
     * @param email Email del proveedor
     * @return Proveedor encontrado o 404 si no existe
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Proveedores> obtenerPorEmail(@PathVariable String email) {
        return proveedoresService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca un proveedor por su nombre
     * GET /api/proveedores/nombre/{nombre}
     * @param nombre Nombre del proveedor
     * @return Proveedor encontrado o 404 si no existe
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Proveedores> obtenerPorNombre(@PathVariable String nombre) {
        return proveedoresService.obtenerPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo proveedor
     * POST /api/proveedores
     * @param proveedor Datos del proveedor a crear
     * @return Proveedor creado con status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<Proveedores> crear(@RequestBody Proveedores proveedor) {
        try {
            Proveedores nuevoProveedor = proveedoresService.crear(proveedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProveedor);
        } catch (RuntimeException e) {
            // Si hay error (ej: email duplicado), retornar 400 BAD REQUEST
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un proveedor existente
     * PUT /api/proveedores/{id}
     * @param id ID del proveedor a actualizar
     * @param proveedor Datos actualizados
     * @return Proveedor actualizado o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Proveedores> actualizar(
            @PathVariable Integer id,
            @RequestBody Proveedores proveedor) {
        try {
            Proveedores proveedorActualizado = proveedoresService.actualizar(id, proveedor);
            return ResponseEntity.ok(proveedorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un proveedor por su ID
     * DELETE /api/proveedores/{id}
     * @param id ID del proveedor a eliminar
     * @return 204 (NO CONTENT) si se eliminó correctamente, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            proveedoresService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Verifica si existe un proveedor con el email dado
     * GET /api/proveedores/existe/email/{email}
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/existe/email/{email}")
    public ResponseEntity<Boolean> existePorEmail(@PathVariable String email) {
        boolean existe = proveedoresService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }
}

