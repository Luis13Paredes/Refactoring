package com.concesionaria.domain.entity;

import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.valueobject.Placa;
import java.util.Objects;

/**
 * Entidad que representa un Vehículo del catálogo de la concesionaria.
 * Protege sus invariantes y su identidad única a través de la Placa.
 */
public class Vehiculo {

    private final Placa _placa;
    private String _tipo;
    private String _marca;
    private String _modelo;
    private String _color;
    private int _anio;

    public Vehiculo(Placa placa, String tipo, String marca, String modelo, String color, int anio) {
        if (placa == null) {
            throw new ReglaNegocioException("La placa del vehículo no puede ser nula.");
        }
        validarCaracteristicas(tipo, marca, modelo, color, anio);

        this._placa = placa;
        this._tipo = tipo.trim();
        this._marca = marca.trim();
        this._modelo = modelo.trim();
        this._color = color.trim();
        this._anio = anio;
    }

    public void actualizarDatos(String nuevoTipo, String nuevaMarca, String nuevoModelo, String nuevoColor, int nuevoAnio) {
        validarCaracteristicas(nuevoTipo, nuevaMarca, nuevoModelo, nuevoColor, nuevoAnio);
        this._tipo = nuevoTipo.trim();
        this._marca = nuevaMarca.trim();
        this._modelo = nuevoModelo.trim();
        this._color = nuevoColor.trim();
        this._anio = nuevoAnio;
    }

    private void validarCaracteristicas(String tipo, String marca, String modelo, String color, int anio) {
        if (tipo == null || tipo.trim().isBlank()) {
            throw new ReglaNegocioException("El tipo de vehículo no puede estar vacío.");
        }
        if (marca == null || marca.trim().isBlank()) {
            throw new ReglaNegocioException("La marca del vehículo no puede estar vacía.");
        }
        if (modelo == null || modelo.trim().isBlank()) {
            throw new ReglaNegocioException("El modelo del vehículo no puede estar vacío.");
        }
        if (color == null || color.trim().isBlank()) {
            throw new ReglaNegocioException("El color del vehículo no puede estar vacío.");
        }
        if (anio < 1900 || anio > 2100) {
            throw new ReglaNegocioException("El año del vehículo debe estar en un rango razonable (1900 - 2100).");
        }
    }

    public Placa getPlaca() {
        return _placa;
    }

    public String getTipo() {
        return _tipo;
    }

    public String getMarca() {
        return _marca;
    }

    public String getModelo() {
        return _modelo;
    }

    public String getColor() {
        return _color;
    }

    public int getAnio() {
        return _anio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return Objects.equals(_placa, vehiculo._placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_placa);
    }

    @Override
    public String toString() {
        return "Vehiculo: Placa " + _placa + ", Marca " + _marca + ", Modelo " + _modelo + ", Color " + _color + ", Tipo " + _tipo + ", Año " + _anio;
    }
}
