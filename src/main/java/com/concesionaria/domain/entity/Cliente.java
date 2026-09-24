package com.concesionaria.domain.entity;

import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.valueobject.Cedula;
import com.concesionaria.domain.valueobject.Telefono;
import java.util.Objects;

/**
 * Entidad que representa a un Cliente de la concesionaria.
 * Protege sus invariantes y encapsula sus datos y modificaciones.
 */
public class Cliente {

    private final Cedula _cedula;
    private String _nombre;
    private String _apellido;
    private Telefono _telefono;
    private String _direccion;

    public Cliente(Cedula cedula, String nombre, String apellido, Telefono telefono, String direccion) {
        if (cedula == null) {
            throw new ReglaNegocioException("La cédula del cliente no puede ser nula.");
        }
        validarDatosPersonales(nombre, apellido, telefono, direccion);

        this._cedula = cedula;
        this._nombre = nombre.trim();
        this._apellido = apellido.trim();
        this._telefono = telefono;
        this._direccion = direccion.trim();
    }

    public void actualizarDatos(String nuevoNombre, String nuevoApellido, Telefono nuevoTelefono, String nuevaDireccion) {
        validarDatosPersonales(nuevoNombre, nuevoApellido, nuevoTelefono, nuevaDireccion);
        this._nombre = nuevoNombre.trim();
        this._apellido = nuevoApellido.trim();
        this._telefono = nuevoTelefono;
        this._direccion = nuevaDireccion.trim();
    }

    private void validarDatosPersonales(String nombre, String apellido, Telefono telefono, String direccion) {
        if (nombre == null || nombre.trim().isBlank()) {
            throw new ReglaNegocioException("El nombre del cliente no puede estar vacío.");
        }
        if (apellido == null || apellido.trim().isBlank()) {
            throw new ReglaNegocioException("El apellido del cliente no puede estar vacío.");
        }
        if (telefono == null) {
            throw new ReglaNegocioException("El teléfono del cliente no puede ser nulo.");
        }
        if (direccion == null || direccion.trim().isBlank()) {
            throw new ReglaNegocioException("La dirección del cliente no puede estar vacía.");
        }
    }

    public Cedula getCedula() {
        return _cedula;
    }

    public String getNombre() {
        return _nombre;
    }

    public String getApellido() {
        return _apellido;
    }

    public String getNombreCompleto() {
        return _nombre + " " + _apellido;
    }

    public Telefono getTelefono() {
        return _telefono;
    }

    public String getDireccion() {
        return _direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(_cedula, cliente._cedula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_cedula);
    }

    @Override
    public String toString() {
        return "Cliente: Cedula " + _cedula + ", Nombre " + _nombre + ", Apellido " + _apellido + ", Telefono " + _telefono + ", Direccion " + _direccion;
    }
}
