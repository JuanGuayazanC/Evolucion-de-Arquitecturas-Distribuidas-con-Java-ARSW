# Parte II — HTTP | Ejemplo: Movie Information System over HTTP

Servidor HTTP que expone una ruta para consultar películas por ID.
A diferencia del servidor TCP, cualquier cliente HTTP puede usarlo sin conocer un protocolo inventado.

## ¿Cómo compilar y ejecutar?

Desde la raíz del proyecto:

```bash
javac -cp src src/tcp/movie/*.java src/http/movie/MovieHttpServer.java
java -cp src http.movie.MovieHttpServer
```

Luego abre el navegador en:

```
http://localhost:8080/movie?id=1
http://localhost:8080/movie?id=2
http://localhost:8080/movie?id=99
```

## Ruta disponible

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/movie?id=N` | Retorna los datos de la película con ese ID en HTML |

## Clases

| Clase | Responsabilidad |
|-------|----------------|
| `MovieHttpServer` | Crea el `HttpServer` en puerto 8080 y registra el handler |
| `MovieHandler` | Parsea el query string, consulta el repositorio y escribe la respuesta |

## Cómo funciona HttpServer

`com.sun.net.httpserver.HttpServer` es una clase incluida en el JDK que implementa el protocolo HTTP.
A diferencia de TCP donde manejábamos bytes a mano, aquí Java ya parsea la petición HTTP y nos entrega
la URI, el método y los headers listos para usar:

```java
// Crear el servidor en el puerto 8080
HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

// Registrar un handler para la ruta /movie
server.createContext("/movie", new MovieHandler(repository));

// Iniciar el servidor
server.start();
```

## Por qué setExecutor(null)

`setExecutor(null)` le dice a `HttpServer` que use el hilo por defecto del JDK para atender peticiones.
En producción se usaría un `ExecutorService` con un pool de hilos para manejar múltiples peticiones
en paralelo, pero para este ejemplo es suficiente.

## Por qué el handler recibe HttpExchange

`HttpExchange` encapsula tanto la petición como la respuesta en un solo objeto:
- `exchange.getRequestURI().getQuery()` — lee el query string (`id=1`)
- `exchange.sendResponseHeaders(200, length)` — establece el código de estado y el tamaño
- `exchange.getResponseBody()` — stream donde se escribe el cuerpo de la respuesta

## Diferencia con MovieServer TCP

| Aspecto | TCP (Parte I) | HTTP (Parte II) |
|---------|--------------|-----------------|
| Protocolo | `MOVIE:1` (inventado) | `GET /movie?id=1` (estándar HTTP) |
| Cliente | Solo `MovieClient.java` | Navegador, curl, Postman, etc. |
| Respuesta | Texto plano | HTML |
| Puerto | 35000 | 8080 |
