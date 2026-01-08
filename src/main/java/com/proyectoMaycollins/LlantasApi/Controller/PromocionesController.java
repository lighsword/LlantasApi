package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Promociones;
import com.proyectoMaycollins.LlantasApi.Service.PromocionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/promociones")
public class PromocionesController {
    private final PromocionesService promocionesService;

    public PromocionesController(PromocionesService promocionesService) {
        this.promocionesService = promocionesService;
    }

    @GetMapping
    public List<Promociones> list() { return promocionesService.list(); }

    @GetMapping("/activas")
    public List<Promociones> activas() { return promocionesService.activas(); }

    @GetMapping("/{id}")
    public ResponseEntity<Promociones> get(@PathVariable Long id) {
        return promocionesService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Promociones> create(@RequestBody Promociones p) {
        Promociones saved = promocionesService.save(p);
        return ResponseEntity.created(URI.create("/api/promociones/" + saved.getPromocionId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promociones> update(@PathVariable Long id, @RequestBody Promociones cambios) {
        return promocionesService.get(id).map(existing -> {
            existing.setActiva(cambios.getActiva());
            existing.setFechaInicio(cambios.getFechaInicio());
            existing.setFechaFin(cambios.getFechaFin());
            existing.setValorDescuento(cambios.getValorDescuento());
            existing.setTipoDescuento(cambios.getTipoDescuento());
            existing.setNombre(cambios.getNombre());
            existing.setDescripcion(cambios.getDescripcion());
            Promociones saved = promocionesService.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return promocionesService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

