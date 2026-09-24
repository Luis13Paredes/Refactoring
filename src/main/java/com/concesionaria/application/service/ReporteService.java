package com.concesionaria.application.service;

import com.concesionaria.application.dto.ClienteDTO;
import com.concesionaria.application.dto.FacturaDTO;
import com.concesionaria.application.dto.InteresClienteDTO;
import com.concesionaria.application.dto.ReporteVentaDTO;
import com.concesionaria.application.dto.VehiculoDTO;
import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.entity.Factura;
import com.concesionaria.domain.entity.InteresCliente;
import com.concesionaria.domain.entity.ItemCompra;
import com.concesionaria.domain.exception.EntidadNoEncontradaException;
import com.concesionaria.domain.repository.IClienteRepository;
import com.concesionaria.domain.repository.IFacturaRepository;
import com.concesionaria.domain.repository.IInteresClienteRepository;
import com.concesionaria.domain.valueobject.Cedula;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Servicio de aplicación para el cálculo analítico de los tres reportes de negocio.
 * Es puramente computacional: no interactúa con consola ni imprime texto.
 */
public class ReporteService {

    private final IClienteRepository _clienteRepository;
    private final IInteresClienteRepository _interesRepository;
    private final IFacturaRepository _facturaRepository;

    public ReporteService(IClienteRepository clienteRepository,
                          IInteresClienteRepository interesRepository,
                          IFacturaRepository facturaRepository) {
        if (clienteRepository == null || interesRepository == null || facturaRepository == null) {
            throw new IllegalArgumentException("Los repositorios de dependencias no pueden ser nulos.");
        }
        this._clienteRepository = clienteRepository;
        this._interesRepository = interesRepository;
        this._facturaRepository = facturaRepository;
    }

    /**
     * REPORTE 1: Clientes interesados en vehículos cuya última compra NO sea de los últimos 3 años.
     */
    public List<InteresClienteDTO> obtenerClientesInteresadosSinComprasRecientes() {
        LocalDate fechaLimite = LocalDate.now().minusYears(3);
        List<Factura> todasLasFacturas = _facturaRepository.listarTodas();

        // Clientes que han comprado en los últimos 3 años (excluidos)
        Set<Cliente> clientesConCompraReciente = new LinkedHashSet<>();
        for (Factura f : todasLasFacturas) {
            if (f.getFechaFactura().isAfter(fechaLimite)) {
                clientesConCompraReciente.add(f.getCliente());
            }
        }

        List<InteresCliente> todosLosIntereses = _interesRepository.listarTodos();
        List<InteresClienteDTO> resultado = new ArrayList<>();

        for (InteresCliente interes : todosLosIntereses) {
            Cliente cliente = interes.getCliente();
            // Si no ha comprado en los últimos 3 años, califica para el reporte
            if (!clientesConCompraReciente.contains(cliente) && interes.hasVehiculos()) {
                resultado.add(InteresClienteDTO.fromDomain(interes));
            }
        }

        return Collections.unmodifiableList(resultado);
    }

    /**
     * REPORTE 2: Historial integral de un cliente (datos, intereses y facturas emitidas).
     */
    public HistorialClienteDTO obtenerHistorialCliente(String cedulaStr) {
        Cedula cedula = new Cedula(cedulaStr);
        Cliente cliente = _clienteRepository.buscarPorCedula(cedula)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente con cédula " + cedulaStr + " no está registrado."));

        ClienteDTO clienteDTO = ClienteDTO.fromDomain(cliente);
        Optional<InteresClienteDTO> interesDTO = _interesRepository.buscarPorCliente(cliente)
                .map(InteresClienteDTO::fromDomain);

        List<Factura> facturas = _facturaRepository.listarPorCliente(cliente);
        List<FacturaDTO> facturasDTO = new ArrayList<>();
        for (Factura f : facturas) {
            facturasDTO.add(FacturaDTO.fromDomain(f));
        }

        return new HistorialClienteDTO(clienteDTO, interesDTO, Collections.unmodifiableList(facturasDTO));
    }

    /**
     * REPORTE 3: Vehículos vendidos en un margen de tiempo con estadísticas y valor facturado.
     */
    public ReporteVentaDTO obtenerVehiculosVendidosPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la fecha final.");
        }

        List<Factura> facturasEnPeriodo = _facturaRepository.listarPorRangoFechas(fechaInicio, fechaFin);
        List<VehiculoDTO> vehiculosVendidos = new ArrayList<>();
        double totalFacturado = 0.0;

        for (Factura f : facturasEnPeriodo) {
            totalFacturado += f.getTotal();
            for (ItemCompra item : f.getItems()) {
                vehiculosVendidos.add(VehiculoDTO.fromDomain(item.getVehiculo()));
            }
        }

        return new ReporteVentaDTO(fechaInicio, fechaFin, Collections.unmodifiableList(vehiculosVendidos), totalFacturado);
    }

    /**
     * DTO contenedor para el historial consolidado de un cliente.
     */
    public static class HistorialClienteDTO {
        private final ClienteDTO _cliente;
        private final Optional<InteresClienteDTO> _interes;
        private final List<FacturaDTO> _facturas;

        public HistorialClienteDTO(ClienteDTO cliente, Optional<InteresClienteDTO> interes, List<FacturaDTO> facturas) {
            this._cliente = cliente;
            this._interes = interes;
            this._facturas = facturas;
        }

        public ClienteDTO getCliente() {
            return _cliente;
        }

        public Optional<InteresClienteDTO> getInteres() {
            return _interes;
        }

        public List<FacturaDTO> getFacturas() {
            return _facturas;
        }
    }
}
