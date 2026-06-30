# Parte I — Sockets TCP | Ejercicio: Sistema de Gestión de Salones

Sistema cliente-servidor TCP para consultar, reservar y liberar salones (E301–E304).
El cliente envía un comando de texto y el servidor responde con el resultado de la operación.

## ¿Cómo compilar y ejecutar?

Desde la raíz del proyecto:

**Terminal 1 — servidor:**
```bash
javac src/tcp/classroom/*.java
java -cp src tcp.classroom.ClassroomServer
```

**Terminal 2 — cliente:**
```bash
java -cp src tcp.classroom.ClassroomClient
```

Cuando el cliente pregunte, escribe el comando completo con el código del salón:

```
Ingrese la operacion y codigo (ej: CONSULTAR_SALON,E303): RESERVAR_SALON,E302
Respuesta del servidor: RESERVA_EXITOSA
```

## Protocolo

| Comando enviado | Respuestas posibles |
|----------------|---------------------|
| `CONSULTAR_SALON,E303` | `SALON_DISPONIBLE` · `SALON_RESERVADO` · `ERROR_SALON_NO_EXISTE` |
| `RESERVAR_SALON,E303` | `RESERVA_EXITOSA` · `ERROR_SALON_NO_EXISTE` · `ERROR_OPERACION_INVALIDA` |
| `LIBERAR_SALON,E303` | `LIBERACION_EXITOSA` · `ERROR_SALON_NO_EXISTE` · `ERROR_OPERACION_INVALIDA` |

## Clases

| Clase | Responsabilidad |
|-------|----------------|
| `State` | Enum con los estados posibles: `DISPONIBLE`, `RESERVADO` |
| `Classroom` | Modelo de un salón: código y estado |
| `ClassroomRepository` | Mapa en memoria con los salones E301–E304 |
| `ClassroomServer` | Escucha en puerto 35001, acepta un cliente a la vez y procesa el comando |
| `ClassroomClient` | Se conecta al servidor, envía el comando del usuario y muestra la respuesta |

## Por qué enum en lugar de boolean o String

Tres opciones para representar el estado de un salón:

- `boolean disponible` — solo dos valores, pero si se agrega un tercer estado (por ejemplo `EN_MANTENIMIENTO`) hay que reescribir toda la lógica de condiciones.
- `String estado` — flexible pero inseguro: nadie impide escribir `"Disponiblee"` y el compilador no lo detecta.
- `enum State` — el compilador garantiza que solo se usen valores válidos. Agregar `EN_MANTENIMIENTO` es una línea; no hay que tocar los `if`.

## Por qué el null check va antes de los if de comando

```java
Classroom classroom = repository.findByCode(codigo);
if (classroom == null) return "ERROR_SALON_NO_EXISTE";  // sale aquí si no existe

if (comando.equals("CONSULTAR_SALON")) { ... }
else if (comando.equals("RESERVAR_SALON")) { ... }
else if (comando.equals("LIBERAR_SALON")) { ... }
```

Si el salón no existe, no tiene sentido evaluar el comando. Salir temprano (`early return`)
evita repetir la misma verificación dentro de cada rama y elimina el riesgo de `NullPointerException`.

## Por qué se usa `.equals()` y no `==` para comparar Strings

`==` compara referencias de memoria, no el contenido del texto. Dos Strings con el mismo
texto pueden estar en posiciones distintas de memoria y `==` retornaría `false`.
`.equals()` compara el contenido carácter a carácter, que es lo que necesitamos.

```java
// MAL — puede fallar aunque el texto sea igual
if (comando == "CONSULTAR_SALON") { ... }

// BIEN
if (comando.equals("CONSULTAR_SALON")) { ... }
```

## Limitaciones de este estilo

- El protocolo vive en convenciones de texto: si el cliente escribe `consultar_salon` en minúsculas el servidor no lo reconoce.
- No hay concurrencia: si dos clientes se conectan al mismo tiempo, el segundo espera a que el primero termine.
- El estado de los salones se pierde al reiniciar el servidor (solo vive en memoria).
