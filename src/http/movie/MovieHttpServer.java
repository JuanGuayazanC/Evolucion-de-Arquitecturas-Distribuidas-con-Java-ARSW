package http.movie;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tcp.movie.Movie;
import tcp.movie.MovieRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Parte II — HTTP | Ejemplo: Movie Information System over HTTP.
 *
 * <p>Servidor HTTP en el puerto 8080 que expone la ruta {@code GET /movie?id=N}.
 * A diferencia del servidor TCP, cualquier cliente HTTP puede consultarlo:
 * un navegador, curl, Postman, etc. — no hace falta un programa Java específico.
 */
public class MovieHttpServer {

    /**
     * Inicia el servidor HTTP y registra el handler para la ruta {@code /movie}.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si no se puede crear el servidor en el puerto 8080
     */
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        MovieRepository repository = new MovieRepository();
        server.createContext("/movie", new MovieHandler(repository));
        server.setExecutor(null);
        server.start();
        System.out.println("MovieHttpServer escuchando en http://localhost:8080/movie?id=1");
    }

    /**
     * Handler HTTP que procesa peticiones {@code GET /movie?id=N}.
     * Responde con los datos de la película en HTML o un mensaje de error.
     */
    static class MovieHandler implements HttpHandler {

        private MovieRepository repository;

        /**
         * @param repository repositorio de películas en memoria
         */
        public MovieHandler(MovieRepository repository) {
            this.repository = repository;
        }

        /**
         * Extrae el parámetro {@code id} del query string, busca la película
         * y escribe la respuesta HTML.
         *
         * @param exchange objeto que representa la petición y la respuesta HTTP
         */
        @Override
        public void handle(HttpExchange exchange) {
            try {
                String query = exchange.getRequestURI().getQuery();
                int id = extractId(query);
                Movie movie = repository.findById(id);
                String response;
                if (movie == null) {
                    response = "<html><body><h1>Película no encontrada</h1></body></html>";
                } else {
                    response = "<html><body><h1>" + movie.toText() + "</h1></body></html>";
                }
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Extrae el valor numérico del parámetro {@code id} del query string.
         *
         * @param query query string de la URL, por ejemplo {@code "id=1"}
         * @return el id como entero, o {@code -1} si el formato es inválido
         */
        private int extractId(String query) {
            if (query == null || !query.startsWith("id=")) {
                return -1;
            }
            return Integer.parseInt(query.substring(3));
        }
    }
}
