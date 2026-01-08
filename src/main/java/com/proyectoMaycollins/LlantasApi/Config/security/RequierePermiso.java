package com.proyectoMaycollins.LlantasApi.Config.security;

import com.proyectoMaycollins.LlantasApi.Model.enums.Accion;
import com.proyectoMaycollins.LlantasApi.Model.enums.Modulo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para verificar permisos RBAC en endpoints
 *
 * Uso:
 * @RequierePermiso(modulo = Modulo.VENTAS, accion = Accion.CREAR)
 * public ResponseEntity<?> crearVenta(...) { ... }
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequierePermiso {
    Modulo modulo();
    Accion accion();
}

