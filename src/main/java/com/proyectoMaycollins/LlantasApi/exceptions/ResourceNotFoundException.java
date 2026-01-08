package com.proyectoMaycollins.LlantasApi.exceptions;

/**
 * Excepción personalizada para indicar que un recurso no fue encontrado
 * Se lanza cuando se busca una entidad por ID y no existe
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje
     * @param mensaje Descripción del error
     */
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error
     */
    public ResourceNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor con nombre del recurso e ID
     * @param nombreRecurso Nombre de la entidad (ej: "Producto", "Cliente")
     * @param id ID del recurso no encontrado
     */
    public ResourceNotFoundException(String nombreRecurso, Long id) {
        super(String.format("%s no encontrado con ID: %d", nombreRecurso, id));
    }
}

