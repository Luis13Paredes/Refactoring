package com.concesionaria.domain.repository;

import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.valueobject.Cedula;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia para la entidad Cliente.
 * Define las operaciones que el dominio y la aplicación necesitan sin acoplarse a detalles técnicos.
 */
public interface IClienteRepository {

    void guardar(Cliente cliente);

    void actualizar(Cliente cliente);

    void eliminar(Cedula cedula);

    Optional<Cliente> buscarPorCedula(Cedula cedula);

    List<Cliente> listarTodos();

    boolean existePorCedula(Cedula cedula);
}
