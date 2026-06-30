package http.classroom;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import http.movie.MovieHttpServer;
import tcp.classroom.Classroom;
import tcp.classroom.ClassroomRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Parte II — HTTP | Ejercicio: Classroom Management System over HTTP.
 *
 * <p>Servidor HTTP en el puerto 8080 que expone cuatro rutas:
 * <ul>
 *   <li>{@code GET  /rooms}              — lista todos los salones y su estado</li>
 *   <li>{@code GET  /rooms?id=E303}      — consulta un salón específico</li>
 *   <li>{@code POST /rooms/reserve?id=E303} — reserva un salón</li>
 *   <li>{@code POST /rooms/release?id=E303} — libera un salón</li>
 * </ul>
 */
public class ClassroomHttpServer {

    /**
     * Inicia el servidor HTTP y registra los handlers para las rutas de salones.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si no se puede crear el servidor en el puerto 8080
     */
    public static void main(String[] args) throws Exception {
        ClassroomRepository repository = new ClassroomRepository();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/rooms", new RoomsHandler(repository));
        server.createContext("/rooms/reserve", new ReserveHandler(repository));
        server.createContext("/rooms/release", new ReleaseHandler(repository));
        server.setExecutor(null);
        server.start();
        System.out.println("ClassroomHttpServer listening on http://localhost:8080/rooms");
    }

    /**
     * Handler para {@code GET /rooms} y {@code GET /rooms?id=E303}.
     * Si hay query string con id, retorna ese salón. Si no, lista todos.
     */
    static class RoomsHandler implements HttpHandler {

        private final ClassroomRepository repository;

        public RoomsHandler(ClassroomRepository repository) {
            this.repository = repository;
        }

        /**
         * @param exchange objeto que representa la petición y la respuesta HTTP
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.startsWith("id=")) {
                String codigo = query.substring(3);
                Classroom classroom = repository.findByCode(codigo);
                if (classroom == null) {
                    sendResponse(exchange, 404, "ERROR_SALON_NO_EXISTE");
                } else {
                    sendResponse(exchange, 200, classroom.getCode() + ": " + (classroom.isAvailable() ? "DISPONIBLE" : "RESERVADO"));
                }
            } else {
                StringBuilder sb = new StringBuilder();
                for (Classroom c : repository.findAll()) {
                    sb.append(c.getCode()).append(": ").append(c.isAvailable() ? "DISPONIBLE" : "RESERVADO").append("\n");
                }
                sendResponse(exchange, 200, sb.toString());
            }
        }
    }

    /**
     * Handler para {@code POST /rooms/reserve?id=E303}.
     * Reserva el salón indicado si está disponible.
     */
    static class ReserveHandler implements HttpHandler {

        private final ClassroomRepository repository;

        ReserveHandler(ClassroomRepository repository) {
            this.repository = repository;
        }

        /**
         * @param exchange objeto que representa la petición y la respuesta HTTP
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.startsWith("id=")) {
                sendResponse(exchange, 400, "Missing parameter. Use ?id=E303");
                return;
            }
            String codigo = query.substring(3);
            Classroom classroom = repository.findByCode(codigo);
            if (classroom == null) {
                sendResponse(exchange, 404, "ERROR_SALON_NO_EXISTE");
            } else if (!classroom.isAvailable()) {
                sendResponse(exchange, 409, "ERROR_OPERACION_INVALIDA");
            } else {
                classroom.reserve();
                sendResponse(exchange, 200, "RESERVA_EXITOSA");
            }
        }
    }

    /**
     * Handler para {@code POST /rooms/release?id=E303}.
     * Libera el salón indicado si está reservado.
     */
    static class ReleaseHandler implements HttpHandler {

        private final ClassroomRepository repository;

        ReleaseHandler(ClassroomRepository repository) {
            this.repository = repository;
        }

        /**
         * @param exchange objeto que representa la petición y la respuesta HTTP
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.startsWith("id=")) {
                sendResponse(exchange, 400, "Missing parameter. Use ?id=E303");
                return;
            }
            String codigo = query.substring(3);
            Classroom classroom = repository.findByCode(codigo);
            if (classroom == null) {
                sendResponse(exchange, 404, "ERROR_SALON_NO_EXISTE");
            } else if (classroom.isAvailable()) {
                sendResponse(exchange, 409, "ERROR_OPERACION_INVALIDA");
            } else {
                classroom.release();
                sendResponse(exchange, 200, "LIBERACION_EXITOSA");
            }
        }
    }

    /**
     * Escribe una respuesta HTTP con el código y cuerpo indicados.
     *
     * @param exchange   objeto HTTP
     * @param statusCode código de estado HTTP (200, 404, 409, etc.)
     * @param body       cuerpo de la respuesta en texto plano
     */
    static void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes();
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
