package com.proyectoMaycollins.LlantasApi.Controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador TEMPORAL para encriptar contraseñas
 * ELIMINAR en producción
 */
@Hidden
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordEncryptController {

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/encrypt-password")
    public Map<String, String> encryptPassword(@RequestBody Map<String, String> request) {
        String plainPassword = request.get("password");
        String encryptedPassword = passwordEncoder.encode(plainPassword);

        return Map.of(
            "plainPassword", plainPassword,
            "encryptedPassword", encryptedPassword,
            "info", "Usa este hash encriptado en la base de datos"
        );
    }

    @PostMapping("/verify-password")
    public Map<String, Object> verifyPassword(@RequestBody Map<String, String> request) {
        String plainPassword = request.get("password");
        String hashedPassword = request.get("hash");

        boolean matches = passwordEncoder.matches(plainPassword, hashedPassword);

        return Map.of(
            "plainPassword", plainPassword,
            "hashedPassword", hashedPassword,
            "matches", matches,
            "message", matches ? "✅ La contraseña coincide" : "❌ La contraseña NO coincide"
        );
    }
}

