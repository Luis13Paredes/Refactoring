package com.concesionaria.application.dto;

import com.concesionaria.domain.entity.InteresCliente;
import com.concesionaria.domain.entity.Vehiculo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object que consolida la información de interés de un cliente.
 */
public class InteresClienteDTO {

    private final String _cedulaCliente;
    private final String _nombreCliente;
    private final LocalDate _fecha;
    private final List<VehiculoDTO> _vehiculos;

    public InteresClienteDTO(String cedulaCliente, String nombreCliente, LocalDate fecha, List<VehiculoDTO> vehiculos) {
        this._cedulaCliente = cedulaCliente;
        this._nombreCliente = nombreCliente;
        this._fecha = fecha;
        this._vehiculos = (vehiculos != null) ? vehiculos : Collections.emptyList();
    }

    public static InteresClienteDTO fromDomain(InteresCliente interes) {
        if (interes == null) return null;
        List<VehiculoDTO> dtos = new ArrayList<>();
        for (Vehiculo v : interes.getVehiculos()) {
            dtos.add(VehiculoDTO.fromDomain(v));
        }
        return new InteresClienteDTO(
                interes.getCliente().getCedula().getValor(),
                interes.getCliente().getNombreCompleto(),
                interes.getFecha(),
                Collections.unmodifiableList(dtos)
        );
    }

    public String getCedulaCliente() {
        return _cedulaCliente;
    }

    public String getNombreCliente() {
        return _nombreCliente;
    }

    public LocalDate getFecha() {
        return _fecha;
    }

    public List<VehiculoDTO> getVehiculos() {
        return _vehiculos;
    }
}
