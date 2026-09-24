package com.concesionaria.application.dto;

import com.concesionaria.domain.entity.Vehiculo;

/**
 * Data Transfer Object inmutable para transportar información de Vehículo
 * hacia la interfaz de usuario sin exponer directamente la entidad de dominio.
 */
public class VehiculoDTO {

    private final String _placa;
    private final String _tipo;
    private final String _marca;
    private final String _modelo;
    private final String _color;
    private final int _anio;

    public VehiculoDTO(String placa, String tipo, String marca, String modelo, String color, int anio) {
        this._placa = placa;
        this._tipo = tipo;
        this._marca = marca;
        this._modelo = modelo;
        this._color = color;
        this._anio = anio;
    }

    public static VehiculoDTO fromDomain(Vehiculo v) {
        if (v == null) return null;
        return new VehiculoDTO(
                v.getPlaca().getValor(),
                v.getTipo(),
                v.getMarca(),
                v.getModelo(),
                v.getColor(),
                v.getAnio()
        );
    }

    public String getPlaca() {
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
    public String toString() {
        return "Vehiculo: Placa " + _placa + ", Marca " + _marca + ", Modelo " + _modelo + ", Color " + _color + ", Tipo " + _tipo + ", Año " + _anio;
    }
}
