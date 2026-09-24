package com.concesionaria.application.dto;

import com.concesionaria.domain.entity.Factura;
import com.concesionaria.domain.entity.ItemCompra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object inmutable para representar una factura de venta emitida.
 */
public class FacturaDTO {

    private final String _cedulaCliente;
    private final String _nombreCliente;
    private final LocalDate _fechaFactura;
    private final List<String> _detallesItems;
    private final double _total;
    private final int _cantidadVehiculos;

    public FacturaDTO(String cedulaCliente, String nombreCliente, LocalDate fechaFactura,
                      List<String> detallesItems, double total, int cantidadVehiculos) {
        this._cedulaCliente = cedulaCliente;
        this._nombreCliente = nombreCliente;
        this._fechaFactura = fechaFactura;
        this._detallesItems = (detallesItems != null) ? detallesItems : Collections.emptyList();
        this._total = total;
        this._cantidadVehiculos = cantidadVehiculos;
    }

    public static FacturaDTO fromDomain(Factura f) {
        if (f == null) return null;
        List<String> detalles = new ArrayList<>();
        for (ItemCompra item : f.getItems()) {
            detalles.add(String.format("Fecha: %s | %s | Precio: $%.2f",
                    item.getFecha(),
                    item.getVehiculo().toString(),
                    item.getPrecio()));
        }
        return new FacturaDTO(
                f.getCliente().getCedula().getValor(),
                f.getCliente().getNombreCompleto(),
                f.getFechaFactura(),
                Collections.unmodifiableList(detalles),
                f.getTotal(),
                f.getCantidadVehiculos()
        );
    }

    public String getCedulaCliente() {
        return _cedulaCliente;
    }

    public String getNombreCliente() {
        return _nombreCliente;
    }

    public LocalDate getFechaFactura() {
        return _fechaFactura;
    }

    public List<String> getDetallesItems() {
        return _detallesItems;
    }

    public double getTotal() {
        return _total;
    }

    public int getCantidadVehiculos() {
        return _cantidadVehiculos;
    }
}
