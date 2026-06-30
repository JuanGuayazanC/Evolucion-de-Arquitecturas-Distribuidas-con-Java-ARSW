# Parte III — RMI | Ejemplo: Movie Information System over RMI

Servicio de consulta de películas usando RMI (Remote Method Invocation).
El cliente llama un método que se ejecuta en otra JVM como si fuera local.

## ¿Cómo compilar y ejecutar?

Desde la raíz del proyecto:

**Terminal 1 — servidor:**
```bash
javac -cp src src/rmi/movie/*.java
java -cp src rmi.movie.MovieRmiServer
```

**Terminal 2 — cliente:**
```bash
java -cp src rmi.movie.MovieRmiClient
```

Cuando el cliente pregunte, ingresa `1`, `2` o `3`.

## Clases

| Clase | Responsabilidad |
|-------|----------------|
| `Movie` | Modelo de película. **Implementa `Serializable`** para viajar por la red |
| `MovieService` | Interfaz remota que **extiende `Remote`** |
| `MovieServiceImpl` | Implementación que **extiende `UnicastRemoteObject`** |
| `MovieRmiServer` | Crea el registry en puerto 23000 y registra el servicio |
| `MovieRmiClient` | Se conecta al registry y llama `findById` remotamente |

## Cómo funciona RMI en 4 pasos

```java
// 1. El servidor crea el registry y registra el servicio con un nombre
Registry registry = LocateRegistry.createRegistry(23000);
registry.rebind("MovieService", service);

// 2. El cliente se conecta al registry existente
Registry registry = LocateRegistry.getRegistry("localhost", 23000);

// 3. El cliente busca el servicio por su nombre y obtiene un "stub" (proxy)
MovieService service = (MovieService) registry.lookup("MovieService");

// 4. El cliente llama métodos como si fueran locales — Java los ejecuta en el servidor
Movie movie = service.findById(id);
```

## Qué es el stub

Cuando el cliente hace `lookup("MovieService")`, no recibe el objeto real — recibe un **stub**:
un proxy local que implementa la misma interfaz. Cada llamada al stub se traduce en una petición
de red al servidor, que ejecuta el método real y devuelve el resultado. El cliente nunca nota
que hay una red de por medio.

## Por qué Movie implementa Serializable

`findById` retorna un objeto `Movie` que debe viajar del servidor al cliente. RMI convierte el
objeto en bytes (serialización) para enviarlo y lo reconstruye al otro lado. Sin `Serializable`,
RMI lanza `NotSerializableException`.

## Diferencia con el ejemplo TCP y HTTP

| Aspecto | TCP | HTTP | RMI |
|---------|-----|------|-----|
| Cómo se invoca | Enviar texto `MOVIE:1` | `GET /movie?id=1` | `service.findById(1)` |
| Contrato | Convención de texto | Rutas | Interfaz Java |
| Respuesta | Texto plano | HTML/texto | Objeto Java |
| Interoperable | Solo quien conozca el protocolo | Cualquier cliente HTTP | Solo Java |
| Puerto | 35000 | 8080 | 23000 |
