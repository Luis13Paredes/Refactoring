package com.concesionaria.presentation.console.menu;

import com.concesionaria.application.dto.ClienteDTO;
import com.concesionaria.application.service.ClienteService;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.presentation.console.input.ConsolaInput;
import com.concesionaria.presentation.console.output.ConsolaOutput;

import java.util.List;

/**
 * Submenú interactivo para la gestión de Clientes.
 */
public class MenuCliente {

    private final ClienteService _clienteService;
    private final ConsolaInput _input;

    public MenuCliente(ClienteService clienteService, ConsolaInput input) {
        this._clienteService = clienteService;
        this._input = input;
    }

    public void mostrar() {
        int opcion;
        do {
            ConsolaOutput.imprimirSubtitulo("Menú Clientes");
            System.out.println("1. Crear Cliente");
            System.out.println("2. Actualizar Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("4. Buscar Cliente");
            System.out.println("5. Lista Cliente");
            System.out.println("6. Regresar");

            opcion = _input.leerEntero("Ingrese la opción deseada: ");

            switch (opcion) {
                case 1 -> crearCliente();
                case 2 -> actualizarCliente();
                case 3 -> eliminarCliente();
                case 4 -> buscarCliente();
                case 5 -> listarClientes();
                case 6 -> { /* Regresa al menú principal */ }
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (opcion != 6);
    }

    private void crearCliente() {
        ConsolaOutput.imprimirSubtitulo("Crear Cliente");
        try {
            String cedula = _input.leerTextoObligatorio("Ingresar cédula (10 dígitos): ");
            String nombre = _input.leerTextoObligatorio("Nombre: ");
            String apellido = _input.leerTextoObligatorio("Apellido: ");
            String telefono = _input.leerTextoObligatorio("Teléfono (10 dígitos): ");
            String direccion = _input.leerTextoObligatorio("Dirección: ");

            _clienteService.registrarCliente(cedula, nombre, apellido, telefono, direccion);
            ConsolaOutput.imprimirExito("Cliente registrado exitosamente.");
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void actualizarCliente() {
        ConsolaOutput.imprimirSubtitulo("Actualizar Cliente");
        try {
            String cedula = _input.leerTextoObligatorio("Ingresar cédula del cliente a actualizar: ");
            ClienteDTO actual = _clienteService.buscarCliente(cedula);
            System.out.println("Datos actuales: " + actual.toString());

            String nombre = _input.leerTextoObligatorio("Nuevo Nombre: ");
            String apellido = _input.leerTextoObligatorio("Nuevo Apellido: ");
            String telefono = _input.leerTextoObligatorio("Nuevo Teléfono (10 dígitos): ");
            String direccion = _input.leerTextoObligatorio("Nueva Dirección: ");

            _clienteService.actualizarCliente(cedula, nombre, apellido, telefono, direccion);
            ConsolaOutput.imprimirExito("Cliente actualizado exitosamente.");
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void eliminarCliente() {
        ConsolaOutput.imprimirSubtitulo("Eliminar Cliente");
        try {
            String cedula = _input.leerTextoObligatorio("Ingresar cédula del cliente: ");
            ClienteDTO actual = _clienteService.buscarCliente(cedula);
            System.out.println("Cliente encontrado: " + actual.getNombreCompleto());

            if (_input.confirmarSiNo("¿Está seguro de eliminar?")) {
                _clienteService.eliminarCliente(cedula);
                ConsolaOutput.imprimirExito("Cliente eliminado correctamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void buscarCliente() {
        ConsolaOutput.imprimirSubtitulo("Buscar Cliente");
        try {
            String cedula = _input.leerTextoObligatorio("Ingresar cédula a buscar: ");
            ClienteDTO cliente = _clienteService.buscarCliente(cedula);
            System.out.println(cliente.toString());
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void listarClientes() {
        ConsolaOutput.imprimirSubtitulo("Listado de Clientes");
        List<ClienteDTO> clientes = _clienteService.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            for (ClienteDTO c : clientes) {
                System.out.println(c.toString());
            }
        }
    }
}
