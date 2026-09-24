package com.concesionaria.domain.valueobject;

import com.concesionaria.domain.exception.ReglaNegocioException;
import java.util.Objects;

/**
 * Value Object inmutable que encapsula y valida la Placa de un vehículo (6 caracteres).
 */
public final class Placa {

    private final String _valor;

    public Placa(String valor) {
        if (valor == null || valor.trim().isBlank()) {
            throw new ReglaNegocioException("La placa no puede estar vacía.");
        }
        String valorLimpio = valor.trim().toUpperCase();
        if (valorLimpio.length() != 6) {
            throw new ReglaNegocioException("La placa debe tener exactamente 6 caracteres.");
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
        Placa placa = (Placa) o;
        return Objects.equals(_valor, placa._valor);
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
