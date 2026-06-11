package classroom;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class RoomHttpServer {
    public static void main(String[] args) throws Exception {
        SalonRepository repository = new SalonRepository();
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/rooms", new RoomHandler(repository));
        server.createContext("/rooms/reserve", new ReserveHandler(repository));
        server.createContext("/rooms/release", new ReleaseHandler(repository));
        server.setExecutor(null);
        server.start();
        System.out.println("RoomHttpServer escuchando en http://localhost:8081/rooms");
    }

    static class RoomHandler implements HttpHandler {
        private final SalonRepository repository;
        RoomHandler(SalonRepository r) { this.repository = r; }

        @Override
        public void handle(HttpExchange exchange) throws java.io.IOException {
            String query = exchange.getRequestURI().getQuery();
            String response;
            if (query != null && query.startsWith("id=")) {
                String id = query.substring(3);
                Salon salon = repository.findById(id);
                response = salon == null
                        ? "<html><body><h1>ERROR: salon no encontrado</h1></body></html>"
                        : "<html><body><h1>" + salon.getId() + ": " + salon.getStatus() + "</h1></body></html>";
            } else {
                StringBuilder sb = new StringBuilder("<html><body><h1>Salones</h1><ul>");
                for (String id : new String[]{"E301","E302","E303","E304"}) {
                    Salon s = repository.findById(id);
                    sb.append("<li>").append(s.getId()).append(": ").append(s.getStatus()).append("</li>");
                }
                sb.append("</ul></body></html>");
                response = sb.toString();
            }
            send(exchange, response);
        }
    }

    static class ReserveHandler implements HttpHandler {
        private final SalonRepository repository;
        ReserveHandler(SalonRepository r) { this.repository = r; }

        @Override
        public void handle(HttpExchange exchange) throws java.io.IOException {
            if (!"POST".equals(exchange.getRequestMethod())) { send(exchange, "Metodo no permitido"); return; }
            String query = exchange.getRequestURI().getQuery();
            String response;
            if (query != null && query.startsWith("id=")) {
                String id = query.substring(3);
                Salon salon = repository.findById(id);
                if (salon == null) response = "ERROR_SALON_NO_EXISTE";
                else if (!salon.isAvailable()) response = "ERROR_OPERACION_INVALIDA";
                else { salon.reserve(); response = "RESERVA_EXITOSA"; }
            } else { response = "ERROR_OPERACION_INVALIDA"; }
            send(exchange, response);
        }
    }

    static class ReleaseHandler implements HttpHandler {
        private final SalonRepository repository;
        ReleaseHandler(SalonRepository r) { this.repository = r; }

        @Override
        public void handle(HttpExchange exchange) throws java.io.IOException {
            if (!"POST".equals(exchange.getRequestMethod())) { send(exchange, "Metodo no permitido"); return; }
            String query = exchange.getRequestURI().getQuery();
            String response;
            if (query != null && query.startsWith("id=")) {
                String id = query.substring(3);
                Salon salon = repository.findById(id);
                if (salon == null) response = "ERROR_SALON_NO_EXISTE";
                else if (salon.isAvailable()) response = "ERROR_OPERACION_INVALIDA";
                else { salon.release(); response = "LIBERACION_EXITOSA"; }
            } else { response = "ERROR_OPERACION_INVALIDA"; }
            send(exchange, response);
        }
    }

    private static void send(HttpExchange exchange, String body) throws java.io.IOException {
        byte[] bytes = body.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
