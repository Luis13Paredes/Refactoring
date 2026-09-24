package com.concesionaria.application.dto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object inmutable que consolida las estadísticas de vehículos vendidos en un rango de fechas.
 */
public class ReporteVentaDTO {

    private final LocalDate _fechaInicio;
    private final LocalDate _fechaFin;
    private final List<VehiculoDTO> _vehiculosVendidos;
    private final int _totalVehiculos;
    private final double _totalFacturado;

    public ReporteVentaDTO(LocalDate fechaInicio, LocalDate fechaFin, List<VehiculoDTO> vehiculosVendidos, double totalFacturado) {
        this._fechaInicio = fechaInicio;
        this._fechaFin = fechaFin;
        this._vehiculosVendidos = (vehiculosVendidos != null) ? vehiculosVendidos : Collections.emptyList();
        this._totalVehiculos = this._vehiculosVendidos.size();
        this._totalFacturado = totalFacturado;
    }

    public LocalDate getFechaInicio() {
        return _fechaInicio;
    }

    public LocalDate getFechaFin() {
        return _fechaFin;
    }

    public List<VehiculoDTO> getVehiculosVendidos() {
        return _vehiculosVendidos;
    }

    public int getTotalVehiculos() {
        return _totalVehiculos;
    }

    public double getTotalFacturado() {
        return _totalFacturado;
    }
}
