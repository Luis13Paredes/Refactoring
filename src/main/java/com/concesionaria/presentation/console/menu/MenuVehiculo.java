package com.concesionaria.presentation.console.menu;

import com.concesionaria.application.dto.VehiculoDTO;
import com.concesionaria.application.service.VehiculoService;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.presentation.console.input.ConsolaInput;
import com.concesionaria.presentation.console.output.ConsolaOutput;

import java.util.List;

/**
 * Submenú interactivo para la gestión de Vehículos en inventario.
 */
public class MenuVehiculo {

    private final VehiculoService _vehiculoService;
    private final ConsolaInput _input;

    public MenuVehiculo(VehiculoService vehiculoService, ConsolaInput input) {
        this._vehiculoService = vehiculoService;
        this._input = input;
    }

    public void mostrar() {
        int opcion;
        do {
            ConsolaOutput.imprimirSubtitulo("Menú Vehículos");
            System.out.println("1. Crear Vehículo");
            System.out.println("2. Actualizar Vehículo");
            System.out.println("3. Eliminar Vehículo");
            System.out.println("4. Buscar Vehículo");
            System.out.println("5. Lista Vehículos");
            System.out.println("6. Regresar");

            opcion = _input.leerEntero("Ingrese la opción deseada: ");

            switch (opcion) {
                case 1 -> crearVehiculo();
                case 2 -> actualizarVehiculo();
                case 3 -> eliminarVehiculo();
                case 4 -> buscarVehiculo();
                case 5 -> listarVehiculos();
                case 6 -> { /* Regresa al menú principal */ }
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (opcion != 6);
    }

    private void crearVehiculo() {
        ConsolaOutput.imprimirSubtitulo("Crear Vehículo");
        try {
            String placa = _input.leerTextoObligatorio("Ingresar placa (6 caracteres): ");
            String tipo = _input.leerTextoObligatorio("Tipo de Vehículo (ej. SUV, Sedan, Camioneta): ");
            String marca = _input.leerTextoObligatorio("Marca: ");
            String modelo = _input.leerTextoObligatorio("Modelo: ");
            String color = _input.leerTextoObligatorio("Color: ");
            int anio = _input.leerEnteroEnRango("Año del Vehículo: ", 1900, 2100);

            _vehiculoService.registrarVehiculo(placa, tipo, marca, modelo, color, anio);
            ConsolaOutput.imprimirExito("Vehículo registrado exitosamente.");
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void actualizarVehiculo() {
        ConsolaOutput.imprimirSubtitulo("Actualizar Vehículo");
        try {
            String placa = _input.leerTextoObligatorio("Ingresar placa del vehículo a actualizar: ");
            VehiculoDTO actual = _vehiculoService.buscarVehiculo(placa);
            System.out.println("Vehículo actual: " + actual.toString());

            String tipo = _input.leerTextoObligatorio("Nuevo Tipo: ");
            String marca = _input.leerTextoObligatorio("Nueva Marca: ");
            String modelo = _input.leerTextoObligatorio("Nuevo Modelo: ");
            String color = _input.leerTextoObligatorio("Nuevo Color: ");
            int anio = _input.leerEnteroEnRango("Nuevo Año: ", 1900, 2100);

            _vehiculoService.actualizarVehiculo(placa, tipo, marca, modelo, color, anio);
            ConsolaOutput.imprimirExito("Vehículo actualizado exitosamente.");
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void eliminarVehiculo() {
        ConsolaOutput.imprimirSubtitulo("Eliminar Vehículo");
        try {
            String placa = _input.leerTextoObligatorio("Ingresar placa del vehículo a eliminar: ");
            VehiculoDTO actual = _vehiculoService.buscarVehiculo(placa);
            System.out.println("Vehículo encontrado: " + actual.toString());

            if (_input.confirmarSiNo("¿Está seguro de eliminar este vehículo?")) {
                _vehiculoService.eliminarVehiculo(placa);
                ConsolaOutput.imprimirExito("Vehículo eliminado correctamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void buscarVehiculo() {
        ConsolaOutput.imprimirSubtitulo("Buscar Vehículo");
        try {
            String placa = _input.leerTextoObligatorio("Ingresar placa a buscar: ");
            VehiculoDTO v = _vehiculoService.buscarVehiculo(placa);
            System.out.println(v.toString());
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void listarVehiculos() {
        ConsolaOutput.imprimirSubtitulo("Listado de Vehículos Disponibles");
        List<VehiculoDTO> lista = _vehiculoService.listarVehiculos();
        if (lista.isEmpty()) {
            System.out.println("No hay vehículos registrados en inventario.");
        } else {
            for (VehiculoDTO v : lista) {
                System.out.println(v.toString());
            }
        }
    }
}
