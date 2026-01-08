package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.enums.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enums")
@CrossOrigin(origins = "*")
public class EnumController {

    /**
     * Obtiene todos los enums disponibles en el sistema
     */
    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getAllEnums() {
        Map<String, List<String>> allEnums = new HashMap<>();

        allEnums.put("roles", Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("categorias", Arrays.stream(CategoriaNombre.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("estadosVenta", Arrays.stream(EstadoVenta.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("tiposCliente", Arrays.stream(TipoCliente.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("nivelesCliente", Arrays.stream(NivelCliente.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("tiposPromocion", Arrays.stream(TipoPromocion.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("tiposNotificacion", Arrays.stream(TipoNotificacion.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("prioridadesNotificacion", Arrays.stream(PrioridadNotificacion.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("estadosNotificacion", Arrays.stream(EstadoNotificacion.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("tiposTerreno", Arrays.stream(TipoTerreno.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        allEnums.put("tiposHerramienta", Arrays.stream(TipoHerramienta.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(allEnums);
    }

    /**
     * Obtiene los valores del enum Role
     */
    @GetMapping("/roles")
    public ResponseEntity<List<String>> getRoles() {
        List<String> roles = Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    /**
     * Obtiene los valores del enum CategoriaNombre
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> getCategorias() {
        List<String> categorias = Arrays.stream(CategoriaNombre.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }

    /**
     * Obtiene los valores del enum EstadoVenta
     */
    @GetMapping("/estados-venta")
    public ResponseEntity<List<String>> getEstadosVenta() {
        List<String> estados = Arrays.stream(EstadoVenta.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(estados);
    }

    /**
     * Obtiene los valores del enum TipoCliente
     */
    @GetMapping("/tipos-cliente")
    public ResponseEntity<List<String>> getTiposCliente() {
        List<String> tipos = Arrays.stream(TipoCliente.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }

    /**
     * Obtiene los valores del enum NivelCliente
     */
    @GetMapping("/niveles-cliente")
    public ResponseEntity<List<String>> getNivelesCliente() {
        List<String> niveles = Arrays.stream(NivelCliente.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(niveles);
    }

    /**
     * Obtiene los valores del enum TipoPromocion
     */
    @GetMapping("/tipos-promocion")
    public ResponseEntity<List<String>> getTiposPromocion() {
        List<String> tipos = Arrays.stream(TipoPromocion.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }

    /**
     * Obtiene los valores del enum TipoNotificacion
     */
    @GetMapping("/tipos-notificacion")
    public ResponseEntity<List<String>> getTiposNotificacion() {
        List<String> tipos = Arrays.stream(TipoNotificacion.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }

    /**
     * Obtiene los valores del enum PrioridadNotificacion
     */
    @GetMapping("/prioridades-notificacion")
    public ResponseEntity<List<String>> getPrioridadesNotificacion() {
        List<String> prioridades = Arrays.stream(PrioridadNotificacion.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prioridades);
    }

    /**
     * Obtiene los valores del enum EstadoNotificacion
     */
    @GetMapping("/estados-notificacion")
    public ResponseEntity<List<String>> getEstadosNotificacion() {
        List<String> estados = Arrays.stream(EstadoNotificacion.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(estados);
    }

    /**
     * Obtiene los valores del enum TipoTerreno
     */
    @GetMapping("/tipos-terreno")
    public ResponseEntity<List<String>> getTiposTerreno() {
        List<String> tipos = Arrays.stream(TipoTerreno.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }

    /**
     * Obtiene los valores del enum TipoHerramienta
     */
    @GetMapping("/tipos-herramienta")
    public ResponseEntity<List<String>> getTiposHerramienta() {
        List<String> tipos = Arrays.stream(TipoHerramienta.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }
}

