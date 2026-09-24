package com.concesionaria.infrastructure.persistence;

import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.entity.Factura;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.repository.IFacturaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación en memoria de IFacturaRepository.
 * Almacena las facturas emitidas en la sesión y permite consultas por cliente y rango de fechas.
 */
public class MemoriaFacturaRepository implements IFacturaRepository {

    private final List<Factura> _facturas;

    public MemoriaFacturaRepository() {
        this._facturas = new ArrayList<>();
    }

    @Override
    public void guardar(Factura factura) {
        if (factura == null) {
            throw new ReglaNegocioException("La factura a guardar no puede ser nula.");
        }
        _facturas.add(factura);
    }

    @Override
    public List<Factura> listarTodas() {
        return Collections.unmodifiableList(new ArrayList<>(_facturas));
    }

    @Override
    public List<Factura> listarPorCliente(Cliente cliente) {
        if (cliente == null) {
            return Collections.emptyList();
        }
        List<Factura> facturasCliente = new ArrayList<>();
        for (Factura f : _facturas) {
            if (f.getCliente().equals(cliente)) {
                facturasCliente.add(f);
            }
        }
        return Collections.unmodifiableList(facturasCliente);
    }

    @Override
    public List<Factura> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return Collections.emptyList();
        }
        List<Factura> resultado = new ArrayList<>();
        for (Factura f : _facturas) {
            LocalDate fechaFactura = f.getFechaFactura();
            boolean dentroDeRango = !fechaFactura.isBefore(fechaInicio) && !fechaFactura.isAfter(fechaFin);
            if (dentroDeRango) {
                resultado.add(f);
            }
        }
        return Collections.unmodifiableList(resultado);
    }
}
