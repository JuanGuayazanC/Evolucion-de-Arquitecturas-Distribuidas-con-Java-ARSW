# Parte II — HTTP | Ejercicio: Sistema de Gestión de Salones vía HTTP

Servidor HTTP que expone las operaciones de gestión de salones como rutas web.
A diferencia del servidor TCP, cualquier cliente HTTP puede usarlo: un navegador, curl, Postman, etc.

## ¿Cómo compilar y ejecutar?

Desde la raíz del proyecto:

```bash
javac -cp src src/tcp/classroom/*.java src/http/classroom/ClassroomHttpServer.java
java -cp src http.classroom.ClassroomHttpServer
```

## Rutas disponibles

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/rooms` | Lista todos los salones y su estado |
| `GET` | `/rooms?id=E303` | Consulta un salón específico |
| `POST` | `/rooms/reserve?id=E303` | Reserva un salón |
| `POST` | `/rooms/release?id=E303` | Libera un salón |

## Cómo probarlo

**Consultar todos los salones** — desde el navegador:
```
http://localhost:8080/rooms
```

**Consultar un salón específico** — desde el navegador:
```
http://localhost:8080/rooms?id=E303
```

**Reservar o liberar** — desde PowerShell (el navegador solo hace GET):
```powershell
Invoke-WebRequest -Method POST "http://localhost:8080/rooms/reserve?id=E301" -UseBasicParsing | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method POST "http://localhost:8080/rooms/release?id=E301" -UseBasicParsing | Select-Object -ExpandProperty Content
```

## Códigos de respuesta HTTP

| Código | Significado | Cuándo ocurre |
|--------|-------------|---------------|
| `200` | OK | Operación exitosa |
| `400` | Bad Request | Falta el parámetro `?id=` |
| `404` | Not Found | El código de salón no existe |
| `409` | Conflict | Salón ya reservado (al reservar) o ya disponible (al liberar) |

## Por qué GET y POST son diferentes

- **GET** — solo consulta, no modifica nada en el servidor. Se puede repetir sin efectos secundarios.
- **POST** — modifica el estado del servidor. Reservar y liberar cambian el estado del salón, por eso usan POST.

Esta distinción es parte del estándar HTTP y permite que herramientas como navegadores, cachés y proxies sepan qué peticiones son seguras de repetir y cuáles no.

## Por qué el navegador no puede hacer POST

Los navegadores solo hacen `POST` desde formularios HTML. Para probar rutas `POST` directamente se necesita una herramienta como `curl`, PowerShell o Postman.

## ¿Qué es parsear?

Parsear es leer un texto y extraer información estructurada de él. Por ejemplo, el query string
`"id=E303"` es solo texto — parsearlo significa identificar el formato `clave=valor` y extraer
el valor `"E303"` para usarlo en el código. Los frameworks HTTP hacen esto automáticamente;
aquí lo hacemos a mano con `substring` y `startsWith`.

## Por qué substring(3) extrae el código del salón

El query string de `/rooms?id=E303` es `"id=E303"`. Los primeros 3 caracteres son `"id="`, entonces `query.substring(3)` retorna `"E303"` — elimina el prefijo y deja solo el código.

## Por qué se usa StringBuilder para listar todos los salones

`String` en Java es inmutable — cada concatenación (`+`) crea un nuevo objeto en memoria.
`StringBuilder` acumula el texto en un buffer interno y genera el String final una sola vez con `.toString()`.
Para construir texto en un bucle, `StringBuilder` es siempre la opción correcta.

## Preguntas de reflexión

**¿Qué ventajas ofrece HTTP frente a un protocolo de texto definido manualmente?**
HTTP es un estándar universal — cualquier cliente (navegador, Postman, app móvil) puede hablar con el servidor sin conocer detalles internos ni acuerdos previos. Con un protocolo manual como el de la Parte I, solo pueden comunicarse clientes que conozcan exactamente el formato `COMANDO,CODIGO`, lo que limita quién puede usar el sistema.

**¿Qué limitaciones tiene construir un servidor HTTP sin framework?**
Todo tiene que implementarse a mano: parsear rutas, distinguir métodos GET de POST, manejar errores, establecer cabeceras de respuesta y gestionar tipos de contenido. Un framework como Spring Boot abstrae todo eso y permite enfocarse en la lógica del negocio. Sin él, agregar autenticación, logging o validaciones requiere código adicional en cada handler.

**¿Cómo cambiaría esta solución si se usara JSON en lugar de texto plano?**
La respuesta de `/rooms?id=E303` pasaría de `E303: DISPONIBLE` a `{"code":"E303","state":"DISPONIBLE"}`. JSON es el estándar de intercambio de datos en la web — cualquier lenguaje tiene librerías para parsearlo automáticamente, lo que elimina la necesidad de acuerdos sobre el formato del texto y hace la API consumible desde JavaScript, Python, etc.

## Diferencia con el servidor TCP

| Aspecto | TCP (Parte I) | HTTP (Parte II) |
|---------|--------------|-----------------|
| Protocolo | Inventado por nosotros (`COMANDO,CODIGO`) | Estándar HTTP |
| Cliente | Solo programas que conozcan el protocolo | Cualquier cliente HTTP |
| Rutas | No existe el concepto | `/rooms`, `/rooms/reserve`, etc. |
| Verbos | No aplica | GET, POST |
| Errores | Texto libre | Códigos estándar (404, 409, etc.) |
