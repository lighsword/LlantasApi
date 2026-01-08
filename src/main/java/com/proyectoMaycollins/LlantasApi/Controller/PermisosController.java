package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.Permisos;
import com.proyectoMaycollins.LlantasApi.Model.enums.Accion;
import com.proyectoMaycollins.LlantasApi.Model.enums.Modulo;
import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import com.proyectoMaycollins.LlantasApi.Service.PermisosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permisos")
@Tag(name = "🔐 Permisos", description = "Gestión de permisos por Role y módulo (RBAC)")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class PermisosController {

    private final PermisosService permisosService;

    @Operation(
            summary = "Obtener permisos del usuario actual",
            description = "Retorna los permisos del usuario autenticado en formato para el frontend. " +
                         "El frontend usa esto para mostrar/ocultar opciones del menú."
    )
    @GetMapping("/mis-permisos")
    public ResponseEntity<Map<String, Map<String, Boolean>>> obtenerMisPermisos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String rolStr = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(null);

        if (rolStr == null) {
            return ResponseEntity.badRequest().build();
        }

        Role rol = Role.valueOf(rolStr);
        Map<String, Map<String, Boolean>> permisos = permisosService.obtenerPermisosParaFrontend(rol);

        return ResponseEntity.ok(permisos);
    }

    @Operation(
            summary = "Obtener permisos por Role",
            description = "⚠️ ADMIN: Obtiene todos los permisos de un Role específico"
    )
    @GetMapping("/Role/{Role}")
    public ResponseEntity<List<Permisos>> obtenerPermisosPorRol(@PathVariable Role Role) {
        List<Permisos> permisos = permisosService.obtenerPermisosPorRol(Role);
        return ResponseEntity.ok(permisos);
    }

    @Operation(
            summary = "Obtener matriz completa de permisos",
            description = "⚠️ ADMIN: Retorna todos los permisos de todos los roles"
    )
    @GetMapping("/matriz")
    public ResponseEntity<Map<Role, Map<Modulo, List<Accion>>>> obtenerMatrizPermisos() {
        Map<Role, Map<Modulo, List<Accion>>> matriz = permisosService.obtenerMatrizPermisos();
        return ResponseEntity.ok(matriz);
    }

    @Operation(
            summary = "Verificar si tengo un permiso específico",
            description = "Verifica si el usuario actual tiene permiso para una acción en un módulo"
    )
    @GetMapping("/verificar")
    public ResponseEntity<Map<String, Object>> verificarPermiso(
            @RequestParam Modulo modulo,
            @RequestParam Accion accion) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String rolStr = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(null);

        if (rolStr == null) {
            return ResponseEntity.badRequest().build();
        }

        Role rol = Role.valueOf(rolStr);
        boolean tienePermiso = permisosService.tienePermiso(rol, modulo, accion);

        return ResponseEntity.ok(Map.of(
                "rol", rol.name(),
                "modulo", modulo.name(),
                "accion", accion.name(),
                "permitido", tienePermiso
        ));
    }

    @Operation(
            summary = "Inicializar permisos por defecto",
            description = "⚠️ ADMIN: Carga los permisos por defecto en la BD. " +
                         "Ejecutar solo una vez al configurar el sistema."
    )
    @PostMapping("/inicializar")
    public ResponseEntity<Map<String, String>> inicializarPermisos() {
        permisosService.inicializarPermisosPorDefecto();
        return ResponseEntity.ok(Map.of(
                "mensaje", "Permisos inicializados correctamente",
                "accion", "Se han creado los permisos por defecto para todos los roles"
        ));
    }

    @Operation(
            summary = "Actualizar un permiso",
            description = "⚠️ ADMIN: Modifica un permiso específico"
    )
    @PutMapping
    public ResponseEntity<Permisos> actualizarPermiso(
            @RequestParam Role rol,
            @RequestParam Modulo modulo,
            @RequestParam Accion accion,
            @RequestParam boolean permitido,
            @RequestParam(required = false) String descripcion) {

        Permisos permiso = permisosService.guardarPermiso(rol, modulo, accion, permitido, descripcion);
        return ResponseEntity.ok(permiso);
    }
}

