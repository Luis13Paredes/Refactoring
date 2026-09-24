package com.concesionaria.infrastructure.config;

import java.io.File;

/**
 * Configuración técnica y rutas físicas del sistema.
 * Desacopla las rutas del disco del resto de la aplicación.
 */
public final class AppConfig {

    private static final String RUTA_BASE_DEFECTO = "F:\\Ruta _Cliente\\";
    private static final String ARCHIVO_CLIENTES_DEFECTO = "archivoClientes.txt";
    private static final String ARCHIVO_VEHICULOS_DEFECTO = "archivoVehiculos.txt";

    private final String _directorioBase;

    public AppConfig() {
        this._directorioBase = RUTA_BASE_DEFECTO;
    }

    public AppConfig(String directorioBase) {
        this._directorioBase = (directorioBase != null && !directorioBase.isBlank())
                ? (directorioBase.endsWith(File.separator) ? directorioBase : directorioBase + File.separator)
                : RUTA_BASE_DEFECTO;
    }

    public String getDirectorioBase() {
        return _directorioBase;
    }

    public String getRutaArchivoClientes() {
        return _directorioBase + ARCHIVO_CLIENTES_DEFECTO;
    }

    public String getRutaArchivoVehiculos() {
        return _directorioBase + ARCHIVO_VEHICULOS_DEFECTO;
    }
}
