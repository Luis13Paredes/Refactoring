package com.concesionaria.infrastructure.persistence;

import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.entity.InteresCliente;
import com.concesionaria.domain.entity.Vehiculo;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.repository.IInteresClienteRepository;
import com.concesionaria.domain.valueobject.Cedula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria de IInteresClienteRepository.
 * Almacena y fusiona los intereses registrados de los clientes durante la sesión.
 */
public class MemoriaInteresClienteRepository implements IInteresClienteRepository {

    private final Map<Cedula, InteresCliente> _intereses;

    public MemoriaInteresClienteRepository() {
        this._intereses = new LinkedHashMap<>();
    }

    @Override
    public void registrarOActualizar(InteresCliente nuevoInteres) {
        if (nuevoInteres == null) {
            throw new ReglaNegocioException("El interés de cliente no puede ser nulo.");
        }
        Cedula cedula = nuevoInteres.getCliente().getCedula();
        if (_intereses.containsKey(cedula)) {
            InteresCliente existente = _intereses.get(cedula);
            for (Vehiculo vehiculo : nuevoInteres.getVehiculos()) {
                existente.agregarVehiculo(vehiculo);
            }
            existente.actualizarFecha(nuevoInteres.getFecha());
        } else {
            _intereses.put(cedula, nuevoInteres);
        }
    }

    @Override
    public Optional<InteresCliente> buscarPorCliente(Cliente cliente) {
        if (cliente == null) {
            return Optional.empty();
        }
        return buscarPorCedula(cliente.getCedula());
    }

    @Override
    public Optional<InteresCliente> buscarPorCedula(Cedula cedula) {
        if (cedula == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(_intereses.get(cedula));
    }

    @Override
    public List<InteresCliente> listarTodos() {
        return Collections.unmodifiableList(new ArrayList<>(_intereses.values()));
    }
}
