package com.concesionaria.domain.valueobject;

import com.concesionaria.domain.exception.ReglaNegocioException;
import java.util.Objects;

/**
 * Value Object inmutable que encapsula y valida el Teléfono (10 dígitos numéricos).
 */
public final class Telefono {

    private final String _valor;

    public Telefono(String valor) {
        if (valor == null || valor.trim().isBlank()) {
            throw new ReglaNegocioException("El teléfono no puede estar vacío.");
        }
        String valorLimpio = valor.trim();
        if (valorLimpio.length() != 10) {
            throw new ReglaNegocioException("El teléfono debe tener exactamente 10 dígitos.");
        }
        if (!valorLimpio.matches("\\d{10}")) {
            throw new ReglaNegocioException("El teléfono debe contener únicamente dígitos numéricos.");
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
        Telefono telefono = (Telefono) o;
        return Objects.equals(_valor, telefono._valor);
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
