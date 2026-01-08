package com.proyectoMaycollins.LlantasApi.Config.security;

import com.proyectoMaycollins.LlantasApi.Service.PermisosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Aspecto que intercepta métodos anotados con @RequierePermiso
 * y verifica que el usuario tenga el permiso necesario
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermisoAspect {

    private final PermisosService permisosService;

    @Around("@annotation(requierePermiso)")
    public Object verificarPermiso(ProceedingJoinPoint joinPoint, RequierePermiso requierePermiso) throws Throwable {

        boolean tienePermiso = permisosService.tienePermiso(
            requierePermiso.modulo(),
            requierePermiso.accion()
        );

        if (!tienePermiso) {
            log.warn("Acceso denegado al endpoint {} - Módulo: {}, Acción: {}",
                joinPoint.getSignature().getName(),
                requierePermiso.modulo(),
                requierePermiso.accion()
            );

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                    "error", "Acceso denegado",
                    "mensaje", String.format("No tienes permiso para %s en %s",
                        requierePermiso.accion().getDescripcion(),
                        requierePermiso.modulo().getDescripcion())
                ));
        }

        return joinPoint.proceed();
    }
}

