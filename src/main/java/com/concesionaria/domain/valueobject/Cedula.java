package com.concesionaria.domain.valueobject;

import com.concesionaria.domain.exception.ReglaNegocioException;
import java.util.Objects;

/**
 * Value Object inmutable que encapsula y valida la Cédula de Identidad (10 dígitos).
 */
public final class Cedula {

    private final String _valor;

    public Cedula(String valor) {
        if (valor == null || valor.trim().isBlank()) {
            throw new ReglaNegocioException("La cédula no puede estar vacía.");
        }
        String valorLimpio = valor.trim();
        if (valorLimpio.length() != 10) {
            throw new ReglaNegocioException("La cédula debe tener exactamente 10 dígitos.");
        }
        if (!valorLimpio.matches("\\d{10}")) {
            throw new ReglaNegocioException("La cédula debe contener únicamente dígitos numéricos.");
        }
        this._valor = valorLimpio;
    }

    public String getValor() {
        return _valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cedula cedula = (Cedula) o;
        return Objects.equals(_valor, cedula._valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_valor);
    }

    @Override
    public String toString() {
        return _valor;
    }
}
