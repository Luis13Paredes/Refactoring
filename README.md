# Refactorización del Proyecto Concesionaria: Antes y Después

## 1. Resumen

Este documento describe el proceso de **refactorización aplicando arquitectura N-Capas (Clean Architecture)** al proyecto original de una concesionaria de vehículos desarrollado en Java.

| Metrica               | Proyecto ORIGINAL                      | Proyecto REFACTORIZADO                |
|-----------------------|----------------------------------------|---------------------------------------|
| Nombre                | `Concesionaria`                        | `ConcesionariaClean`                  |
| Ubicacion             | `OriginalConcesionaria/`               | `ConcesionariaClean/`                 |
| Paquete base          | `Concesionaria` (plano, 1 solo paquete)| `com.concesionaria` (4 capas)         |
| Numero de clases Java | 11                                     | 38                                    |
| Lineas de codigo      | 1.824                                  | 2.983                                 |
| Arquitectura          | Sin separacion (God classes / mezcla)  | N-Capas: Domain, Application, Infrastructure, Presentation |
| Compilacion           | Correcta                               | Correcta (verificada)                 |


## 2. Proyecto ANTES (Original)

### Estructura original

```
src/main/java/Concesionaria/
├── Main.java                -> Punto de entrada (crea Gestor e inicia menu)
├── Gestor.java              -> Todos los menus (Cliente, Vehiculo, Concesionaria, Reportes)
├── CRUD_Entidades.java      -> CRUD de Clientes y Vehiculos + controles de entrada (Scanner)
├── CargarDatosExternos.java -> Lectura inicial de archivos .txt con rutas hardcodeadas
├── Cliente.java             -> Entidad Cliente
├── Vehiculo.java            -> Entidad Vehiculo
├── CompraVehiculo.java      -> Item de compra (vehiculo + fecha + precio)
├── Factura.java             -> Entidad Factura
├── InteresCliente.java      -> Entidad Interes de cliente
├── Concesionario.java       -> Logica de negocio (interes, facturacion, filtros)
└── Reporte.java             -> Los 3 reportes de negocio
```

### Caracteristicas del codigo original

- **Un solo paquete** `Concesionaria` con todo mezclado: entidades, CRUD, menus, entrada/salida por consola y reglas de negocio.
- Las clases `Gestor`, `CRUD_Entidades` y `Concesionario` son **God classes**: concentran demasiadas responsabilidades.
- La **entrada de usuario (Scanner) se creaba una y otra vez** dentro de bucles (`new Scanner(System.in)` repetido).
- Los **menus** se anidaban llamando recursivamente a `MenuPrincipal()` desde los submenus.
- La **persistencia solo leia los archivos .txt una vez al inicio**; los CRUD en memoria **no guardaban cambios** en disco.
- Validacion de cédula/telefono/placa mediante metodos duplicados (ControlCedula, ControlTelefono, ControlString, ControlNumeros, ControlPlaca) todos en `CRUD_Entidades`.
- Las **rutas de los archivos** estaban hardcodeadas: `D:\Listas Concesionaria\archivoClientes.txt` y `archivoVehiculos.txt`.
- `catch (Exception e) { e.getMessage(); }` que **ignoraba silenciosamente los errores**.
- Sin Javadoc, sin inmutabilidad, sin igualdad por identidad definida.

---

## 3. Proyecto DESPUES (Refactorizado)

### Estructura por capas (N-Capas / Clean Architecture)

```
src/main/java/com/concesionaria/
├── ConcesionariaClean.java            -> Bootstrap: ensambla dependencias y arranca
│
├── presentation/console/              (Capa de Presentacion)
│   ├── input/ConsolaInput.java        -> Lectura segura y centralizada de consola
│   ├── output/ConsolaOutput.java      -> Formato estetico de salida
│   ├── output/ReporteConsolaView.java -> Vista exclusiva para imprimir reportes
│   └── menu/
│       ├── MenuPrincipal.java         -> Menu raiz (enrutador)
│       ├── MenuCliente.java           -> CRUD de clientes
│       ├── MenuVehiculo.java          -> CRUD de vehiculos
│       ├── MenuConcesionaria.java     -> Interes + facturacion (flujo guiado)
│       └── MenuReporte.java           -> Solicita parametros y presenta reportes
│
├── application/                       (Capa de Aplicacion)
│   ├── dto/                           -> DTO inmutables
│   │   ├── ClienteDTO.java
│   │   ├── VehiculoDTO.java
│   │   ├── FacturaDTO.java
│   │   ├── InteresClienteDTO.java
│   │   └── ReporteVentaDTO.java
│   └── service/                       -> Casos de uso (logica orquestada)
│       ├── ClienteService.java
│       ├── VehiculoService.java
│       ├── InteresClienteService.java
│       ├── FacturacionService.java
│       └── ReporteService.java
│
├── domain/                            (Capa de Dominio)
│   ├── entity/
│   │   ├── Cliente.java
│   │   ├── Vehiculo.java
│   │   ├── Factura.java
│   │   ├── InteresCliente.java
│   │   └── ItemCompra.java            (reemplaza a CompraVehiculo)
│   ├── valueobject/
│   │   ├── Cedula.java
│   │   ├── Placa.java
│   │   └── Telefono.java
│   ├── exception/
│   │   ├── ReglaNegocioException.java
│   │   └── EntidadNoEncontradaException.java
│   └── repository/
│       ├── IClienteRepository.java
│       ├── IVehiculoRepository.java
│       ├── IInteresClienteRepository.java
│       └── IFacturaRepository.java
│
└── infrastructure/                    (Capa de Infraestructura)
    ├── config/AppConfig.java          -> Rutas de archivos centralizadas
    └── persistence/
        ├── ArchivoClienteRepository.java    -> Clientes en .txt (CSV)
        ├── ArchivoVehiculoRepository.java   -> Vehiculos en .txt (CSV)
        ├── MemoriaFacturaRepository.java    -> Facturas en memoria
        └── MemoriaInteresClienteRepository.java -> Intereses en memoria
```

### Reglas de dependencia

La regla de dependencia apunta siempre **hacia adentro** (hacia el dominio):

```
Presentation  ->  Application  ->  Domain  <-  Infrastructure (implementa)
```

- **Domain**: entidades, value objects, excepciones y contratos (interfaces) de repositorios. No depende de nada.
- **Application**: servicios de casos de uso que orquestan las entidades y usan los contratos. No conoce consola ni archivos.
- **Infrastructure**: implementa los repositorios (archivo o memoria) y la configuracion.
- **Presentation**: menus, entrada y salida. Usa unicamente DTOs y servicios.

---

## 4. Mapeo de funcionalidad (antes -> despues)

| Clase ORIGINAL              | Clase(s) REFACTORIZADA(s)                                                                      |Capa                      |
|-----------------------------|------------------------------------------------------------------------------------------------|--------------------------|
| `Main.java`                 | `ConcesionariaClean.java`                                                                      | Bootstrap                |
| `Gestor.java`               | `MenuPrincipal`, `MenuCliente`, `MenuVehiculo`, `MenuConcesionaria`, `MenuReporte`             | Presentation             |
| `CRUD_Entidades.java`       | `ClienteService`, `VehiculoService`                                                            | Application              |
| `CargarDatosExternos.java`  | `ArchivoClienteRepository`, `ArchivoVehiculoRepository`, `AppConfig`                           | Infrastructure           |
| `Cliente.java`              | `Cliente` + `ClienteDTO`                                                                       | Domain/Application       |
| `Vehiculo.java`             | `Vehiculo` + `VehiculoDTO`                                                                     | Domain/Application       |
| `CompraVehiculo.java`       | `ItemCompra`                                                                                   | Domain                   |
| `Factura.java`              | `Factura` + `FacturaDTO`                                                                       | Domain/Application       |
| `InteresCliente.java`       | `InteresCliente` + `InteresClienteDTO`                                                         | Domain/Application       |
| `Concesionario.java`        | `InteresClienteService` + `FacturacionService`                                                 | Application              |
| `Reporte.java`              | `ReporteService` + `ReporteConsolaView`                                                        | Application/Presentation |
| *(no existia)*              | `Cedula`, `Placa`, `Telefono` (Value Objects)                                                  | Domain                   |
| *(no existia)*              | `IClienteRepository`, `IVehiculoRepository`, `IInteresClienteRepository`, `IFacturaRepository` | Domain                   |
| *(no existia)*              | `ReglaNegocioException`, `EntidadNoEncontradaException`                                        | Domain                   |
| *(no existia)*              | `MemoriaFacturaRepository`, `MemoriaInteresClienteRepository`                                  | Infrastructure           |
| *(no existia)*              | `ConsolaInput`, `ConsolaOutput`, `ReporteConsolaView`                                          | Presentation             |

Las funcionalidades del sistema se conservan intactas: 
CRUD de clientes y vehiculos, registro de interes con filtros guiados (tipo -> modelo -> marca/color/año), facturacion con descuento de inventario y los 3 reportes (clientes interesados sin compra en ultimos 3 años, historial por cliente, vehiculos vendidos en un rango de fechas).



# 5. Cambios realizados

# 5.1 Arquitectura N-Capas
Se separo todo el codigo en 4 capas con responsabilidades claras y dependencias dirigidas al dominio, eliminando las god classes `Gestor`, `CRUD_Entidades` y `Concesionario`.

# 5.2 Patron Repository
Se definieron interfaces en el dominio (`IClienteRepository`, `IVehiculoRepository`, `IFacturaRepository`, `IInteresClienteRepository`) y se implementaron en infraestructura.

# 5.3 Data Transfer Objects (DTO)
La presentacion ya no trabaja con entidades de dominio: recibe DTO inmutables (`ClienteDTO`, `VehiculoDTO`, `FacturaDTO`, `InteresClienteDTO`, `ReporteVentaDTO`).

# 5.4 Value Objects con validacion
`Cedula`, `Telefono` y `Placa` encapsulan y validan su formato (10 digitos numericos, 10 digitos numericos y 6 caracteres respectivamente) de forma centralizada, eliminando los controles duplicados de `CRUD_Entidades`.

### 5.5 Manejo de excepciones
Se crearon `ReglaNegocioException` y `EntidadNoEncontradaException`. Reglas de negocio validadas al construir cada entidad (año 1900–2100, precio > 0, campos no vacios). Se elimino el `catch (Exception e) { e.getMessage(); }` que tragaba errores.

### 5.6 Inyeccion de dependencias
`ConcesionariaClean` (bootstrap) construye los repositorios e inyecta los servicios y menus, sin usar el estado global que antes se compartia por referencia.

### 5.7 Persistencia real
Antes los .txt solo se leian al inicio; los cambios (crear/actualizar/eliminar) se perdian al cerrar. Ahora `ArchivoClienteRepository` y `ArchivoVehiculoRepository` **sincronizan el archivo despues de cada operacion**.

### 5.8 Entrada/Salida centralizada
`ConsolaInput` mantiene **un unico Scanner** (corrige la creacion repetida de Scanners), con metodos robustos: `leerEntero`, `leerEnteroEnRango`, `leerDouble`, `leerTextoObligatorio`, `confirmarSiNo`, `leerFecha` (dd/MM/yyyy) y `pausar`.

### 5.9 Igualdad por identidad
`Cliente.equals/hashCode` por cedula; `Vehiculo.equals/hashCode` por placa (antes se comparaban referencias, lo que rompia `contains`/`remove` en varios puntos).

### 5.10 Javadoc y legibilidad
Todas las clases y metodos publicos documentados, nombres descriptivos y responsabilidad unica.

---

## 6. Errores del proyecto original encontrados y corregidos

1. **Validacion de cédula/telefono rota por desbordamiento** — En `CRUD_Entidades.ControlCedula` y `ControlTelefono` se hacía `Integer.parseInt()` sobre cédulas/telefonos de 10 digitos (`9.999.999.999` no cabe en un `int`, maximo `2.147.483.647`). Una cédula valida como `1712345678` era rechazada. Corregido con Value Objects que validan por longitud y expresion regular `\d{10}`.

2. **Reporte de vehiculos vendidos incluia facturas "de hoy" fuera del rango** — En `Reporte.ObtenerListaFacturas`/`ObtenerListaVehiculos` había `... || factura.getFechaFactura().equals(LocalDate.now())`, que metia las facturas **del dia actual aunque no estuvieran entre las fechas elegidas**. Corregido con `listarPorRangoFechas` de rango inclusivo correcto.

3. **Reporte de clientes interesados dependia del orden de iteracion** — `Reporte.ListaClientesUlt3Anio` agrega si la compra es anterior a 3 años y luego quita si hay compra reciente; el resultado variaba segun el orden y ademas excluia clientes sin compras que tambien cumplen la regla. Corregido deterministamente en `ReporteService.obtenerClientesInteresadosSinComprasRecientes`.

4. **El pom.xml original apuntaba a una clase main inexistente** — `exec.mainClass` = `com.mycompany.concesionaria.Concesionaria` (clase que nunca existio; el main real era `Concesionaria.Main`). El jar Maven no era ejecutable. Corregido: `com.concesionaria.ConcesionariaClean`.

5. **El total de factura se duplicaba** — `Factura.PrecioTotal()` acumulaba sobre `this.total` (`this.total +=`); al llamarlo dos veces el total se duplicaba. Corregido: el total se calcula una sola vez en el constructor (`calcularTotal`).

6. **Los cambios de CRUD no se guardaban** — Crear/actualizar/eliminar clientes y vehiculos solo modificaba listas en memoria. Corregido sincronizando el archivo tras cada operacion.

7. **Menus con recursion infinita potencial** — Los submenus llamaban a `MenuPrincipal()` nuevamente, creando recusion en la pila. Corregido con bucles y opcion de salida.

8. **Creacion repetida de Scanners** — Se generaba `new Scanner(System.in)` dentro de cada bucle y metodo. Corregido con un unico Scanner en `ConsolaInput`.

9. **Excepciones silenciadas** — `catch (Exception e) { e.getMessage(); }` ejecutaba `getMessage()` sin mostrar nada. Ahora se lanzan/traducen errores descriptivos con `[ERROR]`.

10. **`CargarDatosExternos` devolvia `null` ante errores** — Riesgo de `NullPointerException` en cascada. Corregido con `Optional`, manejo defensivo y mensajes de linea omitida.

---

## 7. Lo NUEVO que se implemento (ademas de la refactorizacion)

1. **Value Objects** (`Cedula`, `Placa`, `Telefono`) con validacion inmutable.
2. **DTO inmutables** para toda la comunicacion con la interfaz (antes se exponian las entidades directamente).
3. **Patron Repository** con interfaces en dominio e implementaciones intercambiables en infraestructura (archivo o memoria).
4. **Inyeccion de dependencias** manual en el punto de arranque (composicion del arbol de objetos).
5. **Excepciones de dominio** tipadas conectadas a mensajes claros.
6. **Persistencia funcional de clientes y vehiculos** (sincronizacion con archivo plano tras cada CRUD).
7. **Validacion de reglas de negocio en las entidades** (año en 1900–2100, precio mayor a cero, campos obligatorios, formatos de cédula/telefono/placa).
8. **Reportes como servicio puro** (`ReporteService`) sin nada de `System.out`, separado de la vista (`ReporteConsolaView`).
9. **Componentes de consola reutilizables** (`ConsolaInput`, `ConsolaOutput`) con formato de titulos, subtitulos, avisos `[OK]`, `[AVISO]`, `[ERROR]` y validaciones de rango.
10. **Menu de interes/factura rediseñado** con filtros guiados paso a paso y confirmaciones explicitas, manteniendo la logica de filtrado multicriterio.
11. **Javadoc completo** en todas las clases publicas.

---

## 8. Como compilar y ejecutar

### Requisitos
- JDK 21+ (el proyecto declara `maven.compiler.release` = 27; si usas JDK 21 se reduce a release 21 sin problemas).
- Maven.

### Compilar
```bash
mvn clean compile
```

### Ejecutar
```bash
mvn compile exec:java
```
o bien
```bash
java -cp target/classes com.concesionaria.ConcesionariaClean
```

### Configuracion de archivos de datos
Las rutas de los archivos de clientes y vehiculos estan centralizadas en `com.concesionaria.infrastructure.config.AppConfig`
(ruta base por defecto: `F:\Ruta _Cliente\`). Si la letra de la unidad no existe en tu maquina, ajusta la constante
`RUTA_BASE_DEFECTO` o usa el constructor `AppConfig(String directorioBase)`.

---

## 9. Conclusiones

La refactorizacion no solo reorganizo el codigo en capas: **corrigio errores de logica (validacion de cédula, totales duplicados, reportes con rangos incorrectos), hizo la persistencia realmente funcional**, y aporto mejores practicas (inmutabilidad, Value Objects, DTO, Repository, inyeccion de dependencias, manejo de excepciones) sin perder ninguna funcionalidad del sistema original.

- **Antes**: 11 clases mezcladas, 1.824 lineas, sin separacion de responsabilidades, varios bugs.
- **Despues**: 38 clases en 4 capas, 2.983 lineas, mismo comportamiento de negocio + validaciones y persistencia robusta.