package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.DetalleVentas;
import com.proyectoMaycollins.LlantasApi.Model.Ventas;
import com.proyectoMaycollins.LlantasApi.Service.VentasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentasController {
    private final VentasService ventasService;

    public VentasController(VentasService ventasService) { this.ventasService = ventasService; }

    @GetMapping
    public List<Ventas> list() { return ventasService.list(); }

    @GetMapping("/{id}")
    public ResponseEntity<Ventas> get(@PathVariable Long id) {
        return ventasService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/detalles")
    public List<DetalleVentas> detalles(@PathVariable Long id) { return ventasService.detalles(id); }

    @PostMapping
    public ResponseEntity<Ventas> create(@RequestBody Ventas v) {
        Ventas saved = ventasService.save(v);
        return ResponseEntity.created(URI.create("/api/ventas/" + saved.getId())).body(saved);
    }

    @PostMapping("/{id}/detalles")
    public ResponseEntity<DetalleVentas> addDetalle(@PathVariable Long id, @RequestBody DetalleVentas d) {
        // asociar la venta por id
        Ventas v = new Ventas();
        v.setId(id);
        d.setVenta(v);
        DetalleVentas saved = ventasService.addDetalle(d);
        return ResponseEntity.created(URI.create("/api/ventas/" + id + "/detalles/" + saved.getId())).body(saved);
    }
}
