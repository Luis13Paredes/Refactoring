package com.concesionaria.domain.repository;

import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.entity.Factura;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de persistencia para las facturas emitidas por la concesionaria.
 */
public interface IFacturaRepository {

    void guardar(Factura factura);

    List<Factura> listarTodas();

    List<Factura> listarPorCliente(Cliente cliente);

    List<Factura> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
}
