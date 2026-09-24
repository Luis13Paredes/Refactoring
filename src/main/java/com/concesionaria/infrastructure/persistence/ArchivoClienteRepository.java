package com.concesionaria.infrastructure.persistence;

import com.concesionaria.domain.entity.Cliente;
import com.concesionaria.domain.exception.ReglaNegocioException;
import com.concesionaria.domain.repository.IClienteRepository;
import com.concesionaria.domain.valueobject.Cedula;
import com.concesionaria.domain.valueobject.Telefono;
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
 * Implementación de IClienteRepository que persiste y sincroniza datos
 * mediante archivos de texto plano (.txt / CSV) en el sistema de archivos.
 */
public class ArchivoClienteRepository implements IClienteRepository {

    private final AppConfig _config;
    private final Map<Cedula, Cliente> _clientes;

    public ArchivoClienteRepository(AppConfig config) {
        this._config = (config != null) ? config : new AppConfig();
        this._clientes = new LinkedHashMap<>();
        cargarDesdeArchivo();
    }

    public ArchivoClienteRepository() {
        this(new AppConfig());
    }

    private void cargarDesdeArchivo() {
        File archivo = new File(_config.getRutaArchivoClientes());
        if (!archivo.exists()) {
            archivo = new File(_config.getDirectorioBase() + "clientes.txt");
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
                if (datos.length >= 5) {
                    try {
                        Cedula cedula = new Cedula(datos[0].trim());
                        String nombre = datos[1].trim();
                        String apellido = datos[2].trim();
                        Telefono telefono = new Telefono(datos[3].trim());
                        String direccion = datos[4].trim();

                        Cliente cliente = new Cliente(cedula, nombre, apellido, telefono, direccion);
                        _clientes.put(cedula, cliente);
                    } catch (ReglaNegocioException ex) {
                        System.err.println("Línea omitida por formato inválido en clientes: " + linea);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo leer el archivo de clientes: " + e.getMessage());
        }
    }

    private void sincronizarArchivo() {
        File archivo = new File(_config.getRutaArchivoClientes());
        try {
            File carpeta = archivo.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            try (PrintWriter escritor = new PrintWriter(archivo)) {
                for (Cliente cliente : _clientes.values()) {
                    escritor.println(String.format("%s,%s,%s,%s,%s",
                            cliente.getCedula().getValor(),
                            cliente.getNombre(),
                            cliente.getApellido(),
                            cliente.getTelefono().getValor(),
                            cliente.getDireccion()));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error al sincronizar archivo de clientes: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Cliente cliente) {
        if (cliente == null) {
            throw new ReglaNegocioException("El cliente a guardar no puede ser nulo.");
        }
        if (_clientes.containsKey(cliente.getCedula())) {
            throw new ReglaNegocioException("El cliente con cédula " + cliente.getCedula() + " ya se encuentra registrado.");
        }
        _clientes.put(cliente.getCedula(), cliente);
        sincronizarArchivo();
    }

    @Override
    public void actualizar(Cliente cliente) {
        if (cliente == null) {
            throw new ReglaNegocioException("El cliente a actualizar no puede ser nulo.");
        }
        if (!_clientes.containsKey(cliente.getCedula())) {
            throw new ReglaNegocioException("No existe el cliente con cédula " + cliente.getCedula() + " para actualizar.");
        }
        _clientes.put(cliente.getCedula(), cliente);
        sincronizarArchivo();
    }

    @Override
    public void eliminar(Cedula cedula) {
        if (cedula == null) {
            throw new ReglaNegocioException("La cédula no puede ser nula.");
        }
        if (!_clientes.containsKey(cedula)) {
            throw new ReglaNegocioException("No existe el cliente con cédula " + cedula + " para eliminar.");
        }
        _clientes.remove(cedula);
        sincronizarArchivo();
    }

    @Override
    public Optional<Cliente> buscarPorCedula(Cedula cedula) {
        if (cedula == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(_clientes.get(cedula));
    }

    @Override
    public List<Cliente> listarTodos() {
        return Collections.unmodifiableList(new ArrayList<>(_clientes.values()));
    }

    @Override
    public boolean existePorCedula(Cedula cedula) {
        if (cedula == null) {
            return false;
        }
        return _clientes.containsKey(cedula);
    }
}
