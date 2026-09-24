package com.concesionaria.application.dto;

import com.concesionaria.domain.entity.Cliente;

/**
 * Data Transfer Object inmutable para transportar información de Cliente
 * entre la capa de presentación y la capa de aplicación sin exponer la entidad.
 */
public class ClienteDTO {

    private final String _cedula;
    private final String _nombre;
    private final String _apellido;
    private final String _telefono;
    private final String _direccion;

    public ClienteDTO(String cedula, String nombre, String apellido, String telefono, String direccion) {
        this._cedula = cedula;
        this._nombre = nombre;
        this._apellido = apellido;
        this._telefono = telefono;
        this._direccion = direccion;
    }

    public static ClienteDTO fromDomain(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteDTO(
                cliente.getCedula().getValor(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getTelefono().getValor(),
                cliente.getDireccion()
        );
    }

    public String getCedula() {
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

    public String getTelefono() {
        return _telefono;
    }

    public String getDireccion() {
        return _direccion;
    }

    @Override
    public String toString() {
        return "Cliente: Cedula " + _cedula + ", Nombre " + _nombre + ", Apellido " + _apellido + ", Telefono " + _telefono + ", Direccion " + _direccion;
    }
}
