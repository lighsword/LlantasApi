package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.Model.AuditoriaAcceso;
import com.proyectoMaycollins.LlantasApi.Repository.AuditoriaAccesoRepository;
import com.proyectoMaycollins.LlantasApi.Service.AlertasSeguridadService;
import com.proyectoMaycollins.LlantasApi.Service.SesionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller para monitoreo de seguridad y auditoría
 * Solo accesible por ADMIN
 */
@RestController
@RequestMapping("/api/seguridad")
@Tag(name = "🛡️ Seguridad", description = "Monitoreo de seguridad, alertas y auditoría del sistema")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class SeguridadController {

    private final AlertasSeguridadService alertasService;
    private final SesionService sesionService;
    private final AuditoriaAccesoRepository auditoriaRepository;

    // ==================== RESUMEN Y DASHBOARD ====================

    @Operation(
            summary = "Obtener resumen de seguridad",
            description = "⚠️ ADMIN: Retorna métricas de seguridad de las últimas 24 horas"
    )
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumenSeguridad() {
        Map<String, Object> resumen = alertasService.obtenerResumenSeguridad();
        return ResponseEntity.ok(resumen);
    }

    @Operation(
            summary = "Verificar si una IP es sospechosa",
            description = "⚠️ ADMIN: Verifica si una IP tiene múltiples intentos fallidos"
    )
    @GetMapping("/ip-sospechosa")
    public ResponseEntity<Map<String, Object>> verificarIpSospechosa(@RequestParam String ip) {
        boolean sospechosa = alertasService.esIpSospechosa(ip);
        return ResponseEntity.ok(Map.of(
                "ip", ip,
                "sospechosa", sospechosa,
                "mensaje", sospechosa ? "Esta IP tiene actividad sospechosa" : "IP sin actividad sospechosa"
        ));
    }

    // ==================== AUDITORÍA ====================

    @Operation(
            summary = "Obtener auditoría de un usuario",
            description = "⚠️ ADMIN: Retorna el historial de accesos de un usuario específico"
    )
    @GetMapping("/auditoria/usuario/{usuarioId}")
    public ResponseEntity<List<AuditoriaAcceso>> obtenerAuditoriaUsuario(@PathVariable Long usuarioId) {
        List<AuditoriaAcceso> auditoria = auditoriaRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
        return ResponseEntity.ok(auditoria);
    }

    @Operation(
            summary = "Obtener auditoría por rango de fechas",
            description = "⚠️ ADMIN: Retorna auditoría entre dos fechas"
    )
    @GetMapping("/auditoria/rango")
    public ResponseEntity<List<AuditoriaAcceso>> obtenerAuditoriaPorRango(
            @RequestParam Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        List<AuditoriaAcceso> auditoria = auditoriaRepository.findByUsuarioIdAndFechaHoraBetween(usuarioId, desde, hasta);
        return ResponseEntity.ok(auditoria);
    }

    @Operation(
            summary = "Obtener auditoría por IP",
            description = "⚠️ ADMIN: Retorna todos los accesos desde una IP específica"
    )
    @GetMapping("/auditoria/ip/{ip}")
    public ResponseEntity<List<AuditoriaAcceso>> obtenerAuditoriaPorIp(@PathVariable String ip) {
        List<AuditoriaAcceso> auditoria = auditoriaRepository.findByIpAddressOrderByFechaHoraDesc(ip);
        return ResponseEntity.ok(auditoria);
    }

    @Operation(
            summary = "Obtener últimos accesos por acción",
            description = "⚠️ ADMIN: Filtra auditoría por tipo de acción (LOGIN, LOGOUT, ACCESS_DENIED, etc.)"
    )
    @GetMapping("/auditoria/accion/{accion}")
    public ResponseEntity<List<AuditoriaAcceso>> obtenerAuditoriaPorAccion(
            @PathVariable String accion,
            @RequestParam(defaultValue = "24") int horasAtras) {
        LocalDateTime desde = LocalDateTime.now().minusHours(horasAtras);
        List<AuditoriaAcceso> auditoria = auditoriaRepository.findByAccionAndFechaHoraAfter(accion, desde);
        return ResponseEntity.ok(auditoria);
    }

    // ==================== SESIONES ====================

    @Operation(
            summary = "Obtener sesiones activas de un usuario",
            description = "⚠️ ADMIN: Lista todas las sesiones activas de un usuario"
    )
    @GetMapping("/sesiones/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerSesionesUsuario(@PathVariable Long usuarioId) {
        var sesiones = sesionService.obtenerSesionesActivas(usuarioId);
        return ResponseEntity.ok(Map.of(
                "usuarioId", usuarioId,
                "sesionesActivas", sesiones.size(),
                "sesiones", sesiones
        ));
    }

    @Operation(
            summary = "Cerrar todas las sesiones de un usuario",
            description = "⚠️ ADMIN: Revoca todas las sesiones activas de un usuario (forzar logout)"
    )
    @PostMapping("/sesiones/cerrar-todas/{usuarioId}")
    public ResponseEntity<Map<String, String>> cerrarTodasLasSesiones(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "Cerrado por administrador") String motivo) {
        sesionService.cerrarTodasLasSesiones(usuarioId, motivo);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Todas las sesiones han sido cerradas",
                "usuarioId", usuarioId.toString(),
                "motivo", motivo
        ));
    }

    // ==================== ANÁLISIS MANUAL ====================

    @Operation(
            summary = "Ejecutar análisis de seguridad manual",
            description = "⚠️ ADMIN: Ejecuta el análisis de patrones de seguridad inmediatamente"
    )
    @PostMapping("/analizar")
    public ResponseEntity<Map<String, String>> ejecutarAnalisisSeguridad() {
        alertasService.analizarPatronesSeguridad();
        return ResponseEntity.ok(Map.of(
                "mensaje", "Análisis de seguridad ejecutado",
                "timestamp", LocalDateTime.now().toString(),
                "nota", "Revise los logs para ver las alertas detectadas"
        ));
    }

    // ==================== ESTADÍSTICAS ====================

    @Operation(
            summary = "Obtener estadísticas de accesos",
            description = "⚠️ ADMIN: Estadísticas de logins, logouts y accesos denegados"
    )
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(
            @RequestParam(defaultValue = "24") int horasAtras) {
        LocalDateTime desde = LocalDateTime.now().minusHours(horasAtras);

        long logins = auditoriaRepository.contarPorAccionDesde("LOGIN", desde);
        long logouts = auditoriaRepository.contarPorAccionDesde("LOGOUT", desde);
        long denegados = auditoriaRepository.contarPorAccionDesde("ACCESS_DENIED", desde);
        long alertas = auditoriaRepository.contarPorAccionDesde("SECURITY_ALERT", desde);
        long ipsUnicas = auditoriaRepository.contarIpsUnicasDesde(desde);

        return ResponseEntity.ok(Map.of(
                "periodo", String.format("Últimas %d horas", horasAtras),
                "desde", desde.toString(),
                "hasta", LocalDateTime.now().toString(),
                "logins", logins,
                "logouts", logouts,
                "accesosDenegados", denegados,
                "alertasSeguridad", alertas,
                "ipsUnicas", ipsUnicas,
                "sesionesEstimadas", logins - logouts
        ));
    }
}

