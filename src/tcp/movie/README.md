# Parte I — Sockets TCP | Ejemplo: Movie Information System

## ¿Qué problema resuelve este estilo?

La arquitectura cliente-servidor con sockets TCP permite que dos programas se comuniquen
a través de la red usando un canal de bytes. En este ejemplo, el cliente envía un mensaje
de texto con el ID de una película y el servidor responde con los datos de esa película.

El protocolo es completamente manual: nosotros definimos el formato del mensaje (`MOVIE:id`)
y el formato de la respuesta (`id,titulo,director,año`). Esto es lo más cercano a la
comunicación de red "a nivel de cables".

## Flujo de comunicación

```
Cliente Java
    |
    |  "MOVIE:1"   (texto plano por TCP)
    v
Servidor TCP (puerto 35000)
    |
    |  "1,Interstellar,Christopher Nolan,2014"
    v
Cliente muestra el resultado
```

## Clases

| Clase | Responsabilidad |
|-------|----------------|
| `Movie` | Modelo de datos: id, título, director, año |
| `MovieRepository` | Almacén en memoria usando `HashMap<Integer, Movie>` |
| `MovieServer` | Escucha en el puerto 35000, acepta conexiones y responde |
| `MovieClient` | Se conecta al servidor y envía una consulta |

## Por qué `HashMap` y no `ArrayList`

Un `HashMap` permite buscar por clave (`id`) en tiempo O(1).
Con un `ArrayList` habría que recorrer todos los elementos para encontrar el id correcto: O(n).
Para un repositorio en memoria donde la operación principal es buscar por id, el `HashMap` es la estructura correcta.

## Cómo compilar y ejecutar

Desde la raíz del proyecto (`src/` debe estar marcado como Sources Root):

**Terminal 1 — compilar y levantar el servidor:**
```bash
javac src/tcp/movie/*.java
java -cp src tcp.movie.MovieServer
```

**Terminal 2 — ejecutar el cliente:**
```bash
java -cp src tcp.movie.MovieClient
```

Cuando el cliente pregunte el ID, ingresa `1`, `2` o `3`.

**Resultado esperado:**
```
Ingrese el ID de la pelicula: 1
Respuesta del servidor: 1,Interstellar,Christopher Nolan,2014
```

## Limitaciones de este estilo

- El protocolo (`MOVIE:id`) no está definido en ningún archivo formal: vive en convenciones de texto.
- Agregar una nueva operación requiere modificar el código del servidor y acordar el nuevo formato manualmente.
- No hay control de concurrencia: si dos clientes se conectan al mismo tiempo, el segundo espera a que el primero termine.
- Solo clientes escritos en Java (o que conozcan el protocolo) pueden hablar con el servidor.
