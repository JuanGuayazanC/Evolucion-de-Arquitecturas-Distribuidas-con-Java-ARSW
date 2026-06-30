# Parte III — RMI | Ejercicio: Inventario de Laboratorios

Sistema de inventario de equipos de laboratorio usando RMI (Remote Method Invocation).
El cliente llama métodos que se ejecutan en otra JVM como si fueran locales: consultar,
reservar y liberar equipos.

## ¿Cómo compilar y ejecutar?

Desde la raíz del proyecto:

**Terminal 1 — servidor:**
```bash
javac -cp src src/rmi/lab/*.java
java -cp src rmi.lab.LabRmiServer
```

**Terminal 2 — cliente:**
```bash
java -cp src rmi.lab.LabRmiClient
```

El cliente muestra un menú interactivo con las cuatro operaciones.

## Operaciones remotas

| Método | Descripción | Respuestas |
|--------|-------------|-----------|
| `consultarEquipos()` | Lista todos los equipos | Lista de `Equipment` |
| `consultarEquipo(code)` | Consulta uno por código | `Equipment` o `null` |
| `reservarEquipo(code)` | Reserva un equipo | `RESERVA_EXITOSA` · `ERROR_EQUIPO_NO_EXISTE` · `ERROR_OPERACION_INVALIDA` |
| `liberarEquipo(code)` | Libera un equipo | `LIBERACION_EXITOSA` · `ERROR_EQUIPO_NO_EXISTE` · `ERROR_OPERACION_INVALIDA` |

## Clases

| Clase | Responsabilidad |
|-------|----------------|
| `EquipmentState` | Enum con los estados: `DISPONIBLE`, `RESERVADO` |
| `Equipment` | Modelo de un equipo. **Implementa `Serializable`** para viajar por la red |
| `LabService` | Interfaz remota que **extiende `Remote`** |
| `LabServiceImpl` | Implementación que **extiende `UnicastRemoteObject`** |
| `LabRmiServer` | Crea el registry en puerto 23001 y registra el servicio |
| `LabRmiClient` | Se conecta al registry y llama los métodos remotos |

## Por qué Equipment implementa Serializable

En RMI, cuando un método remoto retorna un objeto (como `Equipment`), ese objeto debe viajar
por la red desde el servidor hasta el cliente. Para convertirlo en bytes y reconstruirlo al
otro lado, Java exige que la clase implemente `Serializable`. Sin eso, RMI lanza
`NotSerializableException` al intentar enviarlo.

```java
public class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;
    ...
}
```

El `serialVersionUID` es un identificador de versión: garantiza que la clase que serializa
y la que deserializa sean compatibles.

## Por qué la interfaz extiende Remote y los métodos lanzan RemoteException

`Remote` marca la interfaz como invocable de forma remota. Cada método declara
`throws RemoteException` porque una llamada de red puede fallar (timeout, conexión perdida),
y Java obliga a manejar esa posibilidad.

## Por qué LabServiceImpl extiende UnicastRemoteObject

`UnicastRemoteObject` exporta el objeto a la red — lo hace accesible para llamadas remotas.
Su constructor lanza `RemoteException` porque la exportación puede fallar.

## Preguntas de reflexión

**¿Qué cambió al pasar de HTTP a RMI?**
Cambió el mecanismo de comunicación. En HTTP el cliente hace peticiones a rutas (`GET /rooms`)
y recibe texto; en RMI el cliente llama métodos directamente (`service.reservarEquipo("PC-01")`)
como si el objeto fuera local. RMI oculta la red: no se ven rutas, ni verbos, ni códigos HTTP —
solo llamadas a métodos Java. A cambio, se pierde la interoperabilidad universal de HTTP.

**¿Dónde está definido el contrato de comunicación?**
En la interfaz `LabService`. A diferencia del protocolo de texto de TCP (que vivía en
convenciones implícitas) o de las rutas de HTTP, aquí el contrato es una interfaz Java formal
que el compilador verifica. Cliente y servidor comparten esa interfaz, y cualquier cambio en
ella se detecta en tiempo de compilación, no en ejecución.

**¿Qué problemas tendría este sistema si un cliente no está escrito en Java?**
No podría funcionar. RMI es una tecnología específica de Java: usa serialización Java y el
protocolo JRMP, que otros lenguajes no entienden. Un cliente en Python, JavaScript o C# no
puede consumir un servicio RMI directamente. Esta es la gran limitación de RMI frente a HTTP
o gRPC, que sí son multiplataforma — y es justo lo que motiva la siguiente parte del taller.
