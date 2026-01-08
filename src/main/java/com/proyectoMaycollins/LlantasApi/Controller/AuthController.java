package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.DTO.AuthLoginRequest;
import com.proyectoMaycollins.LlantasApi.DTO.AuthRegisterRequest;
import com.proyectoMaycollins.LlantasApi.Model.SesionActiva;
import com.proyectoMaycollins.LlantasApi.Model.Usuarios;
import com.proyectoMaycollins.LlantasApi.Service.AuthService;
import com.proyectoMaycollins.LlantasApi.Service.SesionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "🔐 Autenticación", description = "Endpoints para gestión de autenticación con tokens dinámicos y sesiones")
public class AuthController {

    private final AuthService authService;
    private final SesionService sesionService;

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una nueva cuenta de usuario y retorna tokens JWT dinámicos (Access Token + Refresh Token)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"accessToken\": \"eyJhbG...\", \"refreshToken\": \"eyJhbG...\", \"type\": \"Bearer\", \"expiresIn\": 900000}"))),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRegisterRequest req, HttpServletRequest request) {
        try {
            Usuarios u = authService.register(req.getEmail(), req.getNombre(), req.getPassword(), req.getRol());
            Map<String, Object> claims = (u.getRol() != null) ? Map.of("rol", u.getRol().name()) : Map.of();

            // Crear sesión con tokens dinámicos
            Map<String, Object> resultado = sesionService.crearSesion(u, request, claims);

            return ResponseEntity.ok(Map.of(
                    "accessToken", resultado.get("accessToken"),
                    "refreshToken", resultado.get("refreshToken"),
                    "type", "Bearer",
                    "expiresIn", 900000, // 15 minutos en milisegundos
                    "usuario", Map.of(
                            "id", u.getId(),
                            "email", u.getEmail(),
                            "nombre", u.getNombre(),
                            "rol", u.getRol() != null ? u.getRol().name() : "USER"
                    )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y retorna tokens JWT dinámicos únicos. Access Token válido por 15 minutos, Refresh Token válido por 7 días"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"accessToken\": \"eyJhbG...\", \"refreshToken\": \"eyJhbG...\", \"type\": \"Bearer\"}"))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "423", description = "Usuario bloqueado por múltiples intentos fallidos")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthLoginRequest req, HttpServletRequest request) {
        String ipAddress = obtenerIpCliente(request);

        // Verificar si el usuario está bloqueado
        if (sesionService.estaBloqueado(req.getEmail())) {
            return ResponseEntity.status(423).body(Map.of(
                    "error", "Usuario bloqueado temporalmente por múltiples intentos fallidos",
                    "mensaje", "Por favor intente nuevamente en 15 minutos"
            ));
        }

        return authService.validateUser(req.getEmail(), req.getPassword())
                .map(u -> {
                    // Validar que el usuario esté activo
                    if (!Boolean.TRUE.equals(u.getActivo())) {
                        sesionService.registrarIntentoFallido(req.getEmail(), ipAddress);
                        return ResponseEntity.status(401).body(Map.of("error", "Usuario inactivo"));
                    }

                    // Limpiar intentos fallidos previos
                    sesionService.limpiarIntentosFallidos(req.getEmail());

                    // Crear sesión con tokens dinámicos únicos
                    Map<String, Object> claims = (u.getRol() != null) ? Map.of("rol", u.getRol().name()) : Map.of();
                    Map<String, Object> resultado = sesionService.crearSesion(u, request, claims);

                    return ResponseEntity.ok(Map.of(
                            "accessToken", resultado.get("accessToken"),
                            "refreshToken", resultado.get("refreshToken"),
                            "type", "Bearer",
                            "expiresIn", 900000, // 15 minutos
                            "usuario", Map.of(
                                    "id", u.getId(),
                                    "email", u.getEmail(),
                                    "nombre", u.getNombre(),
                                    "rol", u.getRol() != null ? u.getRol().name() : "USER"
                            )
                    ));
                })
                .orElseGet(() -> {
                    // Registrar intento fallido
                    sesionService.registrarIntentoFallido(req.getEmail(), ipAddress);
                    return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
                });
    }

    @Operation(
            summary = "Refrescar tokens (ROTACIÓN COMPLETA)",
            description = "Genera un NUEVO Access Token Y un NUEVO Refresh Token. AMBOS tokens anteriores se invalidan inmediatamente. " +
                         "⚠️ IMPORTANTE: El frontend DEBE guardar el nuevo Refresh Token para la próxima renovación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens rotados exitosamente - Guardar AMBOS tokens nuevos"),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "Refresh token requerido"));
        }

        return sesionService.refreshToken(refreshToken)
                .map(tokens -> ResponseEntity.ok(Map.of(
                        "accessToken", tokens.get("accessToken"),
                        "refreshToken", tokens.get("refreshToken"), // ✨ NUEVO Refresh Token
                        "type", tokens.get("type"),
                        "rotated", true, // Indicador de que se rotaron ambos tokens
                        "mensaje", "⚠️ Guardar el nuevo Refresh Token. El anterior ya no es válido."
                )))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Refresh token inválido o expirado")));
    }

    @Operation(
            summary = "Cerrar sesión",
            description = "Revoca el token actual e invalida la sesión. El token se agrega a la blacklist"
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = authentication.getName();

            // Buscar usuario para obtener ID
            Usuarios usuario = authService.findByEmail(email).orElse(null);
            if (usuario != null) {
                sesionService.cerrarSesion(token, usuario.getId(), email);
                return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada exitosamente"));
            }
        }

        return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada"));
    }

    @Operation(
            summary = "Cerrar todas las sesiones",
            description = "Revoca todas las sesiones activas del usuario autenticado. Útil en caso de sospecha de compromiso de cuenta"
    )
    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        String email = authentication.getName();
        Usuarios usuario = authService.findByEmail(email).orElse(null);

        if (usuario != null) {
            sesionService.cerrarTodasLasSesiones(usuario.getId(), "Logout all solicitado por usuario");
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Todas las sesiones han sido cerradas",
                    "sesionesRevocadas", sesionService.obtenerHistorialSesiones(usuario.getId()).size()
            ));
        }

        return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
    }

    @Operation(
            summary = "Obtener sesiones activas",
            description = "Retorna todas las sesiones activas del usuario autenticado con información de dispositivo e IP"
    )
    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        String email = authentication.getName();
        Usuarios usuario = authService.findByEmail(email).orElse(null);

        if (usuario != null) {
            return ResponseEntity.ok(Map.of(
                    "sesionesActivas", sesionService.obtenerSesionesActivas(usuario.getId()),
                    "total", sesionService.obtenerSesionesActivas(usuario.getId()).size()
            ));
        }

        return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
    }

    @Operation(
            summary = "Validar token",
            description = "Verifica si el token actual es válido y no ha sido revocado"
    )
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean valido = sesionService.validarToken(token);

            if (valido) {
                return ResponseEntity.ok(Map.of("valido", true, "mensaje", "Token válido"));
            }
        }

        return ResponseEntity.status(401).body(Map.of("valido", false, "mensaje", "Token inválido o expirado"));
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

