package com.proyectoMaycollins.LlantasApi.exceptions;

/**
 * Excepción personalizada para indicar que no hay stock suficiente
 * Se lanza cuando se intenta vender un producto sin stock disponible
 */
public class StockInsuficienteException extends RuntimeException {

    /**
     * Constructor con mensaje
     * @param mensaje Descripción del error
     */
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error
     */
    public StockInsuficienteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

