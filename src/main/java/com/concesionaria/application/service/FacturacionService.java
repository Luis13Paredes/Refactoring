package com.concesionaria.application.service;

import com.concesionaria.application.dto.FacturaDTO;
import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.entity.Factura;
import com.concesionaria.domain.entity.ItemCompra;
import com.concesionaria.domain.entity.Vehiculo;
import com.concesionaria.domain.exception.EntidadNoEncontradaException;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.repository.IClienteRepository;
import com.concesionaria.domain.repository.IFacturaRepository;
import com.concesionaria.domain.repository.IInteresClienteRepository;
import com.concesionaria.domain.repository.IVehiculoRepository;
import com.concesionaria.domain.valueobject.Cedula;
import com.concesionaria.domain.valueobject.Placa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Servicio de aplicación que orquesta el proceso comercial de venta y facturación.
 * Garantiza que la venta descuente los vehículos del inventario y registre la factura.
 */
public class FacturacionService {

    private final IClienteRepository _clienteRepository;
    private final IVehiculoRepository _vehiculoRepository;
    private final IInteresClienteRepository _interesRepository;
    private final IFacturaRepository _facturaRepository;

    public FacturacionService(IClienteRepository clienteRepository,
                              IVehiculoRepository vehiculoRepository,
                              IInteresClienteRepository interesRepository,
                              IFacturaRepository facturaRepository) {
        if (clienteRepository == null || vehiculoRepository == null ||
            interesRepository == null || facturaRepository == null) {
            throw new IllegalArgumentException("Ningún repositorio de dependencias puede ser nulo.");
        }
        this._clienteRepository = clienteRepository;
        this._vehiculoRepository = vehiculoRepository;
        this._interesRepository = interesRepository;
        this._facturaRepository = facturaRepository;
    }

    public FacturaDTO emitirFactura(String cedulaStr, Map<String, Double> vehiculosPrecios, LocalDate fechaFactura) {
        if (vehiculosPrecios == null || vehiculosPrecios.isEmpty()) {
            throw new ReglaNegocioException("Debe incluir al menos un vehículo para facturar.");
        }

        Cedula cedula = new Cedula(cedulaStr);
        Cliente cliente = _clienteRepository.buscarPorCedula(cedula)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente con cédula " + cedulaStr + " no existe."));

        LocalDate fechaVenta = (fechaFactura != null) ? fechaFactura : LocalDate.now();
        List<ItemCompra> items = new ArrayList<>();
        List<Placa> placasVendidas = new ArrayList<>();

        for (Map.Entry<String, Double> entry : vehiculosPrecios.entrySet()) {
            Placa placa = new Placa(entry.getKey());
            Vehiculo vehiculo = _vehiculoRepository.buscarPorPlaca(placa)
                    .orElseThrow(() -> new ReglaNegocioException("El vehículo con placa " + entry.getKey() + " ya no está disponible en inventario."));

            double precio = entry.getValue();
            if (precio <= 0) {
                throw new ReglaNegocioException("El precio para el vehículo con placa " + entry.getKey() + " debe ser mayor a cero.");
            }

            items.add(new ItemCompra(vehiculo, fechaVenta, precio));
            placasVendidas.add(placa);
        }

        Factura nuevaFactura = new Factura(cliente, fechaVenta, items);

        // 1. Guardar la factura
        _facturaRepository.guardar(nuevaFactura);

        // 2. Descontar vehículos vendidos del inventario disponible
        for (Placa placa : placasVendidas) {
            _vehiculoRepository.eliminar(placa);
        }

        return FacturaDTO.fromDomain(nuevaFactura);
    }

    public List<FacturaDTO> listarFacturas() {
        List<Factura> facturas = _facturaRepository.listarTodas();
        List<FacturaDTO> dtos = new ArrayList<>();
        for (Factura f : facturas) {
            dtos.add(FacturaDTO.fromDomain(f));
        }
        return Collections.unmodifiableList(dtos);
    }

    public List<FacturaDTO> listarFacturasPorCliente(String cedulaStr) {
        Cedula cedula = new Cedula(cedulaStr);
        Cliente cliente = _clienteRepository.buscarPorCedula(cedula)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente con cédula " + cedulaStr + " no existe."));

        List<Factura> facturas = _facturaRepository.listarPorCliente(cliente);
        List<FacturaDTO> dtos = new ArrayList<>();
        for (Factura f : facturas) {
            dtos.add(FacturaDTO.fromDomain(f));
        }
        return Collections.unmodifiableList(dtos);
    }
}
