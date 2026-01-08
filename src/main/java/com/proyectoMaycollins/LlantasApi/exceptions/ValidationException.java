package com.proyectoMaycollins.LlantasApi.exceptions;

/**
 * Excepción personalizada para validaciones de negocio
 * Se lanza cuando los datos no cumplen con las reglas de negocio
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructor con mensaje
     * @param mensaje Descripción del error de validación
     */
    public ValidationException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error de validación
     * @param causa Excepción que causó este error
     */
    public ValidationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

