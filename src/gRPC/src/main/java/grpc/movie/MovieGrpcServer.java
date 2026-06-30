package grpc.movie;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Parte IV — gRPC | Ejemplo: Movie Information System over gRPC.
 *
 * <p>Servidor gRPC en el puerto 50051 que implementa el servicio definido en
 * {@code movie.proto}. Responde peticiones {@code GetMovie} con los datos de la película.
 */
public class MovieGrpcServer {

    /**
     * Arranca el servidor gRPC y queda esperando peticiones hasta que el proceso termina.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si el servidor no puede iniciarse o es interrumpido
     */
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new MovieServiceImpl())
                .build()
                .start();
        System.out.println("MovieGrpcServer listening on port 50051...");
        server.awaitTermination();
    }

    /**
     * Implementación del servicio gRPC. Extiende la clase base generada
     * automáticamente desde el archivo {@code movie.proto}.
     */
    static class MovieServiceImpl extends MovieServiceGrpc.MovieServiceImplBase {

        private final Map<Integer, MovieResponse> movies = new HashMap<>();

        MovieServiceImpl() {
            movies.put(1, build(1, "Interstellar", "Christopher Nolan", 2014));
            movies.put(2, build(2, "Matrix", "Wachowski", 1999));
            movies.put(3, build(3, "Inception", "Christopher Nolan", 2010));
        }

        /**
         * Construye un {@link MovieResponse} con los datos dados, marcado como encontrado.
         */
        private MovieResponse build(int id, String title, String director, int year) {
            return MovieResponse.newBuilder()
                    .setId(id)
                    .setTitle(title)
                    .setDirector(director)
                    .setYear(year)
                    .setFound(true)
                    .build();
        }

        /**
         * Atiende la llamada remota {@code GetMovie}. Busca la película por ID y
         * envía la respuesta al cliente a través del {@link StreamObserver}.
         *
         * @param request          petición con el ID solicitado
         * @param responseObserver canal por el que se envía la respuesta al cliente
         */
        @Override
        public void getMovie(MovieRequest request, StreamObserver<MovieResponse> responseObserver) {
            MovieResponse response = movies.get(request.getId());
            if (response == null) {
                response = MovieResponse.newBuilder().setFound(false).build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
