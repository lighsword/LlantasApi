package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.AuditoriaAcceso;
import com.proyectoMaycollins.LlantasApi.Repository.AuditoriaAccesoRepository;
import com.proyectoMaycollins.LlantasApi.Repository.IntentoLoginFallidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio de alertas automáticas para detección de anomalías de seguridad
 * Detecta:
 * - Múltiples intentos de login fallidos
 * - Acceso desde múltiples IPs en poco tiempo
 * - Patrones de uso sospechosos
 * - Intentos de acceso no autorizado
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertasSeguridadService {

    private final AuditoriaAccesoRepository auditoriaRepository;
    private final IntentoLoginFallidoRepository intentosRepository;

    // Umbrales de alerta
    private static final int UMBRAL_INTENTOS_FALLIDOS_POR_IP = 10;
    private static final int UMBRAL_INTENTOS_FALLIDOS_POR_EMAIL = 5;
    private static final int UMBRAL_IPS_DIFERENTES_POR_USUARIO = 3;
    private static final int UMBRAL_ACCESOS_DENEGADOS = 5;
    private static final int MINUTOS_VENTANA_ANALISIS = 15;

    /**
     * Análisis programado cada 5 minutos
     */
    @Scheduled(fixedRate = 300000)
    public void analizarPatronesSeguridad() {
        LocalDateTime desde = LocalDateTime.now().minusMinutes(MINUTOS_VENTANA_ANALISIS);

        try {
            detectarAtaqueBrutaFuerzaPorIp(desde);
            detectarAtaqueBrutaFuerzaPorEmail(desde);
            detectarAccesoMultiplesIps(desde);
            detectarAccesosDenegadosExcesivos(desde);
        } catch (Exception e) {
            log.error("Error en análisis de patrones de seguridad: {}", e.getMessage());
        }
    }

    private void detectarAtaqueBrutaFuerzaPorIp(LocalDateTime desde) {
        try {
            List<Object[]> intentosPorIp = intentosRepository.contarIntentosPorIpDesde(desde);

            for (Object[] resultado : intentosPorIp) {
                String ip = (String) resultado[0];
                Long cantidad = (Long) resultado[1];

                if (cantidad >= UMBRAL_INTENTOS_FALLIDOS_POR_IP) {
                    generarAlerta(
                        TipoAlerta.ATAQUE_FUERZA_BRUTA_IP,
                        String.format("Posible ataque de fuerza bruta desde IP %s: %d intentos", ip, cantidad),
                        SeveridadAlerta.ALTA
                    );
                }
            }
        } catch (Exception e) {
            log.debug("No se pudo analizar ataques por IP: {}", e.getMessage());
        }
    }

    private void detectarAtaqueBrutaFuerzaPorEmail(LocalDateTime desde) {
        try {
            List<Object[]> intentosPorEmail = intentosRepository.contarIntentosPorEmailDesde(desde);

            for (Object[] resultado : intentosPorEmail) {
                String email = (String) resultado[0];
                Long cantidad = (Long) resultado[1];

                if (cantidad >= UMBRAL_INTENTOS_FALLIDOS_POR_EMAIL) {
                    generarAlerta(
                        TipoAlerta.CUENTA_BAJO_ATAQUE,
                        String.format("Cuenta %s bajo posible ataque: %d intentos fallidos", email, cantidad),
                        SeveridadAlerta.ALTA
                    );
                }
            }
        } catch (Exception e) {
            log.debug("No se pudo analizar ataques por email: {}", e.getMessage());
        }
    }

    private void detectarAccesoMultiplesIps(LocalDateTime desde) {
        try {
            List<Object[]> loginsPorUsuario = auditoriaRepository.contarIpsPorUsuarioDesde(desde);

            for (Object[] resultado : loginsPorUsuario) {
                Long usuarioId = (Long) resultado[0];
                Long cantidadIps = (Long) resultado[1];

                if (cantidadIps >= UMBRAL_IPS_DIFERENTES_POR_USUARIO) {
                    generarAlerta(
                        TipoAlerta.ACCESO_MULTIPLES_IPS,
                        String.format("Usuario ID %d accedió desde %d IPs diferentes", usuarioId, cantidadIps),
                        SeveridadAlerta.MEDIA
                    );
                }
            }
        } catch (Exception e) {
            log.debug("No se pudo analizar accesos múltiples IPs: {}", e.getMessage());
        }
    }

    private void detectarAccesosDenegadosExcesivos(LocalDateTime desde) {
        try {
            List<Object[]> denegadosPorUsuario = auditoriaRepository.contarAccesosDenegadosPorUsuarioDesde(desde);

            for (Object[] resultado : denegadosPorUsuario) {
                Long usuarioId = (Long) resultado[0];
                Long cantidad = (Long) resultado[1];

                if (cantidad >= UMBRAL_ACCESOS_DENEGADOS) {
                    generarAlerta(
                        TipoAlerta.INTENTO_ESCALADA_PRIVILEGIOS,
                        String.format("Usuario ID %d tiene %d accesos denegados - posible escalada", usuarioId, cantidad),
                        SeveridadAlerta.ALTA
                    );
                }
            }
        } catch (Exception e) {
            log.debug("No se pudo analizar accesos denegados: {}", e.getMessage());
        }
    }

    private void generarAlerta(TipoAlerta tipo, String mensaje, SeveridadAlerta severidad) {
        switch (severidad) {
            case CRITICA, ALTA -> log.error("🚨 ALERTA DE SEGURIDAD [{}]: {}", tipo, mensaje);
            case MEDIA -> log.warn("⚠️ ALERTA DE SEGURIDAD [{}]: {}", tipo, mensaje);
            case BAJA -> log.info("ℹ️ ALERTA DE SEGURIDAD [{}]: {}", tipo, mensaje);
        }

        try {
            AuditoriaAcceso auditoria = AuditoriaAcceso.builder()
                    .accion("SECURITY_ALERT_" + tipo.name())
                    .mensaje(mensaje)
                    .exitoso(false)
                    .fechaHora(LocalDateTime.now())
                    .build();
            auditoriaRepository.save(auditoria);
        } catch (Exception e) {
            log.error("Error guardando alerta en auditoría: {}", e.getMessage());
        }
    }

    public boolean esIpSospechosa(String ip) {
        try {
            LocalDateTime desde = LocalDateTime.now().minusMinutes(MINUTOS_VENTANA_ANALISIS);
            long intentos = intentosRepository.contarIntentosPorIp(ip, desde);
            return intentos >= UMBRAL_INTENTOS_FALLIDOS_POR_IP / 2;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> obtenerResumenSeguridad() {
        LocalDateTime desde = LocalDateTime.now().minusHours(24);
        Map<String, Object> resumen = new HashMap<>();

        try {
            resumen.put("intentosLoginFallidos", intentosRepository.contarIntentosDesde(desde));
            resumen.put("accesosDenegados", auditoriaRepository.contarPorAccionDesde("ACCESS_DENIED", desde));
            resumen.put("loginsExitosos", auditoriaRepository.contarPorAccionDesde("LOGIN", desde));
            resumen.put("ipsUnicas", auditoriaRepository.contarIpsUnicasDesde(desde));
        } catch (Exception e) {
            log.error("Error obteniendo resumen de seguridad: {}", e.getMessage());
            resumen.put("error", "No se pudo obtener el resumen completo");
        }

        resumen.put("ultimaActualizacion", LocalDateTime.now());
        return resumen;
    }

    public enum TipoAlerta {
        ATAQUE_FUERZA_BRUTA_IP,
        CUENTA_BAJO_ATAQUE,
        ACCESO_MULTIPLES_IPS,
        INTENTO_ESCALADA_PRIVILEGIOS,
        TOKEN_REVOCADO_USADO,
        SESION_SECUESTRADA,
        ACTIVIDAD_ANOMALA
    }

    public enum SeveridadAlerta {
        BAJA, MEDIA, ALTA, CRITICA
    }
}

