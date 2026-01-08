package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Almacenes;
import com.proyectoMaycollins.LlantasApi.Service.AlmacenesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
public class AlmacenesController {
    private final AlmacenesService almacenesService;

    public AlmacenesController(AlmacenesService almacenesService) {
        this.almacenesService = almacenesService;
    }

    @GetMapping
    public List<Almacenes> list() { return almacenesService.list(); }

    @GetMapping("/{id}")
    public ResponseEntity<Almacenes> get(@PathVariable Integer id) {
        return almacenesService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Almacenes> create(@RequestBody Almacenes a) {
        Almacenes saved = almacenesService.save(a);
        return ResponseEntity.created(URI.create("/api/almacenes/" + saved.getAlmacenId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Almacenes> update(@PathVariable Integer id, @RequestBody Almacenes cambios) {
        return almacenesService.get(id).map(existing -> {
            existing.setNombre(cambios.getNombre());
            existing.setUbicacion(cambios.getUbicacion());
            Almacenes saved = almacenesService.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return almacenesService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

