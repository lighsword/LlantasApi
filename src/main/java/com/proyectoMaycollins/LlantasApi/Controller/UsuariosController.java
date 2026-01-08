package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.DTO.UsuariosCreateRequest;
import com.proyectoMaycollins.LlantasApi.DTO.UsuariosUpdateRequest;
import com.proyectoMaycollins.LlantasApi.Model.Usuarios;
import com.proyectoMaycollins.LlantasApi.Service.UsuariosService;
import com.proyectoMaycollins.LlantasApi.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuariosController {

    private final UsuariosService usuariosService;

    @GetMapping
    public List<Usuarios> list() {
        return usuariosService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> getById(@PathVariable Long id) {
        return usuariosService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UsuariosCreateRequest req) {
        if (usuariosService.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email ya registrado");
        }
        Usuarios u = Usuarios.builder()
                .email(req.getEmail())
                .nombre(req.getNombre())
                .password(req.getPassword())
                .rol(req.getRol())
                .activo(req.getActivo() != null ? req.getActivo() : true)
                .build();
        return ResponseEntity.ok(usuariosService.create(u, true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UsuariosUpdateRequest req) {
        Usuarios cambios = Usuarios.builder()
                .email(req.getEmail())
                .nombre(req.getNombre())
                .password(req.getPassword())
                .rol(req.getRol())
                .activo(req.getActivo())
                .build();
        return usuariosService.update(id, cambios, true)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return usuariosService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
