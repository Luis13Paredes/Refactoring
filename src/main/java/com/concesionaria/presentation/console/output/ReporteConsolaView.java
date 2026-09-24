package com.concesionaria.presentation.console.output;

import com.concesionaria.application.dto.FacturaDTO;
import com.concesionaria.application.dto.InteresClienteDTO;
import com.concesionaria.application.dto.ReporteVentaDTO;
import com.concesionaria.application.dto.VehiculoDTO;
import com.concesionaria.application.service.ReporteService.HistorialClienteDTO;

import java.util.List;

/**
 * Vista de consola responsable exclusivamente del formateo e impresión de los reportes.
 */
public class ReporteConsolaView {

    public void mostrarReporteClientesInteresados(List<InteresClienteDTO> clientesInteresados) {
        ConsolaOutput.imprimirTitulo("REPORTE: CLIENTES INTERESADOS (SIN COMPRAS EN ÚLTIMOS 3 AÑOS)");
        System.out.println("Clientes interesados en un modelo/marca cuya última compra no sea de los últimos 3 años:\n");

        if (clientesInteresados == null || clientesInteresados.isEmpty()) {
            System.out.println("No hay clientes que mostrar...");
            ConsolaOutput.imprimirSeparador();
            return;
        }

        for (InteresClienteDTO interes : clientesInteresados) {
            System.out.println("Cliente: Cédula " + interes.getCedulaCliente() + " - " + interes.getNombreCliente());
            System.out.println("Fecha de interés registrado: " + interes.getFecha());
            System.out.println("Vehículos de interés:");
            for (VehiculoDTO v : interes.getVehiculos()) {
                System.out.println("  -> Marca: " + v.getMarca() + " | Modelo: " + v.getModelo() + " | Tipo: " + v.getTipo() + " | Color: " + v.getColor() + " | Año: " + v.getAnio());
            }
            ConsolaOutput.imprimirSeparador();
        }
    }

    public void mostrarReporteHistorialCliente(HistorialClienteDTO historial) {
        ConsolaOutput.imprimirTitulo("REPORTE: HISTORIAL DE UN CLIENTE");
        System.out.println(historial.getCliente().toString());
        ConsolaOutput.imprimirSeparador();

        System.out.println("1. REGISTRO DE INTERESES:");
        if (historial.getInteres().isPresent()) {
            InteresClienteDTO interes = historial.getInteres().get();
            System.out.println("Fecha de interés: " + interes.getFecha());
            for (VehiculoDTO v : interes.getVehiculos()) {
                System.out.println("  -> " + v.toString());
            }
        } else {
            System.out.println("  El cliente no tiene registro de intereses.");
        }

        ConsolaOutput.imprimirSeparador();
        System.out.println("2. REGISTRO DE FACTURAS:");
        if (historial.getFacturas().isEmpty()) {
            System.out.println("  El cliente no tiene facturas registradas.");
        } else {
            for (FacturaDTO f : historial.getFacturas()) {
                System.out.println("Factura emitida: " + f.getFechaFactura() + " | Total: $" + String.format("%.2f", f.getTotal()));
                for (String detalle : f.getDetallesItems()) {
                    System.out.println("  -> " + detalle);
                }
            }
        }
        ConsolaOutput.imprimirSeparador();
    }

    public void mostrarReporteVehiculosVendidos(ReporteVentaDTO reporteVenta) {
        ConsolaOutput.imprimirTitulo("REPORTE: VEHÍCULOS VENDIDOS EN EL PERÍODO");
        System.out.println("Rango de fechas: " + reporteVenta.getFechaInicio() + " al " + reporteVenta.getFechaFin());
        ConsolaOutput.imprimirSeparador();

        if (reporteVenta.getVehiculosVendidos().isEmpty()) {
            System.out.println("No hay registros de ventas entre las fechas indicadas.\n");
            ConsolaOutput.imprimirSeparador();
            return;
        }

        System.out.println("Listado de vehículos vendidos:");
        for (VehiculoDTO v : reporteVenta.getVehiculosVendidos()) {
            System.out.println("  -> " + v.toString());
        }

        ConsolaOutput.imprimirSeparador();
        System.out.println("Total de vehículos vendidos: " + reporteVenta.getTotalVehiculos());
        System.out.printf("Valor Total Facturado: $%.2f%n", reporteVenta.getTotalFacturado());
        ConsolaOutput.imprimirSeparador();
    }
}
