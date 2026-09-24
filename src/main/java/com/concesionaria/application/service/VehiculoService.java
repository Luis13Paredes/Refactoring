package com.concesionaria.application.service;

import com.concesionaria.application.dto.VehiculoDTO;
import com.concesionaria.domain.entity.Vehiculo;
import com.concesionaria.domain.exception.EntidadNoEncontradaException;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.repository.IVehiculoRepository;
import com.concesionaria.domain.valueobject.Placa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Servicio de aplicación para la gestión del catálogo e inventario de Vehículos.
 * Ofrece métodos para CRUD y lógica pura de filtrado multicriterio.
 */
public class VehiculoService {

    private final IVehiculoRepository _vehiculoRepository;

    public VehiculoService(IVehiculoRepository vehiculoRepository) {
        if (vehiculoRepository == null) {
            throw new IllegalArgumentException("IVehiculoRepository no puede ser nulo.");
        }
        this._vehiculoRepository = vehiculoRepository;
    }

    public void registrarVehiculo(String placaStr, String tipo, String marca, String modelo, String color, int anio) {
        Placa placa = new Placa(placaStr);
        if (_vehiculoRepository.existePorPlaca(placa)) {
            throw new ReglaNegocioException("El vehículo con placa " + placaStr + " ya se encuentra registrado.");
        }
        Vehiculo vehiculo = new Vehiculo(placa, tipo, marca, modelo, color, anio);
        _vehiculoRepository.guardar(vehiculo);
    }

    public void actualizarVehiculo(String placaStr, String tipo, String marca, String modelo, String color, int anio) {
        Placa placa = new Placa(placaStr);
        Vehiculo vehiculo = _vehiculoRepository.buscarPorPlaca(placa)
                .orElseThrow(() -> new EntidadNoEncontradaException("El vehículo con placa " + placaStr + " no existe."));

        vehiculo.actualizarDatos(tipo, marca, modelo, color, anio);
        _vehiculoRepository.actualizar(vehiculo);
    }

    public void eliminarVehiculo(String placaStr) {
        Placa placa = new Placa(placaStr);
        if (!_vehiculoRepository.existePorPlaca(placa)) {
            throw new EntidadNoEncontradaException("El vehículo con placa " + placaStr + " no existe.");
        }
        _vehiculoRepository.eliminar(placa);
    }

    public VehiculoDTO buscarVehiculo(String placaStr) {
        Placa placa = new Placa(placaStr);
        Vehiculo vehiculo = _vehiculoRepository.buscarPorPlaca(placa)
                .orElseThrow(() -> new EntidadNoEncontradaException("El vehículo con placa " + placaStr + " no existe."));
        return VehiculoDTO.fromDomain(vehiculo);
    }

    public List<VehiculoDTO> listarVehiculos() {
        List<Vehiculo> vehiculos = _vehiculoRepository.listarTodos();
        List<VehiculoDTO> resultado = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            resultado.add(VehiculoDTO.fromDomain(v));
        }
        return Collections.unmodifiableList(resultado);
    }

    public boolean existeVehiculo(String placaStr) {
        try {
            Placa placa = new Placa(placaStr);
            return _vehiculoRepository.existePorPlaca(placa);
        } catch (ReglaNegocioException e) {
            return false;
        }
    }

    public Vehiculo obtenerEntidad(Placa placa) {
        return _vehiculoRepository.buscarPorPlaca(placa)
                .orElseThrow(() -> new EntidadNoEncontradaException("El vehículo con placa " + placa + " no existe."));
    }

    // --- Métodos de filtrado multicriterio (Lógica de Negocio) ---

    public List<String> obtenerTiposDisponibles(List<VehiculoDTO> lista) {
        Set<String> tipos = new LinkedHashSet<>();
        for (VehiculoDTO v : lista) {
            tipos.add(v.getTipo());
        }
        return new ArrayList<>(tipos);
    }

    public List<String> obtenerModelosDisponibles(List<VehiculoDTO> lista) {
        Set<String> modelos = new LinkedHashSet<>();
        for (VehiculoDTO v : lista) {
            modelos.add(v.getModelo());
        }
        return new ArrayList<>(modelos);
    }

    public List<String> obtenerMarcasDisponibles(List<VehiculoDTO> lista) {
        Set<String> marcas = new LinkedHashSet<>();
        for (VehiculoDTO v : lista) {
            marcas.add(v.getMarca());
        }
        return new ArrayList<>(marcas);
    }

    public List<String> obtenerColoresDisponibles(List<VehiculoDTO> lista) {
        Set<String> colores = new LinkedHashSet<>();
        for (VehiculoDTO v : lista) {
            colores.add(v.getColor());
        }
        return new ArrayList<>(colores);
    }

    public List<Integer> obtenerAniosDisponibles(List<VehiculoDTO> lista) {
        Set<Integer> anios = new LinkedHashSet<>();
        for (VehiculoDTO v : lista) {
            anios.add(v.getAnio());
        }
        return new ArrayList<>(anios);
    }

    public List<VehiculoDTO> filtrarPorTipo(List<VehiculoDTO> lista, String tipo) {
        List<VehiculoDTO> filtrados = new ArrayList<>();
        for (VehiculoDTO v : lista) {
            if (v.getTipo().equalsIgnoreCase(tipo)) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }

    public List<VehiculoDTO> filtrarPorModelo(List<VehiculoDTO> lista, String modelo) {
        List<VehiculoDTO> filtrados = new ArrayList<>();
        for (VehiculoDTO v : lista) {
            if (v.getModelo().equalsIgnoreCase(modelo)) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }

    public List<VehiculoDTO> filtrarPorMarcaColorAnio(List<VehiculoDTO> lista, String marca, String color, int anio) {
        List<VehiculoDTO> filtrados = new ArrayList<>();
        for (VehiculoDTO v : lista) {
            if (v.getMarca().equalsIgnoreCase(marca) || v.getColor().equalsIgnoreCase(color) || v.getAnio() == anio) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }
}
