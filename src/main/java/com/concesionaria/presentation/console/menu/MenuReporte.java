package com.concesionaria.presentation.console.menu;

import com.concesionaria.application.dto.InteresClienteDTO;
import com.concesionaria.application.dto.ReporteVentaDTO;
import com.concesionaria.application.service.ReporteService;
import com.concesionaria.application.service.ReporteService.HistorialClienteDTO;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.presentation.console.input.ConsolaInput;
import com.concesionaria.presentation.console.output.ConsolaOutput;
import com.concesionaria.presentation.console.output.ReporteConsolaView;

import java.time.LocalDate;
import java.util.List;

/**
 * Submenú interactivo para solicitar parámetros y presentar los 3 Reportes analíticos.
 */
public class MenuReporte {

    private final ReporteService _reporteService;
    private final ReporteConsolaView _reporteView;
    private final ConsolaInput _input;

    public MenuReporte(ReporteService reporteService, ReporteConsolaView reporteView, ConsolaInput input) {
        this._reporteService = reporteService;
        this._reporteView = reporteView;
        this._input = input;
    }

    public void mostrar() {
        int opcion;
        do {
            ConsolaOutput.imprimirSubtitulo("Menú Reportes");
            System.out.println("1. Rep. Clientes Interesados (Sin compras en últimos 3 años)");
            System.out.println("2. Rep. Historial de Cliente");
            System.out.println("3. Rep. Vehículos Vendidos en Período");
            System.out.println("4. Regresar");

            opcion = _input.leerEntero("Ingrese la opción deseada: ");

            switch (opcion) {
                case 1 -> reporteClientesInteresados();
                case 2 -> reporteHistorialCliente();
                case 3 -> reporteVehiculosVendidos();
                case 4 -> { /* Regresar al menú principal */ }
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (opcion != 4);
    }

    private void reporteClientesInteresados() {
        List<InteresClienteDTO> lista = _reporteService.obtenerClientesInteresadosSinComprasRecientes();
        _reporteView.mostrarReporteClientesInteresados(lista);
    }

    private void reporteHistorialCliente() {
        String cedula = _input.leerTextoObligatorio("Ingresa la cédula del cliente: ");
        try {
            HistorialClienteDTO historial = _reporteService.obtenerHistorialCliente(cedula);
            _reporteView.mostrarReporteHistorialCliente(historial);
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void reporteVehiculosVendidos() {
        ConsolaOutput.imprimirSubtitulo("Consulta de Ventas por Período");
        LocalDate fechaInicio = _input.leerFecha("FECHA INICIAL");
        LocalDate fechaFin = _input.leerFecha("FECHA FINAL");

        if (fechaInicio.isAfter(fechaFin)) {
            ConsolaOutput.imprimirError("La fecha inicial no puede ser posterior a la fecha final.");
            return;
        }

        ReporteVentaDTO reporte = _reporteService.obtenerVehiculosVendidosPorPeriodo(fechaInicio, fechaFin);
        _reporteView.mostrarReporteVehiculosVendidos(reporte);
    }
}
