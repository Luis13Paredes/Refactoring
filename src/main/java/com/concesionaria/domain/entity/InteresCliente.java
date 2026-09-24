package com.concesionaria.domain.entity;

import com.concesionaria.domain.exception.ReglaNegocioException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad que representa la manifestación de interés de un cliente por uno o varios vehículos.
 * Encapsula la lista de vehículos protegiéndola contra mutaciones externas directas.
 */
public class InteresCliente {

    private final Cliente _cliente;
    private LocalDate _fecha;
    private final List<Vehiculo> _vehiculos;

    public InteresCliente(Cliente cliente, LocalDate fecha) {
        if (cliente == null) {
            throw new ReglaNegocioException("El cliente de interés no puede ser nulo.");
        }
        if (fecha == null) {
            throw new ReglaNegocioException("La fecha de registro de interés no puede ser nula.");
        }
        this._cliente = cliente;
        this._fecha = fecha;
        this._vehiculos = new ArrayList<>();
    }

    public void agregarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new ReglaNegocioException("No se puede agregar un vehículo nulo al interés.");
        }
        if (!_vehiculos.contains(vehiculo)) {
            _vehiculos.add(vehiculo);
        }
    }

    public boolean contieneVehiculo(Vehiculo vehiculo) {
        return _vehiculos.contains(vehiculo);
    }

    public void actualizarFecha(LocalDate nuevaFecha) {
        if (nuevaFecha == null) {
            throw new ReglaNegocioException("La fecha actualizada no puede ser nula.");
        }
        this._fecha = nuevaFecha;
    }

    public Cliente getCliente() {
        return _cliente;
    }

    public LocalDate getFecha() {
        return _fecha;
    }

    public List<Vehiculo> getVehiculos() {
        return Collections.unmodifiableList(_vehiculos);
    }

    public boolean hasVehiculos() {
        return !_vehiculos.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteresCliente that = (InteresCliente) o;
        return Objects.equals(_cliente, that._cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_cliente);
    }

    @Override
    public String toString() {
        return "InteresCliente: " + _cliente.getNombreCompleto() + " - Fecha: " + _fecha + " - Total vehículos de interés: " + _vehiculos.size();
    }
}
