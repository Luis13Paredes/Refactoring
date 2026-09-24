package com.concesionaria.infrastructure.persistence;

import com.concesionaria.domain.entity.Vehiculo;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.repository.IVehiculoRepository;
import com.concesionaria.domain.valueobject.Placa;
import com.concesionaria.infrastructure.config.AppConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Implementación de IVehiculoRepository que gestiona el inventario de vehículos
 * cargando y sincronizando los datos en archivo plano (.txt / CSV).
 */
public class ArchivoVehiculoRepository implements IVehiculoRepository {

    private final AppConfig _config;
    private final Map<Placa, Vehiculo> _vehiculos;

    public ArchivoVehiculoRepository(AppConfig config) {
        this._config = (config != null) ? config : new AppConfig();
        this._vehiculos = new LinkedHashMap<>();
        cargarDesdeArchivo();
    }

    public ArchivoVehiculoRepository() {
        this(new AppConfig());
    }

    private void cargarDesdeArchivo() {
        File archivo = new File(_config.getRutaArchivoVehiculos());
        if (!archivo.exists()) {
            archivo = new File(_config.getDirectorioBase() + "vehiculos.txt");
        }

        if (!archivo.exists()) {
            return;
        }

        try (Scanner lector = new Scanner(archivo)) {
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (linea.isEmpty()) {
                    continue;
                }
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    try {
                        Placa placa = new Placa(datos[0].trim());
                        String tipo = datos[1].trim();
                        String marca = datos[2].trim();
                        String modelo = datos[3].trim();
                        String color = datos[4].trim();
                        int anio = Integer.parseInt(datos[5].trim());

                        Vehiculo vehiculo = new Vehiculo(placa, tipo, marca, modelo, color, anio);
                        _vehiculos.put(placa, vehiculo);
                    } catch (NumberFormatException | ReglaNegocioException ex) {
                        System.err.println("Línea omitida por formato inválido en vehículos: " + linea);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo leer el archivo de vehículos: " + e.getMessage());
        }
    }

    private void sincronizarArchivo() {
        File archivo = new File(_config.getRutaArchivoVehiculos());
        try {
            File carpeta = archivo.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            try (PrintWriter escritor = new PrintWriter(archivo)) {
                for (Vehiculo vehiculo : _vehiculos.values()) {
                    escritor.println(String.format("%s,%s,%s,%s,%s,%d",
                            vehiculo.getPlaca().getValor(),
                            vehiculo.getTipo(),
                            vehiculo.getMarca(),
                            vehiculo.getModelo(),
                            vehiculo.getColor(),
                            vehiculo.getAnio()));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error al sincronizar archivo de vehículos: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new ReglaNegocioException("El vehículo a guardar no puede ser nulo.");
        }
        if (_vehiculos.containsKey(vehiculo.getPlaca())) {
            throw new ReglaNegocioException("El vehículo con placa " + vehiculo.getPlaca() + " ya se encuentra registrado.");
        }
        _vehiculos.put(vehiculo.getPlaca(), vehiculo);
        sincronizarArchivo();
    }

    @Override
    public void actualizar(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new ReglaNegocioException("El vehículo a actualizar no puede ser nulo.");
        }
        if (!_vehiculos.containsKey(vehiculo.getPlaca())) {
            throw new ReglaNegocioException("No existe el vehículo con placa " + vehiculo.getPlaca() + " para actualizar.");
        }
        _vehiculos.put(vehiculo.getPlaca(), vehiculo);
        sincronizarArchivo();
    }

    @Override
    public void eliminar(Placa placa) {
        if (placa == null) {
            throw new ReglaNegocioException("La placa no puede ser nula.");
        }
        if (!_vehiculos.containsKey(placa)) {
            throw new ReglaNegocioException("No existe el vehículo con placa " + placa + " para eliminar.");
        }
        _vehiculos.remove(placa);
        sincronizarArchivo();
    }

    @Override
    public Optional<Vehiculo> buscarPorPlaca(Placa placa) {
        if (placa == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(_vehiculos.get(placa));
    }

    @Override
    public List<Vehiculo> listarTodos() {
        return Collections.unmodifiableList(new ArrayList<>(_vehiculos.values()));
    }

    @Override
    public boolean existePorPlaca(Placa placa) {
        if (placa == null) {
            return false;
        }
        return _vehiculos.containsKey(placa);
    }
}
