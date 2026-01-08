package com.proyectoMaycollins.LlantasApi.Model.enums;

import lombok.Getter;

/**
 * Acciones/Permisos que se pueden realizar en cada módulo
 */
@Getter
public enum Accion {
    VER("Ver/Listar"),
    CREAR("Crear/Registrar"),
    EDITAR("Editar/Actualizar"),
    ELIMINAR("Eliminar/Desactivar"),
    EXPORTAR("Exportar datos"),
    IMPRIMIR("Imprimir"),
    APROBAR("Aprobar/Autorizar"),
    RECHAZAR("Rechazar"),
    REACTIVAR("Reactivar"),
    AJUSTAR("Ajustar manualmente"),
    ADMINISTRAR("Administrar completamente");

    private final String descripcion;

    Accion(String descripcion) {
        this.descripcion = descripcion;
    }

}

