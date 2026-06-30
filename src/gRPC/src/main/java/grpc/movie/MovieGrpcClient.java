package grpc.movie;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

/**
 * Parte IV — gRPC | Ejemplo: Movie Information System over gRPC.
 *
 * <p>Cliente gRPC que se conecta al servidor en {@code localhost:50051}, solicita
 * una película por ID usando el stub generado y muestra la respuesta.
 */
public class MovieGrpcClient {

    /**
     * Crea el canal, construye el stub bloqueante, solicita un ID al usuario
     * y muestra la película recibida.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        MovieServiceGrpc.MovieServiceBlockingStub stub = MovieServiceGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID de la pelicula: ");
        int id = Integer.parseInt(scanner.nextLine());

        MovieRequest request = MovieRequest.newBuilder().setId(id).build();
        MovieResponse response = stub.getMovie(request);

        if (response.getFound()) {
            System.out.println("Resultado: " + response.getId() + "," + response.getTitle()
                    + "," + response.getDirector() + "," + response.getYear());
        } else {
            System.out.println("Pelicula no encontrada.");
        }

        channel.shutdown();
    }
}
