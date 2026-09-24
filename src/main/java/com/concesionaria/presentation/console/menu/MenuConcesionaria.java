package com.concesionaria.presentation.console.menu;

import com.concesionaria.application.dto.ClienteDTO;
import com.concesionaria.application.dto.FacturaDTO;
import com.concesionaria.application.dto.InteresClienteDTO;
import com.concesionaria.application.dto.VehiculoDTO;
import com.concesionaria.application.service.ClienteService;
import com.concesionaria.application.service.FacturacionService;
import com.concesionaria.application.service.InteresClienteService;
import com.concesionaria.application.service.VehiculoService;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.presentation.console.input.ConsolaInput;
import com.concesionaria.presentation.console.output.ConsolaOutput;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Submenú interactivo para las operaciones comerciales de la Concesionaria:
 * 1. Registro de Interés del Cliente con filtro guiado multicriterio.
 * 2. Facturación y venta con descuento de inventario.
 */
public class MenuConcesionaria {

    private final ClienteService _clienteService;
    private final VehiculoService _vehiculoService;
    private final InteresClienteService _interesService;
    private final FacturacionService _facturacionService;
    private final ConsolaInput _input;

    public MenuConcesionaria(ClienteService clienteService,
                             VehiculoService vehiculoService,
                             InteresClienteService interesService,
                             FacturacionService facturacionService,
                             ConsolaInput input) {
        this._clienteService = clienteService;
        this._vehiculoService = vehiculoService;
        this._interesService = interesService;
        this._facturacionService = facturacionService;
        this._input = input;
    }

    public void mostrar() {
        int opcion;
        do {
            ConsolaOutput.imprimirSubtitulo("Menú Concesionaria");
            System.out.println("1. Hacer interés cliente");
            System.out.println("2. Hacer Factura");
            System.out.println("3. Regresar");

            opcion = _input.leerEntero("Ingrese la opción deseada: ");

            switch (opcion) {
                case 1 -> hacerInteresCliente();
                case 2 -> hacerFactura();
                case 3 -> { /* Regresar al menú principal */ }
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (opcion != 3);
    }

    private void hacerInteresCliente() {
        ConsolaOutput.imprimirTitulo("Registrar Interés del Cliente");

        String cedula = _input.leerTextoObligatorio("Escriba la cédula del cliente: ");
        ClienteDTO cliente;
        try {
            cliente = _clienteService.buscarCliente(cedula);
            System.out.println("Cliente: " + cliente.toString());
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError("Cliente no encontrado: " + e.getMessage());
            return;
        }

        List<VehiculoDTO> inventario = _vehiculoService.listarVehiculos();
        if (inventario.isEmpty()) {
            ConsolaOutput.imprimirAviso("No hay vehículos disponibles en inventario.");
            return;
        }

        List<String> placasSeleccionadas = new ArrayList<>();
        boolean continuarAgregando = true;

        while (continuarAgregando) {
            List<VehiculoDTO> candidatos = new ArrayList<>(inventario);

            // Filtro N° 1: Tipo
            List<String> tipos = _vehiculoService.obtenerTiposDisponibles(candidatos);
            System.out.println("\nEscoge el tipo de Vehículo (por número):");
            for (int i = 0; i < tipos.size(); i++) {
                System.out.println((i + 1) + ".- " + tipos.get(i));
            }
            int posTipo = _input.leerEnteroEnRango("Escoge un número: ", 1, tipos.size());
            String tipoElegido = tipos.get(posTipo - 1);
            candidatos = _vehiculoService.filtrarPorTipo(candidatos, tipoElegido);

            // Filtro N° 2: Modelo
            List<String> modelos = _vehiculoService.obtenerModelosDisponibles(candidatos);
            System.out.println("\nEscoge el modelo del Vehículo (por número):");
            for (int i = 0; i < modelos.size(); i++) {
                System.out.println((i + 1) + ".- " + modelos.get(i));
            }
            int posModelo = _input.leerEnteroEnRango("Escoge un número: ", 1, modelos.size());
            String modeloElegido = modelos.get(posModelo - 1);
            candidatos = _vehiculoService.filtrarPorModelo(candidatos, modeloElegido);

            // Filtro N° 3: Marca / Color / Año
            List<String> marcas = _vehiculoService.obtenerMarcasDisponibles(candidatos);
            System.out.println("\nEscoge la Marca del Vehículo (por número):");
            for (int i = 0; i < marcas.size(); i++) {
                System.out.println((i + 1) + ".- " + marcas.get(i));
            }
            int posMarca = _input.leerEnteroEnRango("Escoge un número: ", 1, marcas.size());
            String marcaElegida = marcas.get(posMarca - 1);

            List<String> colores = _vehiculoService.obtenerColoresDisponibles(candidatos);
            System.out.println("\nEscoge el Color del Vehículo (por número):");
            for (int i = 0; i < colores.size(); i++) {
                System.out.println((i + 1) + ".- " + colores.get(i));
            }
            int posColor = _input.leerEnteroEnRango("Escoge un número: ", 1, colores.size());
            String colorElegido = colores.get(posColor - 1);

            List<Integer> anios = _vehiculoService.obtenerAniosDisponibles(candidatos);
            System.out.println("\nEscoge el Año del Vehículo (por número):");
            for (int i = 0; i < anios.size(); i++) {
                System.out.println((i + 1) + ".- " + anios.get(i));
            }
            int posAnio = _input.leerEnteroEnRango("Escoge un número: ", 1, anios.size());
            int anioElegido = anios.get(posAnio - 1);

            candidatos = _vehiculoService.filtrarPorMarcaColorAnio(candidatos, marcaElegida, colorElegido, anioElegido);

            if (!candidatos.isEmpty()) {
                VehiculoDTO vehiculoElegido = candidatos.get(0);
                System.out.println("\nVehículo encontrado: " + vehiculoElegido.toString());

                if (_input.confirmarSiNo("¿Desea añadir este Vehículo a la lista?")) {
                    if (!placasSeleccionadas.contains(vehiculoElegido.getPlaca())) {
                        placasSeleccionadas.add(vehiculoElegido.getPlaca());
                        ConsolaOutput.imprimirExito("Vehículo añadido al interés.");
                    } else {
                        ConsolaOutput.imprimirAviso("Este vehículo ya lo agregaste...");
                    }
                }
            } else {
                System.out.println("No se encontró ningún vehículo con esa combinación exacta.");
            }

            continuarAgregando = _input.confirmarSiNo("¿Desea añadir otro Vehículo?");
        }

        if (placasSeleccionadas.isEmpty()) {
            System.out.println("No se seleccionó ningún vehículo de interés.");
            return;
        }

        try {
            InteresClienteDTO interesRegistrado = _interesService.registrarInteres(cedula, placasSeleccionadas, LocalDate.now());
            ConsolaOutput.imprimirTitulo("¡Interés Realizado!");
            System.out.println("Cliente: " + interesRegistrado.getNombreCliente());
            System.out.println("Fecha: " + interesRegistrado.getFecha());
            System.out.println("Vehículos de interés:");
            for (VehiculoDTO v : interesRegistrado.getVehiculos()) {
                System.out.println("  " + v.toString());
            }
            ConsolaOutput.imprimirSeparador();

            if (_input.confirmarSiNo("¿Desea facturar estos datos ahora?")) {
                hacerFacturaParaCliente(cedula);
            }
        } catch (ReglaNegocioException e) {
            ConsolaOutput.imprimirError(e.getMessage());
        }
    }

    private void hacerFactura() {
        ConsolaOutput.imprimirTitulo("Registrar facturación para el Cliente");
        String cedula = _input.leerTextoObligatorio("Escriba la cédula del cliente: ");
        hacerFacturaParaCliente(cedula);
    }

    private void hacerFacturaParaCliente(String cedula) {
        Optional<InteresClienteDTO> interesOpt = _interesService.buscarInteresPorCedula(cedula);
        if (interesOpt.isEmpty() || interesOpt.get().getVehiculos().isEmpty()) {
            ConsolaOutput.imprimirAviso("El cliente no tiene vehículos de interés registrados. Primero registre un interés.");
            return;
        }

        InteresClienteDTO interes = interesOpt.get();
        System.out.println("Cliente: " + interes.getNombreCliente());

        Map<String, Double> vehiculosAFacturar = new LinkedHashMap<>();

        for (VehiculoDTO v : interes.getVehiculos()) {
            System.out.println("\n" + v.toString());

            if (_vehiculoService.existeVehiculo(v.getPlaca())) {
                if (_input.confirmarSiNo("¿Desea facturar este Vehículo?")) {
                    double precio = _input.leerDouble("Escriba el valor del vehículo: ", 100.0, 1000000.0);
                    vehiculosAFacturar.put(v.getPlaca(), precio);
                }
            } else {
                System.out.println("Vehículo ya vendido o no disponible en inventario: Placa " + v.getPlaca());
            }
        }

        if (vehiculosAFacturar.isEmpty()) {
            ConsolaOutput.imprimirAviso("No hay vehículos seleccionados para facturar.");
            return;
        }

        if (_input.confirmarSiNo("¿Desea Realizar esta factura?")) {
            try {
                FacturaDTO facturaEmitida = _facturacionService.emitirFactura(cedula, vehiculosAFacturar, LocalDate.now());
                ConsolaOutput.imprimirTitulo("¡Factura Realizada!");
                System.out.println("Cliente: " + facturaEmitida.getNombreCliente() + " (Cédula: " + facturaEmitida.getCedulaCliente() + ")");
                System.out.println("Fecha: " + facturaEmitida.getFechaFactura());
                System.out.println("Detalles de compra:");
                for (String detalle : facturaEmitida.getDetallesItems()) {
                    System.out.println("  " + detalle);
                }
                ConsolaOutput.imprimirSeparador();
                System.out.printf("Total Factura: $%.2f%n", facturaEmitida.getTotal());
                ConsolaOutput.imprimirSeparador();
                ConsolaOutput.imprimirExito("Venta completada. Los vehículos vendidos han sido descontados del inventario.");
            } catch (ReglaNegocioException e) {
                ConsolaOutput.imprimirError("Error al facturar: " + e.getMessage());
            }
        } else {
            System.out.println("Facturación cancelada.");
        }
    }
}
