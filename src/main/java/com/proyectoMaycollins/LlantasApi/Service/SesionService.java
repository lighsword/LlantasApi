package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Config.jwt.JwtService;
import com.proyectoMaycollins.LlantasApi.Model.*;
import com.proyectoMaycollins.LlantasApi.Repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SesionService {

    private final SesionActivaRepository sesionActivaRepository;
    private final TokenRevocadoRepository tokenRevocadoRepository;
    private final AuditoriaAccesoRepository auditoriaAccesoRepository;
    private final IntentoLoginFallidoRepository intentoLoginFallidoRepository;
    private final JwtService jwtService;

    private static final int MAX_SESIONES_SIMULTANEAS = 3;
    private static final int MAX_INTENTOS_LOGIN = 5;
    private static final int MINUTOS_BLOQUEO = 15;

    /**
     * Crea una nueva sesión con tokens dinámicos únicos
     * @return Map con "sesion", "accessToken" y "refreshToken"
     */
    @Transactional
    public Map<String, Object> crearSesion(Usuarios usuario, HttpServletRequest request, Map<String, Object> claims) {
        // Generar tokens únicos
        String accessToken = jwtService.generateAccessToken(usuario.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(usuario.getEmail());

        // Extraer JTI de los tokens
        String jtiAccess = jwtService.extractJti(accessToken);
        String jtiRefresh = jwtService.extractJti(refreshToken);

        // Obtener información del request
        String ipAddress = obtenerIpCliente(request);
        String userAgent = request.getHeader("User-Agent");

        // Verificar límite de sesiones simultáneas
        long sesionesActivas = sesionActivaRepository.contarSesionesActivasPorUsuario(usuario.getId());
        if (sesionesActivas >= MAX_SESIONES_SIMULTANEAS) {
            // Revocar la sesión más antigua
            List<SesionActiva> sesiones = sesionActivaRepository.findByUsuarioIdAndActivoTrue(usuario.getId());
            if (!sesiones.isEmpty()) {
                SesionActiva sesionAntigua = sesiones.getLast();
                revocarSesion(sesionAntigua.getJtiAccess());
            }
        }

        // Crear nueva sesión
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime expiracionAccess = ahora.plusSeconds(jwtService.getAccessTokenExpirationMs() / 1000);

        SesionActiva sesion = SesionActiva.builder()
                .usuarioId(usuario.getId())
                .refreshToken(refreshToken)
                .jtiAccess(jtiAccess)
                .jtiRefresh(jtiRefresh)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .fechaCreacion(ahora)
                .fechaExpiracion(expiracionAccess)
                .activo(true)
                .build();

        sesion = sesionActivaRepository.save(sesion);

        // Registrar en auditoría
        registrarAuditoria(usuario.getId(), "LOGIN");

        log.info("Sesión creada para usuario: {} desde IP: {}", usuario.getEmail(), ipAddress);

        // Retornar Map con tokens y sesión
        return Map.of(
                "sesion", sesion,
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    /**
     * Obtiene el access token desde el JTI almacenado
     */
    public String getAccessTokenFromSession(SesionActiva sesion) {
        // El token se regenera usando el JTI, pero para simplificar retornamos el refresh
        // En una implementación real, podrías almacenar el access_token completo
        return sesion.getRefreshToken(); // Temporal - ajustar según lógica de negocio
    }

    /**
     * Valida si un token está activo y no ha sido revocado
     */
    public boolean validarToken(String token) {
        // Extraer JTI del token
        String jti = jwtService.extractJti(token);
        if (jti == null) {
            return false;
        }

        // Verificar si el JTI está en la blacklist
        if (tokenRevocadoRepository.existsByJti(jti)) {
            log.warn("Intento de uso de token revocado (JTI: {})", jti);
            return false;
        }

        // Verificar si existe sesión activa con ese JTI
        Optional<SesionActiva> sesion = sesionActivaRepository.findByJtiAccessAndActivoTrue(jti);
        if (sesion.isEmpty()) {
            return false;
        }

        // Verificar si la sesión ha expirado
        if (sesion.get().estaExpirada()) {
            log.warn("Token expirado para usuario: {}", sesion.get().getUsuarioId());
            return false;
        }

        return true;
    }

    /**
     * Refresca el access token usando el refresh token
     */
    @Transactional
    public Optional<Map<String, String>> refreshToken(String refreshToken) {
        // Buscar sesión con el refresh token
        Optional<SesionActiva> sesionOpt = sesionActivaRepository.findByRefreshTokenAndActivoTrue(refreshToken);

        if (sesionOpt.isEmpty()) {
            log.warn("Refresh token inválido o revocado");
            return Optional.empty();
        }

        SesionActiva sesion = sesionOpt.get();

        // Verificar si el refresh token ha expirado
        try {
            if (jwtService.isTokenExpired(refreshToken)) {
                log.warn("Refresh token expirado para usuario: {}", sesion.getUsuarioId());
                revocarSesion(sesion.getJtiAccess());
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error validando refresh token", e);
            return Optional.empty();
        }

        // Revocar token anterior
        agregarTokenABlacklist(refreshToken, sesion.getUsuarioId(), "Rotación de tokens");

        // Generar nuevos tokens
        String email = jwtService.extractSubject(refreshToken);
        Map<String, Object> claims = Map.of("rol", "USER");
        String nuevoAccessToken = jwtService.generateAccessToken(email, claims);
        String nuevoRefreshToken = jwtService.generateRefreshToken(email);

        // Extraer nuevos JTIs
        String nuevoJtiAccess = jwtService.extractJti(nuevoAccessToken);
        String nuevoJtiRefresh = jwtService.extractJti(nuevoRefreshToken);

        // Actualizar sesión
        sesion.setJtiAccess(nuevoJtiAccess);
        sesion.setJtiRefresh(nuevoJtiRefresh);
        sesion.setRefreshToken(nuevoRefreshToken);
        LocalDateTime nuevaExpiracion = LocalDateTime.now().plusSeconds(jwtService.getAccessTokenExpirationMs() / 1000);
        sesion.setFechaExpiracion(nuevaExpiracion);
        sesionActivaRepository.save(sesion);

        registrarAuditoria(sesion.getUsuarioId(), "REFRESH_TOKEN_ROTATION");

        log.info("Tokens renovados para usuario: {}", email);

        return Optional.of(Map.of(
                "accessToken", nuevoAccessToken,
                "refreshToken", nuevoRefreshToken,
                "type", "Bearer"
        ));
    }

    /**
     * Cierra sesión revocando el token
     */
    @Transactional
    public void cerrarSesion(String token, Long usuarioId, String email) {
        String jti = jwtService.extractJti(token);
        if (jti != null) {
            revocarSesion(jti);
        }
        agregarTokenABlacklist(token, usuarioId, "Logout por usuario");
        registrarAuditoria(usuarioId, "LOGOUT");
        log.info("Sesión cerrada para usuario: {}", email);
    }

    /**
     * Revoca todas las sesiones de un usuario
     */
    @Transactional
    public void cerrarTodasLasSesiones(Long usuarioId, String motivo) {
        List<SesionActiva> sesiones = sesionActivaRepository.findByUsuarioIdAndActivoTrue(usuarioId);

        for (SesionActiva sesion : sesiones) {
            agregarTokenABlacklist(sesion.getRefreshToken(), usuarioId, motivo);
        }

        sesionActivaRepository.revocarTodasLasSesionesPorUsuario(usuarioId);
        log.info("Todas las sesiones revocadas para usuario ID: {} - Motivo: {}", usuarioId, motivo);
    }

    /**
     * Registra un intento de login fallido
     */
    @Transactional
    public void registrarIntentoFallido(String email, String ipAddress) {
        IntentoLoginFallido intento = IntentoLoginFallido.builder()
                .email(email)
                .ipAddress(ipAddress)
                .fechaHora(LocalDateTime.now())
                .build();

        LocalDateTime hace15Minutos = LocalDateTime.now().minusMinutes(MINUTOS_BLOQUEO);
        long intentos = intentoLoginFallidoRepository.contarIntentosPorEmail(email, hace15Minutos);

        if (intentos >= MAX_INTENTOS_LOGIN - 1) {
            intento.setBloqueadoHasta(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
            log.warn("Usuario bloqueado por múltiples intentos fallidos: {}", email);
        }

        intentoLoginFallidoRepository.save(intento);
        registrarAuditoria(null, "LOGIN_FAILED");
    }

    /**
     * Verifica si un usuario está bloqueado
     */
    public boolean estaBloqueado(String email) {
        IntentoLoginFallido bloqueo = intentoLoginFallidoRepository.findBloqueoPorEmail(email, LocalDateTime.now());
        return bloqueo != null && bloqueo.estaBloqueado();
    }

    /**
     * Limpia intentos fallidos después de login exitoso
     */
    @Transactional
    public void limpiarIntentosFallidos(String email) {
        intentoLoginFallidoRepository.limpiarIntentosPorEmail(email);
    }

    /**
     * Registra en auditoría (versión simple)
     */
    @Transactional
    public void registrarAuditoria(Long usuarioId, String accion) {
        registrarAuditoria(usuarioId, accion, null, null, null, null, true, null);
    }

    /**
     * Registra en auditoría con información completa
     */
    @Transactional
    public void registrarAuditoria(Long usuarioId, String accion, String ipAddress,
            String userAgent, String endpoint, String metodoHttp, boolean exitoso, String mensaje) {
        AuditoriaAcceso auditoria = AuditoriaAcceso.builder()
                .usuarioId(usuarioId)
                .accion(accion)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .endpoint(endpoint)
                .metodoHttp(metodoHttp)
                .exitoso(exitoso)
                .mensaje(mensaje)
                .fechaHora(LocalDateTime.now())
                .build();
        auditoriaAccesoRepository.save(auditoria);
    }

    /**
     * Registra en auditoría desde un request HTTP
     */
    @Transactional
    public void registrarAuditoriaDesdeRequest(Long usuarioId, String accion,
            HttpServletRequest request, boolean exitoso, String mensaje) {
        String ipAddress = obtenerIpCliente(request);
        String userAgent = request.getHeader("User-Agent");
        String endpoint = request.getRequestURI();
        String metodoHttp = request.getMethod();

        registrarAuditoria(usuarioId, accion, ipAddress, userAgent, endpoint, metodoHttp, exitoso, mensaje);
    }

    // ==================== MÉTODOS PRIVADOS ====================

    private void revocarSesion(String jtiAccess) {
        sesionActivaRepository.revocarSesionPorJti(jtiAccess);
    }

    private void agregarTokenABlacklist(String token, Long usuarioId, String motivo) {
        try {
            // Extraer JTI del token
            String jti = jwtService.extractJti(token);
            if (jti == null) {
                log.warn("No se pudo extraer JTI del token para agregar a blacklist");
                return;
            }

            Date expiracion = jwtService.getExpirationDate(token);
            LocalDateTime fechaExpiracion = expiracion.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            TokenRevocado tokenRevocado = TokenRevocado.builder()
                    .jti(jti)
                    .usuarioId(usuarioId)
                    .motivo(motivo)
                    .fechaExpiracion(fechaExpiracion)
                    .build();

            tokenRevocadoRepository.save(tokenRevocado);
        } catch (Exception e) {
            log.error("Error agregando token a blacklist", e);
        }
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

    private String detectarDispositivo(String userAgent) {
        if (userAgent == null) return "Desconocido";
        if (userAgent.contains("Mobile")) return "Móvil";
        if (userAgent.contains("Tablet")) return "Tablet";
        return "Escritorio";
    }

    // ==================== TAREAS PROGRAMADAS ====================

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void limpiarSesionesExpiradas() {
        LocalDateTime hace1Dia = LocalDateTime.now().minusDays(1);
        sesionActivaRepository.eliminarSesionesExpiradas(hace1Dia);
        log.info("Sesiones expiradas eliminadas");
    }

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void limpiarTokensRevocadosExpirados() {
        LocalDateTime ahora = LocalDateTime.now();
        tokenRevocadoRepository.eliminarTokensExpirados(ahora);
        log.info("Tokens revocados expirados eliminados");
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void limpiarIntentosAntiguos() {
        LocalDateTime hace7Dias = LocalDateTime.now().minusDays(7);
        intentoLoginFallidoRepository.eliminarIntentosAntiguos(hace7Dias);
        log.info("Intentos de login antiguos eliminados");
    }

    /**
     * Obtener sesiones activas de un usuario
     */
    public List<SesionActiva> obtenerSesionesActivas(Long usuarioId) {
        return sesionActivaRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }

    /**
     * Obtener historial de sesiones
     */
    public List<SesionActiva> obtenerHistorialSesiones(Long usuarioId) {
        return sesionActivaRepository.obtenerHistorialSesionesPorUsuario(usuarioId);
    }

    /**
     * Obtener auditoría de un usuario
     */
    public List<AuditoriaAcceso> obtenerAuditoriaUsuario(Long usuarioId) {
        return auditoriaAccesoRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
    }
}


