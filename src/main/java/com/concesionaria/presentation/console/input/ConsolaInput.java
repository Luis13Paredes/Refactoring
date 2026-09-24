package com.concesionaria.presentation.console.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Lector de entrada por consola centralizado y seguro.
 * Mantiene un único Scanner sobre System.in y nunca lo cierra para evitar
 * el bug de cierre accidental del flujo estándar de entrada.
 */
public class ConsolaInput {

    private final Scanner _scanner;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ConsolaInput() {
        this._scanner = new Scanner(System.in);
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = _scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingresa solo números enteros. Inténtalo de nuevo.");
            }
        }
    }

    public int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("¡El número debe estar entre " + min + " y " + max + "!");
        }
    }

    public double leerDouble(String mensaje, double min, double max) {
        while (true) {
            System.out.print(mensaje);
            String linea = _scanner.nextLine().trim().replace(',', '.');
            try {
                double valor = Double.parseDouble(linea);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("¡Ingrese una cantidad correcta entre " + min + " y " + max + "!");
            } catch (NumberFormatException e) {
                System.out.println("¡Ingresa números válidos!");
            }
        }
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return _scanner.nextLine().trim();
    }

    public String leerTextoObligatorio(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (!texto.isBlank()) {
                return texto;
            }
            System.out.println("¡Escriba correctamente! El campo no puede estar vacío.");
        }
    }

    public boolean confirmarSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String linea = _scanner.nextLine().trim().toUpperCase();
            if (!linea.isEmpty()) {
                char opcion = linea.charAt(0);
                if (opcion == 'S') {
                    return true;
                }
                if (opcion == 'N') {
                    return false;
                }
            }
            System.out.println("AVISO: S = Sí, N = No ...");
        }
    }

    public LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (Ejemplo: 02/01/2024): ");
            String linea = _scanner.nextLine().trim();
            try {
                LocalDate fecha = LocalDate.parse(linea, FORMATO_FECHA);
                if (!fecha.isAfter(LocalDate.now())) {
                    return fecha;
                }
                System.out.println("La fecha no debe superar a la fecha actual.");
            } catch (DateTimeParseException e) {
                System.out.println("¡Error! Debes ingresar correctamente dia/mes/año (dd/MM/yyyy).");
            }
        }
    }

    public void pausar() {
        System.out.println("\nPresione ENTER para continuar...");
        _scanner.nextLine();
    }
}
