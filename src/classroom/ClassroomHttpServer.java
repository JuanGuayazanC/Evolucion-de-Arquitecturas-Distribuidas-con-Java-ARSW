package classroom;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class ClassroomHttpServer {
    public static void main(String[] args) throws Exception {
        ClassroomRepository repository = new ClassroomRepository();
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/classrooms", new ClassroomHandler(repository));
        server.createContext("/classrooms/reserve", new ReserveHandler(repository));
        server.createContext("/classrooms/release", new ReleaseHandler(repository));
        server.setExecutor(null);
        server.start();
        System.out.println("ClassroomHttpServer escuchando en http://localhost:8081/classrooms");
    }

    static class ClassroomHandler implements HttpHandler {
        private final ClassroomRepository repository;
        ClassroomHandler(ClassroomRepository r) { this.repository = r; }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String response;
            if (query != null && query.startsWith("id=")) {
                String id = query.substring(3);
                classroom c = repository.findById(id);
                response = c == null
                        ? "<html><body><h1>ERROR: classroom no encontrado</h1></body></html>"
                        : "<html><body><h1>" + c.getId() + ": " + c.getStatus() + "</h1></body></html>";
            } else {
                StringBuilder sb = new StringBuilder("<html><body><h1>Classrooms</h1><ul>");
                for (String id : new String[]{"E301","E302","E303","E304"}) {
                    classroom c = repository.findById(id);
                    sb.append("<li>").append(c.getId()).append(": ").append(c.getStatus()).append("</li>");
                }
                sb.append("</ul></body></html>");
                response = sb.toString();
            }
            send(exchange, response);
        }
    }

    static class ReserveHandler implements HttpHandler {
        private final ClassroomRepository repository;
        ReserveHandler(ClassroomRepository r) { this.repository = r; }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) { send(exchange, "Metodo no permitido"); return; }
            String query = exchange.getRequestURI().getQuery();
            String response;
            if (query != null && query.startsWith("id=")) {
                String id = query.substring(3);
                classroom c = repository.findById(id);
                if (c == null) response = "ERROR_CLASSROOM_NO_EXISTE";
                else if (!c.isAvailable()) response = "ERROR_OPERACION_INVALIDA";
                else { c.reserve(); response = "RESERVA_EXITOSA"; }
            } else response = "ERROR_OPERACION_INVALIDA";
            send(exchange, response);
        }
    }

    static class ReleaseHandler implements HttpHandler {
        private final ClassroomRepository repository;
        ReleaseHandler(ClassroomRepository r) { this.repository = r; }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) { send(exchange, "Metodo no permitido"); return; }
            String query = exchange.getRequestURI().getQuery();
            String response;
            if (query != null && query.startsWith("id=")) {
                String id = query.substring(3);
                classroom c = repository.findById(id);
                if (c == null) response = "ERROR_CLASSROOM_NO_EXISTE";
                else if (c.isAvailable()) response = "ERROR_OPERACION_INVALIDA";
                else { c.release(); response = "LIBERACION_EXITOSA"; }
            } else response = "ERROR_OPERACION_INVALIDA";
            send(exchange, response);
        }
    }

    private static void send(HttpExchange exchange, String body) throws IOException {
        byte[] bytes = body.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
