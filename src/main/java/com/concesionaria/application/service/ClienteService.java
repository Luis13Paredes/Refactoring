package com.concesionaria.application.service;

import com.concesionaria.application.dto.ClienteDTO;
import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.exception.EntidadNoEncontradaException;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.repository.IClienteRepository;
import com.concesionaria.domain.valueobject.Cedula;
import com.concesionaria.domain.valueobject.Telefono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Servicio de aplicación para la gestión del ciclo de vida de los Clientes.
 * Cumple Single Responsibility Principle (SRP) y no depende de la consola.
 */
public class ClienteService {

    private final IClienteRepository _clienteRepository;

    public ClienteService(IClienteRepository clienteRepository) {
        if (clienteRepository == null) {
            throw new IllegalArgumentException("IClienteRepository no puede ser nulo.");
        }
        this._clienteRepository = clienteRepository;
    }

    public void registrarCliente(String cedulaStr, String nombre, String apellido, String telefonoStr, String direccion) {
        Cedula cedula = new Cedula(cedulaStr);
        if (_clienteRepository.existePorCedula(cedula)) {
            throw new ReglaNegocioException("El cliente con cédula " + cedulaStr + " ya se encuentra registrado.");
        }
        Telefono telefono = new Telefono(telefonoStr);
        Cliente cliente = new Cliente(cedula, nombre, apellido, telefono, direccion);
        _clienteRepository.guardar(cliente);
    }

    public void actualizarCliente(String cedulaStr, String nombre, String apellido, String telefonoStr, String direccion) {
        Cedula cedula = new Cedula(cedulaStr);
        Cliente cliente = _clienteRepository.buscarPorCedula(cedula)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente con cédula " + cedulaStr + " no existe."));

        Telefono telefono = new Telefono(telefonoStr);
        cliente.actualizarDatos(nombre, apellido, telefono, direccion);
        _clienteRepository.actualizar(cliente);
    }

    public void eliminarCliente(String cedulaStr) {
        Cedula cedula = new Cedula(cedulaStr);
        if (!_clienteRepository.existePorCedula(cedula)) {
            throw new EntidadNoEncontradaException("El cliente con cédula " + cedulaStr + " no existe.");
        }
        _clienteRepository.eliminar(cedula);
    }

    public ClienteDTO buscarCliente(String cedulaStr) {
        Cedula cedula = new Cedula(cedulaStr);
        Cliente cliente = _clienteRepository.buscarPorCedula(cedula)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente con cédula " + cedulaStr + " no existe."));
        return ClienteDTO.fromDomain(cliente);
    }

    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = _clienteRepository.listarTodos();
        List<ClienteDTO> resultado = new ArrayList<>();
        for (Cliente c : clientes) {
            resultado.add(ClienteDTO.fromDomain(c));
        }
        return Collections.unmodifiableList(resultado);
    }

    public boolean existeCliente(String cedulaStr) {
        try {
            Cedula cedula = new Cedula(cedulaStr);
            return _clienteRepository.existePorCedula(cedula);
        } catch (ReglaNegocioException e) {
            return false;
        }
    }

    public Cliente obtenerEntidad(Cedula cedula) {
        return _clienteRepository.buscarPorCedula(cedula)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente con cédula " + cedula + " no existe."));
    }
}
