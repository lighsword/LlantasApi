package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Inventario;
import com.proyectoMaycollins.LlantasApi.Service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<Inventario> listAll() {
        return inventarioService.listAll();
    }

    @GetMapping("/almacen/{almacenId}")
    public List<Inventario> listByAlmacen(@PathVariable Integer almacenId) {
        return inventarioService.listByAlmacen(almacenId);
    }

    @GetMapping("/producto/{productoId}")
    public List<Inventario> listByProducto(@PathVariable Integer productoId) {
        return inventarioService.listByProducto(productoId);
    }

    @GetMapping("/{productoId}/{almacenId}")
    public ResponseEntity<Inventario> get(@PathVariable Integer productoId, @PathVariable Integer almacenId) {
        return inventarioService.get(productoId, almacenId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Inventario upsert(@RequestBody Inventario body) {
        return inventarioService.upsert(body);
    }

    @DeleteMapping("/{productoId}/{almacenId}")
    public ResponseEntity<Void> delete(@PathVariable Integer productoId, @PathVariable Integer almacenId) {
        inventarioService.delete(productoId, almacenId);
        return ResponseEntity.noContent().build();
    }
}

