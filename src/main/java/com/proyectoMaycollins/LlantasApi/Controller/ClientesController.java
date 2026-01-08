package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Clientes;
import com.proyectoMaycollins.LlantasApi.Service.ClientesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientesController {
    private final ClientesService clientesService;

    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @GetMapping
    public List<Clientes> list() {
        return clientesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clientes> getById(@PathVariable Long id) {
        return clientesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activos")
    public List<Clientes> activos() {
        return clientesService.findActivos();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Clientes> getByEmail(@PathVariable String email) {
        return clientesService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/documento/{documento}")
    public ResponseEntity<Clientes> getByDocumento(@PathVariable String documento) {
        return clientesService.findByDocumentoIdentidad(documento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Clientes> create(@RequestBody Clientes cliente) {
        Clientes created = clientesService.create(cliente);
        return ResponseEntity.created(URI.create("/api/clientes/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clientes> update(@PathVariable Long id, @RequestBody Clientes cambios) {
        return clientesService.update(id, cambios)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = clientesService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

