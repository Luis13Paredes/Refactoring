package com.concesionaria.domain.exception;

/**
 * Excepción para violaciones de reglas e invariantes del negocio.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }

    public ReglaNegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
