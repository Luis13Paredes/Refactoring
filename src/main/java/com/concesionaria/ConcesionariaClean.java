package com.concesionaria;

import com.concesionaria.application.service.ClienteService;
import com.concesionaria.application.service.FacturacionService;
import com.concesionaria.application.service.InteresClienteService;
import com.concesionaria.application.service.ReporteService;
import com.concesionaria.application.service.VehiculoService;
import com.concesionaria.domain.repository.IClienteRepository;
import com.concesionaria.domain.repository.IFacturaRepository;
import com.concesionaria.domain.repository.IInteresClienteRepository;
import com.concesionaria.domain.repository.IVehiculoRepository;
import com.concesionaria.infrastructure.config.AppConfig;
import com.concesionaria.infrastructure.persistence.ArchivoClienteRepository;
import com.concesionaria.infrastructure.persistence.ArchivoVehiculoRepository;
import com.concesionaria.infrastructure.persistence.MemoriaFacturaRepository;
import com.concesionaria.infrastructure.persistence.MemoriaInteresClienteRepository;
import com.concesionaria.presentation.console.input.ConsolaInput;
import com.concesionaria.presentation.console.menu.MenuCliente;
import com.concesionaria.presentation.console.menu.MenuConcesionaria;
import com.concesionaria.presentation.console.menu.MenuPrincipal;
import com.concesionaria.presentation.console.menu.MenuReporte;
import com.concesionaria.presentation.console.menu.MenuVehiculo;
import com.concesionaria.presentation.console.output.ReporteConsolaView;

/**
 * Punto de entrada principal (Bootstrap) del sistema ConcesionariaClean.
 * Ensambla manualmente las dependencias respetando Clean Architecture
 * y arranca el ciclo de ejecución de la aplicación.
 */
public class ConcesionariaClean {

    public static void main(String[] args) {
        // 1. Configuración de Infraestructura
        AppConfig config = new AppConfig();

        // 2. Repositorios de Persistencia (Infrastructure implementando Domain)
        IClienteRepository clienteRepository = new ArchivoClienteRepository(config);
        IVehiculoRepository vehiculoRepository = new ArchivoVehiculoRepository(config);
        IInteresClienteRepository interesRepository = new MemoriaInteresClienteRepository();
        IFacturaRepository facturaRepository = new MemoriaFacturaRepository();

        // 3. Servicios de Casos de Uso (Application orquestando Domain)
        ClienteService clienteService = new ClienteService(clienteRepository);
        VehiculoService vehiculoService = new VehiculoService(vehiculoRepository);
        InteresClienteService interesService = new InteresClienteService(clienteRepository, vehiculoRepository, interesRepository);
        FacturacionService facturacionService = new FacturacionService(clienteRepository, vehiculoRepository, interesRepository, facturaRepository);
        ReporteService reporteService = new ReporteService(clienteRepository, interesRepository, facturaRepository);

        // 4. Componentes de Interfaz de Usuario (Presentation)
        ConsolaInput input = new ConsolaInput();
        ReporteConsolaView reporteView = new ReporteConsolaView();

        MenuCliente menuCliente = new MenuCliente(clienteService, input);
        MenuVehiculo menuVehiculo = new MenuVehiculo(vehiculoService, input);
        MenuConcesionaria menuConcesionaria = new MenuConcesionaria(clienteService, vehiculoService, interesService, facturacionService, input);
        MenuReporte menuReporte = new MenuReporte(reporteService, reporteView, input);

        MenuPrincipal menuPrincipal = new MenuPrincipal(menuCliente, menuVehiculo, menuConcesionaria, menuReporte, input);

        // 5. Iniciar la aplicación
        menuPrincipal.ejecutar();
    }
}
