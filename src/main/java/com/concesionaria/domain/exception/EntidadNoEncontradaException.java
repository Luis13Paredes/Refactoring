package com.concesionaria.domain.exception;

/**
 * Excepción lanzada cuando una entidad solicitada no existe en el sistema.
 */
public class EntidadNoEncontradaException extends ReglaNegocioException {

    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
