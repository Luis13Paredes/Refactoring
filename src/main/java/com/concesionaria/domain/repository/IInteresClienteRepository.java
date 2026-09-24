package com.concesionaria.domain.repository;

import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.entity.InteresCliente;
import com.concesionaria.domain.valueobject.Cedula;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia para el registro histórico de intereses de clientes.
 */
public interface IInteresClienteRepository {

    void registrarOActualizar(InteresCliente interesCliente);

    Optional<InteresCliente> buscarPorCliente(Cliente cliente);

    Optional<InteresCliente> buscarPorCedula(Cedula cedula);

    List<InteresCliente> listarTodos();
}
