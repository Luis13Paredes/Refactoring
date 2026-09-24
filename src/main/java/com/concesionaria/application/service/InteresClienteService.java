package com.concesionaria.application.service;

import com.concesionaria.application.dto.InteresClienteDTO;
import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.entity.InteresCliente;
import com.concesionaria.domain.entity.Vehiculo;
import com.concesionaria.domain.exception.EntidadNoEncontradaException;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.repository.IClienteRepository;
import com.concesionaria.domain.repository.IInteresClienteRepository;
import com.concesionaria.domain.repository.IVehiculoRepository;
import com.concesionaria.domain.valueobject.Cedula;
import com.concesionaria.domain.valueobject.Placa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para la gestión y seguimiento del interés de clientes en vehículos.
 */
public class InteresClienteService {

    private final IClienteRepository _clienteRepository;
    private final IVehiculoRepository _vehiculoRepository;
    private final IInteresClienteRepository _interesRepository;

    public InteresClienteService(IClienteRepository clienteRepository,
                                 IVehiculoRepository vehiculoRepository,
                                 IInteresClienteRepository interesRepository) {
        if (clienteRepository == null || vehiculoRepository == null || interesRepository == null) {
            throw new IllegalArgumentException("Los repositorios de dependencias no pueden ser nulos.");
        }
        this._clienteRepository = clienteRepository;
        this._vehiculoRepository = vehiculoRepository;
        this._interesRepository = interesRepository;
    }

    public InteresClienteDTO registrarInteres(String cedulaStr, List<String> placasInteresadas, LocalDate fecha) {
        Cedula cedula = new Cedula(cedulaStr);
        Cliente cliente = _clienteRepository.buscarPorCedula(cedula)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente con cédula " + cedulaStr + " no está registrado."));

        if (placasInteresadas == null || placasInteresadas.isEmpty()) {
            throw new ReglaNegocioException("Debe seleccionar al menos un vehículo de interés.");
        }

        LocalDate fechaRegistro = (fecha != null) ? fecha : LocalDate.now();
        InteresCliente interes = _interesRepository.buscarPorCedula(cedula)
                .orElse(new InteresCliente(cliente, fechaRegistro));

        interes.actualizarFecha(fechaRegistro);

        for (String placaStr : placasInteresadas) {
            Placa placa = new Placa(placaStr);
            Vehiculo vehiculo = _vehiculoRepository.buscarPorPlaca(placa)
                    .orElseThrow(() -> new EntidadNoEncontradaException("El vehículo con placa " + placaStr + " no existe."));
            interes.agregarVehiculo(vehiculo);
        }

        _interesRepository.registrarOActualizar(interes);
        return InteresClienteDTO.fromDomain(interes);
    }

    public Optional<InteresClienteDTO> buscarInteresPorCedula(String cedulaStr) {
        Cedula cedula = new Cedula(cedulaStr);
        return _interesRepository.buscarPorCedula(cedula).map(InteresClienteDTO::fromDomain);
    }

    public List<InteresClienteDTO> listarTodos() {
        List<InteresCliente> lista = _interesRepository.listarTodos();
        List<InteresClienteDTO> resultado = new ArrayList<>();
        for (InteresCliente i : lista) {
            resultado.add(InteresClienteDTO.fromDomain(i));
        }
        return Collections.unmodifiableList(resultado);
    }

    public InteresCliente obtenerEntidad(Cedula cedula) {
        return _interesRepository.buscarPorCedula(cedula)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente con cédula " + cedula + " no tiene intereses registrados."));
    }
}
