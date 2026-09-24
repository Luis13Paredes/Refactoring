package com.concesionaria.presentation.console.output;

/**
 * Utilidad para la presentación visual y estética en la terminal de consola.
 */
public class ConsolaOutput {

    public static void imprimirTitulo(String titulo) {
        System.out.println("\n========================================================");
        System.out.println("   " + titulo);
        System.out.println("========================================================");
    }

    public static void imprimirSubtitulo(String subtitulo) {
        System.out.println("\n----- " + subtitulo + " -----");
    }

    public static void imprimirExito(String mensaje) {
        System.out.println("\n[OK] " + mensaje);
    }

    public static void imprimirAviso(String mensaje) {
        System.out.println("\n[AVISO] " + mensaje);
    }

    public static void imprimirError(String mensaje) {
        System.out.println("\n[ERROR] " + mensaje);
    }

    public static void imprimirSeparador() {
        System.out.println("----------------------------------------------------------------------------------------");
    }
}
