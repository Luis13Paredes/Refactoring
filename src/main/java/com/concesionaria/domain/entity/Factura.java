package com.concesionaria.domain.entity;

import com.concesionaria.domain.exception.ReglaNegocioException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad que representa una Factura de venta.
 * Protege sus invariantes: requiere cliente, fecha y al menos un ítem.
 * El total se calcula de forma automática y segura a partir de los ítems de compra.
 */
public class Factura {

    private final Cliente _cliente;
    private final LocalDate _fechaFactura;
    private final List<ItemCompra> _items;
    private final double _total;

    public Factura(Cliente cliente, LocalDate fechaFactura, List<ItemCompra> items) {
        if (cliente == null) {
            throw new ReglaNegocioException("La factura requiere un cliente asignado.");
        }
        if (fechaFactura == null) {
            throw new ReglaNegocioException("La fecha de facturación no puede ser nula.");
        }
        if (items == null || items.isEmpty()) {
            throw new ReglaNegocioException("La factura debe contener al menos un vehículo comprado.");
        }

        this._cliente = cliente;
        this._fechaFactura = fechaFactura;
        this._items = new ArrayList<>(items);
        this._total = calcularTotal(this._items);
    }

    private double calcularTotal(List<ItemCompra> items) {
        double acumulador = 0.0;
        for (ItemCompra item : items) {
            acumulador += item.getPrecio();
        }
        return acumulador;
    }

    public Cliente getCliente() {
        return _cliente;
    }

    public LocalDate getFechaFactura() {
        return _fechaFactura;
    }

    public List<ItemCompra> getItems() {
        return Collections.unmodifiableList(_items);
    }

    public double getTotal() {
        return _total;
    }

    public int getCantidadVehiculos() {
        return _items.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return Objects.equals(_cliente, factura._cliente) &&
               Objects.equals(_fechaFactura, factura._fechaFactura) &&
               Objects.equals(_items, factura._items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_cliente, _fechaFactura, _items);
    }

    @Override
    public String toString() {
        return "Factura: Cliente " + _cliente.getNombreCompleto() + " - Fecha: " + _fechaFactura + " - Total: $" + String.format("%.2f", _total);
    }
}
