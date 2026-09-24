package com.concesionaria.domain.repository;

import com.concesionaria.domain.entity.Vehiculo;
import com.concesionaria.domain.valueobject.Placa;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia para la entidad Vehículo.
 * Define las operaciones requeridas para administrar el inventario de la concesionaria.
 */
public interface IVehiculoRepository {

    void guardar(Vehiculo vehiculo);

    void actualizar(Vehiculo vehiculo);

    void eliminar(Placa placa);

    Optional<Vehiculo> buscarPorPlaca(Placa placa);

    List<Vehiculo> listarTodos();

    boolean existePorPlaca(Placa placa);
}
