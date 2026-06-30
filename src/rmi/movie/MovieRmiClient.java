package rmi.movie;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Parte III — RMI | Ejemplo: Movie RMI Client.
 *
 * <p>Se conecta al registry RMI en {@code localhost:23000}, obtiene una referencia
 * remota al servicio de películas y llama sus métodos como si fueran locales.
 */
public class MovieRmiClient {

    /**
     * Solicita un ID al usuario, llama al servicio remoto y muestra el resultado.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si ocurre un error de conexión RMI
     */
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 23000);
        MovieService service = (MovieService) registry.lookup("MovieService");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID de la pelicula: ");
        int id = Integer.parseInt(scanner.nextLine());

        Movie movie = service.findById(id);
        if (movie == null) {
            System.out.println("Pelicula no encontrada.");
        } else {
            System.out.println("Resultado: " + movie);
        }
    }
}
