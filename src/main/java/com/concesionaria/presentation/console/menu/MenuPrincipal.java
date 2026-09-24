package com.concesionaria.presentation.console.menu;

import com.concesionaria.presentation.console.input.ConsolaInput;
import com.concesionaria.presentation.console.output.ConsolaOutput;

/**
 * Menú Principal raíz de la aplicación de consola.
 * Actúa como enrutador delegando la interacción a los submenús especializados.
 */
public class MenuPrincipal {

    private final MenuCliente _menuCliente;
    private final MenuVehiculo _menuVehiculo;
    private final MenuConcesionaria _menuConcesionaria;
    private final MenuReporte _menuReporte;
    private final ConsolaInput _input;

    public MenuPrincipal(MenuCliente menuCliente,
                         MenuVehiculo menuVehiculo,
                         MenuConcesionaria menuConcesionaria,
                         MenuReporte menuReporte,
                         ConsolaInput input) {
        this._menuCliente = menuCliente;
        this._menuVehiculo = menuVehiculo;
        this._menuConcesionaria = menuConcesionaria;
        this._menuReporte = menuReporte;
        this._input = input;
    }

    public void ejecutar() {
        int opcion;
        do {
            ConsolaOutput.imprimirTitulo("SISTEMA DE GESTIÓN - CONCESIONARIA");
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Cliente");
            System.out.println("2. Vehiculo");
            System.out.println("3. Concesionaria");
            System.out.println("4. Reportes");
            System.out.println("5. Salir");

            opcion = _input.leerEntero("Ingrese la opción deseada: ");

            switch (opcion) {
                case 1 -> _menuCliente.mostrar();
                case 2 -> _menuVehiculo.mostrar();
                case 3 -> _menuConcesionaria.mostrar();
                case 4 -> _menuReporte.mostrar();
                case 5 -> System.out.println("\n¡Gracias por utilizar el sistema de la Concesionaria! Hasta pronto.");
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (opcion != 5);
    }
}
