package com.concesionaria.domain.entity;

import com.concesionaria.domain.exception.ReglaNegocioException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa el detalle individual de un vehículo vendido/comprado con su precio acordado.
 * Inmutable y con precio positivo garantizado.
 */
public class ItemCompra {

    private final Vehiculo _vehiculo;
    private final LocalDate _fecha;
    private final double _precio;

    public ItemCompra(Vehiculo vehiculo, LocalDate fecha, double precio) {
        if (vehiculo == null) {
            throw new ReglaNegocioException("El vehículo a comprar no puede ser nulo.");
        }
        if (fecha == null) {
            throw new ReglaNegocioException("La fecha de compra no puede ser nula.");
        }
        if (precio <= 0) {
            throw new ReglaNegocioException("El precio del vehículo debe ser mayor a cero.");
        }
        this._vehiculo = vehiculo;
        this._fecha = fecha;
        this._precio = precio;
    }

    public Vehiculo getVehiculo() {
        return _vehiculo;
    }

    public LocalDate getFecha() {
        return _fecha;
    }

    public double getPrecio() {
        return _precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCompra that = (ItemCompra) o;
        return Objects.equals(_vehiculo, that._vehiculo) && Objects.equals(_fecha, that._fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_vehiculo, _fecha);
    }

    @Override
    public String toString() {
        return "ItemCompra: Fecha " + _fecha + " - " + _vehiculo.toString() + " - Precio: $" + String.format("%.2f", _precio);
    }
}
